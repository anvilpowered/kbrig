/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

package org.anvilpowered.kbrig.builder

import org.anvilpowered.kbrig.Command
import org.anvilpowered.kbrig.SuspendingCommand
import org.anvilpowered.kbrig.context.CommandContext
import org.anvilpowered.kbrig.toBlocking

fun <S, B : ArgumentBuilder<S, B>> B.executesSuspending(command: SuspendingCommand<S>?): B = executes(command?.toBlocking())

fun <S, B : ArgumentBuilder<S, B>> B.executesSingleSuccess(block: (context: CommandContext<S>) -> Unit): B =
    executes { context ->
        try {
            block(context)
            Command.SINGLE_SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

fun <S, B : ArgumentBuilder<S, B>> B.executesFailure(block: (context: CommandContext<S>) -> Unit): B =
    executes { context ->
        try {
            block(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        0
    }
