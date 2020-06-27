package thisescapes;

class EventSource {
    void registerListener(final EventListener e) {
        e.objectChanged();
//        new Thread(() -> e.objectChanged()).start();
//        try { Thread.sleep(10); } catch (InterruptedException e1) { }
    }
}