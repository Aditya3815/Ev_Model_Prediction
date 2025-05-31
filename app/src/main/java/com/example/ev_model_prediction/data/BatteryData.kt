package com.example.ev_model_prediction.data

data class BatteryData(
    val soc: Float = 0f,                    // SOC (%)
    val voltage: Float = 0f,                // Voltage (V)
    val current: Float = 0f,                // Current (A)
    val batteryTemp: Float = 0f,            // Battery Temp (°C)
    val ambientTemp: Float = 0f,            // Ambient Temp (°C)
    val chargingDuration: Float = 0f,       // Charging Duration (min)
    val degradationRate: Float = 0f,        // Degradation Rate (%)
    val efficiency: Float = 0f,             // Efficiency (%)
    val chargingCycles: Float = 0f,         // Charging Cycles
    val chargingMode: String = "Normal",    // Fast, Normal, Slow
    val batteryType: String = "Li-ion",     // Li-ion, LiFePO4
    val evModel: String = "Model A"         // Model A, Model B, Model C
)
