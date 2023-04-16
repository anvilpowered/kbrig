/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

package org.anvilpowered.kbrig

import org.anvilpowered.kbrig.exception.CommandSyntaxException

interface StringReader : ImmutableStringReader {

    override var cursor: Int

    fun read(): Char

    fun skip()

    fun skipWhitespace()

    @Throws(CommandSyntaxException::class)
    fun readInt(): Int

    @Throws(CommandSyntaxException::class)
    fun readLong(): Long

    @Throws(CommandSyntaxException::class)
    fun readDouble(): Double

    @Throws(CommandSyntaxException::class)
    fun readFloat(): Float
    fun readUnquotedString(): String

    @Throws(CommandSyntaxException::class)
    fun readQuotedString(): String

    @Throws(CommandSyntaxException::class)
    fun readStringUntil(terminator: Char): String

    @Throws(CommandSyntaxException::class)
    fun readString(): String

    @Throws(CommandSyntaxException::class)
    fun readBoolean(): Boolean

    @Throws(CommandSyntaxException::class)
    fun expect(c: Char)
}
