/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

package org.anvilpowered.kbrig

import org.anvilpowered.kbrig.context.CommandContext

@Suppress("FUN_INTERFACE_WITH_SUSPEND_FUNCTION")
fun interface SuspendingCommand<in S> {
    suspend fun executeSuspending(context: CommandContext<S>): Int
}
