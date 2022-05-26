package com.zahra.gamebet.predictionsgameapp.presentation.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import com.zahra.gamebet.R
import com.zahra.gamebet.predictionsgameapp.domain.model.Point
import com.zahra.gamebet.predictionsgameapp.presentation.BaseExpandedBottomSheet
import com.zahra.gamebet.utils.click

const val ARG_MATCH_POINT = "arg_match_point"

class PointBottomSheet :
    BaseExpandedBottomSheet() {

    private lateinit var tvTeam1: TextView
    private lateinit var tvTeam1Point: TextView
    private lateinit var btnTeam1Add: ImageButton
    private lateinit var btnTeam1Minus: ImageButton

    private lateinit var tvTeam2: TextView
    private lateinit var tvTeam2Point: TextView
    private lateinit var btnTeam2Add: ImageButton
    private lateinit var btnTeam2Minus: ImageButton
    private lateinit var btnClose: ImageButton

    private var point: Point? = null

    companion object {
        fun newInstance(
            point: Point
        ): PointBottomSheet = PointBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_MATCH_POINT, point)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_point, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        point = arguments?.getParcelable(ARG_MATCH_POINT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews(view)

        listeners()
    }


    private fun bindViews(view: View) {
        btnClose = view.findViewById(R.id.btn_close)

        tvTeam1 = view.findViewById(R.id.tv_team1)
        tvTeam1Point = view.findViewById(R.id.tv_team1_point)
        btnTeam1Add = view.findViewById(R.id.btn_team1_add)
        btnTeam1Minus = view.findViewById(R.id.btn_team1_minus)
        tvTeam2 = view.findViewById(R.id.tv_team2)
        tvTeam2Point = view.findViewById(R.id.tv_team2_point)
        btnTeam2Add = view.findViewById(R.id.btn_team2_add)
        btnTeam2Minus = view.findViewById(R.id.btn_team2_minus)

        tvTeam1.text = point?.team1Name
        tvTeam1Point.text = point?.team1Point.toString()

        tvTeam2.text = point?.team2Name
        tvTeam2Point.text = point?.team2Point.toString()

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun listeners() {

        btnTeam1Add.click {
            point?.increaseTeam1Point()
            tvTeam1Point.text=(point?.team1Point?:0).toString()
        }
        btnTeam2Minus.click {
            if(point?.decreaseTeam2Point()==false) {
                Toast.makeText(activity,getString(R.string.invalid_point),Toast.LENGTH_LONG).show()
                return@click
            }
            tvTeam2Point.text=(point?.team2Point?:0).toString()
        }
        btnTeam2Add.click {
            point?.increaseTeam2Point()
            tvTeam2Point.text=(point?.team2Point?:0).toString()
        }
        btnTeam1Minus.click {
            if(point?.decreaseTeam1Point()==false) {
                Toast.makeText(activity,getString(R.string.invalid_point),Toast.LENGTH_LONG).show()
                return@click
            }
            tvTeam1Point.text=(point?.team1Point?:0).toString()
        }


        btnClose.click {
            listener?.onSelectPoint(point)
            dismissAllowingStateLoss()
        }

    }




    var listener: PointSetListener? = null

    interface PointSetListener {
        fun onSelectPoint(point: Point?)
    }

}