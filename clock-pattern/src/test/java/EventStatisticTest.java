import clock.ManualClock;
import clock.RealClock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stat.EventStatisticImpl;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventStatisticTest {
    private static final Double RMP_EPS = 1e-6;
    private static final Double MIN_PER_HOUR = 60.0;

    private Optional<String> compareStatistic(
        Map<String, Double> expected,
        Map<String, Double> actual
    ) {
        List<String> errors = new ArrayList<>();
        for (Map.Entry<String, Double> ee : expected.entrySet()) {
            Double rpm = actual.get(ee.getKey());
            if (rpm == null) {
                errors.add(String.format(
                    "Name=%s; expected rmp=%f, but actual not found",
                    ee.getKey(),
                    ee.getValue()
                ));
            } else if (Math.abs(rpm - ee.getValue()) > RMP_EPS) {
                errors.add(String.format(
                    "Name=%s; expected rmp=%f, but found rpm=%f",
                    ee.getKey(),
                    ee.getValue(),
                    rpm
                ));
            }
        }
        if (errors.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(String.join("\n", errors));
        }
    }

    private void assertStatEq(
        Map<String, Double> actual,
        Map<String, Double> expected
    ) {
        compareStatistic(expected, actual).ifPresent(Assertions::fail);
    }

    @Test
    public void test_allStat() {
        EventStatisticImpl stat = new EventStatisticImpl(new RealClock());

        stat.incEvent("first");
        stat.incEvent("second");
        stat.incEvent("first");
        stat.incEvent("first");

        assertStatEq(stat.getAllEventStatistic(), Map.of(
            "first", 3.0 / MIN_PER_HOUR,
            "second", 1.0 / MIN_PER_HOUR
        ));
    }

    @Test
    public void test_statByName() {
        EventStatisticImpl stat = new EventStatisticImpl(new RealClock());

        stat.incEvent("abc");
        stat.incEvent("a");
        stat.incEvent("abc");

        assertEquals(2.0 / MIN_PER_HOUR, stat.getEventStatisticByName("abc"));
        assertEquals(1.0 / MIN_PER_HOUR, stat.getEventStatisticByName("a"));
        assertEquals(0, stat.getEventStatisticByName("notFound"));
    }

    @Test void test_fullDelete() {
        ManualClock clock = new ManualClock(Instant.now());
        EventStatisticImpl stat = new EventStatisticImpl(clock);

        stat.incEvent("old");
        stat.incEvent("old");

        clock.add(Duration.ofMinutes(61));

        stat.incEvent("new");
        stat.incEvent("new");

        assertEquals(0, stat.getEventStatisticByName("old"));
        assertEquals(2.0 / MIN_PER_HOUR, stat.getEventStatisticByName("new"));
        assertStatEq(stat.getAllEventStatistic(), Map.of(
            "new", 2.0 / MIN_PER_HOUR
        ));
    }

    @Test void test_partialDelete() {
        ManualClock clock = new ManualClock(Instant.now());
        EventStatisticImpl stat = new EventStatisticImpl(clock);

        stat.incEvent("f");

        clock.add(Duration.ofMinutes(61));

        stat.incEvent("f");
        stat.incEvent("n");

        assertStatEq(stat.getAllEventStatistic(), Map.of(
            "f", 1.0 / MIN_PER_HOUR,
            "n", 1.0 / MIN_PER_HOUR
        ));
    }

    @Test void test_complexTime() {
        ManualClock clock = new ManualClock(Instant.now());
        EventStatisticImpl stat = new EventStatisticImpl(clock);

        stat.incEvent("a1");
        stat.incEvent("a2");

        clock.add(Duration.ofMinutes(15));

        assertStatEq(stat.getAllEventStatistic(), Map.of(
            "a1", 1.0 / MIN_PER_HOUR,
            "a2", 1.0 / MIN_PER_HOUR
        ));

        stat.incEvent("b");
        stat.incEvent("b");

        clock.add(Duration.ofMinutes(45));

        assertStatEq(stat.getAllEventStatistic(), Map.of(
            "a1", 1.0 / MIN_PER_HOUR,
            "a2", 1.0 / MIN_PER_HOUR,
            "b", 2.0 / MIN_PER_HOUR
        ));

        clock.add(Duration.ofMinutes(1));

        assertStatEq(stat.getAllEventStatistic(), Map.of(
            "b", 2.0 / MIN_PER_HOUR
        ));

        clock.add(Duration.ofMinutes(1000));

        for (int i = 0; i < 10; i++) {
            stat.incEvent("b");
        }

        assertStatEq(stat.getAllEventStatistic(), Map.of(
            "b", 10.0 / MIN_PER_HOUR
        ));
    }
}
