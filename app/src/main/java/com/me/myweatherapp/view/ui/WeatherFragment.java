package com.me.myweatherapp.view.ui;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SearchView;

import com.me.myweatherapp.R;
import com.me.myweatherapp.databinding.FragmentWeatherBinding;
import com.me.myweatherapp.db.CityRepository;
import com.me.myweatherapp.db.entity.City;
import com.me.myweatherapp.model.Weather;
import com.me.myweatherapp.view.adapter.WeatherAdapter;
import com.me.myweatherapp.viewmodel.WeatherViewModel;

import java.util.List;

public class WeatherFragment extends Fragment implements WeatherAdapter.ItemsAdapterListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private WeatherAdapter weatherAdapter;
    private WeatherViewModel weatherViewModel;
    private Observer<List<Weather>> citiesWeatherObserver;
    private Observer<Weather> cityWeatherObserver;
    private Observer<List<City>> citiesObserver;
    private CityRepository repository;
    private FragmentWeatherBinding binding;
    private FloatingActionButton fabAdd;
    private List<Weather> mWeather;

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false);
        fabAdd = binding.fabAdd;
        fabAdd.setOnClickListener(this);


        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Weather App");
        inflater.inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Enter a city name...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                weatherAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                weatherAdapter.getFilter().filter(query);
                return true;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        repository = new CityRepository(getActivity());

        citiesWeatherObserver = new Observer<List<Weather>>() {
            @Override
            public void onChanged(@Nullable final List<Weather> weatherData) {
                if (binding.swiperefresh.isRefreshing())
                    binding.swiperefresh.setRefreshing(false);
                if (mWeather == null) {
                    mWeather = weatherData;
                    initRecyclerView(weatherData);
                }
            }
        };

        cityWeatherObserver = new Observer<Weather>() {
            @Override
            public void onChanged(@Nullable final Weather weather) {
                repository.getCity(weather.getCity()).observe(WeatherFragment.this, new Observer<City>() {
                    @Override
                    public void onChanged(@Nullable City city) {
                        if (!binding.swiperefresh.isRefreshing())
                            binding.swiperefresh.setRefreshing(true);
                        if (city == null) {
                            repository.insertCity(weather.getCity());
                            mWeather.add(weather);
                            weatherAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        };

        citiesObserver = new Observer<List<City>>() {
            @Override
            public void onChanged(@Nullable List<City> cities) {
                if (!binding.swiperefresh.isRefreshing())
                    binding.swiperefresh.setRefreshing(true);
                weatherViewModel.getAllCitiesWeather(getActivity(), cities).observe(WeatherFragment.this, citiesWeatherObserver);
            }
        };


        repository.getCities().observe(this, citiesObserver);
        binding.swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        repository.getCities().observe(WeatherFragment.this, citiesObserver);
                    }
                }
        );

    }

    private void initRecyclerView(List<Weather> weatherData) {
        recyclerView = binding.rv;
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        weatherAdapter = new WeatherAdapter(getActivity(), weatherData, this);
        recyclerView.setAdapter(weatherAdapter);
    }

    @Override
    public void onItemClicked(final Weather item) {
        mWeather = null;
        DetailsFragment detailsFragment = DetailsFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("query", item.getCity());
        detailsFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.anim_slide_in_left, R.animator.anim_slide_out_right,
                        R.animator.anim_slide_out_left, R.animator.anim_slide_in_right)
                .replace(R.id.container, detailsFragment).addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View view) {
        if (view == fabAdd) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add a city to the list");
            builder.setCancelable(false);
            final EditText input = new EditText(getActivity());
            input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    weatherViewModel.getCityWeather(getActivity(), input.getText().toString()).observe(WeatherFragment.this, cityWeatherObserver);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            dialog.setView(input);
            dialog.show();
        }
    }
}
