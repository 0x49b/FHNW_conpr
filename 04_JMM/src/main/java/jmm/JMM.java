package jmm;

public class JMM {
    private static int i = 0;

    public static void main(String[] args) {
        i = 1;
        new Thread(() -> {
            i = 4;
            i = i + 2;
        }, "T1").start();
        new Thread(() -> {
            i = 10;
            i++;
        }, "T2").start();
        new Thread(() -> {
            System.out.println(i);
        }, "T3").start();
    }
}
