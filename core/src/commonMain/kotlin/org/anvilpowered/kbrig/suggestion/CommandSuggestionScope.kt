/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

@file:OptIn(ExperimentalTypeInference::class)
@file:Suppress("INAPPLICABLE_JVM_NAME")

package org.anvilpowered.kbrig.suggestion

import kotlinx.coroutines.CompletableDeferred
import org.anvilpowered.kbrig.builder.RequiredArgumentBuilder
import org.anvilpowered.kbrig.context.CommandContext
import org.anvilpowered.kbrig.context.CommandContextScopeDsl
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.intrinsics.createCoroutineUnintercepted
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn
import kotlin.coroutines.resume
import kotlin.experimental.ExperimentalTypeInference
import kotlin.jvm.JvmName

fun <S, T, B : RequiredArgumentBuilder<S, T>> B.suggestsScoped(command: suspend CommandSuggestionScope<S>.() -> Unit) =
    suggests { context, builder ->
        val scope = CommandSuggestionScopeImpl(context, builder)
        val continuation = UnitCommandExecutionContinuation(scope)
        scope.step = command.createCoroutineUnintercepted(scope, continuation)
        scope.await()
    }

interface CommandSuggestionScope<S> : CommandContext.Scope<S> {

    val remainingLowerCase: String

    override val context: CommandContext<S>

    @CommandContextScopeDsl
    fun String.suggest(tooltip: String? = null)

    @CommandContextScopeDsl
    fun Int.suggest(tooltip: String? = null)

    @CommandContextScopeDsl
    @JvmName("suggestAllStrings")
    fun Iterable<String>.suggestAll(tooltip: String? = null)

    @CommandContextScopeDsl
    @JvmName("suggestAllStringsFiltered")
    fun Iterable<String>.suggestAllFiltered(tooltip: String? = null, ignoreCase: Boolean = true)

    @CommandContextScopeDsl
    @JvmName("suggestAllIntegers")
    fun Iterable<Int>.suggestAll(tooltip: String? = null)

    @CommandContextScopeDsl
    @JvmName("suggestAllStrings")
    fun Sequence<String>.suggestAll(tooltip: String? = null)

    @CommandContextScopeDsl
    @JvmName("suggestAllStringsFiltered")
    fun Sequence<String>.suggestAllFiltered(tooltip: String? = null, ignoreCase: Boolean = true)

    @CommandContextScopeDsl
    @JvmName("suggestAllIntegers")
    fun Sequence<Int>.suggestAll(tooltip: String? = null)

    @CommandContextScopeDsl
    @OverloadResolutionByLambdaReturnType
    @JvmName("suggestAllStrings")
    fun <T> Iterable<T>.suggestAll(tooltip: String? = null, mapper: (T) -> String)

    @CommandContextScopeDsl
    @OverloadResolutionByLambdaReturnType
    @JvmName("suggestAllStringsFiltered")
    fun <T> Iterable<T>.suggestAllFiltered(tooltip: String? = null, ignoreCase: Boolean = true, mapper: (T) -> String)

    @CommandContextScopeDsl
    @OverloadResolutionByLambdaReturnType
    @JvmName("suggestAllIntegers")
    fun <T> Iterable<T>.suggestAll(tooltip: String? = null, mapper: (T) -> Int)

    @CommandContextScopeDsl
    @OverloadResolutionByLambdaReturnType
    @JvmName("suggestAllStrings")
    fun <T> Sequence<T>.suggestAll(tooltip: String? = null, mapper: (T) -> String)

    @CommandContextScopeDsl
    @OverloadResolutionByLambdaReturnType
    @JvmName("suggestAllStringsFiltered")
    fun <T> Sequence<T>.suggestAllFiltered(tooltip: String? = null, ignoreCase: Boolean = true, mapper: (T) -> String)

    @CommandContextScopeDsl
    @OverloadResolutionByLambdaReturnType
    @JvmName("suggestAllIntegers")
    fun <T> Sequence<T>.suggestAll(tooltip: String? = null, mapper: (T) -> Int)
}

