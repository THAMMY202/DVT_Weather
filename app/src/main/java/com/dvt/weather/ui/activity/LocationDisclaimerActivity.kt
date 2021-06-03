package com.dvt.weather.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dvt.weather.R
import kotlinx.android.synthetic.main.activity_location_disclaimer.*

class LocationDisclaimerActivity : AppCompatActivity() {

    private val key = "allowLocation"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_disclaimer)

        val dataSharedPref = getSharedPreferences("locationDataSharedPref ", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = dataSharedPref.edit()

        btnForbidding.setOnClickListener {
            editor.putInt(key, 0)
            editor.apply()
            editor.commit()
            startActivity(Intent(this@LocationDisclaimerActivity, MainActivity::class.java))
            finish()
        }

        btnAllow.setOnClickListener(View.OnClickListener {
            editor.putInt(key, 1)
            editor.apply()
            editor.commit()
            startActivity(Intent(this@LocationDisclaimerActivity, MainActivity::class.java))
            finish()
        })

    }
}