package com.example.in2000_team33.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.in2000_team33.R
import com.example.in2000_team33.db.BadestedEntity
import com.example.in2000_team33.util.LagreBadested
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NyttBadestedDialogFragment: DialogFragment() {

    private val viewModel: HjemViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialogView = requireActivity().layoutInflater.inflate(R.layout.nytt_badested, null)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Lagre", null)
            .setNegativeButton("Avbryt") { _, _ ->

            }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val posisjon = viewModel.klikkPosisjon
                val navn = dialogView.findViewById<EditText>(R.id.navn).text.toString()
                val sted = dialogView.findViewById<EditText>(R.id.sted).text.toString()

                val badested = BadestedEntity(0, navn, sted, posisjon.latitude, posisjon.longitude, false, null, null, null, null, null, null)
                LagreBadested.leggTilBadested(badested, requireContext().applicationContext).let { gyldig ->
                    if (gyldig) {
                        dialog.dismiss()
                        viewModel.oppdaterMarkorer()
                        viewModel.oppdaterBadeforhold()
                    }

                    else{
                        dialogView.findViewById<TextView>(R.id.ugyldigBadested).visibility = VISIBLE
                    }
                }
            }
        }

        return dialog
    }
}