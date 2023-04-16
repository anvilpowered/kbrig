/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.builder

import org.anvilpowered.kbrig.argument.ArgumentType
import org.anvilpowered.kbrig.suggestion.SuggestionProvider
import org.anvilpowered.kbrig.tree.ArgumentCommandNode

class RequiredArgumentBuilder<S, T>(
    val name: String,
    private val type: ArgumentType<T>,
) : ArgumentBuilder<S, RequiredArgumentBuilder<S, T>>() {
    private var suggestionsProvider: SuggestionProvider<S>? = null

    fun suggests(provider: SuggestionProvider<S>?): RequiredArgumentBuilder<S, T> {
        suggestionsProvider = provider
        return this
    }

    override val self: RequiredArgumentBuilder<S, T>
        get() = this

    override fun build(): ArgumentCommandNode<S, T> =
        ArgumentCommandNode(name, type, command, requirement, redirect, forks, children, suggestionsProvider)
}
