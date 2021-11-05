import demo.Example;
import profiler.CallStackProfiler;

public class Main {
    public static void main(String[] args) {
        CallStackProfiler.setPackageName("demo");
        Example e = new Example();
        e.run();
        System.out.println(CallStackProfiler.INSTANCE.buildStatistic());
        System.out.println(CallStackProfiler.INSTANCE.buildCallSequenceView());
    }
}
