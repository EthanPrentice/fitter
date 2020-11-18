package com.portalpirates.cufit.ui.view.swimlane

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.portalpirates.cufit.R
import com.portalpirates.cufit.datamodel.data.util.SwimlaneItem
import kotlinx.android.synthetic.main.fit_card_layout.view.*

class SwimlaneAdapter(private val context: Context, private val items: List<SwimlaneItem>, private val defaultImg: Drawable, private val onClickListener: ((v: View?, item: SwimlaneItem) -> Unit?)?)
    : RecyclerView.Adapter<SwimlaneAdapter.SwimlaneViewHolder>() {

    var textAppearance: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwimlaneViewHolder {
        val view = SwimlaneItemView(context)
        view.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT).apply {
            bottomMargin = context.resources.getDimensionPixelOffset(R.dimen.LU_2)
        }
        return SwimlaneViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SwimlaneViewHolder, position: Int) {
        holder.swimlaneItemView.text = items[position].getTitle()
        holder.swimlaneItemView.setImageDrawable(items[position].getDrawable() ?: defaultImg)
        holder.swimlaneItemView.setOnClickListener { v ->
            onClickListener?.invoke(v, items[position])
        }
        if (textAppearance != 0) {
            holder.swimlaneItemView.titleView.setTextAppearance(textAppearance)
        }
    }

    class SwimlaneViewHolder(itemView: SwimlaneItemView) : RecyclerView.ViewHolder(itemView) {
        val swimlaneItemView: SwimlaneItemView
            get() = itemView as SwimlaneItemView
    }
}