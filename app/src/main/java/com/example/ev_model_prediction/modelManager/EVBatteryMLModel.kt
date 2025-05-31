package com.example.ev_model_prediction.modelManager

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.util.Log
import com.example.ev_model_prediction.data.BatteryData
import com.example.ev_model_prediction.data.PredictionResult
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class EVBatteryMLModel(private val context: Context) {
    private var interpreter: Interpreter? = null
    private val modelPath = "ev_battery_model.tflite"

    private val featureMeans = floatArrayOf(
        53.27f,
        3.85f,
        55.37f,
        29.71f,
        24.96f,
        69.92f,
        9.98f,
        98.0f,
        551.77f,
        0.9975f,
        0.5125f,
        1.00625f
    )

    private val featureScales = floatArrayOf(
        26.32f,
        0.2045f,
        26.05f,
        5.72f,
        5.77f,
        29.12f,
        2.70f,
        0.54f,
        264.80f,
        0.8124f,
        0.4998f,
        0.8208f
    )

    private val chargingModeMap = mapOf("Fast" to 0, "Normal" to 1, "Slow" to 2)
    private val batteryTypeMap = mapOf("Li-ion" to 0, "LiFePO4" to 1)
    private val evModelMap = mapOf("Model-A" to 0, "Model B" to 1, "Model C" to 2)

    private val classNames = arrayOf("Short (≤40min)", "Medium (≤80min)", "Long (>80min)")
    private val recommendations = arrayOf(
        "Excellent! Quick charging expected. Perfect for short trips.",
        "Moderate charging time. Good for regular daily use.",
        "Extended charging needed. Plan accordingly for longer trips."
    )

    init {
        loadModel()
    }

    private fun loadModel(): Boolean {
        return try {
            val assetFileDescriptor: AssetFileDescriptor = context.assets.openFd(modelPath)
            val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
            val fileChannel: FileChannel = inputStream.getChannel()
            val startOffset: Long = assetFileDescriptor.startOffset
            val declaredLength: Long = assetFileDescriptor.declaredLength
            val retFile: MappedByteBuffer = fileChannel.map(
                FileChannel.MapMode.READ_ONLY, startOffset, declaredLength
            )
            interpreter = Interpreter(retFile)
            Log.d("EVBatteryML", "Model loaded successfully")
            true
        } catch (e: Exception) {
            Log.e("EVBatteryMLModel", "Error loading model: ${e.message}")
            false
        }
    }

    fun predict(batteryData: BatteryData): PredictionResult? {
        return try {
            interpreter?.let { interpreter ->
                val inputArray = Array(1) { FloatArray(12) }
                val features = floatArrayOf(
                    batteryData.soc,
                    batteryData.voltage,
                    batteryData.current,
                    batteryData.batteryTemp,
                    batteryData.ambientTemp,
                    batteryData.chargingDuration,
                    batteryData.degradationRate,
                    batteryData.efficiency,
                    batteryData.chargingCycles,
                    chargingModeMap[batteryData.chargingMode]?.toFloat() ?: 1f,
                    batteryTypeMap[batteryData.batteryType]?.toFloat() ?: 0f,
                    evModelMap[batteryData.evModel]?.toFloat() ?: 0f
                )
                // Apply feature scaling
                for (i in features.indices) {
                    inputArray[0][i] = (features[i] - featureMeans[i]) / featureScales[i]
                }
                // Prepare output array
                val outputArray = Array(1) { FloatArray(3) }
                // Run inference
                interpreter.run(inputArray, outputArray)

                // Process results
                val probabilities = outputArray[0]
                val predictedClass = probabilities.indices.maxByOrNull { probabilities[it] } ?: 0
                val confidence = probabilities[predictedClass]

                PredictionResult(
                    predictedClass = predictedClass,
                    className = classNames[predictedClass],
                    confidence = confidence,
                    allProbabilities = probabilities,
                    recommendation = recommendations[predictedClass]
                )
            }
        } catch (e: Exception) {
            Log.e("EVBatteryMLModel", "Error during prediction: ${e.message}")
            null
        }
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }
}