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

    @Test
    fun `Entering multiply divide or percent at start does not crash and is ignored`() {
        val emptyWriter1 = ExpressionWriter()
        emptyWriter1.processAction(CalculatorAction.Op(Operation.MULTIPLY))
        assertThat(emptyWriter1.expression).isEmpty()

        val emptyWriter2 = ExpressionWriter()
        emptyWriter2.processAction(CalculatorAction.Op(Operation.DIVIDE))
        assertThat(emptyWriter2.expression).isEmpty()

        val emptyWriter3 = ExpressionWriter()
        emptyWriter3.processAction(CalculatorAction.Op(Operation.PERCENT))
        assertThat(emptyWriter3.expression).isEmpty()
    }

    @Test
    fun `Entering multiply divide or percent after add subtract or opening parenthesis is ignored`() {
        writer.apply {
            processAction(CalculatorAction.Op(Operation.ADD))
            processAction(CalculatorAction.Op(Operation.MULTIPLY))
            processAction(CalculatorAction.Op(Operation.DIVIDE))
            processAction(CalculatorAction.Op(Operation.PERCENT))
            assertThat(expression).isEqualTo("+")
        }

        val parenWriter = ExpressionWriter()
        parenWriter.apply {
            processAction(CalculatorAction.Parentheses)
            processAction(CalculatorAction.Op(Operation.MULTIPLY))
            processAction(CalculatorAction.Op(Operation.DIVIDE))
            processAction(CalculatorAction.Op(Operation.PERCENT))
            assertThat(expression).isEqualTo("(")
        }
    }

    @Test
    fun `Entering multiply divide or percent after number or closing parenthesis is accepted`() {
        writer.apply {
            processAction(CalculatorAction.Number(5))
            processAction(CalculatorAction.Op(Operation.MULTIPLY))
            assertThat(expression).isEqualTo("5x")
        }

        val parenWriter = ExpressionWriter()
        parenWriter.apply {
            processAction(CalculatorAction.Parentheses)
            processAction(CalculatorAction.Number(5))
            processAction(CalculatorAction.Parentheses)
            processAction(CalculatorAction.Op(Operation.DIVIDE))
            assertThat(expression).isEqualTo("(5)/")
        }
    }

    @Test
    fun `Expression is properly evaluated with Calculate`() {
        writer.apply {
            processAction(CalculatorAction.Number(5))
            processAction(CalculatorAction.Op(Operation.ADD))
            processAction(CalculatorAction.Number(4))
            processAction(CalculatorAction.Calculate)
            assertThat(expression).isEqualTo("9.0")
        }
    }
}
