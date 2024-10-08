package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

data class GreetingItem(
    val name: String,
    val title: String,
    val description: String,
)


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = false

            ) {
                val systemUiController= rememberSystemUiController()
                systemUiController.setStatusBarColor(
                    color = MaterialTheme.colorScheme.primary,
                    darkIcons = !isSystemInDarkTheme()
                )
                MyApp(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val list = remember { mutableStateListOf(
        GreetingItem("Name1", "Title1", "Description1"),
        GreetingItem("Name2", "Title2", "Description2"),
    ) }
    var showAddScreen by remember { mutableStateOf(false) }

    Surface(
        modifier=modifier
    ) {
            Greetings(
                modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                onClicked = {showAddScreen=true},
                names = list,
            )
        if (showAddScreen) {
            AddScreen(
                onSubmit = {list.add(it)
                            showAddScreen = false },
                onCancel = { showAddScreen = false }
            )
        }
        }
    }


@Composable
fun AddScreen(
    onSubmit: (GreetingItem) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    AlertDialog(
        shape = RoundedCornerShape(5),
        onDismissRequest = onCancel,
        title = { Text("Add New Item") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Enter title") },
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10)),
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Enter description") },
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10)),
                )
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Enter name") },
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10)),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmit(GreetingItem(name=name, title=title, description=description))
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}



@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit,
    names: List<GreetingItem>,
) {
    Box(modifier = modifier) {
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
            items(items = names) { item ->
                Greeting(item)
            }
        }
        LargeFloatingActionButton(
            onClick = onClicked,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }
    }
}

@Composable
private fun Greeting(item: GreetingItem, modifier: Modifier = Modifier) { // Use the data class
    Card(
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        CardContent(item)
    }
}

@Composable
private fun CardContent(item: GreetingItem, modifier: Modifier = Modifier) { // Use the data class
    var expanded by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(item.title)
            Text(item.name, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold))
            if (expanded) {
                Text(item.description)
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) { "Show More" } else { "Show Less" }
            )
        }
    }
}




