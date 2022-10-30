package com.lokilinks.shoppniglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.lokilinks.shoppniglist.R
import com.lokilinks.shoppniglist.databinding.NewListDialogBinding

object NewListDialog {
    fun showDialog(context: Context, listener: Listener, name: String){
        var dialog: AlertDialog?= null
        val builder = AlertDialog.Builder(context)
        val binding = NewListDialogBinding.inflate(LayoutInflater.from(context))

        builder.setView(binding.root)
        binding.apply {
            edNewListName.setText(name)
            if (name.isNotEmpty()) bCreate.text = context.getString(R.string.update)
            bCreate.setOnClickListener {
                val listname = edNewListName.text.toString()
                if (listname.isNotEmpty()){
                    listener.onClick(listname)
                }
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener{
        fun onClick (name: String)
    }
}