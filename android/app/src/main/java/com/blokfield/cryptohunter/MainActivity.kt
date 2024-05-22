package com.blokfield.cryptohunter

import android.Manifest.*
import android.app.AlertDialog
import android.net.Uri
import android.provider.Settings
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import com.blokfield.cryptohunter.services.BackgroundService
class MainActivity : ReactActivity() {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(null)
            // checkAndRequestPermissions()

  }
   // private fun checkAndRequestPermissions() {
    //     val requiredPermissions = mutableListOf<String>()

    //     // ACTIVITY_RECOGNITION izni
    //     if (ContextCompat.checkSelfPermission(this, permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
    //         requiredPermissions.add(permission.ACTIVITY_RECOGNITION)
    //     }

    //     // FOREGROUND_SERVICE izni
    //     if (ContextCompat.checkSelfPermission(this, permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
    //         requiredPermissions.add(permission.FOREGROUND_SERVICE)
    //     }

    //     // FOREGROUND_SERVICE_HEALTH izni
    //     if (ContextCompat.checkSelfPermission(this, permission.FOREGROUND_SERVICE_HEALTH) != PackageManager.PERMISSION_GRANTED) {
    //         requiredPermissions.add(permission.FOREGROUND_SERVICE_HEALTH)
    //     }

    //     // HIGH_SAMPLING_RATE_SENSORS izni
    //     if (ContextCompat.checkSelfPermission(this, permission.HIGH_SAMPLING_RATE_SENSORS) != PackageManager.PERMISSION_GRANTED) {
    //         requiredPermissions.add(permission.HIGH_SAMPLING_RATE_SENSORS)
    //     }

    //     // İstenmeyen izinler varsa, istek gönder
    //     if (requiredPermissions.isNotEmpty()) {
    //         ActivityCompat.requestPermissions(this, requiredPermissions.toTypedArray(), PERMISSION_REQUEST_CODE)
    //     } else {
    //         // Tüm izinler verilmişse servisi başlat
    //         startService()
    //     }
    // }

    // override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    //     super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    //     if (requestCode == PERMISSION_REQUEST_CODE) {
    //         if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
    //             // Tüm izinler verilmişse servisi başlat
    //             startService()
    //         } else {
    //             // İzinler reddedildi veya iptal edildi, kullanıcıya uygun bir geri bildirim göster
    //             showSettingsAlert()
    //         }
    //     }
    // }

    // private fun showSettingsAlert() {
    //     AlertDialog.Builder(this)
    //             .setTitle("Permissions Required")
    //             .setMessage("To use this application, you must grant physical activity permission from the settings.")
    //             .setPositiveButton("Open Settings") { _, _ ->
    //                 openAppSettings()
    //             }
    //             .setNegativeButton("Cancel", null)
    //             .show()
    // }


    // Uygulamanın ayarlar sayfasını aç
    // private fun openAppSettings() {
    //     val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    //     val uri = Uri.fromParts("package", packageName, null)
    //     intent.data = uri
    //     startActivity(intent)
    // }

    // private fun startService() {
    //     val serviceIntent = Intent(this, BackgroundService::class.java)
    //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    //         startForegroundService(serviceIntent)
    //     } else {
    //         startService(serviceIntent)
    //     }
    // }

  override fun getMainComponentName(): String = "Crypto Hunter"

  /**
   * Returns the instance of the [ReactActivityDelegate]. We use [DefaultReactActivityDelegate]
   * which allows you to enable New Architecture with a single boolean flags [fabricEnabled]
   */
  override fun createReactActivityDelegate(): ReactActivityDelegate =
      DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
}
