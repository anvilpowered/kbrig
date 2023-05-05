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
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

data class FloatArgumentType @JvmOverloads constructor(
    override val minimum: Float,
    override val maximum: Float,
    override val examples: Set<String> = emptySet(),
) : RangedArgumentType<Float> {

    @Throws(CommandSyntaxException::class)
    override fun parse(reader: StringReader): Float = readArgument(
        BuiltInExceptions.floatTooLow,
        BuiltInExceptions.floatTooHigh,
        reader,
    ) { it.readFloat() }

    override fun toString(): String = when {
        minimum == -Float.MAX_VALUE && maximum == Float.MAX_VALUE -> "float()"
        maximum == Float.MAX_VALUE -> "float($minimum)"
        else -> "float($minimum, $maximum)"
    }

    companion object : RangedArgumentType.Companion<Float> {
        @JvmStatic
        override val all = FloatArgumentType(
            -Float.MAX_VALUE,
            Float.MAX_VALUE,
            mutableSetOf("0", "1.2", ".5", "-1", "-.5", "-1234.56"),
        )

        @JvmStatic
        override fun get(context: CommandContext<*>, name: String): Float = context[name]
    }
}
