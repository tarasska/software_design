package profiler;

import org.aspectj.lang.Signature;
import profiler.model.MethodStat;
import profiler.model.TimeInterval;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class CallStackProfiler {
    public static final CallStackProfiler INSTANCE = new CallStackProfiler();

    private static String packageName;

    private Map<String, Long> callCount = new HashMap<>();
    private Map<String, Long> failCount = new HashMap<>();
    private Map<String, Duration> methodSumDuration = new HashMap<>();

    private List<MethodStat> callSequence = new ArrayList<>();

    private Deque<StackNode> callStack = new ArrayDeque<>();

    private CallStackProfiler() {
    }

    private String signatureKey(Signature signature) {
        return signature.toLongString();
    }

    public static void setPackageName(String name) {
        packageName = name;
    }

    public static String getPackageName() {
        return packageName;
    }

    public void methodCall(Signature signature) {
        Instant inTime = Instant.now();
        String key = signatureKey(signature);
        callStack.push(new StackNode(key, inTime));
        callCount.compute(key, (k, oldCnt) -> oldCnt == null ? 1 : oldCnt + 1);
    }

    public void methodExit(Signature signature) {
        Instant outTime = Instant.now();
        String key = signatureKey(signature);
        if (callStack.isEmpty() || !callStack.peek().methodKey.equals(key)) {
            throw new IllegalStateException(
                String.format("The terminating method %s is not equal to the last called.", key)
            );
        }
        StackNode node = callStack.pop();
        methodSumDuration.compute(key, (k, oldDur) -> {
            if (oldDur != null) {
                return oldDur.plus(Duration.between(node.startTime, outTime));
            } else {
                return Duration.between(node.startTime, outTime);
            }
        });
        callSequence.add(new MethodStat(
            signature,
            new TimeInterval(node.startTime, outTime),
            callStack.size())
        );
    }

    public void methodFailed(Signature signature, Throwable t) {
        String key = signatureKey(signature);
        if (callSequence.isEmpty()) {
            throw new IllegalStateException(
                String.format("Unknown method %s failed with msg: %s", key, t.getMessage()),
                t
            );
        }
        MethodStat stat = callSequence.get(callSequence.size() - 1);
        if (!key.equals(signatureKey(stat.getSignature()))) {
            throw new IllegalStateException(String.format(
                "The error information about %s does not match the last completed method %s. ",
                key,
                signatureKey(stat.getSignature())
            ));
        }
        stat.setFailInfo(t);
        failCount.compute(key, (k, oldCnt) -> oldCnt == null ? 1 : oldCnt + 1);
    }

    public String buildStatistic() {
        StringWriter writer = new StringWriter();
        printStatistic(writer);
        return writer.toString();
    }


    public void printStatistic(Writer writer) {
    }

    public String buildCallSequenceView() {
        StringWriter writer = new StringWriter();
        printCallSequenceView(writer);
        return writer.toString();
    }

    public void printCallSequenceView(Writer writer) {

    }

    static class StackNode {
        private final String methodKey;
        private final Instant startTime;

        public StackNode(String methodKey, Instant startTime) {
            this.methodKey = methodKey;
            this.startTime = startTime;
        }
    }
}
