package com.example.david.ermes.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 1/17/2018.
 */

public class PickFriendsAdapter extends RecyclerView.Adapter<PickFriendsAdapter.PickFriendsViewHolder> {

    private List<User> friendship_list = new ArrayList<>();
    private Context context;

    public PickFriendsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PickFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.friend_item,
                parent,
                false
        );
        return new PickFriendsAdapter.PickFriendsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PickFriendsViewHolder holder, int position) {
        holder.bind(position);
    }


    public void refreshList(List<User> friends_list){
        friendship_list = friends_list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return friendship_list.size();
    }

    public class PickFriendsViewHolder extends RecyclerView.ViewHolder {

        CheckBox invite_checkbox;
        TextView friend_name;
        ImageView friend_image;

        public PickFriendsViewHolder(View itemView) {
            super(itemView);

            friend_image = itemView.findViewById(R.id.friend_image_invitation);
            friend_name = itemView.findViewById(R.id.invite_friend_name);
            invite_checkbox = itemView.findViewById(R.id.checkBox_friend);

        }

        public void bind(int pos){
            friend_name.setText(friendship_list.get(pos).getName());

        }

    }
}

