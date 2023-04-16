/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.argument

import org.anvilpowered.kbrig.StringReader
import org.anvilpowered.kbrig.context.CommandContext
import org.anvilpowered.kbrig.exception.CommandSyntaxException
import org.anvilpowered.kbrig.suggestion.Suggestions
import org.anvilpowered.kbrig.suggestion.SuggestionsBuilder

sealed interface ArgumentType<T> {

    @Throws(CommandSyntaxException::class)
    fun parse(reader: StringReader): T

    suspend fun <S> listSuggestions(
        context: CommandContext<S>?,
        builder: SuggestionsBuilder,
    ): Suggestions = Suggestions.empty()

    val examples: Set<String>
        get() = emptySet()

    interface Companion<T> {
        // TODO?: Replace just with context.getArgument<Type>(name)
        fun get(context: CommandContext<*>, name: String): T
    }
}
