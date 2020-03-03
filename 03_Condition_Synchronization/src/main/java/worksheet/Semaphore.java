package worksheet;

public final class Semaphore {
    private int value;

    public Semaphore(int initial) {
        if (initial < 0) throw new IllegalArgumentException();
        value = initial;
    }

    public int available() {
        return value;
    }

    public void acquire() {


        synchronized (this) {
            while (value == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            value--;
        }


    }

    public void release() {
        synchronized (this) {
            value++;
            notifyAll();
        }

    }

}
