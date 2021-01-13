package com.example.adminlearning.assessment;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class QuestionTypeSpinner implements AdapterView.OnItemSelectedListener {

    private Context context;
    private Spinner questionType;

    QuestionTypeSpinner(Context context, Spinner questionType) {
        this.context = context;
        this.questionType = questionType;
    }

    void spinnerActivity() {

        List<String> questionTypeList = new ArrayList<>();
        questionTypeList.add("Short answer");
        questionTypeList.add("MCQ");
        questionTypeList.add("Paragraph");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, questionTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionType.setAdapter(adapter);
        questionType.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(context, "Error retrieving questionTypes", Toast.LENGTH_SHORT).show();
    }

    String getItem() {
        return questionType.getSelectedItem().toString();
    }
}