package com.eldeveloper13.airportroutefinder.util;

import android.content.Context;
import android.text.TextUtils;

import com.eldeveloper13.airportroutefinder.R;
import com.eldeveloper13.airportroutefinder.repo.Airport;
import com.eldeveloper13.airportroutefinder.repo.Route;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    private static final int ROUTE_AIRLINE_INDEX = 0;
    private static final int ROUTE_ORIGIN_INDEX = 1;
    private static final int ROUTE_DESTINATION_INDEX = 2;

    private static final int AIRPORT_NAME_INDEX = 0;
    private static final int AIRPORT_CITY_INDEX = 1;
    private static final int AIRPORT_COUNTRY_INDEX = 2;
    private static final int AIRPORT_IATA_INDEX = 3;
    private static final int AIRPORT_LATITUDE_INDEX = 4;
    private static final int AIRPORT_LONGITUDE_INDEX = 5;

    private static final String NULL_IATA_CHARACTER = "N";

    public List<Route> loadRouteCSV(Context context) throws IOException {
        InputStream is = context.getResources().openRawResource(R.raw.routes);
        InputStreamReader reader = new InputStreamReader(is);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader().withTrim());

        List<Route> routes = new ArrayList<>();
        for (CSVRecord record : parser) {
            routes.add(new Route(record.get(ROUTE_AIRLINE_INDEX),
                    record.get(ROUTE_ORIGIN_INDEX), record.get(ROUTE_DESTINATION_INDEX)));
        }

        return routes;
    }

    public List<Airport> loadAirportCSV(Context context) throws IOException {
        InputStream is = context.getResources().openRawResource(R.raw.airports);
        InputStreamReader reader = new InputStreamReader(is);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader().withTrim());

        List<Airport> airports = new ArrayList<>();
        for (CSVRecord record : parser) {
            if (TextUtils.isEmpty(record.get(AIRPORT_IATA_INDEX)) || record.get(AIRPORT_IATA_INDEX).equalsIgnoreCase(NULL_IATA_CHARACTER)) continue;
            try {
                airports.add(new Airport(record.get(AIRPORT_NAME_INDEX),
                        record.get(AIRPORT_CITY_INDEX),
                        record.get(AIRPORT_COUNTRY_INDEX),
                        record.get(AIRPORT_IATA_INDEX),
                        Double.parseDouble(record.get(AIRPORT_LATITUDE_INDEX)),
                        Double.parseDouble(record.get(AIRPORT_LONGITUDE_INDEX))));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        return airports;
    }
}
