import java.net.*;
import java.io.*;
import java.util.*;

public class CrawlerTask implements Runnable {

  public URLPool pool;

  /** A constant that allows sockets to access the correct port for websites **/
  public static final int PORT = 80;

  /** Constant to hold the acceptable prefix **/
  public static final String PARSE_PREFIX = "a href=\"";

  public CrawlerTask(URLPool p) {
    pool = p;
  }

  public void run() {
    while (true) {
      try {
        URLDepthPair pair = pool.get();
        /** Set the depth for all URLs found on the site to be one greater than the
        current site **/
        int depth = pair.depth + 1;
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
                  pool.put(found);
                }
              }
            }
          }
        }
        sock.close();
      }
      catch (IOException e) {

      }
    }
  }
}
