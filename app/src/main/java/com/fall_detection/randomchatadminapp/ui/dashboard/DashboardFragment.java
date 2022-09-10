package com.fall_detection.randomchatadminapp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fall_detection.randomchatadminapp.Model.VipCost;
import com.fall_detection.randomchatadminapp.databinding.FragmentDashboardBinding;
import com.fall_detection.randomchatadminapp.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DatabaseReference db;
    private String month1,month3,year1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = Constants.databaseReference().child("VipCosts");
        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month1 = binding.month1.getText().toString();
                month3 = binding.month3.getText().toString();
                year1 = binding.year.getText().toString();
                VipCost model = new VipCost(month1,month3,year1);
                db.setValue(model);
                Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
                /*if (!month1.isEmpty()){
                    String key = db.push().getKey();
                    VipCost model = new VipCost(key,"1 Month Subscription",month1);
                    db.child(key).setValue(model);
                }
                if (!month3.isEmpty()){
                    String key = db.push().getKey();
                    VipCost model = new VipCost(key,"3 Month Subscription",month3);
                    db.child(key).setValue(model);
                }
                if (!year1.isEmpty()){
                    String key = db.push().getKey();
                    VipCost model = new VipCost(key,"1 Year Subscription",year1);
                    db.child(key).setValue(model);
                }*/
            }
        });

        getCosts();
        return root;
    }

    private void getCosts() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    VipCost cost = snapshot.getValue(VipCost.class);
                    month1 = cost.getMonth1();
                    month3 = cost.getMonth3();
                    year1 = cost.getYear();
                    binding.month1.setText(month1);
                    binding.month3.setText(month3);
                    binding.year.setText(year1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}