/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

package org.anvilpowered.kbrig.builder

import org.anvilpowered.kbrig.context.CommandContext

fun <S, T : ArgumentBuilder<S, T>> T.executesSingleSuccess(block: (CommandContext<S>) -> Unit) =
    executes { context ->
        try {
            block(context)
            1
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
