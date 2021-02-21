package com.mr3y.thoughts.components.bottombar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun BottomBar() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (fabRef, bottomBarRef) = createRefs()
        FloatingActionButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .size(48.dp, 48.dp)
                .constrainAs(fabRef) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(bottomBarRef.top, 8.dp)
                }
        ) {
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .constrainAs(bottomBarRef) {
                    bottom.linkTo(parent.bottom, 0.dp)
                    top.linkTo(fabRef.bottom, 8.dp)
                },
            shape = NotchShape(
                corners = RoundedCornerShape(4.dp)
            ),
            backgroundColor = Color.Blue
        ) {
        }
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar()
}
