/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.suggestion

import org.anvilpowered.kbrig.context.CommandContext

fun interface SuggestionProvider<in S> {
    suspend fun getSuggestions(context: CommandContext<S>, builder: SuggestionsBuilder): Suggestions
}
