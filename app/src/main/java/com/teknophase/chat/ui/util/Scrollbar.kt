package com.teknophase.chat.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teknophase.chat.ui.constants.padding_extra_small
import com.teknophase.chat.ui.constants.text_small
import com.teknophase.chat.util.getFirstLetter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Math.abs

@Composable
fun <T : AlphabeticalScrollBarItem> ScrollBar(
    list: List<T>,
    modifier: Modifier = Modifier,
    item: @Composable (T) -> Unit
) {
    val items = remember { list.sortedBy { it.name.lowercase() } }
    val headers = remember { items.map { it.name.getFirstLetter().uppercase() }.toSet().toList() }
    var showAlphabet by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        val listState = rememberLazyListState()
        val offsets = remember { mutableStateMapOf<Int, Float>() }
        var selectedHeaderIndex by remember { mutableStateOf(0) }
        val scope = rememberCoroutineScope()

        Row {
            LazyColumn(
                state = listState,
                modifier = modifier.weight(1f)
            ) {
                items(items.size) {
                    if ((it == 0)
                        || (items[it - 1].name.getFirstLetter().lowercase()
                                != items[it].name.getFirstLetter().lowercase())
                    ) {
                        Text(
                            text = items[it].name.getFirstLetter().uppercase(),
                            fontSize = text_small,
                            fontWeight = FontWeight.Bold,
                            color = Color.LightGray
                        )
                        Divider(
                            color = Color.LightGray
                        )
                    }
                    item(items[it])
                }
            }


            fun updateSelectedIndexIfNeeded(offset: Float) {
                val index = offsets
                    .mapValues { abs(it.value - offset) }
                    .entries
                    .minByOrNull { it.value }
                    ?.key ?: return
                if (selectedHeaderIndex == index) return
                selectedHeaderIndex = index
                val selectedItemIndex = items.indexOfFirst {
                    it.name.getFirstLetter().uppercase() == headers[selectedHeaderIndex]
                }
                scope.launch {
                    listState.scrollToItem(selectedItemIndex, -200)
                }
                GlobalScope.launch {
                    showAlphabet = true
                    delay(2000)
                    showAlphabet = false
                }
            }

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = padding_extra_small)
                    .width(16.dp)
//                    .background(Color.LightGray)
                    .pointerInput(Unit) {
                        detectTapGestures {
                            updateSelectedIndexIfNeeded(it.y)
                        }
                    }
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { change, _ ->
                            updateSelectedIndexIfNeeded(change.position.y)
                        }
                    },

                ) {
                headers.forEachIndexed { i, s ->
                    Text(
                        headers[i].uppercase(),
                        modifier = Modifier
                            .onGloballyPositioned {
                                offsets[i] = it.boundsInParent().center.y
                            },
                        fontWeight = if (selectedHeaderIndex == i) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        if (showAlphabet)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = headers[selectedHeaderIndex],
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray
                )
            }

    }
}

@Preview
@Composable
fun ScrollBarPreview() {
    val list = mutableListOf<AlphabeticalScrollBarItem>()
    for (item in LoremIpsum().values.first().split(" ").toSet()) list.add(
        object : AlphabeticalScrollBarItem {
            override val name: String
                get() = item
        }
    )
    Surface {
        ScrollBar(
            list.toList(),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) { item ->
            Text(item.name.replaceFirstChar { it.uppercase() }, fontSize = 15.sp)
        }
    }
}

interface AlphabeticalScrollBarItem {
    val name: String
}