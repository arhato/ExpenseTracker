import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Composable
fun BiometricAuthPrompt(
    onAuthenticationSuccess: () -> Unit,
    onAuthenticationFailed: () -> Unit
) {
    val context = LocalContext.current as FragmentActivity
    val executor: Executor = Executors.newSingleThreadExecutor()

    val biometricPrompt = BiometricPrompt(
        (context),
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                saveBiometricEnabled(context, true)
                Toast.makeText(
                    context, "Authentication Succeeded", Toast.LENGTH_SHORT
                ).show()
                onAuthenticationSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(
                    context, "Authentication error", Toast.LENGTH_SHORT
                ).show()
                onAuthenticationFailed()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(
                    context, "Authentication failed", Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    // Biometric prompt info
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setSubtitle("Authenticate to secure your app")
        .setNegativeButtonText("Cancel")
        .build()

    // Launch biometric prompt
    biometricPrompt.authenticate(promptInfo)
}

fun isBiometricAvailable(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
            BiometricManager.BIOMETRIC_SUCCESS
}

fun saveBiometricEnabled(context: Context, isEnabled: Boolean) {
    val sharedPreferences = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putBoolean("biometric_enabled", isEnabled)
        apply()
    }
}

fun isBiometricEnabled(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("biometric_enabled", false)
}