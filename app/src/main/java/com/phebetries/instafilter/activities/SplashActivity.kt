package com.phebetries.instafilter.activities

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.phebetries.instafilter.R
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    supportActionBar?.hide()
    bindViews()
    initApp()
  }

  fun bindViews() {
    val typeface : Typeface = Typeface.createFromAsset(assets, "font/Billabong.ttf")
    appLogoText.typeface = typeface
  }

  fun initApp() {
    Handler().postDelayed({
      startActivity<MainActivity>()
      finish()
    }, 2000)
  }
}
