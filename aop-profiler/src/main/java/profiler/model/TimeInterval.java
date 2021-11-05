package profiler.model;

import java.time.Instant;

public class TimeInterval {
    Instant from;
    Instant to;

    public TimeInterval(Instant from, Instant to) {
        this.from = from;
        this.to = to;
    }
}
