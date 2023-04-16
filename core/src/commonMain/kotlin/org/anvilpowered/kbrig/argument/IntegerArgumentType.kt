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

class IntegerArgumentType @JvmOverloads constructor(
    override val minimum: Int,
    override val maximum: Int,
    override val examples: Set<String> = emptySet(),
) : RangedArgumentType<Int> {

    @Throws(CommandSyntaxException::class)
    override fun parse(reader: StringReader): Int = readArgument(
        BuiltInExceptions.integerTooLow,
        BuiltInExceptions.integerTooHigh,
        reader,
    ) { it.readInt() }

    override fun toString(): String = when {
        minimum == Int.MIN_VALUE && maximum == Int.MAX_VALUE -> "integer()"
        maximum == Int.MAX_VALUE -> "integer($minimum)"
        else -> "integer($minimum, $maximum)"
    }

    companion object : RangedArgumentType.Companion<Int> {
        @JvmStatic
        override val all = IntegerArgumentType(
            Int.MIN_VALUE,
            Int.MAX_VALUE,
            mutableSetOf("0", "123", "-123"),
        )

        @JvmStatic
        override fun get(context: CommandContext<*>, name: String): Int = context[name]
    }
}
