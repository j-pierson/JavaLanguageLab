import java.net.*;
import java.io.*;
import java.util.*;

/** The is a class that stores all of the information and methods for URLDepth
Pair objects **/
public class URLDepthPair {
  /** Field to hold the URL **/
  public String url;
  /** Field to hold the depth (level of parsing) where the URL was found **/
  public int depth;
  /** Field to hold the URL prefix. e.g. http://, mailto://, ftp:// **/
  public String prefix;
  /** Field to hold the URL host (between prefix and docpath) **/
  public String host;
  /** Field to hold the URL docpath (everything after first /) **/
  public String docpath;

  /** Constant to hold the acceptable prefix **/
  public static final String URL_PREFIX = "http://";

  /** Constructor to initialize all fields **/
  public URLDepthPair(String server, int dep) {
    /** URL is the entire string **/
    this.url = server;
    /** Depth is passed as argument **/
    this.depth = dep;
    /** Identifying the desired string **/
    String str = "://";
    /** Finding the end of the prefix **/
    int i = server.indexOf(str) + str.length();;
    /** Getting prefix substring **/
    this.prefix = server.substring(0, i);
    /** Finding the end of the host name **/
    int j = server.indexOf("/", i);
    /** If there is no docpath **/
    if (j == -1) {
      /** Still need the / character for parsing **/
      this.docpath = "/";
      /** Host is everything after prefix **/
      this.host = server.substring(i);
    }
    /** If there is a docpath **/
    else {
      /** Host is between prefix and docpath **/
      this.host = server.substring(i, j);
      /** docpath is everything after host **/
      this.docpath = server.substring(j);
    }
  }

  /** Method to give the URL, Depth pair information through text **/
  public String toString() {
    /** Text will contain the depth, followed by the URL **/
    String result = "Depth " + depth +": " + url;
    return result;
  }

  /** Method to check if the pair has a valid prefix **/
  public boolean isValid() {
    /** Make sure the prefix equals the constant valid prefix **/
    if (prefix.equals(URL_PREFIX)) {
      return true;
    }
    return false;
  }
}
