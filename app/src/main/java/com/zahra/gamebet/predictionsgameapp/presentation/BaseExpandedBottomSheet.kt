package com.zahra.gamebet.predictionsgameapp.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BaseExpandedBottomSheet : BottomSheetDialogFragment() {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = (it as? Dialog)?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isDraggable = isCancelable
            }
        }

        dialog.setOnKeyListener { _, keyCode, event ->

            if (
                    !isCancelable &&
                    event.action == KeyEvent.ACTION_UP &&
                    keyCode == KeyEvent.KEYCODE_BACK
            ) {
                activity?.onBackPressed()
                return@setOnKeyListener true
            }

            false
        }

        return dialog
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!manager.isStateSaved) {
            super.show(manager, tag)
        }
    }
}