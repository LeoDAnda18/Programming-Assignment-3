package main;

/*
 * This main allows the user to test out what is required of the assignment. 
 * First for loop takes O(n) time.
 * Spider.search takes up to O(n^2) time.
 * 
 * From the 6 options:
 * Option 1 is Insertion, which takes O(lgn) time.
 * Option 2 is search, which takes O(lgn) time.
 * Option 3 is delete, which takes O(lgn) time.
 * Option 4 is to display the content of the RB Tree through inorder traversal. Takes O(n) time.
 * Option 5 is to display the RB Tree.
 * Option 6 is to search again. takes O(n^2) time.
 * All of this is in a do while loop, so the loop goes r times (where r is the number of times user decides to test something.
 * Therefore, this program takes rO(n^2) time at worst.
 */

import java.util.*;
import crawler.*;
import rbtree.*;
import model.*;

public class Main 
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		int User_input, size = 30; // size of array of webpages
		String searchTerm; // user input stored into here.
		WebPage[] urlStorage = new WebPage[size]; //Array to store WebPages
		for(int i = 0; i < size; i++)
		//Initialize all of the objects in the array.
		{	
			urlStorage[i] = new WebPage();
		} 
        Spider spider = new Spider(); //Web Crawler
      
        System.out.println("Enter a search word to browse for: ");
        searchTerm = in.nextLine(); // user input for searching
        System.out.println("Crawling the web and retrieving sites. This will take some time. Please be patient.");
        spider.search("https://www.facebook.com/", searchTerm, urlStorage); //Search the web for the word, starting at facebook.com
        RBTree f1 = new RBTree(urlStorage); // create a RB tree based off the urlStorage.
		
        do
        {
        	// Lists available options for users to select from.
    		System.out.println("Choose an option and enter the corresponding number:");
            System.out.println("1. Insert URL based off its total score and display result.");
            System.out.println("2. Search by PageRank or Total Score and display result.");
            System.out.println("3. Delete a URL and display result.");
            System.out.println("4. Sorted list of URL's based off current RB Tree.");
            System.out.println("5. Print out the RB tree");
            System.out.println("6. Search for a new term");
            System.out.println("0. Quit");
            User_input = in.nextInt(); // takes in the user input
            in.nextLine();
            while(User_input < 0 || User_input > 6) //ensures user enters a valid score.
            {
        		System.out.println("Choose a valid option and enter the corresponding number:");
                User_input = in.nextInt(); // takes in the user input
                in.nextLine();
            }
            switch(User_input)
            {
            	case 1: //Inserts a WebPage into the RB Tree and display result.
            	{
            		int TotalScoreTemp;
            		String URLTemp;
        		
            		WebPage add = new WebPage(); //instantiate an object to hold the webpage to add.
            		System.out.println("Please enter the URL of this new WebPage: ");
            		URLTemp = in.next();
            		System.out.println("Please enter a TotalScore to assign to this WebPage: ");
            		TotalScoreTemp = in.nextInt();
            		add.setRel(TotalScoreTemp);
            		add.setURL(URLTemp);
            		f1.RBInsert(add); //insert the webpage into the RB Tree.
            		System.out.println("Now displaying the newly updated list: ");
            		f1.InOrderTreeWalk(f1.getRoot());
            		System.out.println("Now displaying the RB-Tree");
                	f1.drawTree(); //draw the tree to show it has been added successfully.
            		break;
            	}
            	case 2: //Searches the RB tree for a specific PageRank. If it does not exist, a notification will say that pagerank does not exist.
        		{
        			int searchOption;
        			System.out.println("Would you like to search by score or rank? Enter 1 for Score, 2 for Rank: ");
        			searchOption = in.nextInt();
        			WebPage search = new WebPage(); //Create a webpage object to hold the object found from the search.
        			search = null;
        			if(searchOption == 1)
        			{
        				int totalScoreTemp;
        				System.out.println("Please enter a TotalScore to search for.");
            			totalScoreTemp = in.nextInt(); //Stores pagerank to search for
            			search = f1.treeSearchScore(f1.getRoot(), totalScoreTemp);
        			}
        			else
        			{
        				int PageRankTemp;
            			System.out.println("Please enter a PageRank to search for.");
            			PageRankTemp = in.nextInt(); //Stores pagerank to search for
            			search = f1.treeSearchRank(f1.getRoot(), PageRankTemp);
        			}
        			
        			if(search != null)
        			{
        				search.printContents(); //if webpage found, display content.
        			}
        			break;
        		}
                case 3: //Delete a WebPage from the RB Tree and display the result
            	{
            		int PageRankTemp;
            		int TotalScoreTemp;
            		int optionDelete;
                    f1.InOrderTreeWalk(f1.getRoot()); //display the current RB Tree contents.
                    System.out.println("Would you like to delete by total score or pageRank?. Enter 1 for score, 2 for rank: ");
                    optionDelete = in.nextInt();
                    WebPage search = new WebPage();
                    search = null;
                    if(optionDelete == 1)
                    {
                        System.out.println("Please enter the Total Score value of the URL you would like to delete.");
                        TotalScoreTemp = in.nextInt(); //store user input.
                        search = f1.treeSearchScore(f1.getRoot(), TotalScoreTemp); //search for the node and store the object found
                    }
                    else
                    {
                        System.out.println("Please enter the PageRank value of the URL you would like to delete.");
                        PageRankTemp = in.nextInt(); //store user input.
                        search = f1.treeSearchRank(f1.getRoot(), PageRankTemp); //search for the node and store the object found
                    }
                    if(search != null) //if object found isn't null
                    {
                        f1.RBDelete(search); //delete from tree
                		System.out.println("Now displaying the newly updated list: ");
                		f1.InOrderTreeWalk(f1.getRoot());
                		System.out.println("Now displaying the RB-Tree");
                    	f1.drawTree(); //draw the tree to show it has been added successfully.
                    }
                    break;
            	}
                case 4: //Traverse the tree in order and print out the results.
                {
                	f1.InOrderTreeWalk(f1.getRoot()); //display the content of the tree sorted.
                	break;
                }
                case 5: //Display the RB Tree.
                {
                	f1.drawTree();
                	break;
                }
                case 6: //search for a new term.
                {
                    System.out.println("Enter a search word to browse for: ");
                    searchTerm = in.nextLine(); // user input for searching
                    System.out.println("Crawling the web and retrieving sites. This will take some time. Please be patient.");
                    spider.search("https://www.facebook.com/", searchTerm, urlStorage); //Search the web for the word, starting at facebook.com
                    f1.replaceStorage(urlStorage);
                }
            }
        }while(User_input != 0); //keep running until user quits (enters 0).
        
        in.close();
	}
}