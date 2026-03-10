package com.example.affirmations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.affirmations.data.Datasource
import com.example.affirmations.model.Affirmation
import com.example.affirmations.ui.theme.AffirmationsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AffirmationsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainPage()
                }
            }
        }
    }
}

@Composable
fun MainPage(modifier: Modifier = Modifier) {
    var selectedAffirmation by remember { mutableStateOf<Affirmation?> (null)}

    AffirmationsList(
        affirmationList = Datasource().loadAffirmations(),
        onAffirmationClick = { affirmation ->
            selectedAffirmation = affirmation
        }
    )
    if (selectedAffirmation != null) {
        AffirmationDialog(
            affirmation = selectedAffirmation!!,
            onDismiss = { selectedAffirmation = null }
        )
    }
}

@Composable
fun AffirmationDialog(affirmation: Any, onDismiss: () -> Unit) {
    TODO("Not yet implemented")
}

@Composable
fun AffirmationCard(affirmation: Affirmation, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Card(modifier = modifier.clickable {
        Toast.makeText(context, context.getString(affirmation.stringResourceId), Toast.LENGTH_SHORT).show()
        onClick()
    }) {
        Column {
            Image(
                painter = painterResource(affirmation.imageResourceId),
                contentDescription = stringResource(affirmation.stringResourceId),
                modifier = Modifier.fillMaxWidth().height(194.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = stringResource(affirmation.stringResourceId),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}


@Composable
fun AffirmationsList(
    affirmationList: List<Affirmation>,
    onAffirmationClick: (Affirmation) -> Unit,
    modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(affirmationList) { affirmation ->
        AffirmationCard(
            affirmation = affirmation,
            onClick = { onAffirmationClick(affirmation) },
            modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true,
    showSystemUi = true,
    name = "My Preview")
@Composable
fun AffirmationsPreview() {
    AffirmationsTheme {
        MainPage()
    }
}