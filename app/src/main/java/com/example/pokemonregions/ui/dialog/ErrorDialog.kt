package com.example.pokemonregions.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.example.pokemonregions.R
import com.example.pokemonregions.databinding.ErrorDialogBinding

class ErrorDialog(val context:Context) {

    private var dialog: Dialog = Dialog(context)
    private val binding:ErrorDialogBinding by lazy { ErrorDialogBinding.inflate(LayoutInflater.from(context)) }

    init {
        dialog.setContentView(binding.root)
        dialog.window?.setLayout(
            context.resources.getDimensionPixelSize(R.dimen.error_dialog_width),
            context.resources.getDimensionPixelSize(R.dimen.error_dialog_height)
        )
    }

    fun setTitle(title:CharSequence):ErrorDialog{
        binding.errorTitleDialogTextView.text = title
        return this
    }

    fun setMessage(message:CharSequence):ErrorDialog{
        binding.errorTextDialogTextView.text = message
        return this
    }

    fun setCancelButtonListener(onCancelClick: () -> Unit): ErrorDialog {
        binding.errorOkDialogButton.setOnClickListener {
            onCancelClick.invoke()
            dialog.dismiss()
        }
        return this
    }

    fun setCancelButtonListener(onClickListener: View.OnClickListener): ErrorDialog {
        binding.errorOkDialogButton.setOnClickListener(onClickListener)
        return this
    }

    fun showDialog(){
        if(!dialog.isShowing)
            dialog.show()
    }

    fun dismiss(){
        dialog.dismiss()
    }
}