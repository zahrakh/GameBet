package com.zahra.gamebet.predictionsgameapp.presentation.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.zahra.gamebet.R
import com.zahra.gamebet.predictionsgameapp.domain.model.Match
import com.zahra.gamebet.utils.click


class ListAdapter(
    private var onLoadMoreListener: (Int) -> Unit,
    private var showEmptyState: () -> Unit,
    private val onItemClicked: (Match, View, Int) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_DATA = 1
        private const val VIEW_TYPE_ERROR = 2
    }

    private val items = mutableListOf<Match>()

    var isLoading = false
        set(value) {
            field = value
            notifyItemChanged(itemCount)
        }

    var isError = false
        set(value) {
            field = value
            notifyItemChanged(itemCount)
        }

    var noMoreData = false
    var currentPage = 0

    fun appendData(
        list: List<Match>,
        page: Int = 1,
        totalPages: Int = 1,
        singlePage: Boolean = true
    ) {
        if (!singlePage) {
            if (page == -1) return
            if (currentPage == page) return
        }
        currentPage = page
        val currentItemsSize = items.size
        if (list.isNotEmpty()) {
            if (!singlePage) {
                items.addAll(list)
                val newItemsSize = items.size
                notifyItemRangeChanged(currentItemsSize, newItemsSize - currentItemsSize)
            } else {
                val lastSize = items.size
                items.clear()
                notifyItemRangeRemoved(0, lastSize)
                items.addAll(list)
                val newItemsSize = items.size
                notifyItemRangeChanged(currentItemsSize, newItemsSize - currentItemsSize)
            }
        }
        if (page >= totalPages) {
            noMoreData = true
        }
        if (items.isEmpty() && page != -1) {
            showEmptyState()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == VIEW_TYPE_LOADING) {
            return LoadingViewHolder(inflater.inflate(R.layout.item_match_loading, parent, false))
        }

        if (viewType == VIEW_TYPE_ERROR) {
            return ErrorViewHolder(inflater.inflate(R.layout.item_match_error, parent, false))
        }

        return ListViewHolder(inflater.inflate(R.layout.item_match_list, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ListViewHolder) {
            holder.bind(position)
        }
        if (!noMoreData && !isError && !isLoading && position == itemCount - 1) {
            onLoadMoreListener.invoke(currentPage + 1)
        }
    }


    override fun getItemCount(): Int {
        var dataCount = items.size
        if (isLoading) {
            dataCount++
        }
        if (isError) {//isError and isLoading never be true at the same time
            dataCount++
        }
        return dataCount
    }

    override fun getItemViewType(position: Int): Int {
        val dataCount = items.size

        return if (isLoading) {
            if (position < dataCount) {
                VIEW_TYPE_DATA
            } else {
                VIEW_TYPE_LOADING
            }
        } else if (isError) {
            if (position < dataCount) {
                VIEW_TYPE_DATA
            } else {
                VIEW_TYPE_ERROR
            }
        } else {
            VIEW_TYPE_DATA
        }
    }

    fun updateRow(match: Match, position: Int?) {
        position?.let {
            items.removeAt(it)
            notifyItemRemoved(it)
            items.add(it,match)
            notifyItemRangeChanged(position,itemCount)
        }

    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val clickLayout: FrameLayout = itemView.findViewById(R.id.clickLayout)
        private val txtTeam1Title: TextView = itemView.findViewById(R.id.txtTeam1Title)
        private val txtTeam2Title: TextView = itemView.findViewById(R.id.txtTeam2Title)
        private val txtTeam1Point: TextView = itemView.findViewById(R.id.txtTeam1Point)
        private val txtTeam2Point: TextView = itemView.findViewById(R.id.txtTeam2Point)

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val match = items[position]
            txtTeam1Title.text = match.team1
            txtTeam2Title.text = match.team2
            txtTeam1Point.text = match.getPointTeam1()
            txtTeam2Point.text = match.getPointTeam2()

            clickLayout.click {
                onItemClicked(
                    match,
                    itemView,
                    position
                )
            }
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val loading: View = itemView.findViewById(R.id.loading)
    }

    inner class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val errorButton: MaterialButton = itemView.findViewById(R.id.btn_Error)

        init {
            errorButton.setOnClickListener {
                onLoadMoreListener(currentPage + 1)
            }
        }
    }

}