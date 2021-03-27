import java.net.*;
import java.io.*;
import java.util.*;

public class WebCrawler {

  public static void main(String[] args) {
    /** Check for proper usage **/
    if (args.length != 3) {
      System.out.println("Usage: java Crawler <URL> <depth> <threads>");
      System.exit(1);
    }
    /** Construct the crawler, checking that the depth is a valid integer **/
    try {
      int i = Integer.parseInt(args[1]);
      if (i < 0) {
        System.out.println("<depth> must be a positive integer");
        System.exit(1);
      }
    }
    catch (NumberFormatException e) {
      System.out.println("<depth> must be a positive integer");
      System.exit(1);
    }
    try {
      int i = Integer.parseInt(args[2]);
      if (i < 0) {
        System.out.println("<threads> must be a positive integer");
        System.exit(1);
      }
    }
    catch (NumberFormatException e) {
      System.out.println("<threads> must be a positive integer");
      System.exit(1);
    }
    int maxdepth = Integer.parseInt(args[1]);
    int numthreads = Integer.parseInt(args[2]);
    URLPool pool =  new URLPool(args[0], maxdepth);
    for (int i = 0; i < numthreads; i++) {
      CrawlerTask c = new CrawlerTask(pool);
      Thread t = new Thread(c);
      t.start();
    }
    while (pool.getWaitCount() != numthreads) {
      try {
        Thread.sleep(500);
      }
      catch (InterruptedException ie) {

      }
      System.out.println(pool.getWaitCount());
    }
    pool.printURLs();
    System.exit(1);
  }
}
