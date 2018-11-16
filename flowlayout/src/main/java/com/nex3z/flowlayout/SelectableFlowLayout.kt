package com.balinasoft.school.common.views

import android.content.Context
import android.graphics.Color
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import com.nex3z.flowlayout.FlowLayout
import java.util.*
import kotlin.collections.ArrayList

class SelectableFlowLayout : FlowLayout {

    data class FlowLayoutViewPair(
            val view: View,
            var state: Boolean = false
    )

    private val onClickListener = OnClickListener {
        if (isSelectable) {
            findItem(it)?.let {
                setViewState(it.also { it.state = !it.state })
            }
        }
    }

    @DrawableRes
    var selectedImage: Int? = null
    @DrawableRes
    var unselectedImage: Int? = null
    @DrawableRes
    var selectedBackground: Int? = null
    @DrawableRes
    var unselectedBackground: Int? = null
    var selectedBackgroundColor = Color.LTGRAY
    var unselectedBackgroundColor = Color.TRANSPARENT
    var selectedTextColor = Color.BLACK
    var unselectedTextColor = Color.LTGRAY

    private var isSelectable = false
    private val views = ArrayList<FlowLayoutViewPair>()

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    /**
     * Method for changing FlowLayout views state (selected or unselected). Should be called after
     * configuring FlowLayout resources.
     */
    fun setSelectableState(state: Boolean) {
        if (state) {
            val childCount = childCount

            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.setOnClickListener(onClickListener)

                views.add(FlowLayoutViewPair(child))
                setViewState(views[i])
            }
        }

        isSelectable = state
    }

    /**
     * Method for getting indexes of selected items in the layout.
     */
    fun getSelectedIndexes(): ArrayList<Int> {
        val result = ArrayList<Int>()

        for (i in 0 until views.size) {
            if (views[i].state) {
                result.add(i)
            }
        }

        return result
    }

    private fun setViewState(viewPair: FlowLayoutViewPair) {
        with(viewPair) {
            if (state) {
                if (selectedBackground != null) {
                    view.setBackgroundResource(selectedBackground!!)
                } else {
                    view.setBackgroundColor(selectedBackgroundColor)
                }
                if (view is TextView) {
                    view.setTextColor(selectedTextColor)
                    if (unselectedImage != null) {
                        view.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(view.context, selectedImage!!),
                                null,
                                null,
                                null)
                    }
                }
            } else {
                if (unselectedBackground != null) {
                    view.setBackgroundResource(unselectedBackground!!)
                } else {
                    view.setBackgroundColor(unselectedBackgroundColor)
                }
                if (view is TextView) {
                    view.setTextColor(unselectedTextColor)
                    if (unselectedImage != null) {
                        view.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(view.context, unselectedImage!!),
                                null,
                                null,
                                null)
                    }
                }

            }
        }
    }

    private fun findItem(item: View): FlowLayoutViewPair? {
        for (i in views) {
            if (i.view === item) {
                return i
            }
        }
        return null
    }
}
