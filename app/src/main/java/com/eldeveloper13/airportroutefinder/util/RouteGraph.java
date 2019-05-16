package com.eldeveloper13.airportroutefinder.util;

import com.eldeveloper13.airportroutefinder.repo.Route;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RouteGraph {

    private Map<String, List<String>> adjacency = new HashMap<>();

    public void addRoutes(List<Route> routes) {
        for (Route r : routes) {
            if (!adjacency.containsKey(r.origin)) {
                adjacency.put(r.origin, new ArrayList<>());
            }
            List<String> destinations = adjacency.get(r.origin);
            if (!destinations.contains(r.destination)) {
                destinations.add(r.destination);
            }
        }
    }

    public List<String> findShortestPath(String origin, String destination) {
        LinkedList<String> queue = new LinkedList<>();
        LinkedList<String> visited = new LinkedList<>();
        Map<String, String> parent = new HashMap<>();
        origin = origin.toUpperCase();
        destination = destination.toUpperCase();
        queue.add(origin);

        while (!queue.isEmpty()) {
            String node = queue.pop();
            if (visited.contains(node)) {
                continue;
            }

            List<String> adjacentNodes = adjacency.get(node);

            if (adjacentNodes == null) {
                visited.add(node);
                continue;
            }
            for (String adjNode : adjacentNodes) {
                parent.putIfAbsent(adjNode, node);
            }

            if (adjacentNodes.contains(destination)) {
                Deque<String> path = new LinkedList<>();
                String p = destination;
                path.addFirst(p);
                while (!p.equalsIgnoreCase(origin) && parent.get(p) != null) {
                    path.addFirst(parent.get(p));
                    p = parent.get(p);
                }
                return new ArrayList<>(path);
            } else {
                queue.addAll(adjacentNodes);
                visited.add(node);
            }
        }
        return new ArrayList<>();
    }
}
