package com.lokilinks.shoppniglist.fragments

import androidx.appcompat.app.AppCompatActivity
import com.lokilinks.shoppniglist.R

object FragmentManager {
    var currentFrag : BaseFragment? = null

    fun setFragment(newFrag: BaseFragment, activity: AppCompatActivity){
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.placeholder, newFrag)
        transaction.commit()
        currentFrag = newFrag
    }
}