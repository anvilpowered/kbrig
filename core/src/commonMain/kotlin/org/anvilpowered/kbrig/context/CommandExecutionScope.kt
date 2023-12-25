/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

package org.anvilpowered.kbrig.context

import kotlinx.coroutines.CompletableDeferred
import org.anvilpowered.kbrig.Command
import org.anvilpowered.kbrig.builder.ArgumentBuilder
import org.anvilpowered.kbrig.builder.executesSuspending
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.intrinsics.createCoroutineUnintercepted
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn
import kotlin.coroutines.resume

interface CommandExecutionScope<S> : CommandContext.Scope<S> {
    @CommandContextScopeDsl
    suspend fun yield(value: Int): Nothing
}

@CommandContextScopeDsl
suspend fun <S> CommandExecutionScope<S>.yieldError(): Nothing = yield(value = 0)

@CommandContextScopeDsl
suspend fun <S> CommandExecutionScope<S>.yieldSuccess(): Nothing = yield(value = Command.SINGLE_SUCCESS)

fun <S, B : ArgumentBuilder<S, B>> B.executesScoped(block: suspend CommandExecutionScope<S>.() -> Unit) =
    executesSuspending { context ->
        val scope = CommandExecutionScopeImpl(context)
        val continuation = UnitCommandExecutionContinuation(scope.future)
        scope.step = block.createCoroutineUnintercepted(scope, continuation)
        scope.await()
    }

private class CommandExecutionScopeImpl<S>(
    override val context: CommandContext<S>,
) : CommandExecutionScope<S> {
    lateinit var step: Continuation<Unit>
    val future = CompletableDeferred<Int>()

    suspend fun await(): Int {
        step.resume(Unit)
        return future.await()
    }

    override suspend fun yield(value: Int): Nothing {
        future.complete(value)
        suspendCoroutineUninterceptedOrReturn<Unit> {
            COROUTINE_SUSPENDED
        }
        throw AssertionError("yield() called")
    }
}

private class UnitCommandExecutionContinuation(val future: CompletableDeferred<Int>) : Continuation<Unit> {
    override val context: CoroutineContext get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<Unit>) {
        val exception = result.exceptionOrNull()
        if (exception == null) {
            future.complete(Command.SINGLE_SUCCESS)
        } else {
            future.completeExceptionally(exception)
        }
    }
}
