package com.mouradelamrani.salaam

import android.content.Context
import android.content.res.Configuration
import com.mouradelamrani.salaam.data.AppPreferences
import java.util.*

class LocaleHelper private constructor(context: Context) {

    companion object {
        @Volatile private var INSTANCE: LocaleHelper? = null

        fun getInstance(context: Context): LocaleHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: build(context).also { INSTANCE = it }
        }

        private fun build(context: Context): LocaleHelper = LocaleHelper(context)
    }

    private val appPreferences = AppPreferences.getInstance(context)

    private val currentLocale: Locale
        get() = appPreferences.savedLocale ?: Locale.getDefault()


    val isCurrentLocaleEnglish: Boolean
        get() = currentLocale.language != "nl"

    fun setNewLocale(newLocale: Locale) {
        appPreferences.savedLocale = newLocale
        Locale.setDefault(appPreferences.savedLocale!!)
    }

    fun wrapContext(context: Context): Context {
        val cl = currentLocale

        Locale.setDefault(currentLocale)

        val newConfig = Configuration(context.resources.configuration).apply {
            setLocale(currentLocale)
        }
        return context.createConfigurationContext(newConfig)
    }

}