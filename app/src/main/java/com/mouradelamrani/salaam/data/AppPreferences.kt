package com.mouradelamrani.salaam.data

import android.content.Context
import android.content.SharedPreferences
import com.mouradelamrani.salaam.utils.IntPreference
import com.mouradelamrani.salaam.utils.NullableStringPreference
import java.util.*


class AppPreferences private constructor(context: Context) {

    companion object {
        private const val FILE_NAME = "salaam.app_preferences"
        private const val PREF_LAUNCH_NUMBER = "pref_launch_number"
        private const val PREF_SAVED_LOCALE = "pref_saved_locale"

        @Volatile
        private var INSTANCE: AppPreferences? = null

        fun getInstance(context: Context): AppPreferences = INSTANCE ?: synchronized(this) {
            INSTANCE ?: build(context).also { INSTANCE = it }
        }

        private fun build(context: Context): AppPreferences = AppPreferences(context)
    }

    private val prefs = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    private val prefListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
//        when (key) {
//            PREF_LAUNCH_NUMBER -> _filesCountLiveData.setValueIfNew(files.size)
//        }
    }


    init {
        prefs.registerOnSharedPreferenceChangeListener(prefListener)
    }


    var launchNumber: Int by IntPreference(prefs, PREF_LAUNCH_NUMBER, 0)

    // TODO: save and restore full Locale: language, country, variant
    /** Locale set by user. Null if user didn't change default locale. */
    var savedLocale: Locale?
        set(locale) {
            savedLocaleStr = locale?.language
        }
        get() = savedLocaleStr.let { lang ->
            if (lang != null) Locale(lang)
            else null
        }

    private var savedLocaleStr: String? by NullableStringPreference(prefs, PREF_SAVED_LOCALE, null)

}