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

data class CommandContext<S>(
    val source: S,
    val input: String,
    val argumentFetcher: ArgumentFetcher<*>,
    val child: CommandContext<S>?,
    val forks: Boolean,
)

typealias ArgumentFetcher<T> = (String, KClass<T>) -> T?

fun <S, V : Any> CommandContext<S>.getArgument(name: String, clazz: KClass<V>): V {
    val argument = argumentFetcher(name, clazz)
        ?: throw IllegalArgumentException("No such argument '$name' exists on this command")
    return clazz.cast(argument)
}

inline operator fun <S, reified V : Any> CommandContext<S>.get(name: String) = getArgument(name, V::class)

val <S> CommandContext<S>.lastChild: CommandContext<S>
    get() = generateSequence(this) { it.child }.last()
