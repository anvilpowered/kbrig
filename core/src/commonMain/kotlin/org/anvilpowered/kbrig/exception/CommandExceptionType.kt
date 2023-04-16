/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.exception

import org.anvilpowered.kbrig.ImmutableStringReader

sealed interface CommandExceptionType {
    companion object {
        fun of(messageSupplier: () -> String) = DynamicCommandExceptionType(messageSupplier)
        fun <A> of(messageSupplier: (A) -> String) = DynamicCommandExceptionType1(messageSupplier)
        fun <A, B> of(messageSupplier: (A, B) -> String) = DynamicCommandExceptionType2(messageSupplier)
    }
}

class DynamicCommandExceptionType internal constructor(
    private val messageSupplier: () -> String,
) : CommandExceptionType {
    fun create(): CommandSyntaxException =
        CommandSyntaxException(messageSupplier())

    fun createWithContext(reader: ImmutableStringReader): CommandSyntaxException =
        CommandSyntaxException(messageSupplier(), reader.string, reader.cursor)
}

class DynamicCommandExceptionType1<A> internal constructor(
    private val messageSupplier: (A) -> String,
) : CommandExceptionType {
    fun create(a: A): CommandSyntaxException =
        CommandSyntaxException(messageSupplier(a))

    fun createWithContext(reader: ImmutableStringReader, a: A): CommandSyntaxException =
        CommandSyntaxException(messageSupplier(a), reader.string, reader.cursor)
}

class DynamicCommandExceptionType2<A, B> internal constructor(
    private val messageSupplier: (A, B) -> String,
) : CommandExceptionType {
    fun create(a: A, b: B): CommandSyntaxException =
        CommandSyntaxException(messageSupplier(a, b))

    fun createWithContext(reader: ImmutableStringReader, a: A, b: B): CommandSyntaxException =
        CommandSyntaxException(messageSupplier(a, b), reader.string, reader.cursor)
}
