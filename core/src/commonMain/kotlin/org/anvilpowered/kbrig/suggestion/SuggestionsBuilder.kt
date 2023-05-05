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

class SuggestionsBuilder(
    val input: String,
    val start: Int,
    private val inputLowerCase: String = input.lowercase(),
) {

    private val remaining: String = input.substring(start)
    val remainingLowerCase: String = inputLowerCase.substring(start)
    private val result = mutableListOf<Suggestion>()

//    constructor(input: String, start: Int) : this(input, input.lowercase(), start)

    fun build(): Suggestions = Suggestions.create(input, result)

    @JvmOverloads
    fun suggest(text: String, tooltip: String? = null): SuggestionsBuilder {
        if (text == remaining) {
            return this
        }
        result.add(Suggestion(StringRange.between(start, input.length), text, tooltip))
        return this
    }

    @JvmOverloads
    fun suggest(value: Int, tooltip: String? = null): SuggestionsBuilder {
        result.add(IntegerSuggestion(StringRange.between(start, input.length), value, tooltip))
        return this
    }

    fun add(other: SuggestionsBuilder): SuggestionsBuilder {
        result.addAll(other.result)
        return this
    }

    fun createOffset(start: Int): SuggestionsBuilder {
        return SuggestionsBuilder(input, start, inputLowerCase)
    }

    fun restart(): SuggestionsBuilder {
        return createOffset(start)
    }
}
