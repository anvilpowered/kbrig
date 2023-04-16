/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.suggestion

import org.anvilpowered.kbrig.context.StringRange
import java.util.Objects

class IntegerSuggestion @JvmOverloads constructor(
    range: StringRange,
    val value: Int,
    tooltip: String? = null,
) : Suggestion(range, value.toString(), tooltip) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is IntegerSuggestion) {
            return false
        }
        return value == other.value && super.equals(other)
    }

    override fun hashCode(): Int = Objects.hash(super.hashCode(), value)

    override fun toString(): String = "IntegerSuggestion{value=$value, range=$range, text='$text', tooltip='$tooltip'}"

    override fun compareTo(other: Suggestion): Int {
        return if (other is IntegerSuggestion) {
            value.compareTo(other.value)
        } else {
            super.compareTo(other)
        }
    }

    override fun compareToIgnoreCase(other: Suggestion): Int = compareTo(other)
}
