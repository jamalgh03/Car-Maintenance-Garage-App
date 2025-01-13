package com.example.carmaintenancegarageapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.util.List;

public class CarAdapter extends BaseAdapter {

    private Context context;
    private List<Car> carList;

    public CarAdapter(Context context, List<Car> carList) {
        this.context = context;
        this.carList = carList;
    }

    @Override
    public int getCount() {
        return carList.size();
    }

    @Override
    public Object getItem(int position) {
        return carList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.car_list_item, parent, false);
        }

        Car car = carList.get(position);

        TextView carNameTextView = convertView.findViewById(R.id.carName);
        TextView carModelTextView = convertView.findViewById(R.id.carModel);
        TextView carNumberTextView = convertView.findViewById(R.id.carNumber);
        TextView emailTextView = convertView.findViewById(R.id.email);

        carNameTextView.setText(car.getCarName());
        carModelTextView.setText(car.getCarModel());
        carNumberTextView.setText(car.getCarNumber());
        emailTextView.setText(car.getEmail());

        return convertView;
    }
}
