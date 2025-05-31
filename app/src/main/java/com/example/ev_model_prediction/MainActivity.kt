package com.example.ev_model_prediction

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ev_model_prediction.screens.EVBatteryAppScreen
import com.example.ev_model_prediction.ui.theme.Ev_Model_PredictionTheme
import com.example.ev_model_prediction.viewmodel.EVBatteryViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: EVBatteryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initializeModel(this)
        enableEdgeToEdge()
        setContent {
            Ev_Model_PredictionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EVBatteryAppScreen(viewModel = viewModel)
                }
            }
        }
    }
}
