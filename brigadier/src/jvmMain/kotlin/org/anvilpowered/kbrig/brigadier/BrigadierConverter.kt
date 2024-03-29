/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

@file:JvmName("BrigadierConverter")

package org.anvilpowered.kbrig.brigadier

import org.anvilpowered.kbrig.Command
import org.anvilpowered.kbrig.StringReader
import org.anvilpowered.kbrig.argument.ArgumentType
import org.anvilpowered.kbrig.argument.BooleanArgumentType
import org.anvilpowered.kbrig.argument.DoubleArgumentType
import org.anvilpowered.kbrig.argument.FloatArgumentType
import org.anvilpowered.kbrig.argument.IntegerArgumentType
import org.anvilpowered.kbrig.argument.LongArgumentType
import org.anvilpowered.kbrig.argument.StringArgumentType
import org.anvilpowered.kbrig.context.CommandContext
import org.anvilpowered.kbrig.context.StringRange
import org.anvilpowered.kbrig.coroutine.coroutineToFuture
import org.anvilpowered.kbrig.suggestion.Suggestion
import org.anvilpowered.kbrig.suggestion.SuggestionProvider
import org.anvilpowered.kbrig.suggestion.Suggestions
import org.anvilpowered.kbrig.suggestion.SuggestionsBuilder
import org.anvilpowered.kbrig.tree.ArgumentCommandNode
import org.anvilpowered.kbrig.tree.CommandNode
import org.anvilpowered.kbrig.tree.LiteralCommandNode
import org.anvilpowered.kbrig.tree.RootCommandNode
import java.util.concurrent.CompletableFuture
import com.mojang.brigadier.Command as BrigadierCommand
import com.mojang.brigadier.StringReader as BrigadierStringReader
import com.mojang.brigadier.arguments.ArgumentType as BrigadierArgumentType
import com.mojang.brigadier.arguments.BoolArgumentType as BrigadierBooleanArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType as BrigadierDoubleArgumentType
import com.mojang.brigadier.arguments.FloatArgumentType as BrigadierFloatArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType as BrigadierIntegerArgumentType
import com.mojang.brigadier.arguments.LongArgumentType as BrigadierLongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType as BrigadierStringArgumentType
import com.mojang.brigadier.context.CommandContext as BrigadierCommandContext
import com.mojang.brigadier.context.StringRange as BrigadierStringRange
import com.mojang.brigadier.suggestion.Suggestion as BrigadierSuggestion
import com.mojang.brigadier.suggestion.SuggestionProvider as BrigadierSuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions as BrigadierSuggestions
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
        ): CompletableFuture<BrigadierSuggestions> = coroutineToFuture {
            original.listSuggestions(context.toKBrig(), builder.toKBrig()).toBrigadier()
        }
    }.withChildrenFrom(this)
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
        customSuggestions?.toBrigadier(),
    ).withChildrenFrom(this)
}

fun <S> RootCommandNode<S>.toBrigadier(): BrigadierRootCommandNode<S> {
    return BrigadierRootCommandNode<S>().withChildrenFrom(this)
}

fun <S> CommandNode<S>.toBrigadier(): BrigadierCommandNode<S> = when (this) {
    is LiteralCommandNode<S> -> toBrigadier()
    is ArgumentCommandNode<S, *> -> toBrigadier()
    is RootCommandNode<S> -> toBrigadier()
    else -> throw IllegalArgumentException("Unknown node type: $this")
}

private fun <S, N : BrigadierCommandNode<S>> N.withChildrenFrom(original: CommandNode<S>): N {
    original.children.forEach { (_, child) -> addChild(child.toBrigadier()) }
    return this
}

private fun <S> Command<S>.toBrigadier(): BrigadierCommand<S> =
    BrigadierCommand<S> { context -> execute(context.toKBrig()) }

private fun <S> BrigadierCommandContext<S>.toKBrig(): CommandContext<S> =
    CommandContext<S>(source, input, { name, type -> getArgument(name, type.java) }, child?.toKBrig(), isForked)

/*
 * ========================
 * Argument types
 * ========================
 */

private fun <T> ArgumentType<T>.toBrigadier(): BrigadierArgumentType<T> {
    val original = this

    // first convert standard types to their brigadier equivalents
    original.toDefaultBrigadierArgument()?.let { return it }

    return object : BrigadierArgumentType<T> {
        override fun parse(reader: BrigadierStringReader): T = original.parse(reader.toKBrig())

        override fun <S : Any?> listSuggestions(
            context: BrigadierCommandContext<S>,
            builder: BrigadierSuggestionsBuilder,
        ): CompletableFuture<BrigadierSuggestions> = coroutineToFuture {
            original.listSuggestions(context.toKBrig(), builder.toKBrig()).toBrigadier()
        }

        override fun getExamples(): Collection<String> = original.examples
    }
}

private fun <T> ArgumentType<T>.toDefaultBrigadierArgument(): BrigadierArgumentType<T>? {
    @Suppress("UNCHECKED_CAST")
    return when (this) {
        is BooleanArgumentType -> BrigadierBooleanArgumentType.bool() as BrigadierArgumentType<T>
        is DoubleArgumentType -> BrigadierDoubleArgumentType.doubleArg(minimum, maximum) as BrigadierArgumentType<T>
        is FloatArgumentType -> BrigadierFloatArgumentType.floatArg(minimum, maximum) as BrigadierArgumentType<T>
        is IntegerArgumentType -> BrigadierIntegerArgumentType.integer(minimum, maximum) as BrigadierArgumentType<T>
        is LongArgumentType -> BrigadierLongArgumentType.longArg(minimum, maximum) as BrigadierArgumentType<T>
        is StringArgumentType -> when (this) {
            StringArgumentType.GreedyPhrase -> BrigadierStringArgumentType.greedyString() as BrigadierArgumentType<T>
            StringArgumentType.SingleWord -> BrigadierStringArgumentType.word() as BrigadierArgumentType<T>
            StringArgumentType.QuotablePhrase -> BrigadierStringArgumentType.string() as BrigadierArgumentType<T>
        }
        else -> null
    }
}

private fun <S> SuggestionProvider<S>.toBrigadier(): BrigadierSuggestionProvider<S> {
    val original = this
    return BrigadierSuggestionProvider { context, builder ->
        coroutineToFuture {
            original.getSuggestions(context.toKBrig(), builder.toKBrig()).toBrigadier()
        }
    }
}

private fun Suggestions.toBrigadier(): BrigadierSuggestions = BrigadierSuggestions(range.toBrigadier(), list.map { it.toBrigadier() })
private fun Suggestion.toBrigadier(): BrigadierSuggestion = BrigadierSuggestion(range.toBrigadier(), text)
private fun StringRange.toBrigadier(): BrigadierStringRange = BrigadierStringRange(start, end)
private fun BrigadierSuggestionsBuilder.toKBrig(): SuggestionsBuilder = SuggestionsBuilder(input, start)
private fun BrigadierStringReader.toKBrig(): StringReader = BridgeStringReader(this)
