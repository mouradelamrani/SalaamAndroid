package com.mouradelamrani.salaam.ui.surah

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mouradelamrani.salaam.data.DataRepository
import com.mouradelamrani.salaam.data.models.Surah
import com.mouradelamrani.salaam.data.models.Verse
import com.mouradelamrani.salaam.extensions.sendEvent
import com.mouradelamrani.salaam.extensions.setValueIfNew
import com.mouradelamrani.salaam.ui.base.BaseViewModel
import com.mouradelamrani.salaam.utils.livedata.Event


sealed class PlayerRepeatMode {
    object Stop : PlayerRepeatMode()
    object Loop : PlayerRepeatMode()
    object Next : PlayerRepeatMode()
}

class SurahViewModel(
    private val surahName: String
) : BaseViewModel() {

    private val _surah = MutableLiveData<Surah>()
    val surah: LiveData<Surah> = _surah

    private val _currentVerse = MutableLiveData<Pair<Int, Verse>?>() // position, verse
    val currentVerse: LiveData<Pair<Int, Verse>?> = _currentVerse

    private val _surahEndVisible = MutableLiveData(false)
    val surahEndVisible: LiveData<Boolean> = _surahEndVisible

    private val _hasNextVerse = MutableLiveData<Boolean>()
//    val hasNextVerse: LiveData<Boolean> = _hasNextVerse

    private val _hasPreviousVerse = MutableLiveData<Boolean>()
    val hasPreviousVerse: LiveData<Boolean> = _hasPreviousVerse

    private val _playerRepeatMode = MutableLiveData<PlayerRepeatMode>(PlayerRepeatMode.Stop)
    val playerRepeatMode: LiveData<PlayerRepeatMode> = _playerRepeatMode

    private val _playCurrentVerse = MutableLiveData<Event<Unit>>()
    val playCurrentVerse: LiveData<Event<Unit>> = _playCurrentVerse

    private val _close = MutableLiveData<Event<Unit>>()
    val close: LiveData<Event<Unit>> = _close

    private val verses: List<Verse>
    private var currentVersePosition = 0


    init {
        DataRepository.getSurah(surahName).let { s ->
            if (s != null) _surah.value = s
            else _close.sendEvent()
        }
        verses = DataRepository.verses[surahName] ?: emptyList()
        if (verses.isEmpty()) _close.sendEvent()
        else setVersePosition(0)
    }


    fun nextClick() {
        if (_hasNextVerse.value == true) {
            setVersePosition(currentVersePosition + 1)
            _playCurrentVerse.sendEvent()
        } else {
            setSurahEnd()
        }
    }

    fun previousClick() {
        if (_hasPreviousVerse.value == true) {
            val position =
                if (_surahEndVisible.value != true) currentVersePosition - 1
                else verses.size - 1
            setVersePosition(position)
            _playCurrentVerse.sendEvent()
        }
    }

    fun loopModeClicked() {
        _playerRepeatMode.value =
            if (_playerRepeatMode.value == PlayerRepeatMode.Loop) PlayerRepeatMode.Stop
            else PlayerRepeatMode.Loop
    }

    fun nextModeClicked() {
        _playerRepeatMode.value =
            if (_playerRepeatMode.value == PlayerRepeatMode.Next) PlayerRepeatMode.Stop
            else PlayerRepeatMode.Next
    }

    fun onPlayCompleted() {
        if (_playerRepeatMode.value == PlayerRepeatMode.Next) {
            if (_hasNextVerse.value == true) {
                setVersePosition(currentVersePosition + 1)
                _playCurrentVerse.sendEvent()
            } else {
                setSurahEnd()
            }
        }
    }

    fun repeatSurahClicked() {
        setVersePosition(0)
        _playCurrentVerse.sendEvent()
    }


    private fun setVersePosition(position: Int) {
        currentVersePosition = position
        _currentVerse.value = Pair(position, verses[currentVersePosition])
        _hasNextVerse.setValueIfNew(currentVersePosition < verses.size - 1)
        _hasPreviousVerse.setValueIfNew(currentVersePosition > 0)
        _surahEndVisible.setValueIfNew(false)
    }

    private fun setSurahEnd() {
        _surahEndVisible.value = true
        _currentVerse.value = null
    }

}