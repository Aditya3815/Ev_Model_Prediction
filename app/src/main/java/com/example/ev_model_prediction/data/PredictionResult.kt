package com.example.ev_model_prediction.data

data class PredictionResult(
    val predictedClass: Int,                // 0=Short, 1=Medium, 2=Long
    val className: String,                  // Short (≤40min), Medium (≤80min), Long (>80min)
    val confidence: Float,                  // Prediction confidence
    val allProbabilities: FloatArray,       // All class probabilities
    val recommendation: String              // User recommendation
)
