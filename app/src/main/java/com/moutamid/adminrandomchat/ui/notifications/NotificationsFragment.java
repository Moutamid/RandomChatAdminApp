package com.moutamid.adminrandomchat.ui.notifications;

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

import com.moutamid.adminrandomchat.MainActivity;
import com.moutamid.adminrandomchat.R;
import com.moutamid.adminrandomchat.databinding.FragmentNotificationsBinding;
import com.moutamid.adminrandomchat.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private String interstitial,banner,reward;
    private DatabaseReference db;
    private Context mContext;
    private EditText bannerTxt,inTxt,rewardTxt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MainActivity.context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mContext = MainActivity.context;
        bannerTxt = (EditText)root.findViewById(R.id.bannerid);
        inTxt = (EditText)root.findViewById(R.id.iid);
        rewardTxt = (EditText)root.findViewById(R.id.rid);
        db = Constants.databaseReference().child("AdmobId");

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interstitial = inTxt.getText().toString();
                banner = bannerTxt.getText().toString();
                reward = rewardTxt.getText().toString();
                //if (!id.isEmpty()) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("interstitial", interstitial);
                    hashMap.put("banner", banner);
                    hashMap.put("reward", reward);
                    db.setValue(hashMap);
                    Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
                    bannerTxt.setText("");
                    inTxt.setText("");
                    rewardTxt.setText("");
                //}
            }
        });
        if (mContext != null){
            getIds();
        }

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
                    bannerTxt.setText(banner);
                    inTxt.setText(interstitial);
                    rewardTxt.setText(reward);
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