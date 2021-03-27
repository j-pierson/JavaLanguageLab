import java.net.*;
import java.io.*;
import java.util.*;

/** This is a class that creates a web crawler object and uses socket API to
read from a given url. It will then gather all urls in the given site and do the
same for them up to a given max depth.

The class take two command line argument:

(1) String url -- a string containg the web address of some site with prefix
http://

(2) int maxdepth -- an integer that tells the crawler how many layers of parsing
to **/

public class Crawler {
  /** This field is determined by a command line argument, and it stores the
  number of layers of url that will be parsed by the crawler **/
  private int maxdepth;
  /** This field is a linked list to hold the valid URL, depth pairs as they
  appear during parsing **/
  private LinkedList<URLDepthPair> seenList = new LinkedList<URLDepthPair>();
  /** This field is a linked list to hold URL, depth pairs after they have been
  parsed for next layer URLs **/
  private LinkedList<URLDepthPair> processedList = new LinkedList<URLDepthPair>();

  /** A constant that allows sockets to access the correct port for websites **/
  public static final int PORT = 80;

  /** Constant to hold the acceptable prefix **/
  public static final String PARSE_PREFIX = "a href=\"";

  /** The Crawler constructor. It initialized maxdepth, and adds the first URL,
  depth pair to the linked list **/
  public Crawler(String url, int maxdep) {
    this.maxdepth = maxdep;
    /** Create a new URL, depth pair with the command line url and depth 0 **/
    URLDepthPair pair = new URLDepthPair(url, 0);
    /** Add the pair to the list of unprocessed pairs **/
    seenList.add(pair);
  }

  /** This is a method that will process a single url. It will create a socket
  to connect to the site, and then parse through the returned text to find next
  level URLs to add to seenList **/
  public void processURL() {
    try {
      /** Get the URLDepthPair object from the front of the seenList **/
      URLDepthPair pair = seenList.getFirst();
      /** Set the depth for all URLs found on the site to be one greater than the
      current site **/
      int depth = pair.depth + 1;
      /** Check if the depth is past the max, in which case the parsing is not
      necessary **/
      if (depth > maxdepth) {
        /** Move the pair from the seenList to the processedList **/
        processedList.add(pair);
        seenList.remove();
        return;
      }
      /** Create a socket at the URL for the desired pair **/
      Socket sock = new Socket(pair.host, PORT);
      /** Set the socket to timeout in 3 seconds **/
      sock.setSoTimeout(3000);
      /** Create an outputsream associated with the socket **/
      OutputStream os = sock.getOutputStream();
      /** Create a PrintWriter to transfer type to byte code **/
      PrintWriter writer = new PrintWriter(os, true);
      /** Use the write to send the HTTP request **/
      writer.println("GET " + pair.docpath + " HTTP/1.1");
      writer.println("Host: " + pair.host);
      writer.println("Connection: close");
      writer.println();
      /** Create an inputstream asssociated with the socket **/
      InputStream is = sock.getInputStream();
      /** Reader to turn byte code in to language **/
      InputStreamReader isr = new InputStreamReader(is);
      /** Make a buffered reader to read one line at a time **/
      BufferedReader br = new BufferedReader(isr);
      /** Read the input text **/
      while (true) {
        String str = br.readLine();
        /** Exit when the end of the text is reached **/
        if (str == null) {
          break;
        }
        String line = str.trim();
        //System.out.println(line);
        /** If the line is there, check for URLs **/
        int j = 0;
        while (true) {
          /** Index the start of the URL **/
          int i = line.indexOf(PARSE_PREFIX, j);
          if (i == -1) {
            /** break if there are no URLs on the rest of the line **/
            break;
          }
          else {
            /** Index the end of the URL **/
            j = line.indexOf("\"", i + PARSE_PREFIX.length());
            /** Get the url between stand and end indexes **/
            if (j == -1) {
              break;
            }
            String url = line.substring(i + PARSE_PREFIX.length(), j);
            /** Create a pair for the found URL **/
            if (url.length() != 1)
            {
              URLDepthPair found = new URLDepthPair(url, depth);
              /** If the URL has prefix http:// it gets added to seenList **/
              if (found.isValid()) {
                seenList.add(found);
              }
            }
          }
        }
      }
      sock.close();
      /** Move the pair from seen to processedList **/
      processedList.add(pair);
      seenList.remove();
    }
    catch (IOException e) {

    }
  }

  /** This is a method to print all the found URLs **/
  public void getUrls() {
    /** While the processedList isn't empty, print the first url and then
    remove **/
    while (processedList.peek() != null) {
      System.out.println(processedList.getFirst().toString());
      processedList.remove();
    }
  }

  /** This is a method containing function calls for processing and printing,
  as well as handling exceptions raised during processing **/
  public void runCrawler() {
    /** Check if the command line URL is valid **/
    if (!seenList.getFirst().isValid()) {
      System.out.println("Web crawler only accepts prefix http://");
    }
    else {
      while (seenList.peek() != null) {
        processURL();
      }
    }
    /** Function call to print retrieved URLs **/
    getUrls();
  }

  /** The main method to check usage, initialize the crawler, and run it **/
  public static void main(String[] args) {
    /** Check for proper usage **/
    if (args.length != 2) {
      System.out.println("Usage: java Crawler <URL> <depth>");
      System.exit(1);
    }
    /** Construct the crawler, checking that the depth is a valid integer **/
    try {
      if (Integer.parseInt(args[1]) >= 0) {
        Crawler crawl = new Crawler(args[0], Integer.parseInt(args[1]));
        crawl.runCrawler();
      }
      else {
        System.out.println("<depth> must be a positive integer");
        System.exit(1);
      }
    }
    catch (NumberFormatException e) {
      System.out.println("<depth> must be a positive integer");
      System.exit(1);
    }
  }
}
