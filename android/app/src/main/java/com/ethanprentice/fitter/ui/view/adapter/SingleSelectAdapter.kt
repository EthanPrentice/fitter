package com.ethanprentice.fitter.ui.view.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ethanprentice.fitter.R
import com.ethanprentice.fitter.ui.view.SingleSelectView

/**
 * Manages [SingleSelectView]s for a [RecyclerView], forcing one and only one to be selected
 */
class SingleSelectAdapter(private val context: Context, private val strings: List<String>, private val default: String? = null)
    : RecyclerView.Adapter<SingleSelectAdapter.SingleSelectViewHolder>() {

    constructor(context: Context, stringsResId: Int, default: String? = null)
        : this(context, context.resources.getStringArray(stringsResId).toList(), default)

    private var currChecked: SingleSelectView? = null

    private var onCheckedChangeListener: ((v: SingleSelectView) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleSelectViewHolder {
        val view = SingleSelectView(context)

        if (currChecked == null && default == null) {
            view.isChecked = true
            currChecked = view
        }

        view.setOnClickListener { v ->
            onItemClick(v as SingleSelectView)
        }

        view.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT).apply {
            bottomMargin = context.resources.getDimensionPixelOffset(R.dimen.LU_2)
        }
        return SingleSelectViewHolder(view)
    }

    override fun getItemCount(): Int {
        return strings.size
    }

    override fun onBindViewHolder(holder: SingleSelectViewHolder, position: Int) {
        holder.singleSelectView.text = strings[position]

        if (default == strings[position] && currChecked == null) {
            // simulate item click if is default and not checked by default
            onItemClick(holder.singleSelectView)
        }
    }

    fun onItemClick(v: SingleSelectView) {
        currChecked?.isChecked = false
        v.isChecked = true
        currChecked = v
        onCheckedChangeListener?.invoke(v)
    }

    fun getChecked(): String? {
        return currChecked?.text.toString()
    }

    fun setOnCheckedChangeListener(l: ((v: SingleSelectView) -> Unit)?) {
        onCheckedChangeListener = l
    }

    class SingleSelectViewHolder(itemView: SingleSelectView) : RecyclerView.ViewHolder(itemView) {
        val singleSelectView: SingleSelectView
            get() = itemView as SingleSelectView
    }
}