package ghazimoradi.soheil.materialcalculator.domain

import ghazimoradi.soheil.materialcalculator.domain.ExpressionWriter.Companion.NUMBERS_BETWEEN_0TO9

class ExpressionParser(
    private val calculation: String
) {

    fun parse(): List<ExpressionPart> {
        val result = mutableListOf<ExpressionPart>()
        var i = 0

        while (i < calculation.length) {
            val curChar = calculation[i]

            when {
                curChar in operationSymbols -> {
                    result.add(ExpressionPart.Op(operationFromSymbols(curChar)))
                }

                curChar.isDigit() -> {
                    i = parseNumber(i, result)
                    continue
                }

                curChar in "()" -> {
                    parseParentheses(curChar, result)
                }
            }

            i++
        }

        return result
    }

    private fun parseNumber(
        startingIndex: Int,
        result: MutableList<ExpressionPart>
    ): Int {
        var i = startingIndex

        val numberAsString = buildString {
            while (i < calculation.length && calculation[i] in "$NUMBERS_BETWEEN_0TO9.") {
                append(calculation[i])
                i++
            }
        }

        result.add(ExpressionPart.Number(numberAsString.toDouble()))
        return i
    }

    private fun parseParentheses(
        curChar: Char,
        result: MutableList<ExpressionPart>
    ) {
        result.add(
            ExpressionPart.Parentheses(
                type = when (curChar) {
                    '(' -> ParenthesesType.Opening
                    ')' -> ParenthesesType.Closing
                    else -> throw IllegalArgumentException("Invalid Parentheses type")
                }
            )
        )
    }
}