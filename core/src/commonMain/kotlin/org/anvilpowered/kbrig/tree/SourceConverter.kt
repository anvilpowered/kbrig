/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
@file:JvmName("SourceConverter")

package org.anvilpowered.kbrig.tree

import org.anvilpowered.kbrig.Command
import org.anvilpowered.kbrig.context.CommandContext
import org.anvilpowered.kbrig.suggestion.SuggestionProvider
import kotlin.jvm.JvmName

/**
 * Converts the given [ArgumentCommandNode] with source type [S] to an [ArgumentCommandNode] with source type [R].
 */
fun <S, R, T> ArgumentCommandNode<S, T>.mapSource(mapper: (R) -> S): ArgumentCommandNode<R, T> =
    ArgumentCommandNode(
        name,
        type,
        command?.mapSource(mapper),
        requirement.mapSource(mapper),
        redirect?.mapSource(mapper),
        forks,
        children.mapValues { (_, child) -> child.mapSource(mapper) },
        customSuggestions?.mapSource(mapper),
    )

/**
 * Converts the given [LiteralCommandNode] with source type [S] to an [LiteralCommandNode] with source type [R].
 */
fun <S, R> LiteralCommandNode<S>.mapSource(mapper: (R) -> S): LiteralCommandNode<R> =
    LiteralCommandNode(
        name,
        command?.mapSource(mapper),
        requirement.mapSource(mapper),
        redirect?.mapSource(mapper),
        forks,
        children.mapValues { (_, child) -> child.mapSource(mapper) },
    )

/**
 * Converts the given [RootCommandNode] with source type [S] to an [RootCommandNode] with source type [R].
 */
fun <S, R> RootCommandNode<S>.mapSource(mapper: (R) -> S): RootCommandNode<R> =
    RootCommandNode(children.mapValues { (_, child) -> child.mapSource(mapper) })

/**
 * Converts the given [CommandNode] with source type [S] to an [CommandNode] with source type [R].
 *
 * Only works for the standard node types:
 * - [ArgumentCommandNode]
 * - [LiteralCommandNode]
 * - [RootCommandNode]
 */
fun <S, R> CommandNode<S>.mapSource(mapper: (R) -> S): CommandNode<R> = when (this) {
    is LiteralCommandNode<S> -> mapSource(mapper)
    is ArgumentCommandNode<S, *> -> mapSource(mapper)
    is RootCommandNode<S> -> mapSource(mapper)
    else -> throw IllegalStateException("Unknown CommandNode type: $this")
}

/**
 * Converts the given [Command] with source type [S] to an [Command] with source type [R].
 */
private fun <S, R> Command<S>.mapSource(mapper: (R) -> S): Command<R> =
    Command { context -> execute(context.mapToOriginalSource(mapper)) }

/**
 * Converts a predicate with source type [S] to a predicate with source type [R].
 */
private fun <S, R> ((S) -> Boolean).mapSource(mapper: (R) -> S): (R) -> Boolean = { this(mapper(it)) }

private fun <S, R> SuggestionProvider<S>.mapSource(mapper: (R) -> S): SuggestionProvider<R> =
    SuggestionProvider { context, builder -> getSuggestions(context.mapToOriginalSource(mapper), builder) }

/**
 * Converts the given [CommandContext] with source type [R] to an [CommandContext] with source type [S].
 */
private fun <S, R> CommandContext<R>.mapToOriginalSource(mapper: (R) -> S): CommandContext<S> =
    CommandContext(mapper(source), input, argumentFetcher, child?.mapToOriginalSource(mapper), forks)
