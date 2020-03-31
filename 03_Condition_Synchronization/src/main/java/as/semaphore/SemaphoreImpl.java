package as.semaphore;

public final class SemaphoreImpl implements Semaphore {
    private int value;

    public SemaphoreImpl(int initial) {
        if (initial < 0) throw new IllegalArgumentException();
        value = initial;
    }

    @Override
    public synchronized int available() {
        return value;
    }

    @Override
    public void acquire() {
        synchronized (this) {
            while (value == 0) {
                try {
                    wait();
                } catch (Exception ignore) {
                }
            }
            value--;
        }
    }

    @Override
    public void release() {
        synchronized (this) {
            value++;
            notifyAll();
        }
    }
}
