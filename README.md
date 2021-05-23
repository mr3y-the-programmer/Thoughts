# Thoughts
My playground to play around with Jetpack compose

## Demo
[`Graph.kt`](https://github.com/mr3y-the-programmer/Thoughts/blob/main/app/src/main/java/com/mr3y/thoughts/components/foundation/Graph.kt)

![Graph Demo](https://github.com/mr3y-the-programmer/Thoughts/blob/main/art/Graph.PNG)

Telegram-like Reveal Effect on toggling theme
[`CircularRevealEffect.kt`](https://github.com/mr3y-the-programmer/Thoughts/blob/main/app/src/main/java/com/mr3y/thoughts/components/foundation/circularreveal/CircularRevealEffect.kt)

![Circular Reveal Demo](https://github.com/mr3y-the-programmer/Thoughts/blob/main/art/circularreveal.gif)

*Note: the screen recorder I'm currently using slows down the rendering, hence it drops some frames and you see janky animation, but in reality it is much smoother than that & even further optimizations can be applied. anyway I'll try to provide updated gif soon*

[`Stairs.kt`](https://github.com/mr3y-the-programmer/Thoughts/blob/main/app/src/main/java/com/mr3y/thoughts/components/foundation/stairs/Stairs.kt)

![Stairs Effect Demo](https://github.com/mr3y-the-programmer/Thoughts/blob/main/art/stairs.gif)

### WIP
Also, there is a Work-in-progress DraggableColumn/Row  [`DraggableColumn.kt`](https://github.com/mr3y-the-programmer/Thoughts/blob/main/app/src/main/java/com/mr3y/thoughts/components/foundation/draggable/DraggableColumn.kt)
[`DraggableRow.kt`](https://github.com/mr3y-the-programmer/Thoughts/blob/main/app/src/main/java/com/mr3y/thoughts/components/foundation/draggable/DraggableRow.kt) respectively.

Demo of [`DraggableColumn.kt`](https://github.com/mr3y-the-programmer/Thoughts/blob/main/app/src/main/java/com/mr3y/thoughts/components/foundation/draggable/DraggableColumn.kt)'s API

```
@Preview(widthDp = 360, heightDp = 640)
@Composable
fun DraggableColumnDemo() {
    val state = rememberDragState(itemsNum = 15)
    DraggableColumn(state = state, itemSpacing = 20.dp) {
        Button(onClick = { /*TODO*/ }, modifier = Modifier.size(96.dp, 48.dp)) {
            Text(text = "Button $currentItemIndex")
        }
    }
}
```

## License
```
Copyright [2021] [MR3YY]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
