package com.mainapp.droneapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mainapp.droneapp.databinding.PermissionMainBinding

class PermissionActivity : AppCompatActivity() {

    private lateinit var binding: PermissionMainBinding

    private lateinit var singlePermissionBtn: com.google.android.material.button.MaterialButton

    private companion object {
        private const val TAG = "PERMISSION_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PermissionMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        singlePermissionBtn = binding.enableFineLocation

        singlePermissionBtn.setOnClickListener{
            val permission = Manifest.permission.ACCESS_FINE_LOCATION
            requestSinglePermission.launch(permission)
        }
        checkPermissions()
    }

    private val requestSinglePermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->

        Log.d(TAG, "requestSinglePermission:$isGranted")
        if (isGranted) {
            binding.enableFineLocation.visibility = View.GONE
            checkPermissions()
        } else {
            binding.enableFineLocation.visibility = View.VISIBLE
            Log.d(TAG, "requestSinglePermission: Permission Denied")
            Toast.makeText(this, "Permission Denied.....", Toast.LENGTH_SHORT).show()
        }

    }

    private fun startIntroActivity() {
        val introIntent = Intent(this,MainActivity::class.java)
        startActivity(introIntent)
        finish() // Finish the current activity if needed
    }

    private fun checkPermissions() {
        if (hasFineLocationPermission()) {
            startIntroActivity()
        }
    }

    private fun hasFineLocationPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED == applicationContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}

