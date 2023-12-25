/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

package org.anvilpowered.kbrig.context

/**
 * Marks a class as a DSL for [CommandContext.Scope].
 *
 * See [org.anvilpowered.kbrig.context.executesScoped] and [org.anvilpowered.kbrig.suggestion.suggestsScoped].
 */
@DslMarker
annotation class CommandContextScopeDsl
