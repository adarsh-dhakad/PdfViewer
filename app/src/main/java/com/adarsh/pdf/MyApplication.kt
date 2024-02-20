package com.adarsh.pdf

import android.app.Application
import android.content.Context
//import com.google.android.gms.ads.MobileAds
//import com.google.android.gms.ads.appopen.AppOpenAd

/** Application class that initializes, loads and show ads when activities change states. */
class MyApplication : Application() {
    private lateinit var appOpenAdManager: AppOpenAdManager
    override fun onCreate() {
        super.onCreate()
    //    MobileAds.initialize(this) {}
     //   appOpenAdManager = AppOpenAdManager()
    }

    /** Inner class that loads and shows app open ads. */
    private inner class AppOpenAdManager {
        private var isLoadingAd = false
        var isShowingAd = false

        /** Request an ad. */
        fun loadAd(context: Context) {
            // We will implement this below.
        }

        /** Check if ad exists and can be shown. */
    }
}