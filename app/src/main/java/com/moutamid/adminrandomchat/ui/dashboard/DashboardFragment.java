package com.moutamid.adminrandomchat.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.moutamid.adminrandomchat.MainActivity;
import com.moutamid.adminrandomchat.Model.VipCost;
import com.moutamid.adminrandomchat.R;
import com.moutamid.adminrandomchat.databinding.FragmentDashboardBinding;
import com.moutamid.adminrandomchat.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DatabaseReference db;
    private String month1,month3,year1;
    private Context mContext;
    private EditText month1Txt,month3Txt,yearTxt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MainActivity.context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mContext = MainActivity.context;
        month1Txt = (EditText)root.findViewById(R.id.month1);
        month3Txt = (EditText)root.findViewById(R.id.month3);
        yearTxt = (EditText)root.findViewById(R.id.year);
        db = Constants.databaseReference().child("VipCosts");
        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month1 = month1Txt.getText().toString();
                month3 = month3Txt.getText().toString();
                year1 = yearTxt.getText().toString();
                VipCost model = new VipCost(month1,month3,year1);
                db.setValue(model);
                Toast.makeText(mContext, "Updated!", Toast.LENGTH_SHORT).show();
                month1Txt.setText("");
                month3Txt.setText("");
                yearTxt.setText("");
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


        if (mContext != null){
            getCosts();
        }
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
                    month1Txt.setText(month1);
                    month3Txt.setText(month3);
                    yearTxt.setText(year1);
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