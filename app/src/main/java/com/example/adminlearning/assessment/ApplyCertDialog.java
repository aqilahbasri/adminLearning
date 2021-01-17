   package com.example.adminlearning.assessment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.adminlearning.R;

   public class ApplyCertDialog extends AppCompatDialogFragment {

       public Dialog onCreateDialog(Bundle savedInstanceState) {
           AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomMaterialDialog);
           builder.setTitle("Information")
                   .setMessage("Please ensure you have passed all test sections before applying for certificate.")
                   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialogInterface, int i) {
                       }
                   });
           return builder.create();
       }
   }
