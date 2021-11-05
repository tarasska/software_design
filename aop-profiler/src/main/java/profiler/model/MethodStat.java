package profiler.model;

import org.aspectj.lang.Signature;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.NoSuchElementException;


public class MethodStat {
    private final Signature signature;
    private final TimeInterval timeInterval;
    private final int depth;

    private Throwable failInfo;

    public MethodStat(Signature signature, TimeInterval timeInterval, int depth) {
        this.signature = signature;
        this.timeInterval = timeInterval;
        this.depth = depth;
        this.failInfo = null;
    }

    private String timeToDateString(Instant instant) {
        return new SimpleDateFormat("dd MM yyyy HH:mm:ss").format(Date.from(timeInterval.from));
    }

    public Signature getSignature() {
        return signature;
    }

    public TimeInterval getTimeInterval() {
        return timeInterval;
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
        long sec = Duration.between(timeInterval.from, timeInterval.to).toSeconds();
        return String.format(
            "%s [%s..%s: %s]",
            signature.toLongString(),
            timeToDateString(timeInterval.from),
            timeToDateString(timeInterval.to),
            String.format("%d:%02d:%02d", sec / 3600, (sec % 3600) / 60, (sec % 60))
        );
    }
}
