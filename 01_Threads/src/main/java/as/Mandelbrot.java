package as;

import javafx.scene.paint.Color;

/**
 * Computes the Mandelbrot set.
 * http://en.wikipedia.org/wiki/Mandelbrot_set
 */
public class Mandelbrot {
    public static final int IMAGE_LENGTH = 1024;
    public static final int MAX_ITERATIONS = 512;

    public static final int COLOR_COUNT = 1024;
    private static Color[] colors = generateColors(COLOR_COUNT);

    private static int numThread = 8;
    private static int segments = IMAGE_LENGTH / numThread;
    private static int segmentStart = 0;
    private static int segmentStop = segments;

    private static Color getColor(int iterations) {
        return iterations == MAX_ITERATIONS ? Color.BLACK : colors[iterations % COLOR_COUNT];
    }

    private static Color[] generateColors(int n) {
        Color[] cols = new Color[n];
        for (int i = 0; i < n; i++) {
            cols[i] = Color.hsb(((float) i / (float) n) * 360, 0.85f, 1.0f);
        }
        return cols;
    }

    public static void computeSequential(PixelPainter painter, Plane plane, CancelSupport cancel) {
        double half = plane.length / 2;
        double reMin = plane.center.r - half;
        double imMax = plane.center.i + half;
        double step = plane.length / IMAGE_LENGTH;

        for (int x = 0; x < IMAGE_LENGTH && !cancel.isCancelled(); x++) { // x-axis
            double re = reMin + x * step; // map pixel to complex plane
            for (int y = 0; y < IMAGE_LENGTH; y++) { // y-axis
                double im = imMax - y * step; // map pixel to complex plane

                //int iterations = mandel(re, im);
                int iterations = mandel(new Complex(re, im));
                painter.paint(x, y, getColor(iterations));
            }
        }
    }


    public static void computeParallel(PixelPainter painter, Plane plane, CancelSupport cancel) {
        // TODO Implement a parallel version of the mandelbrot set computation.

        class CalculateParallel implements Runnable {

            double half;
            double reMin;
            double imMax;
            double step;
            PixelPainter painter;
            Plane plane;
            CancelSupport cancel;
            int start;
            int stop;

            public CalculateParallel(PixelPainter painter, Plane plane, CancelSupport cancel, int start, int stop) {
                this.plane = plane;
                this.painter = painter;
                this.cancel = cancel;
                this.half = plane.length / 2;
                this.reMin = plane.center.r - half;
                this.imMax = plane.center.i + half;
                this.step = plane.length / IMAGE_LENGTH;
                this.start = start;
                this.stop = stop;
            }

            public void run() {



                for (int x = start; x < stop && x < IMAGE_LENGTH && !cancel.isCancelled(); x++) { // x-axis
                    double re = reMin + x * step; // map pixel to complex plane
                    for (int y = 0; y < IMAGE_LENGTH; y++) { // y-axis
                        double im = imMax - y * step; // map pixel to complex plane

                        //int iterations = mandel(re, im);
                        int iterations = mandel(new Complex(re, im));
                        painter.paint(x, y, getColor(iterations));
                    }
                }
            }

        }


        for (int i = 1; i <= numThread; i++) {

            Thread t = new Thread(new CalculateParallel(painter, plane, cancel, segmentStart, segmentStop), "calculateParallel"+i);
            t.start();

            segmentStart = segmentStop;
            segmentStop = segmentStop + segments;
        }

        segmentStart = 0;
        segmentStop = segments;

        //throw new RuntimeException("To be implemented!");


    }

    /**
     * z_n+1 = z_n^2 + c starting with z_0 = 0
     * <p>
     * Checks whether c = re + i*im is a member of the Mandelbrot set.
     *
     * @return the number of iterations
     */
    public static int mandel(Complex c) {
        Complex z = Complex.ZERO;
        int iterations = 0;
        while (z.absSq() <= 4 && iterations < MAX_ITERATIONS) {
            z = z.pow(2).plus(c);
            iterations++;
        }
        return iterations;
    }

    /**
     * Same as {@code Mandelbrot#mandel(Complex)} but more efficient.
     */
    public static final int mandel(double cre, double cim) {
        double re = 0.0;
        double im = 0.0;
        int iterations = 0;
        while (re * re + im * im <= 4 && iterations < MAX_ITERATIONS) {
            double re1 = re * re - im * im + cre;
            double im1 = 2 * re * im + cim;
            re = re1;
            im = im1;
            iterations++;
        }
        return iterations;
    }
}
