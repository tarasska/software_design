package demo;

import demo.subpackage.Example;

public class ExampleRunner {
    public void run(int iterCnt) {
        for (int i = 1; i < iterCnt; i++) {
            try {
                int ignored = new Example(i).randomGcd(i);
            } catch (Throwable t) {
                // NOP
            }
        }
    }
}
