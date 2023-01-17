package com.transferwise.sequencelayout

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
internal fun SequenceStep(
    date: AnnotatedString,
    title: AnnotatedString,
    subTitle: AnnotatedString?,
    state: State,
    stateProperty: StateProperty,
    drawDivider: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                state = state,
                stateProperty = stateProperty,
            )
            if (drawDivider) {
                Canvas(
                    modifier = Modifier
                        .size(width = 1.dp, height = 42.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    drawLine(
                        color = stateProperty.dividerColor,
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = 0f, y = size.height),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.size(27.dp))
        Column(
            modifier = Modifier
                .padding(top = 6.dp)
        ) {
            BasicText(text = date, style = stateProperty.dateTextStyle)
            Spacer(modifier = Modifier.size(4.dp))
            BasicText(text = title, style = stateProperty.titleTextStyle)

            if (subTitle != null) {
                Spacer(modifier = Modifier.size(4.dp))
                BasicText(text = subTitle, style = stateProperty.subTitleTextStyle)
            }
        }
    }
}

@Composable
private fun Image(
    modifier: Modifier = Modifier,
    state: State,
    stateProperty: StateProperty,
) {
    when (state) {
        State.ACTIVE -> Canvas(
            modifier = modifier
                .padding(6.dp)
                .size(22.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            drawCircle(
                color = stateProperty.color,
                radius = 5.dp.toPx(),
                center = Offset(x = canvasWidth / 2F, y = canvasHeight / 2F),
            )
        }
        State.COMPLETED -> Icon(
            modifier = modifier
                .padding(6.dp)
                .size(22.dp),
            painter = painterResource(id = R.drawable.ic_check),
            tint = stateProperty.color,
            contentDescription = null
        )
        State.UPCOMING -> Canvas(
            modifier = modifier
                .padding(6.dp)
                .size(22.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            drawCircle(
                color = stateProperty.color,
                radius = 5.dp.toPx(),
                center = Offset(x = canvasWidth / 2F, y = canvasHeight / 2F),
                style = Stroke(width = 1.dp.toPx())
            )
        }
    }
}

enum class State {
    COMPLETED,
    ACTIVE,
    UPCOMING,
}

data class StateProperty(
    val color: Color,
    val dividerColor: Color,
    val dateTextStyle: TextStyle,
    val titleTextStyle: TextStyle,
    val subTitleTextStyle: TextStyle
)