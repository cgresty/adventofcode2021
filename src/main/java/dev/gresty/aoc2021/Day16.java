package dev.gresty.aoc2021;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static dev.gresty.aoc2021.Utils.withString;

public class Day16 {

    public static void main(String[] args) {
        withString(Day16::part1, "day16");
        withString(Day16::part2, "day16");
    }

    public static int part1(String input) {
        Packet root = parse(input);
        int[] count = new int[1];
        root.visit(p -> count[0] += p.version);
        return count[0];
    }

    public static long part2(String input) {
        return parse(input).value();
    }

    static Packet parse(String input) {
        Buffer buffer = new Buffer(input);
        Packet packet = new Packet();
        packet.read(buffer);
        return packet;
    }

    interface Thing {
        void read(Buffer buffer);
        default void visit(Consumer<Packet> visitor) { }
        long value();
    }

    static class Packet implements Thing {
        int version;
        int type;
        Thing body;

        @Override
        public void read(Buffer buffer) {
            version = buffer.nextInt(3);
            type = buffer.nextInt(3);

            if (type == 4) {
                body = new Literal();
            } else {
                body = new Operator(type);
            }
            body.read(buffer);
        }

        @Override
        public void visit(Consumer<Packet> visitor) {
            visitor.accept(this);
            body.visit(visitor);
        }

        @Override
        public long value() {
            return body.value();
        }
    }

    static class Operator implements Thing {

        int type;
        List<Packet> subpackets;

        Operator(int type) {
            this.type = type;
        }

        @Override
        public void read(Buffer buffer) {
            int lengthType = buffer.next();
            SubpacketFactory subpacketFactory = ((lengthType == 0 ? new SubpacketsByBitLength() : new SubpacketsByCount()));
            subpacketFactory.read(buffer);
            subpackets = subpacketFactory.readSubpackets(buffer);
        }

        @Override
        public void visit(Consumer<Packet> visitor) {
            for (Packet packet : subpackets) {
                packet.visit(visitor);
            }
        }

        @Override
        public long value() {
            return switch (type) {
                case 0 -> subpackets.stream().mapToLong(Packet::value).sum();
                case 1 -> subpackets.stream().mapToLong(Packet::value).reduce(1, (a, b) -> a * b);
                case 2 -> subpackets.stream().mapToLong(Packet::value).min().getAsLong();
                case 3 -> subpackets.stream().mapToLong(Packet::value).max().getAsLong();
                case 5 -> subpackets.get(0).value() > subpackets.get(1).value() ? 1 : 0;
                case 6 -> subpackets.get(0).value() < subpackets.get(1).value() ? 1 : 0;
                case 7 -> subpackets.get(0).value() == subpackets.get(1).value() ? 1 : 0;
                default -> throw new IllegalArgumentException();
            };
        }
    }

    static abstract class SubpacketFactory implements Thing {

        int length;

        @Override
        public void read(Buffer buffer) {
            length = buffer.nextInt(numbits());
        }

        @Override
        public long value() {
            throw new UnsupportedOperationException();
        }

        abstract int numbits();
        abstract List<Packet> readSubpackets(Buffer buffer);
    }

    static class SubpacketsByCount extends SubpacketFactory {

        @Override
        public int numbits() { return 11; }

        @Override
        List<Packet> readSubpackets(Buffer buffer) {
            List<Packet> packets = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                Packet packet = new Packet();
                packet.read(buffer);
                packets.add(packet);
            }
            return packets;
        }
    }

    static class SubpacketsByBitLength extends SubpacketFactory {

        @Override
        public int numbits() { return 15; }

        @Override
        List<Packet> readSubpackets(Buffer buffer) {
            int end = buffer.index + length;
            List<Packet> packets = new ArrayList<>();
            while (buffer.index < end) {
                Packet packet = new Packet();
                packet.read(buffer);
                packets.add(packet);
            }
            return packets;
        }
    }

    static class Literal implements Thing {
        long value;

        @Override
        public void read(Buffer buffer) {
            int more = 1;
            while (more == 1) {
                more = buffer.next();
                value = value << 4;
                value += buffer.nextInt(4);
            }
        }

        @Override
        public long value() {
            return value;
        }
    }

    static class Buffer {
        String contents;
        int length;
        int index;
        int currentCharVal;

        Buffer(String contents) {
            this.contents = contents;
            this.length = contents.length() * 4;
            this.index = 0;
        }

        int nextInt(int count) {
            return (int) next(count);
        }

        long next(int count) {
            long val = 0;
            for (int i = 0; i < count; i++) {
                val = (val << 1);
                val += next();
            }
            return val;
        }

        int next() {
            int bit = index % 4;
            if (bit == 0) {
                currentCharVal = Integer.parseInt(contents.substring(index / 4, index / 4 + 1), 16);
            }
            index++;
            int shift = 3 - bit;
            return (currentCharVal & (1 << shift)) >> shift;
        }
    }
}
