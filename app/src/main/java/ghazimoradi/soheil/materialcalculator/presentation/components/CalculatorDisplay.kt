package ghazimoradi.soheil.materialcalculator.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorDisplay(
    expression: String,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.CenterEnd
    ) {
        val containerMaxWidth = maxWidth
        val maxWidthPx = constraints.maxWidth
        val textMeasurer = rememberTextMeasurer()
        val scrollState = rememberScrollState()

        val maxFontSize = 80f
        val minFontSize = 32f
        val step = 2f

        val optimalFontSize = remember(expression, maxWidthPx) {
            if (expression.isEmpty() || maxWidthPx <= 0) {
                maxFontSize.sp
            } else {
                var currentSize = maxFontSize
                while (currentSize > minFontSize) {
                    val measuredWidth = textMeasurer.measure(
                        text = expression,
                        style = TextStyle(fontSize = currentSize.sp)
                    ).size.width
                    if (measuredWidth <= maxWidthPx) {
                        break
                    }
                    currentSize -= step
                }
                currentSize.coerceAtLeast(minFontSize).sp
            }
        }

        LaunchedEffect(expression, scrollState.maxValue) {
            scrollState.scrollTo(scrollState.maxValue)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .defaultMinSize(minWidth = containerMaxWidth)
                    .fillMaxHeight(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = expression,
                    style = TextStyle(
                        fontSize = optimalFontSize,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.End
                    ),
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}