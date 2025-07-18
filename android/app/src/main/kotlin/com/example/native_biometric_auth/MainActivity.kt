package com.example.native_biometric_auth

import android.os.Bundle
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import io.flutter.embedding.android.FlutterFragmentActivity   // ⬅️ change
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.util.concurrent.Executor

class MainActivity : FlutterFragmentActivity() {            // ⬅️ change
    private val CHANNEL = "native_auth"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call, result ->
                if (call.method == "authenticate") {
                    showBiometricPrompt(result)
                } else {
                    result.notImplemented()
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
            this,                      // now a FragmentActivity ✅
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(authResult: BiometricPrompt.AuthenticationResult) {
                    result.success(true)
                }
                override fun onAuthenticationError(code: Int, msg: CharSequence) {
                    result.success(false)
                }
                override fun onAuthenticationFailed() {
                    result.success(false)
                }
            }
        )

        biometricPrompt.authenticate(promptInfo)
    }
}
