/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */

package org.anvilpowered.kbrig.argument

import org.anvilpowered.kbrig.context.CommandContext
import org.anvilpowered.kbrig.context.get
import kotlin.reflect.KProperty

inline operator fun <reified T> CommandContext<*>.getValue(thisRef: Any?, property: KProperty<*>): T = this[property.name]
