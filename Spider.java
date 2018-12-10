package crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import model.*;

/*
 * This webcrawler was taken from the internet to use in this program. It requires jsoup jar file to be linked to this folder.
 * The spider class, along with the spiderleg class, are both from the internet.
 * Source: http://www.netinstructions.com/how-to-make-a-simple-web-crawler-in-java/
 * The while loop within this contains crawl, which has a for loop. This means that the total search takes O(n^2) time.
 */


public class Spider
{
  private static final int MAX_PAGES_TO_SEARCH = 30; // limits the number of searches to 31 times.
  private Set<String> pagesVisited = new HashSet<String>(); //Keeps a list of pages visited already
  private List<String> pagesToVisit = new LinkedList<String>(); //Keeps a list of pages to visit.

  /**
   * Our main launching point for the Spider's functionality. Internally it creates spider legs
   * that make an HTTP request and parse the response (the web page).
   * 
   * @param url
   *            - The starting point of the spider
   * @param searchWord
   *            - The word or string that you are searching for
   * @param urlArray
   * 			- An array to store all of the urls searched through.
   */
  public void search(String url, String searchWord, WebPage[] urlArray)
  {
     while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) //My program searches for 31 urls, so we cap it at 31.
      {
          String currentUrl; // will store url of the site.
          SpiderLeg leg = new SpiderLeg(); // leg will crawl the site.
          if(this.pagesToVisit.isEmpty()) // checks if there is any more pages to visit.
          {
              currentUrl = url; //if no pages to visit, set currenturl to the url of the current page.
              this.pagesVisited.add(url); //says you visited that page so that you don't visit it again when crawling.
          }
          else
          {
              currentUrl = this.nextUrl(); //if there is something to visit before that url, move on to next url.
          }
          leg.crawl(currentUrl); //Look at the crawl method in SpiderLeg for specifications.
          boolean success = leg.searchForWord(searchWord); // Modified this from the source to stop it from breaking.
          												// Determines if the word does exist on the page, but for our simulation,
          												// we will ignore this.
          urlArray[this.pagesVisited.size() - 1].setURL(currentUrl); // store the currenturl visited into Storage.
          urlArray[this.pagesVisited.size() - 1].setIndex(this.pagesVisited.size() - 1); //sets the nonsorted index of the webpage
          urlArray[this.pagesVisited.size() - 1].RandomSetRel(); // sets all of the score factors in the webpage.
          this.pagesToVisit.addAll(leg.getLinks()); //all links on url are added to pagestovisit.
      }
      System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)"); // end notice saying it visited x amount of pages.
      this.pagesVisited.clear(); //size goes back to 0, allows for multiple searches to be done.
  }

  /**
   * Returns the next URL to visit (in the order that they were found). We also do a check to make
   * sure this method doesn't return a URL that has already been visited.
   * 
   * @return
   */
  private String nextUrl()
  {
      String nextUrl; //stores the url of the next page to visit.
      do
      {
        nextUrl = this.pagesToVisit.remove(0); //sets the url of the next page to visit.
      } while(this.pagesVisited.contains(nextUrl)); //if we already visited that url, contain the loop until a new one shows up.
      this.pagesVisited.add(nextUrl); //Once we find a new url to visit, add it to pagesVisited.
      return nextUrl; //returns the next new url.
  }
}
