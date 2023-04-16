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
import org.anvilpowered.kbrig.exception.BuiltInExceptions
import org.anvilpowered.kbrig.exception.CommandSyntaxException

class LongArgumentType @JvmOverloads constructor(
    override val minimum: Long,
    override val maximum: Long,
    override val examples: Set<String> = emptySet(),
) : RangedArgumentType<Long> {

    @Throws(CommandSyntaxException::class)
    override fun parse(reader: StringReader): Long = readArgument(
        BuiltInExceptions.longTooLow,
        BuiltInExceptions.longTooHigh,
        reader,
    ) { it.readLong() }

    override fun toString(): String = when {
        minimum == Long.MIN_VALUE && maximum == Long.MAX_VALUE -> "longArg()"
        maximum == Long.MAX_VALUE -> "longArg($minimum)"
        else -> "longArg($minimum, $maximum)"
    }

    companion object : RangedArgumentType.Companion<Long> {
        @JvmStatic
        override val all = LongArgumentType(
            Long.MIN_VALUE,
            Long.MAX_VALUE,
            mutableSetOf("0", "123", "-123"),
        )

        @JvmStatic
        override fun get(context: CommandContext<*>, name: String): Long = context[name]
    }
}
