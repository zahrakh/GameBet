package com.zahra.gamebet.predictionsgameapp.presentation.list

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.zahra.gamebet.R
import com.zahra.gamebet.predictionsgameapp.domain.model.Point
import com.zahra.gamebet.predictionsgameapp.presentation.BaseFragment
import com.zahra.gamebet.utils.click
import com.zahra.gamebet.utils.gone
import com.zahra.gamebet.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

const val ARG_MATCH_CURRENT_PAGE = "arg_match_current_page"
const val ARG_MATCH_LAST_UPDATE_TIME = "arg_match_last_update_time"

@AndroidEntryPoint
class ListFragment : BaseFragment(
    R.layout.fragment_list
), PointBottomSheet.PointSetListener {

    private val viewModel by viewModels<ListViewModel>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var shoeResult: TextView

    private val adapter = ListAdapter(
        onLoadMoreListener = {
            viewModel.getMatchList()
        },
        onItemClicked = { match, _, position ->
            PointBottomSheet.newInstance(
                Point(
                    id = position,
                    team1Name = match.team1,
                    team1Point = match.team1Point ?: 0,
                    team2Name = match.team2,
                    team2Point = match.team2Point ?: 0,
                )
            ).show(childFragmentManager, "ButtonSheet")
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

        viewModel.init()
    }

    private fun observers() {
        viewModel.machList.observe(viewLifecycleOwner) { list ->
            list.matches?.let { adapter.appendData(it) }
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

        viewModel.errorMessageRes.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(activity, getString(it), Toast.LENGTH_LONG).show()
            }
        }

        viewModel.showResult.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(
                    resId = R.id.action_predictionListFragment_to_resultFragment
                )
                viewModel.clearResult()
            }
        }
    }

    private fun bindViews(view: View) {
        shoeResult = view.findViewById(R.id.txtShowResult)
        emptyView = view.findViewById(R.id.tv_empty_state)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.itemAnimator = null
        shoeResult.text=getString(R.string.show_result)
    }

    private fun listeners() {

        childFragmentManager.addFragmentOnAttachListener { _, fragment ->
            when (fragment) {
                is PointBottomSheet -> fragment.listener = this
            }
        }

        shoeResult.click {
            viewModel.openResultPage()
        }
    }

    private fun recycler() {
        recyclerView.adapter = adapter
    }

    override fun onSelectPoint(point: Point?) {
        viewModel.updateMatch(point)
    }

    override fun onBackPressed() {
        activity?.finish()
    }

    override fun onDestroy() {
        viewModel.onDestroyView()
        super.onDestroy()
    }

}