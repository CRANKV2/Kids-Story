import android.os.Build
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat

class SystemUiController(private val window: Window) {
    var isSystemBarsVisible: Boolean = true
        @RequiresApi(Build.VERSION_CODES.R)
        set(value) {
            field = value
            if (!value) {
                hideSystemUI()
            } else {
                showSystemUI()
            }
        }

    @RequiresApi(Build.VERSION_CODES.R)
    var systemBarsBehavior: Int = WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_TOUCH
        @RequiresApi(Build.VERSION_CODES.R)
        set(value) {
            field = value
            window.insetsController?.systemBarsBehavior = value
        }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.insetsController?.apply {
            hide(WindowInsets.Type.systemBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.insetsController?.show(WindowInsets.Type.systemBars())
    }
} 