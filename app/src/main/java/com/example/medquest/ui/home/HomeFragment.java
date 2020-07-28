package com.example.medquest.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medquest.CasesAdapter;
import com.example.medquest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private static final String TAG = "HomeLog";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);


        final TextView textView = root.findViewById(R.id.text_home);

//        final String[] myDataset;
//        ArrayList<String> myDataset;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<String> myDataset = new ArrayList<>();
        db.collection("clinicalCases")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> clinicalCase = document.getData();
//                                Log.d(TAG, (String) clinicalCase.get("description"));
//                                textView.setText((String) clinicalCase.get("title"));
//                                Log.d(TAG, document.getId() + " => " + document.getData());

                                myDataset.add((String) clinicalCase.get("title"));
                            }
                            recyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);

                            // use this setting to improve performance if you know that changes
                            // in content do not change the layout size of the RecyclerView
                            recyclerView.setHasFixedSize(true);

                            // use a linear layout manager
                            layoutManager = new LinearLayoutManager(root.getContext());
                            recyclerView.setLayoutManager(layoutManager);


                            // specify an adapter (see also next example)
                            mAdapter = new CasesAdapter(myDataset);
                            Log.d(TAG, myDataset.toString());
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return root;
    }
}