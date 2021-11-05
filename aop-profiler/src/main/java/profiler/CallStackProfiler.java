package profiler;

import org.aspectj.lang.Signature;
import profiler.model.MethodStat;
import profiler.model.TimeInterval;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class CallStackProfiler {
    public static final CallStackProfiler INSTANCE = new CallStackProfiler();

    private static final String NL = System.lineSeparator();
    private static String packageName;

    private final Map<String, Long> callCount = new HashMap<>();
    private final Map<String, Long> failCount = new HashMap<>();
    private final Map<String, Duration> methodSumDuration = new HashMap<>();

    private final List<MethodStat> callSequence = new ArrayList<>();

    private final Deque<StackNode> callStack = new ArrayDeque<>();

    private CallStackProfiler() {
    }

    private String signatureKey(Signature signature) {
        return signature.toLongString();
    }

    private void uncheckedWrite(Writer writer, String s) {
        try {
            writer.write(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildShortStat(String key, Long durationNS) {
        long callCnt = callCount.getOrDefault(key, 0L);
        long failCnt = failCount.getOrDefault(key, 0L);
        return key + ";" + NL
            + "Total number of calls: " + callCnt + NL
            + "Total number of errors: " + failCnt + NL
            + "Total execution time: " + MethodStat.nsToString(durationNS) + NL
            + "Average running time: " + MethodStat.nsToString(durationNS / callCnt) + NL;
    }

    private String callSeqNodeToString(MethodStat ms) {
        String indent = " ".repeat(ms.getDepth());
        String res = indent + ms.getMainInfo();
        if (ms.isFailed()) {
            return res + NL
                + indent + "  " + "Method failed with message: "
                + ms.getFailInfo().getMessage() + NL;
        } else {
            return res + NL;
        }
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
        callStack.push(new StackNode(key, inTime, callSequence.size()));
        callCount.compute(key, (k, oldCnt) -> oldCnt == null ? 1 : oldCnt + 1);
        callSequence.add(new MethodStat(
            signature,
            callStack.size()
        ));
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
        MethodStat uncompletedStat = callSequence.get(node.seqPos);
        uncompletedStat.setTimeInterval(new TimeInterval(node.startTime, outTime));
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
        try {
            printStatistic(writer);
        } catch (IOException ignored) {
            // Unreachable, string writer does not throw IOException.
        }
        return writer.toString();
    }

    public void printStatistic(Writer writer) throws IOException {
        writer.write(String.format("%s package statistic:%s", getPackageName(), NL));
        methodSumDuration.entrySet().stream()
            .map(e -> Map.entry(e.getKey(), e.getValue().toNanos()))
            .sorted(Map.Entry.comparingByValue())
            .forEach(e -> uncheckedWrite(writer, buildShortStat(e.getKey(), e.getValue())));
    }

    public String buildCallSequenceView() {
        StringWriter writer = new StringWriter();
        try {
            printCallSequenceView(writer);
        }  catch (IOException ignored) {
            // Unreachable, string writer does not throw IOException.
        }
        return writer.toString();
    }

    public void printCallSequenceView(Writer writer) throws IOException {
        writer.write(String.format("%s package statistic:%s", getPackageName(), NL));
        callSequence.forEach(
            methodStat -> uncheckedWrite(writer, callSeqNodeToString(methodStat))
        );
    }

    public void clear() {
        callCount.clear();
        failCount.clear();
        methodSumDuration.clear();
        callSequence.clear();
        callStack.clear();
    }

    static class StackNode {
        private final String methodKey;
        private final Instant startTime;
        private final int seqPos;

        public StackNode(String methodKey, Instant startTime, int seqPos) {
            this.methodKey = methodKey;
            this.startTime = startTime;
            this.seqPos = seqPos;
        }
    }
}
