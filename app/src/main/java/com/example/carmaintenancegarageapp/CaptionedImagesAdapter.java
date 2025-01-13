package com.example.carmaintenancegarageapp;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

public class CaptionedImagesAdapter extends RecyclerView.Adapter<CaptionedImagesAdapter.ViewHolder> {

    private List<ServiceCar> serviceCars;

    public CaptionedImagesAdapter(List<ServiceCar> serviceCars) {
        this.serviceCars = serviceCars;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_captioned_images_adapter,
                parent,
                false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = cardView.findViewById(R.id.image);
        TextView txt = cardView.findViewById(R.id.txtName);

        ServiceCar serviceCar = serviceCars.get(position);
        txt.setText(serviceCar.getName());

        Glide.with(cardView.getContext())
                .load(serviceCar.getImageUrl())
                .into(imageView);

        cardView.setOnClickListener(v -> {
        });
    }

    @Override
    public int getItemCount() {
        return serviceCars.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}
