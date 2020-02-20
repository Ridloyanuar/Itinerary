package org.malucky.itinerary

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            return Intent(activity, MainActivity::class.java)
        }
    }

    override fun getView(): Int = R.layout.activity_main
    lateinit var navController: NavController

    override fun onActivityCreated() {
        navController = Navigation.findNavController(this,R.id.nav_host_fragment)
        bottom_navigation.setupWithNavController(navController)
    }

}