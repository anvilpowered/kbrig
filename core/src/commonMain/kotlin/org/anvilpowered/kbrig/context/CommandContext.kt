/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.context

import kotlin.reflect.KClass
import kotlin.reflect.cast

data class CommandContext<out S>(
    val source: S,
    val input: String,
    val argumentFetcher: ArgumentFetcher<*>,
    val child: CommandContext<S>?,
    val forks: Boolean,
) {
    interface Scope<out S> {
        /**
         * The [CommandContext] of the command being executed.
         */
        val context: CommandContext<S>

        @CommandContextScopeDsl
        suspend fun abort(): Nothing
    }
}

typealias ArgumentFetcher<T> = (String, KClass<T>) -> T?

operator fun <T : Any> CommandContext<*>.get(name: String, clazz: KClass<T>): T {
    val argument = argumentFetcher(name, clazz)
        ?: throw IllegalArgumentException("No such argument '$name' exists on this command")
    return clazz.cast(argument)
}

inline operator fun <reified T : Any> CommandContext<*>.get(name: String) = get(name, T::class)

val <S> CommandContext<S>.lastChild: CommandContext<S>
    get() = generateSequence(this) { it.child }.last()
