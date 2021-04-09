package com.mouradelamrani.salaam.utils

import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class StringPreference(
    private val preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: String
) : ReadWriteProperty<Any, String> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String =
        preferences.getString(key, defaultValue)!!

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) =
        preferences.edit { putString(key, value) }

}

class BooleanPreference(
    private val preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
        preferences.getBoolean(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) =
        preferences.edit { putBoolean(key, value) }

}

class IntPreference(
    private val preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Int
) : ReadWriteProperty<Any, Int> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int =
        preferences.getInt(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) =
        preferences.edit { putInt(key, value) }

}

class LongPreference(
    private val preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Long
) : ReadWriteProperty<Any, Long> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Long =
        preferences.getLong(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) =
        preferences.edit { putLong(key, value) }

}

class NullableLongPreference(
    private val preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Long?
) : ReadWriteProperty<Any, Long?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Long? =
        if (preferences.contains(key)) preferences.getLong(key, 0L)
        else defaultValue

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long?) =
        preferences.edit {
            if (value != null) putLong(key, value)
            else remove(key)
        }

}

class NullableStringPreference(
    private val preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: String?
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String? =
        if (preferences.contains(key)) preferences.getString(key, defaultValue)
        else defaultValue

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) =
        preferences.edit {
            if (value != null) putString(key, value)
            else remove(key)
        }

}

class NullableIntPreference(
    private val preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Int?
) : ReadWriteProperty<Any, Int?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int? =
        if (preferences.contains(key)) preferences.getInt(key, 0)
        else defaultValue

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int?) =
        preferences.edit {
            if (value != null) putInt(key, value)
            else remove(key)
        }

}