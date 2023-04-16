/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

package org.anvilpowered.kbrig.argument

interface RangedArgumentType<T : Comparable<T>> : ArgumentType<T> {
    val minimum: T
    val maximum: T

    interface Companion<T : Comparable<T>> : ArgumentType.Companion<T> {
        val all: RangedArgumentType<T>
    }
}
