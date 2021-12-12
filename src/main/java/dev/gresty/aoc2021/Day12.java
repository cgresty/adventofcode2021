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
        return new RouteFinder(input).findRoutes(new Rule1());
    }

    public static long part2(Stream<String> input) {
        return new RouteFinder(input).findRoutes(new Rule2());
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

        List<Cave> route = new ArrayList<>();
        int routeCount;

        int findRoutes(Rule rule) {
            route.clear();
            routeCount = 0;

            findRoutes(start, rule);
            return routeCount;
        }

        void findRoutes(Cave cave, Rule rule) {
            route.add(cave);
            if (cave == end) {
                routeCount++;
                return;
            }
            rule.add(cave);
            cave.routes()
                    .filter(rule::canVisit)
                    .forEach(next -> findRoutes(next, rule));
            route.remove(route.size() - 1);
            rule.remove(cave);
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

        @Override
        public String toString() {
            return name;
        }
    }

    interface Rule {
        void add(Cave cave);
        void remove(Cave cave);
        boolean canVisit(Cave cave);
    }

    static class Rule1 implements Rule {
        Set<Cave> visitedSmall = new HashSet<>();

        @Override
        public void add(Cave cave) {
            if (cave.small)
                visitedSmall.add(cave);
        }

        @Override
        public void remove(Cave cave) {
            visitedSmall.remove(cave);
        }

        @Override
        public boolean canVisit(Cave cave) {
            return !visitedSmall.contains(cave);
        }
    }

    static class Rule2 implements Rule {
        Set<Cave> visitedSmall = new HashSet<>();
        Cave twiceVisited;

        @Override
        public void add(Cave cave) {
            if (cave.small) {
                if (visitedSmall.contains(cave)) {
                    twiceVisited = cave;
                } else {
                    visitedSmall.add(cave);
                }
            }
        }

        @Override
        public void remove(Cave cave) {
            if (twiceVisited == cave) {
                twiceVisited = null;
            } else {
                visitedSmall.remove(cave);
            }
        }

        @Override
        public boolean canVisit(Cave next) {
            return !visitedSmall.contains(next) ||
                    (!next.name.equals("start") && !next.name.equals("end") && twiceVisited == null);
        }
    }

}
