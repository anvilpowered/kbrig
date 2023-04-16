/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.tree

import org.anvilpowered.kbrig.Command
import org.anvilpowered.kbrig.StringReader
import org.anvilpowered.kbrig.builder.ArgumentBuilder
import org.anvilpowered.kbrig.context.CommandContext
import org.anvilpowered.kbrig.suggestion.Suggestions
import org.anvilpowered.kbrig.suggestion.SuggestionsBuilder

abstract class CommandNode<S>(
    val name: String,
    open val command: Command<S>?,
    val requirement: (S) -> Boolean,
    val redirect: CommandNode<S>?,
    val forks: Boolean,
) : Comparable<CommandNode<S>> {

    private val _children = mutableMapOf<String, CommandNode<S>>()
    val children: Map<String, CommandNode<S>> get() = _children

    private val arguments = mutableMapOf<String, ArgumentCommandNode<S, *>>()
    private var hasLiterals = false

    abstract val usageText: String
    abstract val examples: Set<String>

    abstract suspend fun listSuggestions(context: CommandContext<S>, builder: SuggestionsBuilder): Suggestions
    abstract fun toBuilder(): ArgumentBuilder<S, *>

    fun addChild(node: CommandNode<S>) {
        if (node is RootCommandNode<*>) {
            throw UnsupportedOperationException("Cannot add a RootCommandNode as a child to any other CommandNode")
        }
        val child = children[node.name]
        if (child != null) {
            // The original Brigadier code replaces the old command with the one from the new node.
            // This is the only place that mutates the command, so in the interests of immutability
            // we're going to skip this step.

            // We've found something to merge onto
            // if (node.command != null) {
            //    child.command = node.command
            // }
            for (grandchild in node.children.values) {
                child.addChild(grandchild)
            }
        } else {
            _children[node.name] = node
            if (node is LiteralCommandNode<*>) {
                hasLiterals = true
            } else if (node is ArgumentCommandNode<*, *>) {
                arguments[node.name] = node as ArgumentCommandNode<S, *>
            }
        }
    }

    fun removeChildByName(name: String) {
        val child = _children.remove(name)
        if (child != null) {
            arguments.remove(name)
        }
    }

    fun getRelevantNodes(input: StringReader): Collection<CommandNode<S>> {
        return if (hasLiterals) {
            val cursor = input.cursor
            while (input.canRead() && input.peek() != ' ') {
                input.skip()
            }
            val text = input.string.substring(cursor, input.cursor)
            input.cursor = cursor
            val node = children[text]!!
            if (node is LiteralCommandNode<*>) {
                listOf<CommandNode<S>>(node) + arguments.values
            } else {
                arguments.values
            }
        } else {
            arguments.values
        }
    }

    override fun compareTo(other: CommandNode<S>): Int {
        if (this is LiteralCommandNode<*> == other is LiteralCommandNode<*>) {
            return name.compareTo(other.name)
        }
        return if (other is LiteralCommandNode<*>) 1 else -1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CommandNode<*>) return false
        val that = other as CommandNode<S>
        if (children != that.children) return false
        return command == that.command
    }

    override fun hashCode(): Int {
        return 31 * children.hashCode() + command.hashCode()
    }

    abstract override fun toString(): String
}
