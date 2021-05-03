package com.example.archproject;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.List;
import java.util.Locale;

/**
 *
 * TODO: Replace the implementation with code for your data type.
 */
public class MyWeightRecyclerViewAdapter extends RecyclerView.Adapter<MyWeightRecyclerViewAdapter.ViewHolder> {

    private List<WeightEntry> mValues;

    public MyWeightRecyclerViewAdapter(List<WeightEntry> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mWeightView.setText(String.format("%.2f", mValues.get(position).weight));
        holder.mDateView.setText(mValues.get(position).date);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mWeightView;
        public TextView mDateView;
        public WeightEntry mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mWeightView = (TextView) view.findViewById(R.id.weight);
            mDateView = (TextView) view.findViewById(R.id.date);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mWeightView.getText() + "'";
        }
    }
}