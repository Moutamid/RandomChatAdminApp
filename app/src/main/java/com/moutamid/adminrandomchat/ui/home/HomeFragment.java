package com.moutamid.adminrandomchat.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.adminrandomchat.Adapter.UserListAdapter;
import com.moutamid.adminrandomchat.MainActivity;
import com.moutamid.adminrandomchat.Model.UserModel;
import com.moutamid.adminrandomchat.R;
import com.moutamid.adminrandomchat.databinding.FragmentHomeBinding;
import com.moutamid.adminrandomchat.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private List<UserModel> userModelList;
    private UserListAdapter adapter;
    private Context mContext;
    private RecyclerView recyclerView;
    private EditText searchTxt;
    private TextView totalTxt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MainActivity.context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        totalTxt = (TextView) root.findViewById(R.id.total);
        searchTxt = (EditText) root.findViewById(R.id.searchTxt);
        recyclerView = (RecyclerView)root.findViewById(R.id.recyclerView);
        mContext = MainActivity.context;
        userModelList = new ArrayList<>();
        if (mContext != null) {
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(manager);

            getUserList();
        }

        searchTxt.addTextChangedListener(new TextWatcher() {
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

        return root;
    }

    private void getUserList() {

        Constants.databaseReference().child(Constants.USERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            userModelList.clear();
                            totalTxt.setText("Total Users: " + snapshot.getChildrenCount());
                            for (DataSnapshot ds : snapshot.getChildren()){
                                UserModel model = ds.getValue(UserModel.class);
                                userModelList.add(model);
                            }
                        }

                        adapter = new UserListAdapter(mContext,userModelList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
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