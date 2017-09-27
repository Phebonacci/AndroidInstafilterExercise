package com.phebetries.instafilter.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.phebetries.instafilter.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onPrepared
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    // region Variable
    companion object {

        val REQUEST_CODE_PHOTO_FILTER = 0
    }
    // endregion

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        bindViews()
    }

    override fun onResume() {

        super.onResume()
        bindViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {

            REQUEST_CODE_PHOTO_FILTER -> {

                when (resultCode) {

                    Activity.RESULT_OK -> {
                        toast("Photo saved in Instafilter.")
                    }

                    else -> { }
                }
            }

            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun bindViews() {
        setAppLogoFont()

        setBackground()

        setClickListener()
    }

    fun setAppLogoFont() {
        val logoTypeface : Typeface = Typeface.createFromAsset(assets, "font/Billabong.ttf")
        appLogoText.typeface = logoTypeface
    }

    fun setBackground() {

        backgroundVideo.setZOrderOnTop(false)
        backgroundVideo.setOnPreparedListener { mp ->
            mp.isLooping = true
            mp.start()
        }
        val video : Uri = Uri.parse("android.resource://${packageName}/${R.raw.video_bg}" )
        backgroundVideo.setVideoURI(video)
    }

    fun setClickListener() {
        backgroundView.onClick {
            val intent = intentFor<PhotoFilterActivity>()
            startActivityForResult(intent, REQUEST_CODE_PHOTO_FILTER)
        }
    }
}
