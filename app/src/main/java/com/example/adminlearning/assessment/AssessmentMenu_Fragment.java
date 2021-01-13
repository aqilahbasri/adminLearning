package com.example.adminlearning.assessment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.adminlearning.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssessmentMenu_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentMenu_Fragment extends Fragment implements View.OnClickListener {

    boolean completeStatus = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AssessmentMenu_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssessmentMenu_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssessmentMenu_Fragment newInstance(String param1, String param2) {
        AssessmentMenu_Fragment fragment = new AssessmentMenu_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ((MainActivity) getActivity()).setTitle("Assessment");  //set action bar title here

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_assessment_menu, container, false);

//        if user not complete assessment, set cardView colour to grey
        if (!completeStatus) {
            v.findViewById(R.id.applyCertificate).setBackgroundColor(Color.parseColor("#A9A9A9"));
//            TODO: set radius of cardview
        }

        CardView applyCertificate = v.findViewById(R.id.applyCertificate);
        applyCertificate.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {

        Intent i;

        if (!completeStatus) {
            if (v.getId() == R.id.applyCertificate) {
                openDialog();
            }
        }
        else {
            switch (v.getId()) {
                case R.id.applyCertificate : i = new Intent(getActivity(), ManageCertificationActivity.class);startActivity(i); break;
//            case R.id.manageQuestionBank : i = new Intent(getActivity(), ViewResults.class);startActivity(i); break;
//            case R.id.manageCoursework : i = new Intent(getActivity(), SubmitCoursework_Activity.class);startActivity(i); break;
//            case R.id.manageOnlineInterview : i = new Intent(getActivity(), StartAssessment2.class);startActivity(i); break;
//                case R.id.manageCertification : i = new Intent(getActivity(), ManageCertificationActivity.class);startActivity(i); break;
                default:break;
            }
        }
    }

    public void openDialog() {
        ApplyCertDialog applyCertDialog = new ApplyCertDialog();
        applyCertDialog.show(getActivity().getSupportFragmentManager(), "example dialog");
    }

    //Set action bar title
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Assessment");
    }
}