/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

@file:JvmName("ArgumentTypeConverter")

package org.anvilpowered.kbrig.brigadier

import org.anvilpowered.kbrig.StringReader
import org.anvilpowered.kbrig.argument.ArgumentType
import org.anvilpowered.kbrig.context.StringRange
import org.anvilpowered.kbrig.coroutine.coroutineToFuture
import org.anvilpowered.kbrig.suggestion.Suggestion
import org.anvilpowered.kbrig.suggestion.Suggestions
import org.anvilpowered.kbrig.suggestion.SuggestionsBuilder
import java.util.concurrent.CompletableFuture
import com.mojang.brigadier.StringReader as BrigadierStringReader
import com.mojang.brigadier.arguments.ArgumentType as BrigadierArgumentType
import com.mojang.brigadier.context.CommandContext as BrigadierCommandContext
import com.mojang.brigadier.context.StringRange as BrigadierStringRange
import com.mojang.brigadier.suggestion.Suggestion as BrigadierSuggestion
import com.mojang.brigadier.suggestion.Suggestions as BrigadierSuggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder as BrigadierSuggestionsBuilder

fun <T> ArgumentType<T>.toBrigadier(): BrigadierArgumentType<T> {
    val original = this
    return object : BrigadierArgumentType<T> {
        override fun parse(reader: BrigadierStringReader): T = original.parse(reader.toKBrig())

        override fun <S : Any?> listSuggestions(
            context: BrigadierCommandContext<S>,
            builder: BrigadierSuggestionsBuilder,
        ): CompletableFuture<BrigadierSuggestions> = coroutineToFuture {
            original.listSuggestions(context.toKBrig(), builder.toKBrig()).toBrigadier()
        }

        override fun getExamples(): Collection<String> = original.examples
    }
}

fun Suggestions.toBrigadier(): BrigadierSuggestions =
    BrigadierSuggestions(range.toBrigadier(), list.map { it.toBrigadier() })

fun Suggestion.toBrigadier(): BrigadierSuggestion =
    BrigadierSuggestion(range.toBrigadier(), text)

fun StringRange.toBrigadier(): BrigadierStringRange =
    BrigadierStringRange(start, end)

fun BrigadierSuggestionsBuilder.toKBrig(): SuggestionsBuilder =
    SuggestionsBuilder(input, start)

fun BrigadierStringReader.toKBrig(): StringReader =
    StringReader(string, cursor)
