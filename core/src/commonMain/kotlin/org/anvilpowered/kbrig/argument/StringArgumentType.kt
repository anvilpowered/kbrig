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
import org.anvilpowered.kbrig.context.get
import org.anvilpowered.kbrig.exception.CommandSyntaxException

sealed class StringArgumentType private constructor(
    override val examples: Set<String> = emptySet(),
) : ArgumentType<String> {
    object SingleWord : StringArgumentType(setOf("word", "words_with_underscores")) {
        @Throws(CommandSyntaxException::class)
        override fun parse(reader: StringReader): String = reader.readUnquotedString()
        override fun toString(): String = "string.SingleWord()"
    }

    object QuotablePhrase : StringArgumentType(setOf("\"quoted phrase\"", "word", "\"\"")) {
        @Throws(CommandSyntaxException::class)
        override fun parse(reader: StringReader): String = reader.readString()
        override fun toString(): String = "string.QuotablePhrase()"
    }

    object GreedyPhrase : StringArgumentType(setOf("word", "words with spaces", "\"and symbols\"")) {
        @Throws(CommandSyntaxException::class)
        override fun parse(reader: StringReader): String = reader.remaining.also { reader.cursor = reader.totalLength }
        override fun toString(): String = "string.GreedyPhrase()"
    }

    companion object : ArgumentType.Companion<String> {

        @JvmStatic
        fun singleWord() = SingleWord

        @JvmStatic
        fun quotablePhrase() = QuotablePhrase

        @JvmStatic
        fun greedyPhrase() = GreedyPhrase

        @JvmStatic
        override fun get(context: CommandContext<*>, name: String): String = context[name]
    }
}
