package com.fall_detection.randomchatadminapp.ui.notifications;

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
import com.fall_detection.randomchatadminapp.databinding.FragmentNotificationsBinding;
import com.fall_detection.randomchatadminapp.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private String interstitial,banner,reward;
    private DatabaseReference db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = Constants.databaseReference().child("AdmobId");

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interstitial = binding.iid.getText().toString();
                banner = binding.bid.getText().toString();
                reward = binding.rid.getText().toString();
                //if (!id.isEmpty()) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("interstitial", interstitial);
                hashMap.put("banner", banner);
                hashMap.put("reward", reward);
                    db.setValue(hashMap);
                    Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
                //}
            }
        });
        getIds();
        return root;
    }

    private void getIds() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    interstitial=snapshot.child("interstitial").getValue().toString();
                    banner=snapshot.child("banner").getValue().toString();
                    reward=snapshot.child("reward").getValue().toString();
                    binding.bid.setText(banner);
                    binding.iid.setText(interstitial);
                    binding.rid.setText(reward);
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