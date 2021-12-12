package dev.gresty.aoc2021;

import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
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
            });
            start = caves.get("start");
            end = caves.get("end");
        }

        int findRoutes(Rule rule) {
            return findRoutes(start, rule);
        }

        int findRoutes(Cave cave, Rule rule) {
            if (cave == end) {
                return 1;
            }
            rule.add(cave);
            int count = 0;
            Cave next;
            for (int i = 0; i < cave.numRoutes; i++) {
                next = cave.routes[i];
                if (rule.canVisit(next)) {
                    count += findRoutes(next, rule);
                }
            }
            rule.remove(cave);
            return count;
        }
    }

    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    static class Cave {

        @EqualsAndHashCode.Include
        final String name;
        final boolean small;
        Cave[] routes = new Cave[10];
        int numRoutes = 0;
        boolean visited;

        Cave(String name) {
            this.name = name;
            small = name.charAt(0) >= 'a';
        }

        void addRoute(Cave other) {
            if (other.name.equals("start")) return;
            routes[numRoutes++] = other;
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

        @Override
        public void add(Cave cave) {
            if (cave.small) cave.visited = true;
        }

        @Override
        public void remove(Cave cave) {
            cave.visited = false;
        }

        @Override
        public boolean canVisit(Cave cave) {
            return !cave.visited;
        }
    }

    static class Rule2 implements Rule {
        Cave twiceVisited;

        @Override
        public void add(Cave cave) {
            if (cave.small) {
                if (cave.visited) {
                    twiceVisited = cave;
                } else {
                    cave.visited = true;
                }
            }
        }

        @Override
        public void remove(Cave cave) {
            if (twiceVisited == cave) {
                twiceVisited = null;
            } else {
                cave.visited = false;
            }
        }

        @Override
        public boolean canVisit(Cave next) {
            return !next.visited || twiceVisited == null;
        }
    }

}
