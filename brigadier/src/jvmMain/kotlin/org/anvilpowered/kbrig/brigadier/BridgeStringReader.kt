/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

package org.anvilpowered.kbrig.brigadier

import org.anvilpowered.kbrig.StringReader
import com.mojang.brigadier.StringReader as BrigadierStringReader

internal class BridgeStringReader(private val delegate: BrigadierStringReader) : StringReader {
    override val string: String by delegate::string
    override var cursor: Int by delegate::cursor
    override fun read(): Char = delegate.read()
    override fun skip() = delegate.skip()
    override fun skipWhitespace() = delegate.skipWhitespace()
    override fun readInt(): Int = delegate.readInt()
    override fun readLong(): Long = delegate.readLong()
    override fun readDouble(): Double = delegate.readDouble()
    override fun readFloat(): Float = delegate.readFloat()
    override fun readUnquotedString(): String = delegate.readUnquotedString()
    override fun readQuotedString(): String = delegate.readQuotedString()
    override fun readStringUntil(terminator: Char): String = delegate.readStringUntil(terminator)
    override fun readString(): String = delegate.readString()
    override fun readBoolean(): Boolean = delegate.readBoolean()
    override fun expect(c: Char) = delegate.expect(c)
    override fun canRead(length: Int): Boolean = delegate.canRead(length)
    override fun canRead(): Boolean = delegate.canRead()
}
