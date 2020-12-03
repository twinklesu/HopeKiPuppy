package com.example.hopekipuppy.title

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SleepTrackerViewModelFactory(
    //private val dataSource: SleepDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LostViewModel::class.java)) {
            return LostViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
