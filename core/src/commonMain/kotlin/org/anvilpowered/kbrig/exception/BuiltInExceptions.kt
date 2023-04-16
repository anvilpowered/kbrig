/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.exception

internal object BuiltInExceptions : ExceptionProvider {
    override val doubleTooLow: DynamicCommandExceptionType2<Double, Double> =
        CommandExceptionType.of { min, found -> "Double must not be less than $min, found $found" }
    override val doubleTooHigh: DynamicCommandExceptionType2<Double, Double> =
        CommandExceptionType.of { max, found -> "Double must not be more than $max, found $found" }
    override val floatTooLow: DynamicCommandExceptionType2<Float, Float> =
        CommandExceptionType.of { min, found -> "Float must not be less than $min, found $found" }
    override val floatTooHigh: DynamicCommandExceptionType2<Float, Float> =
        CommandExceptionType.of { max, found -> "Float must not be more than $max, found $found" }
    override val integerTooLow: DynamicCommandExceptionType2<Int, Int> =
        CommandExceptionType.of { min, found -> "Integer must not be less than $min, found $found" }
    override val integerTooHigh: DynamicCommandExceptionType2<Int, Int> =
        CommandExceptionType.of { max, found -> "Integer must not be more than $max, found $found" }
    override val longTooLow: DynamicCommandExceptionType2<Long, Long> =
        CommandExceptionType.of { min, found -> "Long must not be less than $min, found $found" }
    override val longTooHigh: DynamicCommandExceptionType2<Long, Long> =
        CommandExceptionType.of { max, found -> "Long must not be more than $max, found $found" }
    override val literalIncorrect: DynamicCommandExceptionType1<String> =
        CommandExceptionType.of { found -> "Expected literal $found" }
    override val readerExpectedStartOfQuote: DynamicCommandExceptionType =
        CommandExceptionType.of { "Expected quote to start a string" }
    override val readerExpectedEndOfQuote: DynamicCommandExceptionType =
        CommandExceptionType.of { "Unclosed quoted string" }
    override val readerInvalidEscape: DynamicCommandExceptionType1<String> =
        CommandExceptionType.of { escape -> "Invalid escape sequence '$escape' in quoted string" }
    override val readerInvalidBool: DynamicCommandExceptionType1<String> =
        CommandExceptionType.of { value -> "Invalid bool, expected true or false but found $value" }
    override val readerInvalidInt: DynamicCommandExceptionType1<String> =
        CommandExceptionType.of { value -> "Invalid integer $value" }
    override val readerExpectedInt: DynamicCommandExceptionType =
        CommandExceptionType.of { "Expected integer" }
    override val readerInvalidLong: DynamicCommandExceptionType1<String> =
        CommandExceptionType.of { value -> "Invalid long $value" }
    override val readerExpectedLong: DynamicCommandExceptionType =
        CommandExceptionType.of { "Expected long" }
    override val readerInvalidDouble: DynamicCommandExceptionType1<String> =
        CommandExceptionType.of { value -> "Invalid double $value" }
    override val readerExpectedDouble: DynamicCommandExceptionType =
        CommandExceptionType.of { "Expected double" }
    override val readerInvalidFloat: DynamicCommandExceptionType1<String> =
        CommandExceptionType.of { value -> "Invalid float $value" }
    override val readerExpectedFloat: DynamicCommandExceptionType =
        CommandExceptionType.of { "Expected float" }
    override val readerExpectedBool: DynamicCommandExceptionType =
        CommandExceptionType.of { "Expected bool" }
    override val readerExpectedSymbol: DynamicCommandExceptionType1<String> =
        CommandExceptionType.of { symbol -> "Expected $symbol" }
    override val dispatcherUnknownCommand: DynamicCommandExceptionType =
        CommandExceptionType.of { "Unknown command" }
    override val dispatcherUnknownArgument: DynamicCommandExceptionType =
        CommandExceptionType.of { "Incorrect argument for command" }
    override val dispatcherExpectedArgumentSeparator: DynamicCommandExceptionType =
        CommandExceptionType.of { "Expected whitespace to end one argument, but found trailing data" }
    override val dispatcherParseException: DynamicCommandExceptionType1<String> =
        CommandExceptionType.of { message -> "Could not parse command: $message" }
}
