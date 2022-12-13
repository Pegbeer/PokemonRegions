package com.example.pokemonregions.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.pokemonregions.R
import com.example.pokemonregions.databinding.DialogInputBinding

class InputDialog(private val context:Context) {

    private var dialog: Dialog = Dialog(context)
    private val binding:DialogInputBinding by lazy { DialogInputBinding.inflate(LayoutInflater.from(context)) }

    init{
        dialog.setContentView(binding.root)
        dialog.window?.setLayout(
            context.resources.getDimensionPixelSize(R.dimen.input_dialog_width),
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }

    fun setPositiveButton(text:CharSequence,action:(CharSequence?,InputDialog)->Unit):InputDialog{
        binding.buttonDialogInput.text = text
        binding.buttonDialogInput.setOnClickListener{
            action.invoke(binding.valueDialogInputEditText.editText?.text,this)
        }
        return this
    }

    fun setTitle(text: CharSequence):InputDialog{
        binding.titleDialogInputTextView.text = text
        return this
    }

    fun getInput():String{
        return binding.valueDialogInputEditText.editText?.text.toString()
    }

    fun showDialog(){
        if(!dialog.isShowing)
            dialog.show()
    }

    fun dismiss(){
        dialog.dismiss()
    }
}