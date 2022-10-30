package com.lokilinks.shoppniglist.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.lokilinks.shoppniglist.R
import com.lokilinks.shoppniglist.billing.BillingManager
import com.lokilinks.shoppniglist.databinding.ActivityMainBinding
import com.lokilinks.shoppniglist.dialogs.NewListDialog
import com.lokilinks.shoppniglist.fragments.FragmentManager
import com.lokilinks.shoppniglist.fragments.NoteFragment
import com.lokilinks.shoppniglist.fragments.ShopListNamesFragment
import com.lokilinks.shoppniglist.settings.SettingsActivity

class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding
    private lateinit var defPref :SharedPreferences
    private lateinit var pref :SharedPreferences
    private var currentMenuItemId = R.id.shop_list
    private var currentTheme = ""
    private var iAd: InterstitialAd? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = defPref.getString("theme_key", "blue").toString()
        setTheme(getSelectedTheme())

        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(BillingManager.MAIN_PREF, MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        setBottomNavListener()
        if (!pref.getBoolean(BillingManager.REMOVE_ADS_KEY, false)) loadInterAd()
    }

    private fun loadInterAd(){
        val request = AdRequest.Builder().build()
        InterstitialAd.load(this, getString(R.string.inter_ad_id), request, object :InterstitialAdLoadCallback(){
            override fun onAdLoaded(ad: InterstitialAd) {
                iAd = ad
            }
            override fun onAdFailedToLoad(p0: LoadAdError) {
                iAd = null
            }
        })
    }

    private fun showInterAd(adListener : AdListener){
        if (iAd !== null){
            iAd?.fullScreenContentCallback = object :FullScreenContentCallback(){
                override fun onAdDismissedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                    adListener.onFinish()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    iAd = null
                    loadInterAd()
                }

                override fun onAdShowedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                }
            }
            iAd?.show(this)
        }else {
            adListener.onFinish()
        }
    }

    private fun setBottomNavListener(){
        binding.bNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.settings->{
                    showInterAd(object : AdListener{
                        override fun onFinish() {
                            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                        }
                    })
                }
                R.id.notes->{
                    currentMenuItemId = R.id.notes
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.shop_list->{
                    currentMenuItemId = R.id.shop_list
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
                }
                R.id.new_item->{
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bNav.selectedItemId = currentMenuItemId
        if (defPref.getString("theme_key", "blue") !== currentTheme ) recreate()

    }

    private fun getSelectedTheme(): Int{
        return if (defPref.getString("theme_key", "blue") == "blue"){
            R.style.Theme_ShoppingListBlue
        }else{
            R.style.Theme_ShoppingListRed
        }
    }

    override fun onClick(name: String) {
        Toast.makeText(this, "You just pressed new_item", Toast.LENGTH_LONG).show()
    }

    interface AdListener{
        fun onFinish()
    }

}