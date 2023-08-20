package com.adarsh.pdf.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.adarsh.pdf.R
import com.adarsh.pdf.databinding.ActivityMainBinding
import com.adarsh.pdf.fragments.HomeFragment
import com.adarsh.pdf.fragments.PdfViewerFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(
            binding.root
        )
//        if(isFirst(this) == 0){
//            firstTime(this)
//            showDetailsSave(this)
//        }
        if (checkPermission()) {

            //   Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }


        if (intent.data != null) {
            val bundle = Bundle()
            bundle.putString("data", intent.data.toString())
            val fm: FragmentManager = supportFragmentManager
            val fragobj = PdfViewerFragment()
            fragobj.arguments = bundle
            fm.beginTransaction()
                .replace(R.id.main_contenier, fragobj)
                //    .addToBackStack("Later Transaction").commit()
                .commit()
        } else {
            val fm: FragmentManager = supportFragmentManager
            val fragment = HomeFragment()
            fm.beginTransaction()
                .replace(R.id.main_contenier, fragment).commit()
            //    .addToBackStack("Later Transaction").commit()
        }
    }


    private fun checkPermission(): Boolean {
        // checking of permissions.
        val permission1 =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission2 =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        // requesting permissions if not provided.
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU){
            return
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 200
        const val READ_STORAGE_PERMISSION_CODE = 12
    }
}
