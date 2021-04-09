package com.mouradelamrani.salaam.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mouradelamrani.salaam.ui.base.BaseViewModel

class MenuViewModel : BaseViewModel() {

    private val _languagesVisible = MutableLiveData(false)
    val languagesVisible: LiveData<Boolean> = _languagesVisible


    fun toggleLanguagesVisible() {
        _languagesVisible.value = !_languagesVisible.value!!
    }

}