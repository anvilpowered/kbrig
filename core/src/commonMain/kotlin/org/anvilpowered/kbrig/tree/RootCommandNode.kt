/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.tree

import org.anvilpowered.kbrig.builder.ArgumentBuilder
import org.anvilpowered.kbrig.context.CommandContext
import org.anvilpowered.kbrig.suggestion.Suggestions
import org.anvilpowered.kbrig.suggestion.SuggestionsBuilder

class RootCommandNode<S> : CommandNode<S>(
    name = "",
    command = null,
    requirement = { true },
    redirect = null,
    forks = false,
) {

    override val usageText: String = ""

    override suspend fun listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder,
    ): Suggestions = Suggestions.empty()

    override fun toBuilder(): ArgumentBuilder<S, *> {
        throw IllegalStateException("Cannot convert root into a builder")
    }

    override val examples: Set<String> = emptySet()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other !is RootCommandNode<*>) false else super.equals(other)
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + usageText.hashCode()
        result = 31 * result + examples.hashCode()
        return result
    }

    override fun toString(): String = "<root>"
}
