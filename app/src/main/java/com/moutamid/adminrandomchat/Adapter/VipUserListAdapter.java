package com.moutamid.adminrandomchat.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.moutamid.adminrandomchat.Model.UserModel;
import com.moutamid.adminrandomchat.R;
import com.moutamid.adminrandomchat.utils.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

//adapter is a class which we used to show list of data for example this adapter is used to show all the compaings in the project
public class VipUserListAdapter extends RecyclerView.Adapter<VipUserListAdapter.View_Holder> implements Filterable {
    private VipUserListAdapter.OnitemClickListener mListener;

    public interface OnitemClickListener {
        void OnItemClick(int position);//
        void onaddclick(int position);

    }

    public void setOnItemClick(VipUserListAdapter.OnitemClickListener listener) {
        mListener = listener;
    }

    LayoutInflater layoutInflater;
    List<UserModel> users;
    Context context;


    public VipUserListAdapter(Context ctx, List<UserModel> users) {
        this.context = ctx;
      //  this.layoutInflater = LayoutInflater.from(ctx);
        this.users = users;
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vip_user_item, parent, false);//here we define what view is our adapter showing here we are showing row_all_compaings view which you can see in res->layout
        return new View_Holder(view, mListener);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<UserModel> filterList = new ArrayList<>();

            if (charSequence.toString().isEmpty()){
                filterList.addAll(users);
            }else {
                for (UserModel data : users){
                    if (data.getName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filterList.add(data);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            users.clear();
            users.addAll((Collection<? extends UserModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {

        UserModel model = users.get(position);
        boolean isChecked = false;

        holder.title.setText(model.getName());
        if(model.getProfile_url().equals("")){

            Glide.with(context)
                    .asBitmap()
                    .load(R.drawable.profile)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.img);
        }else {

            Glide.with(context)
                    .asBitmap()
                    .load(model.profile_url)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(holder.img);
        }
        if (model.isIs_vip()){
            holder.onOff.setChecked(true);
        }else {
            holder.onOff.setChecked(false);
        }
        holder.onOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!model.isIs_vip()){
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("is_vip",true);
                    Constants.databaseReference().child(Constants.USERS)
                            .child(model.getUid()).updateChildren(hashMap);

                }else {
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("is_vip",false);
                    Constants.databaseReference().child(Constants.USERS)
                            .child(model.getUid()).updateChildren(hashMap);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView img;
        Switch onOff;

        public View_Holder(@NonNull View itemView, final VipUserListAdapter.OnitemClickListener listener) {
            super(itemView);
            //here we are initializing our components that were in the roww_all_views
            title = (TextView) itemView.findViewById(R.id.userNames);
            img=itemView.findViewById(R.id.imgProfile);
            onOff = itemView.findViewById(R.id.onOff);
        }
    }
}


