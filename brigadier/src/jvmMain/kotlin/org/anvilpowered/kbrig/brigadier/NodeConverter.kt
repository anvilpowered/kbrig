/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

@file:JvmName("NodeConverter")

package org.anvilpowered.kbrig.brigadier

import com.mojang.brigadier.suggestion.Suggestions
import org.anvilpowered.kbrig.Command
import org.anvilpowered.kbrig.context.CommandContext
import org.anvilpowered.kbrig.coroutine.coroutineToFuture
import org.anvilpowered.kbrig.tree.ArgumentCommandNode
import org.anvilpowered.kbrig.tree.CommandNode
import org.anvilpowered.kbrig.tree.LiteralCommandNode
import org.anvilpowered.kbrig.tree.RootCommandNode
import java.util.concurrent.CompletableFuture
import com.mojang.brigadier.Command as BrigadierCommand
import com.mojang.brigadier.context.CommandContext as BrigadierCommandContext
import com.mojang.brigadier.suggestion.SuggestionsBuilder as BrigadierSuggestionsBuilder
import com.mojang.brigadier.tree.ArgumentCommandNode as BrigadierArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode as BrigadierCommandNode
import com.mojang.brigadier.tree.LiteralCommandNode as BrigadierLiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode as BrigadierRootCommandNode

fun <S> LiteralCommandNode<S>.toBrigadier(): BrigadierLiteralCommandNode<S> {
    val original = this
    return object : BrigadierLiteralCommandNode<S>(name, command?.toBrigadier(), requirement, redirect?.toBrigadier(), null, forks) {
        override fun listSuggestions(
            context: BrigadierCommandContext<S>,
            builder: BrigadierSuggestionsBuilder,
        ): CompletableFuture<Suggestions> = coroutineToFuture {
            original.listSuggestions(context.toKBrig(), builder.toKBrig()).toBrigadier()
        }
    }
}

fun <S, T> ArgumentCommandNode<S, T>.toBrigadier(): BrigadierArgumentCommandNode<S, T> {
    return BrigadierArgumentCommandNode(
        name,
        type.toBrigadier(),
        command?.toBrigadier(),
        requirement,
        redirect?.toBrigadier(),
        null,
        forks,
        null,
    )
}

fun <S> RootCommandNode<S>.toBrigadier(): BrigadierRootCommandNode<S> {
    return BrigadierRootCommandNode<S>().also {
        children.forEach { (_, child) -> it.addChild(child.toBrigadier()) }
    }
}

fun <S> CommandNode<S>.toBrigadier(): BrigadierCommandNode<S> = when (this) {
    is LiteralCommandNode<S> -> toBrigadier()
    is ArgumentCommandNode<S, *> -> toBrigadier()
    is RootCommandNode<S> -> toBrigadier()
    else -> throw IllegalArgumentException("Unknown node type: $this")
}

fun <S> Command<S>.toBrigadier(): BrigadierCommand<S> =
    BrigadierCommand<S> { context -> execute(context.toKBrig()) }

fun <S> BrigadierCommandContext<S>.toKBrig(): CommandContext<S> =
    CommandContext<S>(source, input, { name, type -> getArgument(name, type.java) }, child?.toKBrig(), isForked)
