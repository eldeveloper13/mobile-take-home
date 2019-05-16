package com.eldeveloper13.airportroutefinder.repo;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"airline", "origin", "destination"})
public class Route {
    @NonNull
    public String airline;
    @NonNull
    public String origin;
    @NonNull
    public String destination;

    public Route(@NonNull String airline, @NonNull String origin, @NonNull String destination) {
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Route route = (Route) o;
//        return Objects.equals(airline, route.airline) &&
//                Objects.equals(origin, route.origin) &&
//                Objects.equals(destination, route.destination);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(airline, origin, destination);
//    }
}
