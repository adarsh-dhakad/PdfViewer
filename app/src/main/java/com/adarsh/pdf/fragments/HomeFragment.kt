package com.adarsh.pdf.fragments

//import com.google.android.gms.ads.AdListener
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.AdView
//import com.google.android.gms.ads.LoadAdError
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.adarsh.pdf.R
import com.adarsh.pdf.activities.MainActivity
import com.adarsh.pdf.databinding.FragmentHomeBinding
import com.adarsh.pdf.utils.Constants
import com.adarsh.pdf.utils.PreferenceHelper
import com.google.android.gms.ads.*
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.things.update.UpdateManager
import com.google.android.things.update.UpdatePolicy
import com.google.android.things.update.UpdatePolicy.POLICY_APPLY_AND_REBOOT
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.pow
//import com.yalantis.ucrop.UCrop


class HomeFragment : Fragment() {
   private lateinit var binding: FragmentHomeBinding
    private lateinit var updateManager: AppUpdateManager
    lateinit var mAdView: AdView
    lateinit var adRequest:AdRequest
    private val RC_APP_UPDATE = 72
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

//        MobileAds.initialize(requireContext()) {}
//        val testDeviceIds = Arrays.asList("5eb2f91f-7863-4030-ab2b-10ca1ad1ad1abe1")
//        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
//        MobileAds.setRequestConfiguration(configuration)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.btnPdfRender.setOnClickListener {

            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU){
                val gallery = Intent()
                gallery.type = "application/*"
                gallery.action = Intent.ACTION_GET_CONTENT
                resultLauncherGallery.launch(gallery)
            }else if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                val gallery = Intent()
                gallery.type = "application/*"
                gallery.action = Intent.ACTION_GET_CONTENT
                resultLauncherGallery.launch(gallery)
            } else {
                // Requests permission
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    MainActivity.READ_STORAGE_PERMISSION_CODE
                )
            }
        }
        mAdView = binding.adView
     //   if(showOrNot(requireActivity())){
            bannerAds()
   //     }
        return binding.root
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    private var resultLauncherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
                val bundle = Bundle()
                bundle.putString("data", data.data.toString())
                val fm: FragmentManager = requireActivity().supportFragmentManager
                val fragobj = PdfViewerFragment()
                fragobj.arguments = bundle
                fm.beginTransaction()
                    .replace(R.id.main_contenier, fragobj)
                    .addToBackStack("Later Transaction").commit()
            }
        }

    }

    private fun bannerAds() {
        adRequest = AdRequest.Builder()
            .build()
        var retryAttempt = 0
        mAdView.loadAd(adRequest)
        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                retryAttempt = 0
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)
                // 2 power 6 = 64
                retryAttempt++
                val delayMillis: Long = TimeUnit.SECONDS.toMillis(
                    2.0.pow(6.coerceAtMost(retryAttempt).toDouble()).toLong()
                )


                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        mAdView.loadAd(adRequest)
                    },
                    delayMillis
                )
            }

            override fun onAdOpened() {
       //         mAdView.visibility = View.GONE
            }

            override fun onAdClicked() {
        //        mAdView.visibility = View.GONE
            }

            override fun onAdClosed() {
          //      mAdView.visibility = View.GONE
            }
        }

    }

}