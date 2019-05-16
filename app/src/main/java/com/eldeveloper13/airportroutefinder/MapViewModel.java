package com.eldeveloper13.airportroutefinder;

import android.app.Application;
import android.text.TextUtils;

import com.eldeveloper13.airportroutefinder.repo.Airport;
import com.eldeveloper13.airportroutefinder.repo.AppDatabase;
import com.eldeveloper13.airportroutefinder.repo.Route;
import com.eldeveloper13.airportroutefinder.util.CsvReader;
import com.eldeveloper13.airportroutefinder.util.RouteGraph;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MapViewModel extends AndroidViewModel {

    MutableLiveData<List<Airport>> pathLiveData = new MutableLiveData<>();
    MutableLiveData<SearchError> searchErrorLiveData = new MutableLiveData<>();
    private RouteGraph routeGraph = new RouteGraph();
    private AppDatabase database;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public MapViewModel(@NonNull Application application) {
        super(application);

        CsvReader csvReader = new CsvReader();

        database = AppDatabase.getDatabaseInstance(application);
        
        executorService.execute(() -> {
            try {
                List<Route> routes = csvReader.loadRouteCSV(getApplication());
                routeGraph.addRoutes(routes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }

    void searchPath(String origin, String destination) {
        pathLiveData.setValue(null);
        searchErrorLiveData.setValue(null);
        executorService.execute(() -> {
            SearchError error = validateSearchInput(origin, destination);
            if (error != null) {
                searchErrorLiveData.postValue(error);
                return;
            }

            List<String> path = routeGraph.findShortestPath(origin, destination);
            if (path.size() == 0) {
                searchErrorLiveData.postValue(new SearchError(getApplication().getString(R.string.search_error_no_path_found)));
            } else {
                List<Airport> airports = database.airportDAO().findAirportsBYIATA3(path);
                if (airports.size() != path.size()) {
                    searchErrorLiveData.postValue(new SearchError(getApplication().getString(R.string.search_error_no_airport_info)));
                    return;
                }

                // Cannot use DAO result as-is because it doesn't respect input order
                List<Airport> airportPath = path.stream()
                        .map(p -> airports.stream().filter(a -> a.IATA3.equalsIgnoreCase(p)).findFirst().get())
                        .collect(Collectors.toList());
                pathLiveData.postValue(airportPath);
            }
        });
    }

    void dismissSearchError() {
        searchErrorLiveData.setValue(null);
    }

    private SearchError validateSearchInput(String origin, String destination) {
        if (TextUtils.isEmpty(origin.trim())) {
            return new SearchError(getApplication().getString(R.string.search_error_empty_origin));
        } else if (TextUtils.isEmpty(destination.trim())) {
            return new SearchError(getApplication().getString(R.string.search_error_empty_destination));
        } else if (origin.equalsIgnoreCase(destination)) {
            return new SearchError(getApplication().getString(R.string.search_error_origin_destination_same));
        } else if (database.airportDAO().findAirportByIATA3(origin) == null) {
            return new SearchError(getApplication().getString(R.string.search_error_cannot_find_origin));
        } else if (database.airportDAO().findAirportByIATA3(destination) == null) {
            return new SearchError(getApplication().getString(R.string.search_error_cannot_find_destination));
        } else {
            return null;
        }
    }

}
