package com.mouradelamrani.salaam.ui.main

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mouradelamrani.salaam.data.AppPreferences
import com.mouradelamrani.salaam.extensions.sendEvent
import com.mouradelamrani.salaam.extensions.setValueIfNew
import com.mouradelamrani.salaam.ui.base.BaseViewModel
import com.mouradelamrani.salaam.utils.livedata.Event

class MainViewModel(private val appPreferences: AppPreferences) : BaseViewModel() {

    private val _menuOpen = MutableLiveData(false)
    val menuOpen: LiveData<Boolean> = _menuOpen

    private val _menuBackgroundBitmap = MutableLiveData<Bitmap?>()
    val menuBackgroundBitmap: LiveData<Bitmap?> = _menuBackgroundBitmap

    private val _captureMenuBackground = MutableLiveData<Event<Unit>>()
    val captureMenuBackground: LiveData<Event<Unit>> = _captureMenuBackground

    init {
        _captureMenuBackground.sendEvent()
    }

    override fun onCleared() {
        _menuBackgroundBitmap.value = null
    }


    fun setMenuOpen(isOpen: Boolean) {
        _menuOpen.setValueIfNew(isOpen)
    }

    fun saveMenuBackground(bitmap: Bitmap) {
        _menuBackgroundBitmap.value = bitmap
    }

}