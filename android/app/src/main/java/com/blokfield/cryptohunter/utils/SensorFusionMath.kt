package com.blokfield.cryptohunter.utils

import kotlin.math.sqrt

object SensorFusionMath {
    fun sum(vector: FloatArray): Float {
        var summation = 0f
        for (v in vector) {
            summation += v
        }
        return summation
    }

    fun cross(vectorA: FloatArray, vectorB: FloatArray): FloatArray {
        val outVector = FloatArray(3)
        outVector[0] = vectorA[1] * vectorB[2] - vectorA[2] * vectorB[1]
        outVector[1] = vectorA[2] * vectorB[0] - vectorA[0] * vectorB[2]
        outVector[2] = vectorA[0] * vectorB[1] - vectorA[1] * vectorB[0]
        return outVector
    }

    fun norm(vector: FloatArray): Float {
        var initFloat = 0f
        for (v in vector) {
            initFloat += v * v
        }
        return sqrt(initFloat)
    }

    fun dot(a: FloatArray, b: FloatArray): Float {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2]
    }

    fun normalize(vector: FloatArray): FloatArray {
        val normalizedVector = FloatArray(vector.size)
        val normed = norm(vector)
        for (i in vector.indices) {
            normalizedVector[i] = vector[i] / normed
        }
        return normalizedVector
    }
}
