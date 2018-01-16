package com.example.david.ermes.View;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.ermes.R;

/**
 * Created by david on 1/17/2018.
 */

public class PickFriendsAdapter extends RecyclerView.Adapter {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.friend_item,
                parent,
                false
        );
        return new PickFriendsAdapter.PickFriendsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class PickFriendsViewHolder extends RecyclerView.ViewHolder {

        CheckBox invite_checkbox;
        TextView friend_name;
        ImageView friend_image;

        public PickFriendsViewHolder(View itemView) {
            super(itemView);

            friend_image = itemView.findViewById(R.id.friend_image);
            friend_name = itemView.findViewById(R.id.friend_name_invitation);
            invite_checkbox = itemView.findViewById(R.id.checkBox_friend);

        }

        public void bind(int pos){

        }

    }
}

