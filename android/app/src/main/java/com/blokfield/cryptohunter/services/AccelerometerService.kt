package com.blokfield.cryptohunter.services

import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import blokfield.cryptohunter.StepCounterModule
import com.blokfield.cryptohunter.utils.SensorFusionMath.dot
import com.blokfield.cryptohunter.utils.SensorFusionMath.norm
import com.blokfield.cryptohunter.utils.SensorFusionMath.normalize
import com.blokfield.cryptohunter.utils.SensorFusionMath.sum
import kotlin.math.min

class AccelerometerService(
    counterModule: StepCounterModule,
    sensorManager: SensorManager
) : SensorListenService(counterModule, sensorManager) {

    override val sensorTypeString = "Accelerometer"
    override val sensorType = Sensor.TYPE_ACCELEROMETER
    override val detectedSensor: Sensor? = sensorManager.getDefaultSensor(sensorType)
    override var currentSteps: Double = 0.0
    private var velocityRingCounter: Int = 0
    private var accelRingCounter: Int = 0
    private var oldVelocityEstimate: Float = 0f
    private var lastStepTimeNs: Long = 0

    private val accelRingX = FloatArray(ACCEL_RING_SIZE)
    private val accelRingY = FloatArray(ACCEL_RING_SIZE)
    private val accelRingZ = FloatArray(ACCEL_RING_SIZE)
    private val velocityRing = FloatArray(VELOCITY_RING_SIZE)
    private var gravity: FloatArray? = null

    override fun updateCurrentSteps(eventData: FloatArray): Boolean {
        val timeNs = System.nanoTime()

        accelRingCounter++
        accelRingX[accelRingCounter % ACCEL_RING_SIZE] = eventData[0]
        accelRingY[accelRingCounter % ACCEL_RING_SIZE] = eventData[1]
        accelRingZ[accelRingCounter % ACCEL_RING_SIZE] = eventData[2]

        // Low-pass filter to remove gravity component
        val filteredEventData = lowPass(eventData, gravity)
        gravity = filteredEventData

        val currentZ = dot(normalize(filteredEventData), eventData) - norm(filteredEventData)
        velocityRingCounter++
        velocityRing[velocityRingCounter % VELOCITY_RING_SIZE] = currentZ

        val velocityEstimate = sum(velocityRing)

        val isWalkingOrRunning = velocityEstimate > STEP_THRESHOLD &&
                oldVelocityEstimate <= STEP_THRESHOLD &&
                timeNs - lastStepTimeNs > STEP_DELAY_NS

        if (isWalkingOrRunning) {
            currentSteps++
            Log.d(TAG_NAME, "STATUS: $currentSteps steps. TIMESTAMP: $timeNs")
            lastStepTimeNs = timeNs
        }

        oldVelocityEstimate = velocityEstimate
        return isWalkingOrRunning
    }

    private fun lowPass(input: FloatArray, output: FloatArray?): FloatArray {
        val alpha = 0.8f
        if (output == null) return input
        for (i in input.indices) {
            output[i] = output[i] + alpha * (input[i] - output[i])
        }
        return output
    }

    companion object {
        private const val STEP_DELAY_NS = 300000000 // 300ms
        private const val ACCEL_RING_SIZE = 60
        private const val VELOCITY_RING_SIZE = 15
        private const val STEP_THRESHOLD = 16f // 4f-16f

        val TAG_NAME: String = AccelerometerService::class.java.name
    }
}
// Path: android/app/src/main/java/com/blokfield/cryptohunter/services/BackgroundService.kt