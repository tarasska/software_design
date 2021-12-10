package stat;

import clock.Clock;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EventStatisticImpl implements EventStatistic<Double> {
    private final static Double MIN_PER_HOUR = (double) Duration.ofHours(1).toMinutes();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Queue<Event> eventsQueue = new ArrayDeque<>();
    private final Map<String, Integer> eventCnt = new HashMap<>();

    private final Clock clock;

    public EventStatisticImpl(Clock clock) {
        this.clock = clock;
    }

    private <T> T read(Supplier<T> action) {
        lock.readLock().lock();
        try {
            return action.get();
        } finally {
            lock.readLock().unlock();
        }
    }

    private void write(Runnable action) {
        lock.writeLock().lock();
        try {
            action.run();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void deleteOldEvents(Instant time) {
        Instant delTime = time.minus(Duration.ofHours(1));
        write(() -> {
           while (!eventsQueue.isEmpty() && eventsQueue.peek().time.isBefore(delTime)) {
               Event event = eventsQueue.poll();
               eventCnt.computeIfPresent(event.name, (key, oldVal) -> Math.max(oldVal - 1, 0));
           };
        });
    }

    @Override
    public void incEvent(String name) {
        write(() -> {
            eventsQueue.add(new Event(name, clock.now()));
            eventCnt.compute(name, (key, oldValue) -> oldValue == null ? 1 : oldValue + 1);
        });
    }

    @Override
    public Double getEventStatisticByName(String name) {
        deleteOldEvents(clock.now());
        return read(() -> eventCnt.getOrDefault(name, 0) / MIN_PER_HOUR);
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        deleteOldEvents(clock.now());
        return read(() -> eventCnt.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() / MIN_PER_HOUR)));
    }

    @Override
    public void printStatistic() {
        System.out.println("Events statistics:");
        for (Map.Entry<String, Double> stat : getAllEventStatistic().entrySet()) {
            System.out.println(String.format("Name: %s, RPM: %f", stat.getKey(), stat.getValue()));
        }
    }

    private static class Event {
        private final String name;
        private final Instant time;

        private Event(String name, Instant time) {
            this.name = name;
            this.time = time;
        }
    }
}
