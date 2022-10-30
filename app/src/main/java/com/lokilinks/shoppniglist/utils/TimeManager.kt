package com.lokilinks.shoppniglist.utils

import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

object TimeManager {

    const val  DEF_TIME_FORMAT = "hh:mm:ss - yyyy/MM/dd"

    @RequiresApi(Build.VERSION_CODES.N)
     fun getCurrentTime():String{
        val formatter = SimpleDateFormat(DEF_TIME_FORMAT, Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getTimeFormat(time: String, defPreferences: SharedPreferences): String {
        val defFormatter = SimpleDateFormat(DEF_TIME_FORMAT, Locale.getDefault())
        val defDate = defFormatter.parse(time)
        val newFormat = defPreferences.getString("time_format_key", DEF_TIME_FORMAT)
        val newFormatter = SimpleDateFormat(newFormat, Locale.getDefault())

        return if (defDate !== null){
            newFormatter.format(defDate)
        } else newFormatter.toString()
    }
}