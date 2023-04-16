/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.suggestion

import org.anvilpowered.kbrig.context.StringRange
import kotlin.math.max
import kotlin.math.min

data class Suggestions(
    val range: StringRange,
    val list: List<Suggestion>,
) {

    val isEmpty: Boolean
        get() = list.isEmpty()

    companion object {
        private val EMPTY = Suggestions(StringRange.at(0), emptyList())
        fun empty(): Suggestions = EMPTY

        fun merge(command: String, input: Collection<Suggestions>): Suggestions {
            if (input.isEmpty()) {
                return EMPTY
            } else if (input.size == 1) {
                return input.first()
            }
            val texts: MutableSet<Suggestion> = HashSet()
            for (suggestions in input) {
                texts.addAll(suggestions.list)
            }
            return create(command, texts)
        }

        fun create(command: String, suggestions: Collection<Suggestion>): Suggestions {
            if (suggestions.isEmpty()) {
                return EMPTY
            }
            var start = Int.MAX_VALUE
            var end = Int.MIN_VALUE
            for (suggestion in suggestions) {
                start = min(suggestion.range.start, start)
                end = max(suggestion.range.end, end)
            }
            val range = StringRange(start, end)
            return Suggestions(
                range,
                suggestions
                    .map { it.expand(command, range) }
                    .sortedWith { a, b -> a.compareToIgnoreCase(b) },
            )
        }
    }
}
