package ghazimoradi.soheil.materialcalculator.domain

enum class Operation(val symbol: Char) {
    ADD('+'),
    SUBTRACT('-'),
    MULTIPLY('x'),
    DIVIDE('/'),
    PERCENT('%'),
}

val operationEntries = Operation.entries

val operationSymbols = operationEntries.map { it.symbol }.joinToString("")

fun operationFromSymbols(symbol: Char): Operation {
    return operationEntries.find { it.symbol == symbol }
        ?: throw IllegalArgumentException("Invalid symbol")
}