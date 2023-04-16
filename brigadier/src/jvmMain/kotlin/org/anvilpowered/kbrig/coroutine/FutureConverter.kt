/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

package org.anvilpowered.kbrig.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ForkJoinPool

private val dispatcher = ForkJoinPool.commonPool().asCoroutineDispatcher()

internal fun <T> coroutineToFuture(block: suspend () -> T): CompletableFuture<T> =
    CoroutineScope(dispatcher).async { block() }.asCompletableFuture()
