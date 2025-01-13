package com.example.carmaintenancegarageapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<ShowNotfication> notificationList;

    public NotificationAdapter(List<ShowNotfication> notificationList, Notfication notfication) {
        this.notificationList = notificationList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShowNotfication notification = notificationList.get(position);
        holder.carNameTextView.setText("Car name: " + notification.getCarName());
        holder.carNumberTextView.setText("Car number: " + notification.getCarNumber());
        holder.statusTextView.setText("Status: " + notification.getStauts());
        holder.messageTextView.setText(notification.getMsg());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView carNameTextView, carNumberTextView, statusTextView, messageTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            carNameTextView = itemView.findViewById(R.id.carNameTextView);
            carNumberTextView = itemView.findViewById(R.id.carNumberTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }
    }
}
