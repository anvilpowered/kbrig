/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.context

import org.anvilpowered.kbrig.ImmutableStringReader
import kotlin.math.max
import kotlin.math.min

data class StringRange(val start: Int, val end: Int) {

    operator fun get(reader: ImmutableStringReader): String {
        return reader.string.substring(start, end)
    }

    operator fun get(string: String): String {
        return string.substring(start, end)
    }

    val isEmpty: Boolean
        get() = start == end
    val length: Int
        get() = end - start

    companion object {
        @JvmStatic
        fun at(pos: Int): StringRange {
            return StringRange(pos, pos)
        }

        @JvmStatic
        fun between(start: Int, end: Int): StringRange {
            return StringRange(start, end)
        }

        @JvmStatic
        fun encompassing(a: StringRange, b: StringRange): StringRange {
            return StringRange(min(a.start, b.start), max(a.end, b.end))
        }
    }
}
