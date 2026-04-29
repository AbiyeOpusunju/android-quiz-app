package com.dma.studentapplication

import com.dma.studentapplication.navigation.AppNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dma.studentapplication.ui.theme.StudentApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentApplicationTheme(dynamicColor = false) {
                StudentApplication()
            }
        }
    }
}

@Composable
fun StudentApplication() {
    AppNavigation()
}


@Preview(showBackground = true,
    showSystemUi = true)
@Composable
fun StudentApplicationPreview() {
    StudentApplicationTheme {
        StudentApplication()
    }
}
