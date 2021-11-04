package profiler;

import org.aspectj.lang.Signature;

public class CallStackProfiler {

    private CallStackProfiler() {}

    public static final CallStackProfiler INSTANCE = new CallStackProfiler();

    private static String packageName;

    public static void setPackageName(String name) {
        packageName = name;
    }

    public static String getPackageName() {
        return packageName;
    }

    public void methodCall(Signature signature) {
        System.err.println("IN " + signature.getName());
    }

    public void methodExit(Signature signature) {
        System.err.println("OUT " + signature.getName());
    }

    public void methodFailed(Signature signature, Throwable t) {
        System.err.println("OUT " + signature.getName() + ". With msg: " + t.getMessage());
    }
}
