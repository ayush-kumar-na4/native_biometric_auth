package com.example.native_biometric_auth

import android.os.Bundle
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.util.concurrent.Executor

class MainActivity : FlutterFragmentActivity() {
    private val CHANNEL = "native_auth"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call, result ->
                when (call.method) {
                    "authenticate" -> showBiometricPrompt(result)
                    "hasDeviceSecurity" -> checkDeviceSecurity(result)
                    else -> result.notImplemented()
                }
            }
    }

    private fun checkDeviceSecurity(result: MethodChannel.Result) {
        val biometricManager = BiometricManager.from(this)
        
        // Check if device has any form of authentication available
        val canAuthenticate = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
            BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )
        
        when (canAuthenticate) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Device has biometric or device credential available
                result.success(true)
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // No biometric enrolled, but device might have PIN/password
                // Check if device has PIN/password/pattern
                val hasDeviceCredential = biometricManager.canAuthenticate(
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
                result.success(hasDeviceCredential == BiometricManager.BIOMETRIC_SUCCESS)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                // No biometric hardware, check if device has PIN/password/pattern
                val hasDeviceCredential = biometricManager.canAuthenticate(
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
                result.success(hasDeviceCredential == BiometricManager.BIOMETRIC_SUCCESS)
            }
            else -> {
                // No authentication available at all
                result.success(false)
            }
        }
    }

    private fun showBiometricPrompt(result: MethodChannel.Result) {
        val executor: Executor = ContextCompat.getMainExecutor(this)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setSubtitle("Unlock to continue")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        val biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(authResult: BiometricPrompt.AuthenticationResult) {
                    result.success(true)
                }
                override fun onAuthenticationError(code: Int, msg: CharSequence) {
                    // Only handle user cancellation - let system handle other cases
                    if (code == BiometricPrompt.ERROR_USER_CANCELED) {
                        result.success(false)
                        finishAffinity() // Exit app if user cancels
                    }
                    // For other errors, let the system handle fallback
                }
                override fun onAuthenticationFailed() {
                    // Don't handle this - let system manage retry attempts
                }
            }
        )

        biometricPrompt.authenticate(promptInfo)
    }
}