private class CommandSuggestionScopeImpl<S>(
    override val context: CommandContext<S>,
    val builder: SuggestionsBuilder,
) : CommandSuggestionScope<S> {
    lateinit var step: Continuation<Unit>
    val future = CompletableDeferred<Boolean>()

    suspend fun await(): Suggestions {
        step.resume(Unit)
        return if (future.await()) {
            builder.build()
        } else {
            Suggestions.empty()
        }
    }

    override val remainingLowerCase: String
        get() = builder.remainingLowerCase

    override suspend fun abort(): Nothing {
        future.complete(false)
        suspendCoroutineUninterceptedOrReturn<Unit> {
            COROUTINE_SUSPENDED
        }
        throw AssertionError("abort() called")
    }

    override fun String.suggest(tooltip: String?) {
        builder.suggest(text = this, tooltip)
    }

    override fun Int.suggest(tooltip: String?) {
        builder.suggest(value = this, tooltip)
    }

    @JvmName("suggestAllStrings")
    override fun Iterable<String>.suggestAll(tooltip: String?) = forEach { builder.suggest(it, tooltip) }

    @JvmName("suggestAllStringsFiltered")
    override fun Iterable<String>.suggestAllFiltered(tooltip: String?, ignoreCase: Boolean) =
        forEach {
            if (it.startsWith(remainingLowerCase, ignoreCase)) {
                builder.suggest(it, tooltip)
            }
        }

    @JvmName("suggestAllIntegers")
    override fun Iterable<Int>.suggestAll(tooltip: String?) =
        forEach { builder.suggest(it, tooltip) }

    @JvmName("suggestAllStrings")
    override fun Sequence<String>.suggestAll(tooltip: String?) =
        forEach { builder.suggest(it, tooltip) }

    @JvmName("suggestAllStringsFiltered")
    override fun Sequence<String>.suggestAllFiltered(tooltip: String?, ignoreCase: Boolean) =
        filter { it.startsWith(remainingLowerCase, ignoreCase) }.suggestAll(tooltip)

    @JvmName("suggestAllIntegers")
    override fun Sequence<Int>.suggestAll(tooltip: String?) =
        forEach { builder.suggest(it, tooltip) }

    @JvmName("suggestAllStrings")
    override fun <T> Iterable<T>.suggestAll(tooltip: String?, mapper: (T) -> String) =
        forEach { builder.suggest(mapper(it), tooltip) }

    @JvmName("suggestAllStringsFiltered")
    override fun <T> Iterable<T>.suggestAllFiltered(tooltip: String?, ignoreCase: Boolean, mapper: (T) -> String) =
        forEach {
            val mapped = mapper(it)
            if (mapped.startsWith(remainingLowerCase, ignoreCase)) {
                builder.suggest(mapped, tooltip)
            }
        }

    @JvmName("suggestAllIntegers")
    override fun <T> Iterable<T>.suggestAll(tooltip: String?, mapper: (T) -> Int) =
        forEach { builder.suggest(mapper(it), tooltip) }

    @JvmName("suggestAllStrings")
    override fun <T> Sequence<T>.suggestAll(tooltip: String?, mapper: (T) -> String) =
        forEach { builder.suggest(mapper(it), tooltip) }

    @JvmName("suggestAllStringsFiltered")
    override fun <T> Sequence<T>.suggestAllFiltered(tooltip: String?, ignoreCase: Boolean, mapper: (T) -> String) =
        map(mapper)
            .filter { it.startsWith(remainingLowerCase, ignoreCase) }
            .forEach { builder.suggest(it, tooltip) }

    @JvmName("suggestAllIntegers")
    override fun <T> Sequence<T>.suggestAll(tooltip: String?, mapper: (T) -> Int) =
        forEach { builder.suggest(mapper(it), tooltip) }
}

private class UnitCommandExecutionContinuation(val scope: CommandSuggestionScopeImpl<*>) : Continuation<Unit> {
    override val context: CoroutineContext get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<Unit>) {
        val exception = result.exceptionOrNull()
        if (exception == null) {
            scope.future.complete(true)
        } else {
            scope.future.completeExceptionally(exception)
        }
    }
}
