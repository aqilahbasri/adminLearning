package com.example.adminlearning.assessment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.adminlearning.R;

public class AdministrationMenuFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdministrationMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdministrationMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdministrationMenuFragment newInstance(String param1, String param2) {
        AdministrationMenuFragment fragment = new AdministrationMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_administration_menu, container, false);
        CardView questionBank = v.findViewById(R.id.manageQuestionBank);
        CardView manageAssessment = v.findViewById(R.id.manageSettings);
        CardView manageCertification = v.findViewById(R.id.manageCertification);
        CardView manageCoursework = v.findViewById(R.id.manageCoursework);
        CardView manageOnlineInterview = v.findViewById(R.id.manageOnlineInterview);

        manageAssessment.setOnClickListener(this);
        questionBank.setOnClickListener(this);
        manageCoursework.setOnClickListener(this);
        manageOnlineInterview.setOnClickListener(this);
        manageCertification.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.manageSettings:
                i = new Intent(getActivity(), ManageTestSettingsActivity.class);
                startActivity(i);
                break;
            case R.id.manageQuestionBank:
                i = new Intent(getActivity(), ManageQuestionsActivity.class);
                startActivity(i);
                break;
            case R.id.manageCoursework:
                i = new Intent(getActivity(), ManageCourseworkActivity.class);
                startActivity(i);
                break;
            case R.id.manageOnlineInterview:
                i = new Intent(getActivity(), ManageOnlineInterviewActivity.class);
                startActivity(i);
                break;
            case R.id.manageCertification:
                i = new Intent(getActivity(), ManageCertificationActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Assessment");
    }
}