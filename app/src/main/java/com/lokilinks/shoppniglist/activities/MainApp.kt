package com.lokilinks.shoppniglist.activities

import android.app.Application
import com.lokilinks.shoppniglist.db.MainDataBase

class MainApp:Application() {
    val database by lazy {MainDataBase.getDataBase(this)}
}