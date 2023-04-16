/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.tree

import org.anvilpowered.kbrig.Command
import org.anvilpowered.kbrig.argument.ArgumentType
import org.anvilpowered.kbrig.builder.RequiredArgumentBuilder
import org.anvilpowered.kbrig.context.CommandContext
import org.anvilpowered.kbrig.suggestion.SuggestionProvider
import org.anvilpowered.kbrig.suggestion.Suggestions
import org.anvilpowered.kbrig.suggestion.SuggestionsBuilder

class ArgumentCommandNode<S, T>(
    name: String,
    val type: ArgumentType<T>,
    override val command: Command<S>,
    requirement: (S) -> Boolean,
    redirect: CommandNode<S>? = null,
    forks: Boolean = false,
    private val customSuggestions: SuggestionProvider<S>? = null,
) : CommandNode<S>(name, command, requirement, redirect, forks) {

    override val usageText: String
        get() = USAGE_ARGUMENT_OPEN + name + USAGE_ARGUMENT_CLOSE

    override val examples: Set<String>
        get() = type.examples

    override suspend fun listSuggestions(context: CommandContext<S>, builder: SuggestionsBuilder): Suggestions {
        return customSuggestions?.getSuggestions(context, builder) ?: type.listSuggestions(context, builder)
    }

    override fun toBuilder(): RequiredArgumentBuilder<S, T> {
        return RequiredArgumentBuilder<S, T>(name, type)
            .requires(requirement)
            .forward(redirect, forks)
            .suggests(customSuggestions)
            .executes(command)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ArgumentCommandNode<*, *>) return false
        if (name != other.name) return false
        return if (type != other.type) false else super.equals(other)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    override fun toString(): String = "<argument $name:$type>"

    companion object {
        private const val USAGE_ARGUMENT_OPEN = "<"
        private const val USAGE_ARGUMENT_CLOSE = ">"
    }
}
