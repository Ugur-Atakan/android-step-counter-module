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


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(null)
            // checkAndRequestPermissions()

  }
   
  override fun getMainComponentName(): String = "Crypto Hunter"

  override fun createReactActivityDelegate(): ReactActivityDelegate =
      DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
}
