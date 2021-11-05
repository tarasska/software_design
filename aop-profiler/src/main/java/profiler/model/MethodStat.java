package profiler.model;

import org.aspectj.lang.Signature;

import java.time.Duration;
import java.util.NoSuchElementException;


public class MethodStat {
    private final Signature signature;
    private final int depth;

    private TimeInterval timeInterval;
    private Throwable failInfo;

    public MethodStat(Signature signature, int depth) {
        this.signature = signature;
        this.depth = depth;
        this.failInfo = null;
    }

    public static String nsToString(Long ns) {
        return String.format("%d:%d", ns / 1_000_000_000, ns % 1_000_000_000);
    }

    public Signature getSignature() {
        return signature;
    }

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

    public int getDepth() {
        return depth;
    }

    public Throwable getFailInfo() {
        if (failInfo == null) {
            throw new NoSuchElementException("Method finished successful. No error info is present.");
        }
        return failInfo;
    }

    public void setFailInfo(Throwable t) {
        failInfo = t;
    }

    public boolean isFailed() {
        return failInfo != null;
    }

    public String getMainInfo() {
        long ns = Duration.between(timeInterval.from, timeInterval.to).toNanos();
        return String.format(
            "%s [%s..%s: %s]",
            signature.toLongString(),
            timeInterval.from.toString(),
            timeInterval.to.toString(),
            nsToString(ns)
        );
    }
}
