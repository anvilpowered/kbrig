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
import org.anvilpowered.kbrig.context.getArgument
import org.anvilpowered.kbrig.exception.CommandSyntaxException
import org.anvilpowered.kbrig.suggestion.Suggestions
import org.anvilpowered.kbrig.suggestion.SuggestionsBuilder

object BooleanArgumentType : ArgumentType<Boolean>, ArgumentType.Companion<Boolean> {

    @Throws(CommandSyntaxException::class)
    override fun parse(reader: StringReader): Boolean = reader.readBoolean()

    override suspend fun <S> listSuggestions(
        context: CommandContext<S>?,
        builder: SuggestionsBuilder,
    ): Suggestions {
        if ("true".startsWith(builder.remainingLowerCase)) {
            builder.suggest("true")
        }
        if ("false".startsWith(builder.remainingLowerCase)) {
            builder.suggest("false")
        }
        return builder.build()
    }

    override val examples = mutableSetOf("true", "false")

    override fun get(context: CommandContext<*>, name: String): Boolean = context.getArgument(name)
}
