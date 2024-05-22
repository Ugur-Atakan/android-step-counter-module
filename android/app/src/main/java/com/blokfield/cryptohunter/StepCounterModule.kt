package blokfield.cryptohunter
import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.util.Log
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.blokfield.cryptohunter.services.AccelerometerService
import com.blokfield.cryptohunter.services.BackgroundService
import com.blokfield.cryptohunter.services.SensorListenService
import com.blokfield.cryptohunter.services.StepCounterService
import com.blokfield.cryptohunter.utils.AndroidVersionHelper

class StepCounterModule internal constructor(context: ReactApplicationContext) :
        ReactContextBaseJavaModule(context) {
    companion object {
        const val NAME: String = "StepCounter"
        private val TAG_NAME: String = StepCounterModule::class.java.name
        private const val STEP_COUNTER = "android.permission.ACTIVITY_RECOGNITION"
    }

    private val appContext: ReactApplicationContext = context
    private val  sensorManager: SensorManager
    private val stepsOK: Boolean
        get() = checkSelfPermission(appContext, STEP_COUNTER) == PERMISSION_GRANTED
    private val accelOK: Boolean
        get() = AndroidVersionHelper.isHardwareAccelerometerEnabled(appContext)
    private val supported: Boolean
        get() = AndroidVersionHelper.isHardwareStepCounterEnabled(appContext)
    private val walkingStatus: Boolean
        get() = stepCounterListener !== null

    private var stepCounterListener: SensorListenService? = null

    init {
        sensorManager = context.getSystemService(
            Context.SENSOR_SERVICE
        ) as SensorManager
        stepCounterListener = if (stepsOK) {
            StepCounterService(this, sensorManager)
        } else {
            AccelerometerService(this, sensorManager)
        }
        appContext.addLifecycleEventListener(stepCounterListener)
    }

    @ReactMethod
     fun isStepCountingSupported(promise: Promise) {
        Log.d(TAG_NAME, "hardware_step_counter? $supported")
        Log.d(TAG_NAME, "step_counter granted? $stepsOK")
        Log.d(TAG_NAME, "accelerometer granted? $accelOK")
        sendDeviceEvent("stepDetected", walkingStatus)
        promise.resolve(
                Arguments.createMap().apply {
                    putBoolean("supported", supported)
                    putBoolean("granted", stepsOK || accelOK)
                    putBoolean("working", walkingStatus)
                }
        )
    }

    @ReactMethod
     fun startStepCounterUpdate() {
        stepCounterListener = stepCounterListener ?: if (stepsOK) {
            StepCounterService(this, sensorManager)
        } else {
            AccelerometerService(this, sensorManager)
        }
        Log.d(TAG_NAME, "startStepCounterUpdate")
        stepCounterListener!!.startService()
        startBackgroundService()
    }

    @ReactMethod
     fun stopStepCounterUpdate() {
        Log.d(TAG_NAME, "stopStepCounterUpdate")
        stepCounterListener!!.stopService()
    }

    @ReactMethod
    fun startBackgroundService() {
        val serviceIntent = Intent(reactApplicationContext, BackgroundService::class.java)
        reactApplicationContext.startForegroundService(serviceIntent)
    }

    @ReactMethod
    fun stopBackgroundService() {
        val serviceIntent = Intent(reactApplicationContext, BackgroundService::class.java)
        reactApplicationContext.stopService(serviceIntent)
    }

    /**
     * Keep: Required for RN built in Event Emitter Support.
     * @param eventName the name of the event. usually "stepCounterUpdate".
     */
    @ReactMethod
     fun addListener(eventName: String) {}

    /**
     * Keep: Required for RN built in Event Emitter Support.
     * @param count the number of listeners to remove.
     * not implemented.
     */
    @ReactMethod
     fun removeListeners(count: Double) {}

    /**
     * StepCounterPackage requires this property for the module.
     * @return the name of the module. usually "StepCounter".
     */
    override fun getName(): String = NAME

    /**
     * Send the step counter update event to the react-native code.
     * @param eventPayload the object that contains information about the step counter update.
     * @return Nothing.
     * @see WritableMap
     * @see RCTDeviceEventEmitter
     * @see com.facebook.react.modules.core.DeviceEventManagerModule
     * @throws RuntimeException if the event emitter is not initialized.
     */
    fun sendDeviceEvent(eventType: String, eventPayload: Any) {
        try {
            appContext.getJSModule(RCTDeviceEventEmitter::class.java)
                    .emit("$NAME.$eventType", eventPayload)
        } catch (e: RuntimeException) {
            e.message?.let { Log.e(TAG_NAME, it) }
            Log.e(TAG_NAME, eventType, e)
        }
    }
}
