/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig

import org.anvilpowered.kbrig.context.CommandContext

fun interface Command<S> {
    fun execute(context: CommandContext<S>): Int

    companion object {
        const val SINGLE_SUCCESS = 1
    }
}
