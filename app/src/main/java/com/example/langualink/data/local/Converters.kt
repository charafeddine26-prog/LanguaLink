package com.example.langualink.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringToIntList(value: String?): List<Int> {
        return value?.takeIf { it.isNotEmpty() }?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()
    }

    @TypeConverter
    fun fromIntListToString(list: List<Int>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun fromStringToStringList(value: String?): List<String> {
        return value?.takeIf { it.isNotEmpty() }?.split(",") ?: emptyList()
    }

    @TypeConverter
    fun fromStringListToString(list: List<String>?): String {
        return list?.joinToString(",") ?: ""
    }
}
