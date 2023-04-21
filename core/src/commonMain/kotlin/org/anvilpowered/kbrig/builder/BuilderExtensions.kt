/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

package org.anvilpowered.kbrig.builder

import org.anvilpowered.kbrig.Command
import org.anvilpowered.kbrig.context.CommandContext

fun <S, B : ArgumentBuilder<S, B>> B.executesSingleSuccess(block: (context: CommandContext<S>) -> Unit) =
    executes { context ->
        try {
            block(context)
            Command.SINGLE_SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
