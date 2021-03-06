package gordon.lab.github_user_mvc.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class NavControllerHelper {

    private val navigationMutex = Mutex()
    private var isNavigated: Boolean = false

    fun onResume() {
        isNavigated = false
    }

    fun navigate(navController: NavController, @IdRes resId: Int, args: Bundle? = null) {
        if (!haveNavigated()) {
            navController.navigate(resId, args)
        }
    }

    fun navigate(navController: NavController, directions: NavDirections) {
        if (!haveNavigated()) {
            navController.navigate(directions)
        }
    }

    private fun haveNavigated(): Boolean = runBlocking {
        navigationMutex.withLock {
            if (isNavigated) true
            else {
                isNavigated = true
                false
            }
        }
    }
}