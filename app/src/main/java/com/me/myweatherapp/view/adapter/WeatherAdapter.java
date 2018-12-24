package com.me.myweatherapp.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.daimajia.swipe.SwipeLayout;
import com.me.myweatherapp.R;
import com.me.myweatherapp.databinding.LayoutItemsBinding;
import com.me.myweatherapp.db.CityRepository;
import com.me.myweatherapp.model.Weather;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> implements Filterable {

    private List<Weather> weatherList;
    private List<Weather> weatherListFiltered;
    private LayoutInflater layoutInflater;
    private ItemsAdapterListener listener;
    private CityRepository repository;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    weatherListFiltered = weatherList;
                } else {
                    List<Weather> filteredList = new ArrayList();
                    for (Weather row : weatherList) {
                        if (row.getCity().toLowerCase().startsWith(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    weatherListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = weatherListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                weatherListFiltered = (List<Weather>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final LayoutItemsBinding binding;

        public MyViewHolder(LayoutItemsBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    public WeatherAdapter(Context context, List<Weather> weatherList, ItemsAdapterListener listener) {
        this.weatherList = weatherList;
        this.weatherListFiltered = weatherList;
        this.listener = listener;
        this.repository = new CityRepository(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutItemsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_items, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.binding.setItem(weatherListFiltered.get(position));
        holder.binding.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClicked(weatherListFiltered.get(position));
                }
            }
        });
        holder.binding.swipelayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.binding.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(position);
                repository.deleteCity(holder.binding.city.getText().toString());
            }
        });
    }


    @Override
    public int getItemCount() {
        return weatherListFiltered.size();
    }

    public void remove(int position) {
        weatherListFiltered.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, weatherListFiltered.size());
    }


    public interface ItemsAdapterListener {
        void onItemClicked(Weather item);
    }

}
