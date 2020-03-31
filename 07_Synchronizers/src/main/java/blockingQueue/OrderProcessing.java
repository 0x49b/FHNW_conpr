package blockingQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class OrderProcessing {

    public static void main(String[] args) {
        int nCustomers = 10;
        int nValidators = 2;
        int nProcessors = 3;
        BlockingQueue<Order> nvOrders = new ArrayBlockingQueue(1000);
        BlockingQueue<Order> vOrders = new ArrayBlockingQueue(1000);

        for (int i = 0; i < nCustomers; i++) {
            new Customer("" + i, nvOrders).start();
        }

        for (int i = 0; i < nValidators; i++) {
            new OrderValidator(nvOrders, vOrders).start();
        }

        for (int i = 0; i < nProcessors; i++) {
            new OrderProcessor(vOrders).start();
        }
    }

    static class Order {
        public final String customerName;
        public final int itemId;

        public Order(String customerName, int itemId) {
            this.customerName = customerName;
            this.itemId = itemId;
        }

        @Override
        public String toString() {
            return "Order: [name = " + customerName + " ], [item = " + itemId + " ]";
        }
    }


    static class Customer extends Thread {

        private BlockingQueue q;

        public Customer(String name, BlockingQueue queue) {
            super(name);
            q = queue;
        }

        private Order createOrder() {
            Order o = new Order(getName(), (int) (Math.random() * 100));
            System.out.println("Created:   " + o);
            return o;
        }

        private void handOverToValidator(Order o) throws InterruptedException {
            q.add(o);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Order o = createOrder();
                    handOverToValidator(o);
                    Thread.sleep((long) (Math.random() * 1000));
                }
            } catch (InterruptedException e) {
            }
        }
    }


    static class OrderValidator extends Thread {

        private BlockingQueue nvQ;
        private BlockingQueue vQ;

        public OrderValidator(BlockingQueue nvqueue, BlockingQueue vqueue) {
            nvQ = nvqueue;
            vQ = vqueue;
        }

        public Order getNextOrder() throws InterruptedException {
            return (Order) nvQ.poll();
        }

        public boolean isValid(Order o) {
            return o.itemId < 50;
        }

        public void handOverToProcessor(Order o) throws InterruptedException {
            vQ.add(o);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Order o = getNextOrder();
                    if (isValid(o)) {
                        handOverToProcessor(o);
                    } else {
                        System.err.println("Destroyed: " + o);
                    }
                    Thread.sleep((long) (Math.random() * 1000));
                }
            } catch (InterruptedException e) {
            }
        }
    }


    static class OrderProcessor extends Thread {

        private BlockingQueue q;

        public OrderProcessor(BlockingQueue queue) {
            q = queue;
        }

        public Order getNextOrder() throws InterruptedException {
            return (Order) q.poll();
        }

        public void processOrder(Order o) {
            System.out.println("Processed: " + o);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Order o = getNextOrder();
                    processOrder(o);
                    Thread.sleep((long) (Math.random() * 1000));
                }
            } catch (InterruptedException e) {
            }
        }
    }
}
