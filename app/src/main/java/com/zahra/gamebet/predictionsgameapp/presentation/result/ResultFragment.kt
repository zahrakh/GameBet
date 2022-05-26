package com.zahra.gamebet.predictionsgameapp.presentation.result

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.zahra.gamebet.R
import com.zahra.gamebet.predictionsgameapp.presentation.BaseFragment
import com.zahra.gamebet.utils.click
import com.zahra.gamebet.utils.gone
import com.zahra.gamebet.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ResultFragment : BaseFragment(
    R.layout.fragment_result
) {

    private val viewModel by viewModels<ResultViewModel>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var txtReset: TextView

    private val adapter = ResultAdapter(
        onLoadMoreListener = {
            viewModel.matchResults()
        },
        showEmptyState = {
            recyclerView.gone()
            emptyView.show()
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews(view)

        listeners()

        observers()

        recycler()
    }

    private fun observers() {
        viewModel.machListResult.observe(viewLifecycleOwner) { list ->
            list.matchesResult?.let { adapter.appendData(it) }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest { isError ->
                adapter.isError = isError
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loading.collectLatest { isLoading ->
                adapter.isLoading = isLoading
            }
        }

    }

    private fun bindViews(view: View) {
        emptyView = view.findViewById(R.id.tv_empty_state)
        txtReset = view.findViewById(R.id.txtShowResult)
        recyclerView = view.findViewById(R.id.recycler_view)

        recyclerView.itemAnimator = null
        txtReset.text = getString(R.string.reStart)
    }

    private fun listeners() {

        txtReset.click {
            viewModel.resetData()
            onBackPressed()
        }
    }

    private fun recycler() {
        recyclerView.adapter = adapter
    }


    override fun onBackPressed() {
        findNavController().popBackStack()
    }


    override fun onDestroy() {
        viewModel.onDestroyView()
        super.onDestroy()
    }

}