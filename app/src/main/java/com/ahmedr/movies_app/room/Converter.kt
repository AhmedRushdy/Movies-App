package com.ahmedr.movies_app.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
    var gson = Gson()
    @TypeConverter
    fun fromListToGson(list:List<Int>): String? {
        return if (list.isEmpty()){
            "empty"
        } else
            gson.toJson(list)
    }
    @TypeConverter
    fun fromGsonToList(st:String): List<Int>? {
        val itemType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(st,itemType)
    }

}