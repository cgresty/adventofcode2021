package dev.gresty.aoc2021;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static dev.gresty.aoc2021.Utils.withStrings;

public class Day12 {

    public static void main(String[] args) {
        withStrings(Day12::part1, "day12");
        withStrings(Day12::part2, "day12");
    }

    public static int part1(Stream<String> input) {
        return new RouteFinder(input).findRoutes();
    }

    public static long part2(Stream<String> input) {
        return 0;
    }

    static class RouteFinder {

        Cave start;
        Cave end;

        RouteFinder(Stream<String> routes) {
            Map<String, Cave> caves = new HashMap<>();
            routes.forEach(route -> {
                String[] names = route.split("-");
                Cave from = caves.computeIfAbsent(names[0], Cave::new);
                Cave to = caves.computeIfAbsent(names[1], Cave::new);
                from.addRoute(to);
                to.addRoute(from);
                if (from.name.equals("start")) start = from;
                if (to.name.equals("end")) end = to;
            });
        }

        Set<Cave> visitedSmall = new HashSet<>();
        List<Cave> route = new ArrayList<>();
        List<List<Cave>> routes = new ArrayList<>();

        int findRoutes() {
            visitedSmall.clear();
            route.clear();
            routes.clear();

            findRoutes(start);
            return routes.size();
        }

        void findRoutes(Cave cave) {
            route.add(cave);
            if (cave == end) {
                routes.add(new ArrayList<>(route));
                return;
            }
            if (cave.small) visitedSmall.add(cave);
            cave.routes()
                    .filter(next -> !visitedSmall.contains(next))
                    .forEach(this::findRoutes);
            visitedSmall.remove(cave);
        }
    }

    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    static class Cave {

        @EqualsAndHashCode.Include
        final String name;
        final boolean small;
        private final Set<Cave> routes = new HashSet<>();

        Cave(String name) {
            this.name = name;
            small = name.charAt(0) >= 'a';
        }

        void addRoute(Cave other) {
            routes.add(other);
        }

        Stream<Cave> routes() {
            return routes.stream();
        }
    }

}
