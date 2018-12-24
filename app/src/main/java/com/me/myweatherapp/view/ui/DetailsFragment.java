package com.me.myweatherapp.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.me.myweatherapp.R;
import com.me.myweatherapp.databinding.FragmentDetailsBinding;
import com.me.myweatherapp.model.Weather;
import com.me.myweatherapp.viewmodel.DetailsViewModel;

public class DetailsFragment extends Fragment {

    private DetailsViewModel detailsViewModel;
    private FragmentDetailsBinding binding;
    private Observer<Weather> weatherObserver;

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);
        binding.city.setText(getArguments().getString("query"));

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getArguments().getString("query"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        weatherObserver = new Observer<Weather>() {
            @Override
            public void onChanged(@Nullable Weather weather) {
                binding.setItem(weather);
            }
        };
        detailsViewModel.getWeather(getActivity(), getArguments().getString("query")).observe(DetailsFragment.this, weatherObserver);
    }

}
