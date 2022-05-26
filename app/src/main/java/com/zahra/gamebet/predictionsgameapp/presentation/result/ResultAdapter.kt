package com.zahra.gamebet.predictionsgameapp.presentation.result

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.zahra.gamebet.R
import com.zahra.gamebet.predictionsgameapp.domain.model.MatchResultModel


class ResultAdapter(
    private var onLoadMoreListener: (Int) -> Unit,
    private var showEmptyState: () -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_DATA = 1
        private const val VIEW_TYPE_ERROR = 2
    }

    private val items = mutableListOf<MatchResultModel>()

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

    fun appendData(list: List<MatchResultModel>, page: Int=1, totalPages: Int=1) {
        if (page == -1) return
        if (currentPage == page) return
        currentPage = page
        val currentItemsSize = items.size
        if (list.isNotEmpty()) {
            items.addAll(list)
            val newItemsSize = items.size
            notifyItemRangeChanged(currentItemsSize, newItemsSize - currentItemsSize)
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

        return ReviewViewHolder(inflater.inflate(R.layout.item_match_result, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ReviewViewHolder) {
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

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTeam1Name: TextView = itemView.findViewById(R.id.txtTeam1Title)
        private val txtTeam2Name: TextView = itemView.findViewById(R.id.txtTeam2Title)

        private val txtTeam1Point: TextView = itemView.findViewById(R.id.txtTeam1Point)
        private val txtTeam2Point: TextView = itemView.findViewById(R.id.txtTeam2Point)

        private val txtTeam1Prediction: TextView = itemView.findViewById(R.id.txtTeam1PointPrediction)
        private val txtTeam2Prediction: TextView = itemView.findViewById(R.id.txtTeam2PointPrediction)


        fun bind(position: Int) {

            val match = items[position]
            txtTeam1Name.text = match.team1
            txtTeam2Name.text = match.team2

            txtTeam1Point.text = match.getActualPointTeam1()
            txtTeam2Point.text = match.getActualPointTeam2()

            txtTeam1Prediction.text = match.getPredictionPointTeam1()
            txtTeam2Prediction.text = match.getPredictionPointTeam2()

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