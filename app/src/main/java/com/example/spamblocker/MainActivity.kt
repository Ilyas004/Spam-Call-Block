package com.example.spamblocker

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.spamblocker.services.MyService

class MainActivity : AppCompatActivity() {
    private val requestCode = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ANSWER_PHONE_CALLS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Разрешение на ANSWER_PHONE_CALLS не предоставлено, запрашиваем его у пользователя.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ANSWER_PHONE_CALLS),
                requestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMyServiceWithPermission()
            }
        }
    }

    private fun startMyServiceWithPermission() {
        val serviceIntent = Intent(this, MyService::class.java)
        serviceIntent.action = "permission_granted"
        serviceIntent.putExtra("permissionGranted", true)
        startService(serviceIntent)
    }
}