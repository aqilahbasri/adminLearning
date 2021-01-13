package com.example.adminlearning.assessment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.adminlearning.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ApprovedCertAddressDialog extends AppCompatDialogFragment {

    private String key, name;
    private static final String TAG = "ApprovedCertDialog";
    private TextView addressField, cityField, postcodeField, stateField;
    FirebaseDatabase database;
    DatabaseReference detailsRef;

    public ApprovedCertAddressDialog(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("View Address");
        builder.setMessage("View address for " + name);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_approved_cert_address, null);

        addressField = view.findViewById(R.id.address_field);
        cityField = view.findViewById(R.id.city_field);
        postcodeField = view.findViewById(R.id.postcode_field);
        stateField = view.findViewById(R.id.state_field);

        database = FirebaseDatabase.getInstance();
        detailsRef = database.getReference().child("CertApplication_StudentInfo").child("ApprovedCertification");
        detailsRef.keepSynced(true);

        detailsRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String address = snapshot.child("address").getValue().toString();
                String city = snapshot.child("city").getValue().toString();
                String postcode = snapshot.child("postcode").getValue().toString();
                String state = snapshot.child("state").getValue().toString();

                addressField.setText(address);
                cityField.setText(city);
                postcodeField.setText(postcode);
                stateField.setText(state);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });

        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        return builder.create();
    }

}