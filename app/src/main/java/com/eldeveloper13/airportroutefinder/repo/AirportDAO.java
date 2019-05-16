package com.eldeveloper13.airportroutefinder.repo;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface AirportDAO {

    @Query("SELECT * FROM Airport")
    List<Airport> getAll();

    @Query("SELECT * FROM Airport WHERE IATA3 LIKE :IATA3 LIMIT 1")
    Airport findAirportByIATA3(String IATA3);

    @Query("SELECT * FROM Airport WHERE IATA3 IN (:IATA3)")
    List<Airport> findAirportsBYIATA3(List<String> IATA3);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Airport... airports);
}
