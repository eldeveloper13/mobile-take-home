package com.eldeveloper13.airportroutefinder.repo;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Airport {

    @ColumnInfo
    public String name;
    @ColumnInfo
    public String city;
    @ColumnInfo
    public String country;
    @PrimaryKey
    @NonNull
    public String IATA3;
    @ColumnInfo
    public double lat;
    @ColumnInfo
    public double lng;

    public Airport(String name, String city, String country, @NonNull String IATA3, double lat, double lng) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.IATA3 = IATA3;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Double.compare(airport.lat, lat) == 0 &&
                Double.compare(airport.lng, lng) == 0 &&
                Objects.equals(name, airport.name) &&
                Objects.equals(city, airport.city) &&
                Objects.equals(country, airport.country) &&
                Objects.equals(IATA3, airport.IATA3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, city, country, IATA3, lat, lng);
    }

    @Override
    public String toString() {
        return "Airport{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", IATA3='" + IATA3 + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
