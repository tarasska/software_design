package demo.subpackage;

import java.util.Random;

public class Example {
    private final Random random = new Random();
    private final int failNumber;
    private final int activeWaitingNumber;

    public Example(int shift) {
        this.failNumber = 1 + shift + random.nextInt(5 * shift);
        this.activeWaitingNumber = 1 + random.nextInt(shift);
    }

    private void activeWait(int cnt) {
        int i = 0;
        while (i < cnt) i++;
    }

    public int randomGcd(int a) {
        return gcd(a, 1 + random.nextInt(2 * a) + random.nextInt(2 * a));
    }

    public int gcd(int a, int b) {
        if (a % failNumber == 0) {
            throw new IllegalStateException("Unexpected value.");
        } else if (a % activeWaitingNumber == 0) {
            activeWait(a);
        }
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }
}
