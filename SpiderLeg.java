package crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * The leg does the crawling and the searching. For our simulation purpose, the crawler is simplified to retrieve 31 urls,
 * regardless if the word actually exists within it or not. 
 * The longest, and most important feature, crawl, takes O(n) time.
 */
public class SpiderLeg
{
    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =  //apparently we need this to make the site load and not think it is a robot.
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<String>(); //stores a list of links on the page to potentially visit.
    private Document htmlDocument; // stores the webpage, which will then be converted into a body of text.

    /**
     * This performs all the work. It makes an HTTP request, checks the response, and then gathers
     * up all the links on the page. Perform a searchForWord after the successful crawl
     * 
     * @param url
     *            - The URL to visit
     * @return whether or not the crawl was successful
     */
    public boolean crawl(String url)
    {
        try
        {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT); //This will connect us to a browser and to the url.
            Document htmlDocument = connection.get(); //Retrieves the page of the url.
            this.htmlDocument = htmlDocument; //set the spiderLeg variable equal to the htmlDocument.
            if(!connection.response().contentType().contains("text/html")) //If the retrieved url does not have text or is not a link.
            {
                System.out.println("**Failure** Retrieved something other than HTML"); //state there is an error with the crawling.
                return false; //failed to crawl the page.
            }
            Elements linksOnPage = htmlDocument.select("a[href]"); //Retrieve all the links on that url.
            for(Element link : linksOnPage) //for all the links on the page
            {
                this.links.add(link.absUrl("href")); //adds the link to the list.
            }
            return true; //successful crawl
        }
        catch(IOException ioe)
        {
            return false;  // We were not successful in our HTTP request
        }
    }

    /**
     * Performs a search on the body of on the HTML document that is retrieved. This method should
     * only be called after a successful crawl.
     * 
     * @param searchWord
     *            - The word or string to look for
     * @return whether or not the word was found
     */
    public boolean searchForWord(String searchWord)
    {
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null) //If there is no page to search through
        {
            return false;
        }
        String bodyText = this.htmlDocument.body().text(); //Converts the page into a body of text.
        return bodyText.toLowerCase().contains(searchWord.toLowerCase()); //search the body of text for the word.
    }

    //Getter
    public List<String> getLinks()
    {
        return this.links;
    }

}
