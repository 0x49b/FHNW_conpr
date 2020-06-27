package spotbugs;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.LinkedList;

@SuppressWarnings("serial")
public class I extends LinkedList<Integer> {
  @SuppressFBWarnings("SE_NO_SERIALVERSIONID")
  public I() {
    new Thread(new Runnable() {
      public void run() {
        add(1);
      }
    }).start();
  }
}
