/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

package org.anvilpowered.kbrig.argument

import org.anvilpowered.kbrig.StringReader
import org.anvilpowered.kbrig.exception.DynamicCommandExceptionType2

internal fun <T : Comparable<T>> RangedArgumentType<T>.readArgument(
    tooLow: DynamicCommandExceptionType2<T, T>,
    tooHigh: DynamicCommandExceptionType2<T, T>,
    reader: StringReader,
    readFun: (StringReader) -> T,
): T {
    val start = reader.cursor
    val result = readFun(reader)
    if (result < minimum) {
        reader.cursor = start
        throw tooLow.createWithContext(reader, result, minimum)
    }
    if (result > maximum) {
        reader.cursor = start
        throw tooHigh.createWithContext(reader, result, maximum)
    }
    return result
}
