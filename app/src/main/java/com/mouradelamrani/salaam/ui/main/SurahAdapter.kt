package com.mouradelamrani.salaam.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mouradelamrani.salaam.data.models.Surah
import com.mouradelamrani.salaam.views.SurahItemView

class SurahAdapter(private val listener: Listener) :
    RecyclerView.Adapter<SurahAdapter.SurahViewHolder>() {

    interface Listener {
        fun onSurahClick(surah: Surah)
    }


    var items: List<Surah> = emptyList()
        set(newItems) {
            field = newItems
            notifyDataSetChanged()
        }


    // region adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahViewHolder {
        val view = SurahItemView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return SurahViewHolder(view)
    }

    override fun onBindViewHolder(holder: SurahViewHolder, position: Int) {
        holder.view.showSurah(items[position])
    }

    override fun getItemCount(): Int = items.size

    // endregion


    // region view holder

    inner class SurahViewHolder(val view: SurahItemView) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnClickListener
                listener.onSurahClick(items[position])
            }
        }

    }

    // endregion
}