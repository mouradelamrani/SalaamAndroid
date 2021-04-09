package com.mouradelamrani.salaam.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mouradelamrani.salaam.data.AppPreferences


class MainViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val appPreferences = AppPreferences.getInstance(context)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(appPreferences) as T
        else -> throw IllegalArgumentException("Unknown ViewModel class")
    }

}