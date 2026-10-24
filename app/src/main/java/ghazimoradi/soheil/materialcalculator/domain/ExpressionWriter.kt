package ghazimoradi.soheil.materialcalculator.domain;

class ExpressionWriter {

    companion object {
        const val NUMBERS_BETWEEN_0TO9 = "0123456789"
    }

    var expression = ""

    fun processAction(action: CalculatorAction) {
        when (action) {

            CalculatorAction.Calculate -> {
                val parser = ExpressionParser(prepareForCalculation())
                val evaluator = ExpressionEvaluator(parser.parse())
                expression = evaluator.evaluate().toString()
            }

            CalculatorAction.Clear -> expression = ""

            CalculatorAction.Decimal -> {
                if (canEnterDecimal()) {
                    expression += "."
                }
            }

            CalculatorAction.Delete -> expression = expression.dropLast(1)

            is CalculatorAction.Number -> expression += action.number

            is CalculatorAction.Op -> {
                if (canEnterOperation(action.operation)) {
                    expression += action.operation.symbol
                }
            }

            CalculatorAction.Parentheses -> processParentheses()
        }
    }

    private fun processParentheses() {
        val openingCount = expression.count { it == '(' }
        val closingCount = expression.count { it == ')' }

        expression += when {
            expression.isEmpty() || expression.last() in "$operationSymbols(" -> "("
            expression.last() in "$NUMBERS_BETWEEN_0TO9)" && openingCount == closingCount -> return
            else -> ")"
        }
    }

    private fun canEnterDecimal(): Boolean {

        if (expression.isEmpty() || expression.last() in "$operationSymbols.()") {
            return false
        }

        return !expression.takeLastWhile {
            it in "$NUMBERS_BETWEEN_0TO9."
        }.contains(".")
    }

    private fun canEnterOperation(operation: Operation): Boolean {
        if (operation in listOf(Operation.ADD, Operation.SUBTRACT)) {
            return expression.isEmpty() || expression.last() in "$operationSymbols()$NUMBERS_BETWEEN_0TO9"
        }

        return expression.isNotEmpty() || expression.last() in "$NUMBERS_BETWEEN_0TO9)"
    }

    private fun prepareForCalculation(): String {

        val newExpression = expression.takeLastWhile {
            it in "$operationSymbols(."
        }

        return newExpression.ifEmpty { "0" }
    }
}