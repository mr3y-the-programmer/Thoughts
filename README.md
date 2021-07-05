# Thoughts
My playground to play around with Jetpack compose

## Demo
| [`RecursionLoop.kt`](https://github.com/mr3y-the-programmer/Thoughts/blob/main/app/src/main/java/com/mr3y/thoughts/components/foundation/recursion/RecursionLoop.kt) | [`DockedBottomBar.kt`](https://github.com/mr3y-the-programmer/Thoughts/blob/main/app/src/main/java/com/mr3y/thoughts/components/bottombar/DockedBottomBar.kt) | [`Stairs.kt`](https://github.com/mr3y-the-programmer/Thoughts/blob/main/app/src/main/java/com/mr3y/thoughts/components/foundation/stairs/Stairs.kt) |
| :--- | :---: | ---: |
| ![Recursion Loop](https://github.com/mr3y-the-programmer/Thoughts/blob/main/art/Recursion.gif) | ![Docked bottom bar Demo](https://github.com/mr3y-the-programmer/Thoughts/blob/main/art/DockedBottomBar.gif) | ![Stairs Effect Demo](https://github.com/mr3y-the-programmer/Thoughts/blob/main/art/stairs.gif) |
| [`Graph.kt`](https://github.com/mr3y-the-programmer/Thoughts/blob/main/app/src/main/java/com/mr3y/thoughts/components/foundation/Graph.kt) | Telegram-like Reveal Effect on toggling theme, see [`CircularRevealEffect.kt`](https://github.com/mr3y-the-programmer/Thoughts/blob/main/app/src/main/java/com/mr3y/thoughts/components/foundation/circularreveal/CircularRevealEffect.kt)
| ![Graph Demo](https://github.com/mr3y-the-programmer/Thoughts/blob/main/art/Graph.PNG) | ![Circular Reveal Demo](https://github.com/mr3y-the-programmer/Thoughts/blob/main/art/circularreveal.gif) |

### WIP
Also, there is a Work-in-progress DraggableColumn/Row  [`DraggableColumn.kt`](https://github.com/mr3y-the-programmer/Thoughts/blob/main/app/src/main/java/com/mr3y/thoughts/components/foundation/draggable/DraggableColumn.kt)
[`DraggableRow.kt`](https://github.com/mr3y-the-programmer/Thoughts/blob/main/app/src/main/java/com/mr3y/thoughts/components/foundation/draggable/DraggableRow.kt) respectively.

Demo of [`DraggableColumn.kt`](https://github.com/mr3y-the-programmer/Thoughts/blob/main/app/src/main/java/com/mr3y/thoughts/components/foundation/draggable/DraggableColumn.kt)'s API

```kotlin
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
