/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.builder

import org.anvilpowered.kbrig.Command
import org.anvilpowered.kbrig.tree.CommandNode

sealed class ArgumentBuilder<S, T : ArgumentBuilder<S, T>> {

    private val _children = mutableMapOf<String, CommandNode<S>>()
    val children: Map<String, CommandNode<S>>
        get() = _children

    var command: Command<S>? = null
        private set
    var requirement: (S) -> Boolean = { true }
        private set
    var redirect: CommandNode<S>? = null
        private set
    var forks = false
        private set

    protected abstract val self: T

    fun then(argument: CommandNode<S>): T {
        check(redirect == null) { "Cannot add children to a redirected node" }
        _children[argument.name] = argument
        return self
    }

    fun then(argument: ArgumentBuilder<S, *>): T = then(argument.build())

    fun executes(command: Command<S>?): T {
        this.command = command
        return self
    }

    fun requires(requirement: (S) -> Boolean): T {
        this.requirement = requirement
        return self
    }

    fun forward(target: CommandNode<S>?, forks: Boolean): T {
        check(children.isEmpty()) { "Cannot forward a node with children" }
        redirect = target
        this.forks = forks
        return self
    }

    fun redirect(target: CommandNode<S>): T = forward(target, false)
    fun fork(target: CommandNode<S>): T = forward(target, true)

    abstract fun build(): CommandNode<S>
}
