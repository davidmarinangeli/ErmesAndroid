package com.example.david.ermes.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.david.ermes.Model.models.Notification;
import com.example.david.ermes.R;

import java.util.List;

/**
 * Created by nicol on 10/01/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;
    private Context context;

    public NotificationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.default_notification_item,
                parent,
                false
        );

        return new NotificationAdapter.NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return notifications != null ? notifications.size() : 0;
    }

    public void refreshList(List<Notification> notifications){
        this.notifications = notifications;

        notifyDataSetChanged();
    }


    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView text;

        public NotificationViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.notification_title);
            text = itemView.findViewById(R.id.notification_text);
        }

        public void bind(int position) {
            if (notifications != null) {
                Notification n = notifications.get(position);

                title.setText(n.getTitle());
                text.setText(n.getText());
            }
        }
    }
}
