package com.example.ev_model_prediction.screens

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Battery6Bar
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ev_model_prediction.data.BatteryData
import com.example.ev_model_prediction.data.PredictionResult
import com.example.ev_model_prediction.viewmodel.EVBatteryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EVBatteryAppScreen(viewModel: EVBatteryViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.ElectricCar,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "EV Battery Analytics",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            //Input Form
            item {
                InputSection(
                    batteryData = viewModel.batteryData,
                    onDataChange = viewModel::updateBatteryData
                )
            }
            // Prediction Button
            item {
                PredictionButtonSection(
                    isLoading = viewModel.isLoading,
                    onPredict = viewModel::predict,
                    onReset = viewModel::resetData
                )
            }
            // Results Section
            item {
                viewModel.predictionResult?.let { result ->
                    PredictionResultSection(result = result)
                }
            }
        }
    }
}
@Composable
fun PredictionButtonSection(
    isLoading: Boolean,
    onPredict: () -> Unit,
    onReset: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onPredict,
            modifier = Modifier.weight(1f),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
            } else {
                Icon(Icons.Default.Psychology, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text("Predict Duration")
        }

        OutlinedButton(
            onClick = onReset,
            modifier = Modifier.weight(0.5f)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Reset")
        }
    }
}
@Composable
fun PredictionResultSection(result: PredictionResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (result.predictedClass) {
                0 -> Color(0xFF4CAF50).copy(alpha = 0.1f) // Green for Short
                1 -> Color(0xFFFF9800).copy(alpha = 0.1f) // Orange for Medium
                else -> Color(0xFFF44336).copy(alpha = 0.1f) // Red for Long
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Prediction Result",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Main prediction
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when (result.predictedClass) {
                        0 -> Color(0xFF4CAF50)
                        1 -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    }
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = result.className,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Confidence: ${(result.confidence * 100).toInt()}%",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Recommendation
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = result.recommendation,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Probability breakdown
            Text(
                text = "Probability Breakdown",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            val classNames = arrayOf("Short", "Medium", "Long")
            val colors = arrayOf(Color(0xFF4CAF50), Color(0xFFFF9800), Color(0xFFF44336))

            result.allProbabilities.forEachIndexed { index, probability ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = classNames[index],
                        modifier = Modifier.width(60.dp),
                        fontSize = 12.sp
                    )

                    LinearProgressIndicator(
                        progress = probability,
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp),
                        color = colors[index],
                        trackColor = colors[index].copy(alpha = 0.2f)
                    )

                    Text(
                        text = "${(probability * 100).toInt()}%",
                        modifier = Modifier.width(40.dp),
                        fontSize = 12.sp,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSection(batteryData: BatteryData, onDataChange: (BatteryData) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Battery Parameters",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            // SOC
            OutlinedTextField(
                value = if (batteryData.soc == 0f) "" else batteryData.soc.toString(),
                onValueChange = {
                    onDataChange(batteryData.copy(soc = it.toFloatOrNull() ?: 0f))
                },
                label = { Text("State of Charge (%)") },
                leadingIcon = { Icon(Icons.Default.Battery6Bar, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Voltage
            OutlinedTextField(
                value = if (batteryData.voltage == 0f) "" else batteryData.voltage.toString(),
                onValueChange = {
                    onDataChange(batteryData.copy(voltage = it.toFloatOrNull() ?: 0f))
                },
                label = { Text("Voltage (V)") },
                leadingIcon = { Icon(Icons.Default.Bolt, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Current
            OutlinedTextField(
                value = if (batteryData.current == 0f) "" else batteryData.current.toString(),
                onValueChange = {
                    onDataChange(batteryData.copy(current = it.toFloatOrNull() ?: 0f))
                },
                label = { Text("Current (A)") },
                leadingIcon = { Icon(Icons.Default.Power, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Battery Temperature
            OutlinedTextField(
                value = if (batteryData.batteryTemp == 0f) "" else batteryData.batteryTemp.toString(),
                onValueChange = {
                    onDataChange(batteryData.copy(batteryTemp = it.toFloatOrNull() ?: 0f))
                },
                label = { Text("Battery Temperature (°C)") },
                leadingIcon = { Icon(Icons.Default.Thermostat, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Ambient Temperature
            OutlinedTextField(
                value = if (batteryData.ambientTemp == 0f) "" else batteryData.ambientTemp.toString(),
                onValueChange = {
                    onDataChange(batteryData.copy(ambientTemp = it.toFloatOrNull() ?: 0f))
                },
                label = { Text("Ambient Temperature (°C)") },
                leadingIcon = { Icon(Icons.Default.DeviceThermostat, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Charging Duration
            OutlinedTextField(
                value = if (batteryData.chargingDuration == 0f) "" else batteryData.chargingDuration.toString(),
                onValueChange = {
                    onDataChange(batteryData.copy(chargingDuration = it.toFloatOrNull() ?: 0f))
                },
                label = { Text("Charging Duration (min)") },
                leadingIcon = { Icon(Icons.Default.Timer, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Degradation Rate
            OutlinedTextField(
                value = if (batteryData.degradationRate == 0f) "" else batteryData.degradationRate.toString(),
                onValueChange = {
                    onDataChange(batteryData.copy(degradationRate = it.toFloatOrNull() ?: 0f))
                },
                label = { Text("Degradation Rate (%)") },
                leadingIcon = {
                    Icon(
                        Icons.AutoMirrored.Filled.TrendingDown,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Efficiency
            OutlinedTextField(
                value = if (batteryData.efficiency == 0f) "" else batteryData.efficiency.toString(),
                onValueChange = {
                    onDataChange(batteryData.copy(efficiency = it.toFloatOrNull() ?: 0f))
                },
                label = { Text("Efficiency (%)") },
                leadingIcon = {
                    Icon(
                        Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Charging Cycles
            OutlinedTextField(
                value = if (batteryData.chargingCycles == 0f) "" else batteryData.chargingCycles.toString(),
                onValueChange = {
                    onDataChange(batteryData.copy(chargingCycles = it.toFloatOrNull() ?: 0f))
                },
                label = { Text("Charging Cycles") },
                leadingIcon = { Icon(Icons.Default.Refresh, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdowns for categorical features
            Text(
                text = "Vehicle Configuration",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            var expandedChargingMode by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedChargingMode,
                onExpandedChange = { expandedChargingMode = !expandedChargingMode }
            ) {
                OutlinedTextField(
                    value = batteryData.chargingMode,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Charging Mode") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedChargingMode) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedChargingMode,
                    onDismissRequest = { expandedChargingMode = false }
                ) {
                    listOf("Fast", "Normal", "Slow").forEach { mode ->
                        DropdownMenuItem(
                            text = { Text(mode) },
                            onClick = {
                                onDataChange(batteryData.copy(chargingMode = mode))
                                expandedChargingMode = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            var expandedBatteryType by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedBatteryType,
                onExpandedChange = { expandedBatteryType = !expandedBatteryType }
            ) {
                OutlinedTextField(
                    value = batteryData.batteryType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Battery Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedBatteryType) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedBatteryType,
                    onDismissRequest = { expandedBatteryType = false }
                ) {
                    listOf("Li-ion", "LiFePO4").forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                onDataChange(batteryData.copy(batteryType = type))
                                expandedBatteryType = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            var expandedEVModel by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedEVModel,
                onExpandedChange = { expandedEVModel = !expandedEVModel }
            ) {
                OutlinedTextField(
                    value = batteryData.evModel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("EV Model") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEVModel) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedEVModel,
                    onDismissRequest = { expandedEVModel = false }
                ) {
                    listOf("Model A", "Model B", "Model C").forEach { model ->
                        DropdownMenuItem(
                            text = { Text(model) },
                            onClick = {
                                onDataChange(batteryData.copy(evModel = model))
                                expandedEVModel = false
                            }
                        )
                    }
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun EVBatteryAppScreenPreview() {
    EVBatteryAppScreen(
        viewModel = EVBatteryViewModel()
    )
}