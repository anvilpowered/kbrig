/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.builder

import org.anvilpowered.kbrig.tree.LiteralCommandNode

class LiteralArgumentBuilder<S>(private val literal: String) : ArgumentBuilder<S, LiteralArgumentBuilder<S>>() {
    override val self: LiteralArgumentBuilder<S>
        get() = this

    override fun build(): LiteralCommandNode<S> =
        LiteralCommandNode(literal, command, requirement, redirect, forks, children)
}
