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

data class DoubleArgumentType @JvmOverloads constructor(
    override val minimum: Double,
    override val maximum: Double,
    override val examples: Set<String> = emptySet(),
) : RangedArgumentType<Double> {

    @Throws(CommandSyntaxException::class)
    override fun parse(reader: StringReader): Double = readArgument(
        BuiltInExceptions.doubleTooLow,
        BuiltInExceptions.doubleTooHigh,
        reader,
    ) { it.readDouble() }

    override fun toString(): String = when {
        minimum == -Double.MAX_VALUE && maximum == Double.MAX_VALUE -> "double()"
        maximum == Double.MAX_VALUE -> "double($minimum)"
        else -> "double($minimum, $maximum)"
    }

    companion object : RangedArgumentType.Companion<Double> {

        @JvmStatic
        override val all = DoubleArgumentType(
            -Double.MAX_VALUE,
            Double.MAX_VALUE,
            mutableSetOf("0", "1.2", ".5", "-1", "-.5", "-1234.56"),
        )

        @JvmStatic
        override fun get(context: CommandContext<*>, name: String): Double = context[name]
    }
}
