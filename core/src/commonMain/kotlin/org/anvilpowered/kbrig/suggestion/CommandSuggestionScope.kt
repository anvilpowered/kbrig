/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

package org.anvilpowered.kbrig.suggestion

import org.anvilpowered.kbrig.builder.RequiredArgumentBuilder
import org.anvilpowered.kbrig.context.CommandContext
import kotlin.experimental.ExperimentalTypeInference
import kotlin.jvm.JvmName

@DslMarker
annotation class CommandSuggestionScopeDsl

@OptIn(ExperimentalTypeInference::class)
@Suppress("INAPPLICABLE_JVM_NAME")
class CommandSuggestionScope<S> internal constructor(
    override val context: CommandContext<S>,
    internal val builder: SuggestionsBuilder,
) : CommandContext.Scope<S> {

    @CommandSuggestionScopeDsl
    fun String.suggest(tooltip: String? = null) {
        builder.suggest(text = this, tooltip)
    }

    @CommandSuggestionScopeDsl
    fun Int.suggest(tooltip: String? = null) {
        builder.suggest(value = this, tooltip)
    }

    @JvmName("suggestAllStrings")
    @CommandSuggestionScopeDsl
    fun Iterable<String>.suggestAll(tooltip: String? = null) {
        forEach { builder.suggest(it, tooltip) }
    }

    @JvmName("suggestAllIntegers")
    @CommandSuggestionScopeDsl
    fun Iterable<Int>.suggestAll(tooltip: String? = null) {
        forEach { builder.suggest(it, tooltip) }
    }

    @JvmName("suggestAllStrings")
    @CommandSuggestionScopeDsl
    @OverloadResolutionByLambdaReturnType
    fun <T> Iterable<T>.suggestAll(tooltip: String? = null, mapper: (T) -> String) {
        forEach { builder.suggest(mapper(it), tooltip) }
    }

    @JvmName("suggestAllIntegers")
    @CommandSuggestionScopeDsl
    @OverloadResolutionByLambdaReturnType
    fun <T> Iterable<T>.suggestAll(tooltip: String? = null, mapper: (T) -> Int) {
        forEach { builder.suggest(mapper(it), tooltip) }
    }

    @CommandSuggestionScopeDsl
    @JvmName("suggestAllStrings")
    fun Sequence<String>.suggestAll(tooltip: String? = null) {
        forEach { builder.suggest(it, tooltip) }
    }

    @CommandSuggestionScopeDsl
    @JvmName("suggestAllIntegers")
    fun Sequence<Int>.suggestAll(tooltip: String? = null) {
        forEach { builder.suggest(it, tooltip) }
    }

    @CommandSuggestionScopeDsl
    @JvmName("suggestAllStrings")
    @OverloadResolutionByLambdaReturnType
    fun <T> Sequence<T>.suggestAll(tooltip: String? = null, mapper: (T) -> String) {
        forEach { builder.suggest(mapper(it), tooltip) }
    }

    @CommandSuggestionScopeDsl
    @JvmName("suggestAllIntegers")
    @OverloadResolutionByLambdaReturnType
    fun <T> Sequence<T>.suggestAll(tooltip: String? = null, mapper: (T) -> Int) {
        forEach { builder.suggest(mapper(it), tooltip) }
    }
}

fun <S, T, B : RequiredArgumentBuilder<S, T>> B.suggestsScoped(command: suspend CommandSuggestionScope<S>.() -> Unit) =
    suggests { context, builder ->
        val scope = CommandSuggestionScope(context, builder)
        scope.command()
        scope.builder.build()
    }
