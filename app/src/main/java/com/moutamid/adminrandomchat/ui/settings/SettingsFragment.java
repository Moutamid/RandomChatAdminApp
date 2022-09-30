package com.moutamid.adminrandomchat.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moutamid.adminrandomchat.MainActivity;
import com.moutamid.adminrandomchat.PrivacyPolicyScreen;
import com.moutamid.adminrandomchat.TermsAndCondition;
import com.moutamid.adminrandomchat.VipUserActivity;


public class SettingsFragment extends Fragment {

    private com.moutamid.adminrandomchat.databinding.FragmentSettingsBinding binding;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = MainActivity.context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = com.moutamid.adminrandomchat.databinding.FragmentSettingsBinding.inflate(inflater, container, false);
        this.mContext = MainActivity.context;
        binding.policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, PrivacyPolicyScreen.class));
            }
        });
        binding.terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, TermsAndCondition.class));
            }
        });

        binding.vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, VipUserActivity.class));
            }
        });
        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}