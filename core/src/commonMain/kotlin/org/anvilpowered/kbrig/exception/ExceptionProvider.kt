/*
 *   KBrig - AnvilPowered.org
 *   Copyright (c) 2023 Contributors
 *
 *     Use of this source code is governed by an MIT-style license that can be found
 *     in the LICENSE file or at https://opensource.org/licenses/MIT.
 */
package org.anvilpowered.kbrig.exception

internal interface ExceptionProvider {
    val doubleTooLow: DynamicCommandExceptionType2<Double, Double>
    val doubleTooHigh: DynamicCommandExceptionType2<Double, Double>
    val floatTooLow: DynamicCommandExceptionType2<Float, Float>
    val floatTooHigh: DynamicCommandExceptionType2<Float, Float>
    val integerTooLow: DynamicCommandExceptionType2<Int, Int>
    val integerTooHigh: DynamicCommandExceptionType2<Int, Int>
    val longTooLow: DynamicCommandExceptionType2<Long, Long>
    val longTooHigh: DynamicCommandExceptionType2<Long, Long>
    val literalIncorrect: DynamicCommandExceptionType1<String>
    val readerExpectedStartOfQuote: DynamicCommandExceptionType
    val readerExpectedEndOfQuote: DynamicCommandExceptionType
    val readerInvalidEscape: DynamicCommandExceptionType1<String>
    val readerInvalidBool: DynamicCommandExceptionType1<String>
    val readerInvalidInt: DynamicCommandExceptionType1<String>
    val readerExpectedInt: DynamicCommandExceptionType
    val readerInvalidLong: DynamicCommandExceptionType1<String>
    val readerExpectedLong: DynamicCommandExceptionType
    val readerInvalidDouble: DynamicCommandExceptionType1<String>
    val readerExpectedDouble: DynamicCommandExceptionType
    val readerInvalidFloat: DynamicCommandExceptionType1<String>
    val readerExpectedFloat: DynamicCommandExceptionType
    val readerExpectedBool: DynamicCommandExceptionType
    val readerExpectedSymbol: DynamicCommandExceptionType1<String>
    val dispatcherUnknownCommand: DynamicCommandExceptionType
    val dispatcherUnknownArgument: DynamicCommandExceptionType
    val dispatcherExpectedArgumentSeparator: DynamicCommandExceptionType
    val dispatcherParseException: DynamicCommandExceptionType1<String>
}
