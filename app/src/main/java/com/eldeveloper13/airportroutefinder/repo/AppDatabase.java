package com.eldeveloper13.airportroutefinder.repo;

import android.content.Context;

import com.eldeveloper13.airportroutefinder.util.CsvReader;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Airport.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AirportDAO airportDAO();

    private static final String DATABASE_NAME = "airport.db";

    private static AppDatabase instance;

    public static AppDatabase getDatabaseInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(() -> {
                                try {
                                    CsvReader csvReader = new CsvReader();

                                    List<Airport> airports = csvReader.loadAirportCSV(context);
                                    instance.airportDAO().insertAll(airports.toArray(new Airport[airports.size()]));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    })
                    .build();

            // Create a fake query to trigger onCreate callback.  Don't know why callback is not being called normally
            // TODO: fix database init workaround
            Executors.newSingleThreadExecutor().execute(() -> instance.airportDAO().findAirportByIATA3(""));
        }
        return instance;
    }
}
