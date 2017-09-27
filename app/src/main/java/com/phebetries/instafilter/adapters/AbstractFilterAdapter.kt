/**
 * Instafilter
 *
 * Created by iomusashi on 9/25/17.
 * Copyright (c) 2017 iomusashi. All rights reserved.
 */

package com.phebetries.instafilter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.phebetries.instafilter.filters.AbstractFilter
import kotlinx.android.synthetic.main.list_filter.view.*

////////////////////////////////////////////////////////////////////////////////////////////////////
class AbstractFilterAdapter(context: Context,
                            val resource: Int,
                            private val filters: Array<AbstractFilter>)
  : ArrayAdapter<AbstractFilter>(context, resource, filters) {

  companion object {

    var lastPosition: Int = -1
  }

  private val inflater by lazy { LayoutInflater.from(context) }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

    var row: View? = null
    var viewHolder: FilterViewHolder? = null

    if (convertView == null) {

      row = inflater.inflate(resource, parent, false)
      viewHolder = FilterViewHolder(position, lastPosition, row)
      row.tag = viewHolder
    } else {

      row = convertView
      viewHolder = row.tag as FilterViewHolder
    }

    viewHolder.bindViews(getItem(position))
    return row ?: View(context)
  }

  override fun getItem(position: Int): AbstractFilter {
    return filters[position]
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getCount(): Int {
    return filters.size
  }
}
////////////////////////////////////////////////////////////////////////////////////////////////////
class FilterViewHolder(private val position: Int,
                       private val lastPosition: Int,
                       private val view: View) {

  fun bindViews(filter: AbstractFilter) {

    if (position == lastPosition) {
      view.filterContainerLayout.setBackgroundColor(0x222222)
    } else {
      view.filterContainerLayout.setBackgroundColor(0x333333)
    }

    if (filter.drawable != Int.MIN_VALUE) {
      view.filterImage.setImageResource(filter.drawable)
    }
    if (filter.name.isNotEmpty()) {
      view.filterText.text = filter.name
    }
  }
}
////////////////////////////////////////////////////////////////////////////////////////////////////