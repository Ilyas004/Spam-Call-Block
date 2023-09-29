package com.example.spamblocker.services

import android.Manifest
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat

class MyService : Service() {
    private var phoneReceiver: BroadcastReceiver? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        val isPermissionGranted = intent?.getBooleanExtra("permissionGranted", false) ?: false

        if (action == "permission_granted" && isPermissionGranted) {
            phoneReceiver = object : BroadcastReceiver() {
                override fun onReceive(p0: Context?, p1: Intent?) {
                    if (p1?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
                        val phoneNumber = p1.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

                        if (phoneNumber != null && phoneNumber.startsWith("+79") && phoneNumber.startsWith("89")) {
                            val telecomManager = p0?.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                            if (ActivityCompat.checkSelfPermission(
                                    applicationContext,
                                    Manifest.permission.ANSWER_PHONE_CALLS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                return
                            }
                            telecomManager.endCall()
                        }
                    }
                }
            }
            val filter  = IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
            registerReceiver(phoneReceiver, filter)
        } else {

        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Отмените регистрацию Broadcast Receiver при уничтожении службы.
        unregisterReceiver(phoneReceiver)
    }


    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


}