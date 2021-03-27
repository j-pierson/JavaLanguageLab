import java.net.*;
import java.io.*;
import java.util.*;

public class URLPool {
  /** This field is a linked list to hold the valid URL, depth pairs as they
  appear during parsing **/
  private LinkedList<URLDepthPair> pendingList = new LinkedList<URLDepthPair>();
  /** This field is a linked list to hold URL, depth pairs after they have been
  parsed for next layer URLs **/
  public LinkedList<URLDepthPair> scannedList = new LinkedList<URLDepthPair>();

  private int maxdepth;

  private int waitcount;

  public URLPool(String url, int depth) {
    URLDepthPair pair = new URLDepthPair(url, 0);
    this.put(pair);
    maxdepth = depth;
    waitcount = 0;
  }

  public synchronized URLDepthPair get() {
    while (pendingList.size() == 0) {
      waitcount++;
      try {
        wait();
      }
      catch (InterruptedException e) {

      }
      waitcount--;
    }
    return pendingList.removeFirst();
  }

  public synchronized void put(URLDepthPair pair) {
    scannedList.add(pair);
    if (pair.depth < maxdepth) {
      pendingList.add(pair);
    }
    notify();
  }

  public synchronized int getWaitCount() {
    return waitcount;
  }

  public void printURLs() {
    while (scannedList.peek() != null) {
      System.out.println(scannedList.getFirst().toString());
      scannedList.remove();
    }
  }
}
