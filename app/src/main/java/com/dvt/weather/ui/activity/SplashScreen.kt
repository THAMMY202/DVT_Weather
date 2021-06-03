package com.dvt.weather.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dvt.weather.R

class SplashScreen : AppCompatActivity() {
    var topAnimation: Animation? = null
    var buttonAnimation: Animation? = null
    var slideOut: Animation? = null
    var slideIn: Animation? = null
    var logo: TextView? = null
    var appName: TextView? = null
    var createdBy: TextView? = null
    private val key = "allowLocation"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val dataSharedPref = getSharedPreferences("locationDataSharedPref ", MODE_PRIVATE)

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right)


        logo = findViewById<View>(R.id.logo) as TextView
        appName = findViewById<View>(R.id.appName) as TextView
        createdBy = findViewById<View>(R.id.createdBy) as TextView
        logo!!.animation = topAnimation
        appName!!.animation = buttonAnimation
        createdBy!!.animation = buttonAnimation
        Handler().postDelayed({

            if (dataSharedPref.getInt(key, 0) == 0) {
                val intent = Intent(this@SplashScreen, LocationDisclaimerActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                finish()
            } else {
                val intent = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                finish()
            }

        }, splash_screen.toLong())
    }

    companion object {
        private const val splash_screen = 3000
    }
}
