/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.suggestion

import org.anvilpowered.kbrig.context.StringRange
import kotlin.jvm.JvmOverloads

open class Suggestion @JvmOverloads constructor(
    val range: StringRange,
    val text: String,
    val tooltip: String? = null,
) : Comparable<Suggestion> {

    fun apply(input: String): String {
        if (range.start == 0 && range.end == input.length) {
            return text
        }
        val result = StringBuilder()
        if (range.start > 0) {
            result.append(input.substring(0, range.start))
        }
        result.append(text)
        if (range.end < input.length) {
            result.append(input.substring(range.end))
        }
        return result.toString()
    }

    fun expand(command: String, range: StringRange): Suggestion {
        if (range == this.range) {
            return this
        }
        val result = StringBuilder()
        if (range.start < this.range.start) {
            result.append(command.substring(range.start, this.range.start))
        }
        result.append(text)
        if (range.end > this.range.end) {
            result.append(command.substring(this.range.end, range.end))
        }
        return Suggestion(range, result.toString(), tooltip)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Suggestion) {
            return false
        }
        return range == other.range && text == other.text && tooltip == other.tooltip
    }

    override fun hashCode(): Int {
        var result = range.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + (tooltip?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String = "Suggestion{range=$range, text='$text', tooltip='$tooltip'}"
    override fun compareTo(other: Suggestion): Int = text.compareTo(other.text)
    open fun compareToIgnoreCase(other: Suggestion): Int = text.compareTo(other.text, ignoreCase = true)
}
