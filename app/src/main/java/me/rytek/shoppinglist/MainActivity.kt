package me.rytek.shoppinglist

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_activity.*
import me.rytek.shoppinglist.http.networkModule
import me.rytek.shoppinglist.ui.main.MainFragment
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.nav_host_fragment, MainFragment.newInstance())
//                .commitNow()
//        }

        fullscreenLayout()

        nav_host_fragment.post { // wait for NavHostFragment to inflate
            val navController = findNavController(R.id.nav_host_fragment);
            navController.addOnDestinationChangedListener {
                    controller, destination, arguments ->
                supportActionBar?.title = navController.currentDestination?.label
            }
        }

//        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
//        navController.addOnDestinationChangedListener {
//                controller, destination, arguments ->
//             supportActionBar?.title = navController.currentDestination?.label
//        }
    }

    fun fullscreenLayout() {
        supportActionBar?.hide()
        val w: Window = window // in Activity's onCreate() for instance
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        mainLayout.setBackgroundResource(R.drawable.login_gradient)
    }

    fun normalLayout() {
        supportActionBar?.show()
        val w: Window = window // in Activity's onCreate() for instance
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS.inv(),
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        mainLayout.setBackgroundResource(0)
        window.statusBarColor = getColor(R.color.colorStatusBarNormal)
    }

}
