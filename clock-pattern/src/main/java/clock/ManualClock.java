package clock;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.concurrent.atomic.AtomicReference;

public class ManualClock implements Clock {

    private final AtomicReference<Instant> currentTime;

    public ManualClock(Instant initTime) {
        this.currentTime = new AtomicReference<>(initTime);
    }

    @Override
    public Instant now() {
        return currentTime.get();
    }

    public Instant add(TemporalAmount amount) {
        return currentTime.updateAndGet(t -> t.plus(amount));
    }

    public Instant sub(TemporalAmount amount) {
        return currentTime.updateAndGet(t -> t.minus(amount));
    }

    public void resetClock(Instant newInitTime) {
        currentTime.set(newInitTime);
    }
}
