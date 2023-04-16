/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

package org.anvilpowered.kbrig

import org.anvilpowered.kbrig.exception.BuiltInExceptions
import org.anvilpowered.kbrig.exception.CommandSyntaxException

class StringReader(
    override val string: String,
    override var cursor: Int = 0,
) : ImmutableStringReader {

    constructor(other: StringReader) : this(other.string, other.cursor)

    private fun read(): Char = string[cursor++]

    fun skip() {
        cursor++
    }

    fun skipWhitespace() {
        while (canRead() && Character.isWhitespace(peek())) {
            skip()
        }
    }

    @Throws(CommandSyntaxException::class)
    fun readInt(): Int {
        val start = cursor
        while (canRead() && isAllowedNumber(peek())) {
            skip()
        }
        val number = string.substring(start, cursor)
        if (number.isEmpty()) {
            throw BuiltInExceptions.readerExpectedInt.createWithContext(this)
        }
        return try {
            number.toInt()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw BuiltInExceptions.readerInvalidInt
                .createWithContext(this, number)
        }
    }

    @Throws(CommandSyntaxException::class)
    fun readLong(): Long {
        val start = cursor
        while (canRead() && isAllowedNumber(peek())) {
            skip()
        }
        val number = string.substring(start, cursor)
        if (number.isEmpty()) {
            throw BuiltInExceptions.readerExpectedLong.createWithContext(this)
        }
        return try {
            number.toLong()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw BuiltInExceptions.readerInvalidLong
                .createWithContext(this, number)
        }
    }

    @Throws(CommandSyntaxException::class)
    fun readDouble(): Double {
        val start = cursor
        while (canRead() && isAllowedNumber(peek())) {
            skip()
        }
        val number = string.substring(start, cursor)
        if (number.isEmpty()) {
            throw BuiltInExceptions.readerExpectedDouble.createWithContext(this)
        }
        return try {
            number.toDouble()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw BuiltInExceptions.readerInvalidDouble
                .createWithContext(this, number)
        }
    }

    @Throws(CommandSyntaxException::class)
    fun readFloat(): Float {
        val start = cursor
        while (canRead() && isAllowedNumber(peek())) {
            skip()
        }
        val number = string.substring(start, cursor)
        if (number.isEmpty()) {
            throw BuiltInExceptions.readerExpectedFloat.createWithContext(this)
        }
        return try {
            number.toFloat()
        } catch (ex: NumberFormatException) {
            cursor = start
            throw BuiltInExceptions.readerInvalidFloat
                .createWithContext(this, number)
        }
    }

    fun readUnquotedString(): String {
        val start = cursor
        while (canRead() && isAllowedInUnquotedString(peek())) {
            skip()
        }
        return string.substring(start, cursor)
    }

    @Throws(CommandSyntaxException::class)
    fun readQuotedString(): String {
        if (!canRead()) {
            return ""
        }
        val next = peek()
        if (!isQuotedStringStart(next)) {
            throw BuiltInExceptions.readerExpectedStartOfQuote
                .createWithContext(this)
        }
        skip()
        return readStringUntil(next)
    }

    @Throws(CommandSyntaxException::class)
    fun readStringUntil(terminator: Char): String {
        val result = StringBuilder()
        var escaped = false
        while (canRead()) {
            val c = read()
            if (escaped) {
                if (c == terminator || c == SYNTAX_ESCAPE) {
                    result.append(c)
                    escaped = false
                } else {
                    cursor--
                    throw BuiltInExceptions.readerInvalidEscape
                        .createWithContext(this, c.toString())
                }
            } else if (c == SYNTAX_ESCAPE) {
                escaped = true
            } else if (c == terminator) {
                return result.toString()
            } else {
                result.append(c)
            }
        }
        throw BuiltInExceptions.readerExpectedEndOfQuote.createWithContext(this)
    }

    @Throws(CommandSyntaxException::class)
    fun readString(): String {
        if (!canRead()) {
            return ""
        }
        val next = peek()
        if (isQuotedStringStart(next)) {
            skip()
            return readStringUntil(next)
        }
        return readUnquotedString()
    }

    @Throws(CommandSyntaxException::class)
    fun readBoolean(): Boolean {
        val start = cursor
        val value = readString()
        if (value.isEmpty()) {
            throw BuiltInExceptions.readerExpectedBool
                .createWithContext(this)
        }
        return if (value == "true") {
            true
        } else if (value == "false") {
            false
        } else {
            cursor = start
            throw BuiltInExceptions.readerInvalidBool
                .createWithContext(this, value)
        }
    }

    @Throws(CommandSyntaxException::class)
    fun expect(c: Char) {
        if (!canRead() || peek() != c) {
            throw BuiltInExceptions.readerExpectedSymbol
                .createWithContext(this, c.toString())
        }
        skip()
    }

    companion object {
        private const val SYNTAX_ESCAPE = '\\'
        private const val SYNTAX_DOUBLE_QUOTE = '"'
        private const val SYNTAX_SINGLE_QUOTE = '\''
        fun isAllowedNumber(c: Char): Boolean {
            return c in '0'..'9' || c == '.' || c == '-'
        }

        fun isQuotedStringStart(c: Char): Boolean {
            return c == SYNTAX_DOUBLE_QUOTE || c == SYNTAX_SINGLE_QUOTE
        }

        fun isAllowedInUnquotedString(c: Char): Boolean {
            return c in '0'..'9' ||
                c in 'A'..'Z' ||
                c in 'a'..'z' ||
                c == '_' ||
                c == '-' ||
                c == '.' ||
                c == '+'
        }
    }
}
