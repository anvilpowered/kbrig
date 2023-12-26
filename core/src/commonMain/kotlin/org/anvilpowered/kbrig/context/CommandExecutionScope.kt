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

/**
 * Adds an execution block to this argument builder using the [CommandContextScopeDsl].
 *
 * The control-flow method [CommandExecutionScope.yield] and any of its specializations will exit the provided [block] early and
 * lead to the provided value being returned during command execution.
 * If [CommandExecutionScope.yield] is not invoked, [Command.SINGLE_SUCCESS] will be returned during command execution.
 * Exceptions will be propagated to the caller of [Command.execute].
 *
 * See [CommandExecutionScope] for more information and examples.
 */
fun <S, B : ArgumentBuilder<S, B>> B.executesScoped(block: suspend CommandExecutionScope<S>.() -> Unit) =
    executesSuspending { context ->
        val scope = CommandExecutionScopeImpl(context)
        val continuation = UnitCommandExecutionContinuation(scope.future)
        scope.step = block.createCoroutineUnintercepted(scope, continuation)
        scope.await()
    }

/**
 * A scope that allows for the extraction of arguments from the command context and manipulation of the control-flow outside
 * the main command execution block.
 *
 * ## Example usage
 *
 * ### Creating your own DSL
 *
 * A simple extraction of the current source, or `null` if the source is not a player.
 * Does not interrupt control-flow and does not send a message to the source.
 *
 * ```kt
 * @CommandContextScopeDsl
 * fun CommandContext.Scope<CommandSource>.extractPlayerSourceOrNull(): Player? =
 *     context.source as? Player
 * ```
 *
 * A more complex extraction of the current source; exits early and sends a message to the source if not a player.
 * Interrupts control-flow and returns 0 during command execution with [CommandExecutionScope.yieldError].
 * Note that this replaces the traditional control-flow statement with `return 0` in the main command execution block.
 *
 * In this case, the Kotlin compiler is able to smart-cast the source to a non-null Player after the null-check because none
 * of the yield methods return.
 *
 * ```kt
 * @CommandContextScopeDsl
 * suspend fun CommandExecutionScope<CommandSource>.extractPlayerSource(): Player {
 *     val player = extractPlayerSourceOrNull()
 *     if (player == null) {
 *         context.source.sendMessage(
 *             Component.text(
 *                 "You must be a player to use this command!",
 *                 NamedTextColor.RED
 *             )
 *         )
 *         yieldError()
 *     }
 *     return player
 * }
 * ```
 *
 * Extracts a player argument from the command context; exits early and sends a message to the source if the player is not found.
 *
 * ```kt
 * @CommandContextScopeDsl
 * suspend fun CommandExecutionScope<CommandSource>.extractPlayerArgument(
 *     proxyServer: ProxyServer,
 *     argumentName: String = "player",
 * ): Player {
 *     val playerName = context.get<String>(argumentName)
 *     val player = proxyServer.getPlayer(playerName).getOrNull()
 *     if (player == null) {
 *         context.source.sendMessage(
 *             Component.text()
 *                 .append(Component.text("Player with name ", NamedTextColor.RED))
 *                 .append(Component.text(playerName, NamedTextColor.GOLD))
 *                 .append(Component.text(" not found!", NamedTextColor.RED))
 *                 .build(),
 *         )
 *         yieldError()
 *     }
 *     return player
 * }
 * ```
 *
 * ### Using the DSL
 *
 * The strength in this approach lies in the ability for invoked methods
 * to interrupt control-flow.
 * Usually, it is necessary to introduce a control-flow statement in the main command execution block;
 * for example, to check whether a player exists and exit early if not.
 *
 * The following example shows how to use the DSL to create a command that sends a private message to another player.
 * It uses the examples defined above to extract the source player and the target player.
 *
 * ```kt
 * ArgumentBuilder.required<CommandSource, String>("message", StringArgumentType.GreedyPhrase)
 *     .executesScoped {
 *         // exits early and sends a message to the source if not a player
 *         val sourcePlayer = extractPlayerSource()
 *         // exits early and sends a message to the source if the player is not found
 *         val targetPlayer = extractPlayerArgument(proxyServer)
 *
 *         if (sourcePlayer.uniqueId == targetPlayer.uniqueId) {
 *             context.source.sendMessage(PluginMessages.messageSelf)
 *             yieldError() // exits the block early and returns 0 during command execution
 *         }
 *         privateMessageService.sendMessage(
 *             sourcePlayer,
 *             targetPlayer,
 *             MiniMessage.miniMessage().deserialize(context.get<String>("message")),
 *         )
 *         yieldSuccess()
 *     }.build(),
 * ```
 */
interface CommandExecutionScope<S> : CommandContext.Scope<S> {
    @CommandContextScopeDsl
    suspend fun yield(value: Int): Nothing

    @CommandContextScopeDsl
    override suspend fun abort(): Nothing = yieldError()
}

@CommandContextScopeDsl
suspend fun <S> CommandExecutionScope<S>.yieldError(): Nothing = yield(value = 0)

@CommandContextScopeDsl
suspend fun <S> CommandExecutionScope<S>.yieldSuccess(): Nothing = yield(value = Command.SINGLE_SUCCESS)

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
