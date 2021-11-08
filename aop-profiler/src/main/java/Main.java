import demo.ExampleRunner;
import profiler.CallStackProfiler;

public class Main {
    public static void main(String[] args) {
        CallStackProfiler.setPackageName("demo");
        ExampleRunner e = new ExampleRunner();
        e.run(100);
        System.out.println(CallStackProfiler.INSTANCE.buildStatistic());
        System.out.println(CallStackProfiler.INSTANCE.buildCallSequenceView());
    }
}
