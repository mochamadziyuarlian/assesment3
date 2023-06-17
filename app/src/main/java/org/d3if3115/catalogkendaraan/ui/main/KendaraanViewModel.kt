package org.d3if3115.catalogkendaraan.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3115.catalogkendaraan.model.Kendaraan
import org.d3if3115.catalogkendaraan.network.ApiStatus
import org.d3if3115.catalogkendaraan.network.KendaraanApi
import org.d3if3115.catalogkendaraan.network.UpdateWorker
import java.util.concurrent.TimeUnit

class KendaraanViewModel : ViewModel() {
    private val data = MutableLiveData<List<Kendaraan>>()
    private val status = MutableLiveData<ApiStatus>()
    init {
        retrieveData()
    }
    private fun retrieveData() {
        viewModelScope.launch (Dispatchers.IO) {
            status.postValue(ApiStatus.LOADING)
            try {
                data.postValue(KendaraanApi.service.getKendaraan())
                status.postValue(ApiStatus.SUCCESS)
            } catch (e: Exception) {
                Log.d("KendaraanViewModel", "Failure: ${e.message}")
                status.postValue(ApiStatus.FAILED)
            }
        }
    }

    fun getData(): LiveData<List<Kendaraan>> = data

    fun getStatus(): LiveData<ApiStatus> = status

    fun scheduleUpdater(app: Application) {
        val request = OneTimeWorkRequestBuilder<UpdateWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(app).enqueueUniqueWork(
            UpdateWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }


}