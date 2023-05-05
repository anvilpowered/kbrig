/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

package org.anvilpowered.kbrig

interface ImmutableStringReader {

    /**
     * The underlying string.
     */
    val string: String

    /**
     * The current cursor position.
     */
    val cursor: Int

    /**
     * The number of characters remaining that may be read.
     */
    val remainingLength: Int
        get() = totalLength - cursor

    /**
     * The total number of characters in the string.
     */
    val totalLength: Int
        get() = string.length

    /**
     * The substring from the start of the string to the current cursor position.
     */
    val previous: String
        get() = string.substring(0, cursor)

    /**
     * The substring from the current cursor position to the end of the string.
     */
    val remaining: String
        get() = string.substring(cursor)

    /**
     * Whether [length] number of characters can be read.
     */
    fun canRead(length: Int): Boolean = cursor + length <= totalLength

    /**
     * Whether 1 character can be read.
     */
    fun canRead(): Boolean = canRead(1)

    /**
     * Returns the character at the current cursor position + [offset] but does not advance the cursor.
     */
    fun peek(offset: Int): Char = string[cursor + offset]

    /**
     * Returns the character at the current cursor position but does not advance the cursor.
     */
    fun peek(): Char = peek(0)
}
