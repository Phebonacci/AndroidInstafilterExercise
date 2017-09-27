package com.phebetries.instafilter.activities;

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import com.phebetries.instafilter.R
import com.phebetries.instafilter.adapters.AbstractFilterAdapter
import com.phebetries.instafilter.filters.AbstractFilter
import com.phebetries.instafilter.filters.Amartoka
import com.phebetries.instafilter.filters.Juno
import com.phebetries.instafilter.filters.Original
import jp.co.cyberagent.android.gpuimage.GPUImage
import kotlinx.android.synthetic.main.activity_photo_filter.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

////////////////////////////////////////////////////////////////////////////////////////////////////
class PhotoFilterActivity : AppCompatActivity() {

  // region Variables
  companion object {

    val REQUEST_CODE_PHOTO_PICK = 1
    val PERMISSION_WRITE_EXTERNAL_STORAGE = 1000
  }

  private val filters by lazy {

    Array<AbstractFilter>(3, { i ->

      when (i) {

        0 -> Original(this@PhotoFilterActivity)
        1 -> Juno(this@PhotoFilterActivity)
        2 -> Amartoka(this@PhotoFilterActivity)
        else -> throw IndexOutOfBoundsException()
      }
    })
  }

  private var lastFilterIndex = 0
  private var lastUsedFilter: AbstractFilter? = null

  private val adapter: AbstractFilterAdapter by lazy {

    AbstractFilterAdapter.lastPosition = lastFilterIndex
    AbstractFilterAdapter(this@PhotoFilterActivity,
        R.layout.list_filter, filters)
  }
  // endregion

  // region Lifecycle
  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_photo_filter)
    bindViews()
    initAdapter()
    pickPhoto()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    when (requestCode) {

      REQUEST_CODE_PHOTO_PICK -> {

        when (resultCode) {

          Activity.RESULT_OK -> {

            gpuImage.setImage(data?.data)
            gpuImage.setScaleType(GPUImage.ScaleType.CENTER_CROP)
          }

          else -> finish()
        }
      }

      else -> super.onActivityResult(requestCode, resultCode, data)
    }
  }
  // endregion

  // region menu
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater?.inflate(R.menu.photo_filter_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when(item?.itemId) {
      R.id.saveButton -> {
        saveImage()
      }
    }
    return true
  }
  // endregion

  // region permissions
  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    when(requestCode) {
      PERMISSION_WRITE_EXTERNAL_STORAGE -> {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) saveImageWithPermission()
      }
    }
  }
  // endregion

  // region Salin-salin methods
  private fun bindViews() {

    gpuImage.onClick{

      pickPhoto()
    }
  }

  private fun initAdapter() {

    AbstractFilterAdapter.lastPosition = lastFilterIndex
    horizontalList.adapter = adapter
    horizontalList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

      try {

        if (position == lastFilterIndex) return@OnItemClickListener
        lastFilterIndex = position
        AbstractFilterAdapter.lastPosition = position
        adapter.notifyDataSetChanged()

        filters[position].context = this@PhotoFilterActivity
        filters[position].initialize()
        val imageFilter = filters[position].filter

        gpuImage.filter = imageFilter
        gpuImage.requestRender()

        if (lastUsedFilter == null) {
          lastUsedFilter = filters[position]
        } else {
          lastUsedFilter?.release()
          lastUsedFilter = filters[position]
        }
      } catch (e: OutOfMemoryError) {

        Log.e("APP:", "Error $e")
      }
    }
  }

  private fun pickPhoto() {

    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    startActivityForResult(intent, REQUEST_CODE_PHOTO_PICK)
  }

  private fun saveImage() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        alert(R.string.permission_required, R.string.write_access_needed){}
      } else {
        ActivityCompat.requestPermissions(this,
                Array (1, {i -> Manifest.permission.WRITE_EXTERNAL_STORAGE}),
                PERMISSION_WRITE_EXTERNAL_STORAGE)
      }
    } else {
      saveImageWithPermission()
    }
  }

  private fun saveImageWithPermission() {
    val filename = "${System.currentTimeMillis()}.jpg"
    val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val file = File(path, "${getString(R.string.app_name)}/$filename")
    file.parentFile.mkdirs()

    var out: FileOutputStream? = null
    val bitmap = gpuImage.gpuImage.bitmapWithFilterApplied
    try {
      out = FileOutputStream(file)
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    } catch (e: Exception) {
      e.printStackTrace()
    } finally {
      try {
        out?.close()
      } catch (ioe: IOException) {
        ioe.printStackTrace()
      }
      setResult(Activity.RESULT_OK)
      finish()
    }
  }
  // endregion
}
////////////////////////////////////////////////////////////////////////////////////////////////////