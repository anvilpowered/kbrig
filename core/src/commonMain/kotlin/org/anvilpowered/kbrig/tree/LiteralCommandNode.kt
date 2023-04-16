/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.tree

import org.anvilpowered.kbrig.Command
import org.anvilpowered.kbrig.builder.LiteralArgumentBuilder
import org.anvilpowered.kbrig.context.CommandContext
import org.anvilpowered.kbrig.suggestion.Suggestions
import org.anvilpowered.kbrig.suggestion.SuggestionsBuilder

class LiteralCommandNode<S>(
    literal: String,
    override var command: Command<S>,
    requirement: (S) -> Boolean,
    redirect: CommandNode<S>?,
    forks: Boolean,
) : CommandNode<S>(literal, command, requirement, redirect, forks) {

    override val usageText: String = name
    private val literalLowerCase: String = name.lowercase()

    override suspend fun listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder,
    ): Suggestions {
        return if (literalLowerCase.regionMatches(
                0,
                builder.remainingLowerCase,
                0,
                builder.remainingLowerCase.length,
                ignoreCase = true,
            )
        ) {
            builder.suggest(name).build()
        } else {
            Suggestions.empty()
        }
    }

    override fun toBuilder(): LiteralArgumentBuilder<S> {
        return LiteralArgumentBuilder<S>(name)
            .requires(requirement)
            .forward(redirect, forks)
            .executes(command)
    }

    override val examples: Set<String> = setOf(name)

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is LiteralCommandNode<*>) return false
        return if (name != o.name) false else super.equals(o)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + super.hashCode()
        return result
    }

    override fun toString(): String = "<literal $name>"
}
