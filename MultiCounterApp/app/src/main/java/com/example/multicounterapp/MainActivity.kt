// package line stays as Android Studio created it, for example:
// package com.example.multicounterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multicounterapp.ui.theme.MultiCounterAppTheme   // <-- change package if needed

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultiCounterAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MultiCounterScreen()
                }
            }
        }
    }
}

// Simple data class for each counter
data class CounterItem(
    val id: Int,
    val name: String,
    var value: Int
)

@Composable
fun MultiCounterScreen() {
    // List of counters that survive recomposition
    val counters = remember {
        mutableStateListOf<CounterItem>().apply {
            // Start with 5 counters
            repeat(5) { index ->
                add(CounterItem(id = index, name = "Counter_${index + 1}", value = 0))
            }
        }
    }

    // Next id when we add new counter
    val nextId = (counters.maxOfOrNull { it.id } ?: -1) + 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Multi Counter (LazyColumn)",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Add new counter at runtime
                val newId = (counters.maxOfOrNull { it.id } ?: -1) + 1
                counters.add(
                    CounterItem(
                        id = newId,
                        name = "Counter_${newId + 1}",
                        value = 0
                    )
                )
            }
        ) {
            Text("Add Counter")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LazyColumn = scrollable list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = counters,
                key = { it.id }
            ) { item ->
                CounterRow(
                    item = item,
                    onIncrement = {
                        item.value++
                        // Trigger recomposition
                        counters[counters.indexOfFirst { it.id == item.id }] = item
                    },
                    onDecrement = {
                        item.value--
                        counters[counters.indexOfFirst { it.id == item.id }] = item
                    },
                    onRemove = {
                        counters.remove(item)
                    }
                )
            }
        }
    }
}

@Composable
fun CounterRow(
    item: CounterItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Value: ${item.value}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Button(onClick = onDecrement) {
                    Text("-")
                }
                Button(onClick = onIncrement) {
                    Text("+")
                }
                Button(onClick = onRemove) {
                    Text("X")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MultiCounterPreview() {
    MultiCounterAppTheme {
        MultiCounterScreen()
    }
}
