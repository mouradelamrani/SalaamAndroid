package com.mouradelamrani.salaam.ui.surah

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SurahViewModelFactory(
    context: Context,
    private val surahName: String
) : ViewModelProvider.Factory {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(SurahViewModel::class.java) -> SurahViewModel(surahName) as T
        else -> throw IllegalArgumentException("Unknown ViewModel class")
    }

}