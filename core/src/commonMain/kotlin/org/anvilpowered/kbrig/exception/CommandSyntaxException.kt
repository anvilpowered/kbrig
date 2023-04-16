/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.exception

import kotlin.math.max
import kotlin.math.min

class CommandSyntaxException(message: String) : Exception(message) {

    constructor(message: String, input: String, cursor: Int) : this(message + getContext(input, cursor))

    companion object {
        private const val CONTEXT_AMOUNT = 10

        private fun getContext(input: String, cursor: Int): String {
            require(cursor >= 0)
            val builder = StringBuilder()
            val adjustedCursor = min(input.length, cursor)
            if (adjustedCursor > CONTEXT_AMOUNT) {
                builder.append("...")
            }
            builder.append(input, max(0, adjustedCursor - CONTEXT_AMOUNT), adjustedCursor)
            builder.append("<--[HERE]")
            return builder.toString()
        }
    }
}
