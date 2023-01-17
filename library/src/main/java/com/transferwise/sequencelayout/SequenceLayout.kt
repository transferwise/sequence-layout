package com.transferwise.sequencelayout

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SequenceLayout(
    modifier: Modifier = Modifier,
    steps: List<Step>
) {
    Column(modifier) {
        steps.mapIndexed { index, step ->
            SequenceStep(
                date = step.date,
                title = step.title,
                subTitle = step.subTitle,
                state = step.state,
                drawDivider = index != steps.size - 1,
                stateProperty = step.stateProperty,
            )
        }
    }
}

@Preview
@Composable
private fun SequenceLayoutPreview() {
    val items = buildList {
        add(Step(
            date = AnnotatedString("Today at 3:21 pm"),
            title = AnnotatedString("You set up your transfer"),
            subTitle = null,
            state = State.COMPLETED,
            stateProperty = StateProperty(
                color = Color.Black,
                dateTextStyle = TextStyle.Default,
                titleTextStyle = TextStyle.Default,
                subTitleTextStyle = TextStyle.Default,
                dividerColor = Color.LightGray,
            ),
        ))

        add(Step(
            date = AnnotatedString("Today at 3:25 pm"),
            title = AnnotatedString("You used your EUR balance"),
            subTitle = null,
            state = State.ACTIVE,
            stateProperty = StateProperty(
                color = Color.Green,
                 dateTextStyle = TextStyle.Default,
                titleTextStyle = TextStyle.Default,
                subTitleTextStyle = TextStyle.Default,
                dividerColor = Color.LightGray,
            )
        ))

        add(Step(
            date = AnnotatedString("Today at 3:40 pm"),
            title = AnnotatedString("We pay out your USD"),
            subTitle = null,
            state = State.UPCOMING,
            stateProperty = StateProperty(
                color = Color.LightGray,
                 dateTextStyle = TextStyle.Default,
                titleTextStyle = TextStyle.Default,
                subTitleTextStyle = TextStyle.Default,
                dividerColor = Color.LightGray,
            )
        ))
    }

    SequenceLayout(steps = items)
}

data class Step(
    val date: AnnotatedString,
    val title: AnnotatedString,
    val subTitle: AnnotatedString?,
    val state: State,
    val stateProperty: StateProperty,
)