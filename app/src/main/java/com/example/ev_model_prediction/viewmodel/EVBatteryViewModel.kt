package com.example.ev_model_prediction.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ev_model_prediction.data.BatteryData
import com.example.ev_model_prediction.data.PredictionResult
import com.example.ev_model_prediction.modelManager.EVBatteryMLModel

class EVBatteryViewModel : ViewModel() {

    var batteryData by mutableStateOf(BatteryData())
        private set
    var predictionResult by mutableStateOf<PredictionResult?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    private var mlModel: EVBatteryMLModel? = null

    fun initializeModel(context: Context){
        mlModel = EVBatteryMLModel(context)
    }
    fun updateBatteryData(newData: BatteryData){
        batteryData = newData
    }

    fun predict(){
        isLoading = true
        mlModel?.predict(batteryData)?.let { result ->
            predictionResult = result
            isLoading = false
        }
    }

    fun resetData(){
        batteryData = BatteryData()
        predictionResult = null
    }

    override fun onCleared() {
        super.onCleared()
        mlModel?.close()
    }
}