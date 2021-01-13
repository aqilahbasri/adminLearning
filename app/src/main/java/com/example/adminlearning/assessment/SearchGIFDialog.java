package com.example.adminlearning.assessment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminlearning.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchGIFDialog extends AppCompatDialogFragment {

    private static final String TAG = "SearchGIFDialog";
    private RecyclerView myGIFList, myGIFRecommendationList;
    private SearchView searchView;
    private TextView noResult;
    private static SearchGIFDialog uniqueInstance = new SearchGIFDialog();

    private DatabaseReference GIFRef;
    private FirebaseAuth mAuth;

    private Context fragContext;

    ArrayList<GIF> list;

    private SearchGIFDialog() {
    }

    public static SearchGIFDialog getInstance() {
        return uniqueInstance;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomMaterialDialog);
        builder.setTitle("Search GIF");

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View GIFView = inflater.inflate(R.layout.dialog_search_gif, null);

        myGIFList = GIFView.findViewById(R.id.gif_list);
//        myGIFRecommendationList = GIFView.findViewById(R.id.recommendation_list);
        searchView = GIFView.findViewById(R.id.search_bar);
        noResult = GIFView.findViewById(R.id.no_result);

        mAuth = FirebaseAuth.getInstance();

        GIFRef = FirebaseDatabase.getInstance().getReference().child("SignLanguageGIF");
        builder.setView(GIFView);

        init();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final GIFAdapter adapter = GIFAdapter.getInstance();
                if (!adapter.getGifUrl().equals(null)) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (getFragContext() == AddNewQuestionsFragment.getInstance().getContext()) {
                                final AddNewQuestionsFragment context = AddNewQuestionsFragment.getInstance();
                                context.selectedGifTxt.setText("You have selected GIF of \"" + adapter.getMalayCaption() + "/" + adapter.getEngCaption() + "\"");
                                context.selectedGifTxt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                context.gifView.setVisibility(View.VISIBLE);
                                context.gifPicture.loadUrl(adapter.gifUrl);
                                context.gifPicture.getSettings().setLoadWithOverviewMode(true);
                                context.gifPicture.getSettings().setUseWideViewPort(true);
                                context.engCaption.setText(adapter.getEngCaption());
                                context.malayCaption.setText(adapter.getMalayCaption());

                            } else if (getFragContext() == EditLevel1QuestionFragment.getInstance().getContext()) {
                                final EditLevel1QuestionFragment context = EditLevel1QuestionFragment.getInstance();
                                context.selectedGifTxt.setText("You have selected GIF of \"" + adapter.getMalayCaption() + "/" + adapter.getEngCaption() + "\"");
                                context.selectedGifTxt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                context.gifPicture.loadUrl(adapter.gifUrl);
                                context.gifPicture.getSettings().setLoadWithOverviewMode(true);
                                context.gifPicture.getSettings().setUseWideViewPort(true);
                                context.engCaption.setText(adapter.getEngCaption());
                                context.malayCaption.setText(adapter.getMalayCaption());
                            }
                        }
                    });
                } else dialog.dismiss();
                //TODO: Exception if null
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        return builder.create();
    }

    public void init() {

        if (GIFRef != null) {
            GIFRef.orderByChild("malayCaption").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        list = new ArrayList<>();

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            list.add(ds.getValue(GIF.class));
                        }

                        GIFAdapter gifAdapter = GIFAdapter.getInstance();
                        gifAdapter.setContext(getActivity());
                        gifAdapter.setGifList(list);
                        myGIFList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                        myGIFList.setAdapter(gifAdapter);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }

    private void search(String s) {
        ArrayList<GIF> myList = new ArrayList<>();
        ArrayList<GIF> wordList = new ArrayList<>();
        ArrayList<GIF> categoryList = new ArrayList<>();
//        boolean resultExist = false;

        myGIFList.setVisibility(View.VISIBLE);
//        myGIFRecommendationList.setVisibility(View.INVISIBLE);
        noResult.setVisibility(View.INVISIBLE);

        for (GIF gif : list) {
            if (gif.getEngCaption().toLowerCase().contains(s.toLowerCase()) || gif.getMalayCaption().toLowerCase().contains(s.toLowerCase())) {
                myList.add(gif);
//                resultExist = true;
            }

        }

        if (myList.isEmpty()) {
            myGIFList.setVisibility(View.INVISIBLE);
            myGIFRecommendationList.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.VISIBLE);

            String[] sw = s.split(" "); //search word
            ArrayList<String> sc = new ArrayList<>(); //search category

            //recommendation by word
            for (String searchWord : sw) {
                if (searchWord != "I" || searchWord != "i") {
                    for (GIF gif : list) {
                        if (gif.getEngCaption().toLowerCase().contains(searchWord.toLowerCase()) || gif.getMalayCaption().toLowerCase().contains(searchWord.toLowerCase())) {
                            wordList.add(gif);
                            if (sc.contains(gif.getCategory())) {

                            } else {
                                sc.add(gif.getCategory());
                            }
                        }
                    }
                }
            }

            //recommendation by category
            for (String searchCategory : sc) {
                for (GIF gif : list) {
                    if (gif.getCategory().contains(searchCategory)) {
                        categoryList.add(gif);
                    }
                }
            }

        } else {
//            GIFAdapter gifAdapter = new GIFAdapter(getContext(), myList);
            GIFAdapter gifAdapter = GIFAdapter.getInstance();
            gifAdapter.setContext(getActivity());
            gifAdapter.setGifList(myList);
            myGIFList.setAdapter(gifAdapter);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public Context getFragContext() {
        return fragContext;
    }

    public void setFragContext(Context fragContext) {
        this.fragContext = fragContext;
    }

}