package com.moutamid.adminrandomchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.moutamid.adminrandomchat.Adapter.VipUserListAdapter;
import com.moutamid.adminrandomchat.Model.UserModel;
import com.moutamid.adminrandomchat.databinding.ActivityVipUserBinding;
import com.moutamid.adminrandomchat.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VipUserActivity extends AppCompatActivity {

    private ActivityVipUserBinding b;
    private List<UserModel> userModelList;
    private VipUserListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityVipUserBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        b.recyclerView.setLayoutManager(manager);
        userModelList = new ArrayList<>();
        getUserList();
        b.searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0){
                    adapter.getFilter().filter(charSequence);
                }else {
                    getUserList();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    private void getUserList() {

        Constants.databaseReference().child(Constants.USERS)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            userModelList.clear();
                            b.total.setText("Total Users: " + snapshot.getChildrenCount());
                            for (DataSnapshot ds : snapshot.getChildren()){
                                UserModel model = ds.getValue(UserModel.class);
                                userModelList.add(model);
                            }
                        }

                        adapter = new VipUserListAdapter(VipUserActivity.this,userModelList);
                        b.recyclerView.setAdapter(adapter);
                       /* adapter.setOnItemClick(new VipUserListAdapter.OnitemClickListener() {
                            @Override
                            public void onaddclick(int position, View view) {
                                UserModel userModel = userModelList.get(position);
                                if (!userModel.is_vip){
                                    HashMap<String,Object> hashMap = new HashMap<>();
                                    hashMap.put("is_vip",true);
                                    Constants.databaseReference().child(Constants.USERS)
                                            .child(userModel.getUid()).updateChildren(hashMap);
                                }else {
                                    HashMap<String,Object> hashMap = new HashMap<>();
                                    hashMap.put("is_vip",false);
                                    Constants.databaseReference().child(Constants.USERS)
                                            .child(userModel.getUid()).updateChildren(hashMap);
                                }

                            }
                        });*/
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}