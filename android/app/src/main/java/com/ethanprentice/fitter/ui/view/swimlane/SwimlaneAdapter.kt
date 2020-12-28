package com.ethanprentice.fitter.ui.view.swimlane

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ethanprentice.fitter.R
import com.ethanprentice.fitter.datamodel.data.util.SwimlaneItem

class SwimlaneAdapter(private val context: Context, private val items: List<SwimlaneItem>, private val defaultImg: Drawable, private val onClickListener: ((v: View?, item: SwimlaneItem) -> Unit?)?)
    : RecyclerView.Adapter<SwimlaneAdapter.SwimlaneViewHolder>() {

    private var includeAddItem = false
        set(value) {
            if (field != value) {
                if (value) {
                    notifyItemInserted(0)
                } else {
                    notifyItemRemoved(0)
                }
            }
            field = value
        }
    private var addItemOnClickListener: (() -> Unit?)? = null
    private val itemDisplacement: Int
        get() = if (includeAddItem) 1 else 0


    var textAppearance: Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwimlaneViewHolder {
        val view = SwimlaneItemView(context)
        view.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT).apply {
            bottomMargin = context.resources.getDimensionPixelOffset(R.dimen.LU_2)
        }
        return SwimlaneViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size + itemDisplacement
    }

    override fun onBindViewHolder(holder: SwimlaneViewHolder, position: Int) {

        if (includeAddItem && position == 0) { // Is add item
            holder.swimlaneItemView.text = "Add"
            
            holder.swimlaneItemView.setImageResource(R.drawable.add_circle)
            holder.swimlaneItemView.setOnClickListener {
                addItemOnClickListener?.invoke()
            }
        }
        else { // Not add item
            holder.swimlaneItemView.text = items[position - itemDisplacement].getTitle()
            holder.swimlaneItemView.setImageDrawable(items[position  - itemDisplacement].getDrawable() ?: defaultImg)
            holder.swimlaneItemView.setOnClickListener { v ->
                onClickListener?.invoke(v, items[position  - itemDisplacement])
            }
        }

        if (textAppearance != 0) {
            holder.swimlaneItemView.titleView.setTextAppearance(textAppearance)
        }
    }


    fun enableAddSwimlaneItem(onClickListener: (() -> Unit?)?) {
        includeAddItem = true
        addItemOnClickListener = onClickListener
    }

    fun disableAddSwimlaneItem() {
        includeAddItem = false
        addItemOnClickListener = null
    }


    class SwimlaneViewHolder(itemView: SwimlaneItemView) : RecyclerView.ViewHolder(itemView) {
        val swimlaneItemView: SwimlaneItemView
            get() = itemView as SwimlaneItemView
    }
}