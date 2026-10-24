package ghazimoradi.soheil.materialcalculator.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ExpressionWriterTest {
    private lateinit var writer: ExpressionWriter

    @Before
    fun setUp() {
        writer = ExpressionWriter()
    }

    @Test
    fun `Initial parentheses parsed`() {
        writer.apply {
            processAction(CalculatorAction.Parentheses)
            processAction(CalculatorAction.Number(5))
            processAction(CalculatorAction.Op(Operation.ADD))
            processAction(CalculatorAction.Number(4))
            processAction(CalculatorAction.Parentheses)

            assertThat(expression).isEqualTo("(5+4)")
        }
    }

    @Test
    fun `Closing parentheses at the start not parsed`() {
        writer.apply {
            processAction(CalculatorAction.Parentheses)
            processAction(CalculatorAction.Parentheses)

            assertThat(expression).isEqualTo("((")
        }
    }

    @Test
    fun `Parentheses around a number are parsed`() {
        writer.apply {
            processAction(CalculatorAction.Parentheses)
            processAction(CalculatorAction.Number(6))
            processAction(CalculatorAction.Parentheses)

            assertThat(expression).isEqualTo("(6)")
        }
    }
}