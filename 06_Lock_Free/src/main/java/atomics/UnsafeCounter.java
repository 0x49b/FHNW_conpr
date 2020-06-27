package atomics;

import java.lang.reflect.Field;

import sun.misc.Unsafe;


public class UnsafeCounter {
    private static final Unsafe unsafe = getUnsafe();

    private static final long valueOffset;
    private volatile long value = 0;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset(
                    UnsafeCounter.class.getDeclaredField("value"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    public long getValue() {
        return value;
    }

    public long increment() {
        while (true) {
            long current = getValue();
            long next = current + 1;
            if (compareAndSwap(current, next))
                return next;
        }
    }

    private boolean compareAndSwap(long expected, long newVal) {
        return unsafe.compareAndSwapLong(this, valueOffset, expected, newVal);
    }

    public static void main(String[] args) {
        final UnsafeCounter c = new UnsafeCounter();

        for (int i = 0; i < 100; i++) {
            c.increment();
        }
        System.out.println(c.value);
    }

    private static Unsafe getUnsafe() {
        // Java 8 requires -Xbootclasspath/p:${project_loc:/06_LE_Lock_Free}/bin/.
        // return Unsafe.getUnsafe();

        // Java 9 requires module dependency --add-modules jdk.unsupported
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }
}