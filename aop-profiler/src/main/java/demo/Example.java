package demo;

public class Example {
    public int run() {
        int x = div(2, 2);
        try {
            int y = div(2, 0);
        } catch (Throwable t) {
            //NOP
        }
        return 0;
    }

    public int div(int a, int b) {
        return a / b;
    }
}
