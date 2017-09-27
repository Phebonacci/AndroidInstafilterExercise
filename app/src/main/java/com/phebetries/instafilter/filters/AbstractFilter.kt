/**
 * Instafilter
 *
 * Created by iomusashi on 9/25/17.
 * Copyright (c) 2017 iomusashi. All rights reserved.
 */

package com.phebetries.instafilter.filters

import android.content.Context
import android.graphics.Bitmap
import jp.co.cyberagent.android.gpuimage.GPUImageFilter

////////////////////////////////////////////////////////////////////////////////////////////////////
abstract class AbstractFilter(val drawable: Int,
                              val name: String) {

  var filter: GPUImageFilter? = null
  var context: Context? = null
  protected var lookup: Bitmap? = null

  abstract fun initialize()

  fun release() {

    filter = null
    lookup?.recycle()
    lookup = null
    context = null
  }
}
////////////////////////////////////////////////////////////////////////////////////////////////////