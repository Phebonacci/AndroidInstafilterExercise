/**
 * Instafilter
 *
 * Created by iomusashi on 9/26/17.
 * Copyright (c) 2017 iomusashi. All rights reserved.
 */

package com.phebetries.instafilter.filters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.phebetries.instafilter.R
import jp.co.cyberagent.android.gpuimage.GPUImageLookupFilter

////////////////////////////////////////////////////////////////////////////////////////////////////
class Amartoka(context: Context) :
        AbstractFilter(R.drawable.amarto, Amartoka::class.java.simpleName) {

  override fun initialize() {

    val amartoka = GPUImageLookupFilter()
    val bitmapOptions = BitmapFactory.Options()
    bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
    lookup = BitmapFactory.decodeResource(context?.resources,
            R.drawable.lookup_amatorka, bitmapOptions)
    amartoka.setBitmap(lookup)
    filter = amartoka
  }
}
////////////////////////////////////////////////////////////////////////////////////////////////////