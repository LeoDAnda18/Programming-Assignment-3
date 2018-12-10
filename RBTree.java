package rbtree;

import java.util.ArrayList;
import java.lang.Math;

import model.*;

/*
 * This class stores all of the required RB tree functions specified in the book and in the guidelines.
 * These functions have been adjusted to work in Java and with the WebPage class. The root is Storage[0], T.nil is WebPage nil,
 * and printTree is a function designed to print out the tree (prints each nodes total score and color).
 */

public class RBTree 
{
	WebPage root = new WebPage(); //holds the content of the root of the tree.
	int pageRankCounter = 1; //Used to assign a pageRank value to each node in the tree. 
	 //(Especially useful after an insert/delete, which needs to redo values).
	ArrayList<WebPage> Storage = new ArrayList<WebPage>(); //dynamic size useful for insert/delete operations.
	WebPage nil = new WebPage(); //all leaf children point to nil, and the parent of root is nil.
	//Getters
	public WebPage getRoot() {return root;}
	public WebPage getElement(int i) {return Storage.get(i);}
	
	//Setters
	public void setRoot(WebPage R) {root = R;}
	
	/*
	 * Constructor. Takes in a WebPage array, stores a copy into Storage, 
	 * and then calls buildTree to create a RBTree
	 * For loop takes O(n) time, buildTree takes O(nlgn) time to build an RBTree (O(lgn) for inserts, O(n) for loop, O(nlgn) total).
	 */
	public RBTree(WebPage[] a1)
	{
		nil.setColor('B'); //Property of RB Tree.
		nil.setLeft(nil);
		nil.setRight(nil);
		for(int i = 0; i < a1.length; i++)
		{
			Storage.add(a1[i]); //Stores a copy into Storage, 
		}
		buildTree(); //Builds up a RBTree by calling multiple treeInserts.
	}
	
	public void buildTree()
	{
		//Single element tree has no children or parent. The root is black (property of RB Tree)
		Storage.get(0).setLeft(nil);
		Storage.get(0).setRight(nil);
		Storage.get(0).setParent(nil);
		Storage.get(0).setColor('B');
		root = Storage.get(0); //root is the first element of the storage.
		
		pageRankCounter = 1;
		for(int i = 1; i < Storage.size(); i++) //First element in this array is the root, so it is already inserted appropriately.
		{
			RBInsert(Storage.get(i)); //insert elements into the tree appropriately.
		}
		resetPageRanks(root); //After tree is constructed, do an inorder traversal and set the PageRank accordingly.
	}

	/*
	 * When searching for new terms, can't use the same array. Therefore, have to clear the Storage and then have it contain a copy of the newArray.
	 * This function takes O(n) time for each for loop and O(nlgn) for build tree.
	 */
	public void replaceStorage(WebPage[] newArray)
	{
		for(int i = 0; i < Storage.size(); i++)
		{
			Storage.get(i).clearPointers(); //sets all pointers back to null within the array.
		}
		Storage.clear(); //empty the current storage.
		nil.setColor('B'); //Property of RB Tree.
		for(int i = 0; i < newArray.length; i++)
		{
			Storage.add(newArray[i]); //fills the ArrayList with the contents of newArray.
		}
		buildTree(); //builds up a RB Tree based off the new Storage.
	}
	/*
	 * This function is called right after inserting or deleting from the tree. These operations will mess up the PageRanks,
	 * and so we have to recalculate the pageranks. This function does the recalculating for us.
	 * This takes O(1) + O(n) time, so total of O(n) time.
	 */
	public void resetPageRanks(WebPage x)
	{
		int RBTreeSize = pageRankCounter;
		setPageRanks(x); //Sets each node's pagerank equal to the value of pageRankCounter
		pageRankCounter = RBTreeSize;
	}
	
	/*
	 * This function is called to calculate the PageRanks of each node. It does an InOrderTraversal, but instead of printing something,
	 * it assigns a PageRank value to the node. This runs in O(n) time, since it has to go through each node in the tree.
	 */
	public void setPageRanks(WebPage x)
	{
		//Recursively call this function so long as the current node isn't nil.
		if(x != nil)
		{
			setPageRanks(x.getLeft()); //Traverse left first
			x.setPRank(pageRankCounter); //Assign a pagerank value to the node equal to pageRankCounter.
			pageRankCounter--; //increment pageRankCounter
			setPageRanks(x.getRight()); //Traverse to the right after assigning a pageRank value.
		}
	}
	
	/*
	 * This function displays the contents of the tree in an ascending order manner based off the insert function.
	 * This runs in O(n) time, since it has to go through each node in the tree.
	 */
	public void InOrderTreeWalk(WebPage x)
	{
		//Recursively call this function so long as the current node isn't null.
		if(x != nil)
		{
			InOrderTreeWalk(x.getLeft()); //Traverse left first
			x.printContents(); //Display the contents of the node.
			InOrderTreeWalk(x.getRight()); //Traverse right after displaying the node's content.
		}
	}
	
	/*
	 * This function takes in a PageRank value (assigned to k) and then, starting from the root, searches for the node containing that pageRank.
	 * If PageRank < k, it will traverse to the left. If PageRank > k, it will traverse to the right.
	 * If PageRank = k, it will return that node. If PageRank is not in the tree, it will print out a message saying it was not found.
	 * This function takes O(lgn) since it is a self balancing tree.
	 */
	public WebPage treeSearchRank(WebPage x, int k)
	{
		if(x == nil) //If no node contains the given pageRank.
		{
			System.out.println("No webpage contains a pageRank of " + k); //display a message to notify the user.
			return null; 
		}
		if(x.getPRank() == k) //if a node is found to contain the pagerank value.
			return x; 
		if(k > x.getPRank()) //if the pagerank is less than the current node's pagerank value, traverse left (higher pagerank = smaller score, go left).
			return treeSearchRank(x.getLeft(), k);
		else //if the pagerank is greater than the current node's pagerank value, traverse right (lower pagerank = larger score.
			return treeSearchRank(x.getRight(), k);
	}
	
	/*
	 * This function takes in a TotalScore value (assigned to k) and then, starting from the root, searches for the node containing that TotalScore.
	 * If TotalScore < k, it will traverse to the right. If TotalScore > k, it will traverse to the left.
	 * If TotalScore = k, it will return that node. If TotalScore is not in the tree, it will print out a message saying it was not found.
	 * This function takes O(lgn) since it is a self balancing tree.
	 */
	public WebPage treeSearchScore(WebPage x, int k)
	{
		if(x == nil) //If no node contains the given TotalScore.
		{
			System.out.println("No webpage contains a TotalScore of " + k); //display a message to notify the user.
			return null; 
		}
		if(x.getRel() == k) //if a node is found to contain the TotalScore value.
			return x; 
		if(k > x.getRel()) //if the TotalScore is less than the current node's TotalScore value, traverse right
			return treeSearchScore(x.getRight(), k);
		else //if the TotalScore is greater than the current node's TotalScore value, traverse left.
			return treeSearchScore(x.getLeft(), k);
	}
	
	/*
	 * This function inserts a new node into the tree. It starts by checking the totalScore of the new node z with the root, and then
	 * traversing left or right depending if it is less than or greater than the root. Once the location is found, pointer manipulation is done
	 * to insert the node into the tree. 
	 * After the node is inserted, it calls on InsertFixUp to fix the RB tree in order to preserve the 5 properties.
	 * This function takes O(lgn) to only insert (not fix up as well).
	 */
	public void RBInsert(WebPage z)
	{
		WebPage y = new WebPage(); //Used to keep track of the previous node checked.
		WebPage x = new WebPage(); //used to keep track of the current node checked.
		y = nil; //Parent of root is nil.
		x = getRoot(); //current node is the root.
		while (x != nil) //keep traversing the tree until you reach nil.
		{
			y = x; //set previous pointer to current x pointer.
			if(z.getRel() < x.getRel())
				x = x.getLeft(); //set current x pointer to its left child.
			else 
				x = x.getRight(); //set current x pointer to its right child.
		}
		z.setParent(y); //set the new nodes parent to y.
		if(y == nil) //if y is nil, then the tree is empty.
		{
			Storage.add(z);
			root = z;
		}
		else if(z.getRel() < y.getRel()) //if key < node key, set it as left child.
		{
			y.setLeft(z);
		}
		else
		{
			y.setRight(z); //set it as right child.
		}
		z.setLeft(nil); //Set children of leafs to nil, as required for RB Tree.
		z.setRight(nil); //Set children of leafs to nil, as required for RB Tree.
		z.setColor('R'); //Adding a new node must always be Red at the start.
		RBInsertFixUp(z); //Fix the tree since the insertion may have messed up the RB tree properties.
		pageRankCounter++; //Increment counter to reflect size of tree.
		resetPageRanks(getRoot()); //Redo the pageranks because of the new node.
	}
	
	/*
	 * This function is called at the end of insert to fix the RBTree and preserve the 5 properties.
	 * First, we determine if we need to do a left or right rotation.
	 * Furthermore, There are 3 cases to consider when fixing up the tree.
	 * Case 1: z's uncle y is red.
	 * Case 2: z's uncle y is black and z is a right child.
	 * Case 3: z's uncle y is black and z is a left child.
	 * This function takes at most lgn amount of time (if parent keeps being red), but most cases, it takes less than O(lgn) time.
	 */
	public void RBInsertFixUp(WebPage z)
	{
		WebPage y = new WebPage();
		while(z.getParent().getColor() == 'R') //Can't have 2 consecutive reds.
		{
			if (z.getParent() == z.getParent().getParent().getLeft()) //if the parent is a left child of its parent.
			{
				y = z.getParent().getParent().getRight(); //set y to be the right child of the grandparent (needed for rotation).
				if(y.getColor() == 'R') //Case 1
				{
					z.getParent().setColor('B');
					y.setColor('B');
					z.getParent().getParent().setColor('R');
					z = z.getParent().getParent();
				}
				else 
				{
					if(z == z.getParent().getRight()) //Case 2
					{
						z = z.getParent();
						LeftRotate(z);
					}
					 //Case 3
					z.getParent().setColor('B');
					z.getParent().getParent().setColor('R');
					RightRotate(z.getParent().getParent());
				}
			}
			else
			{
				if(z.getParent() == z.getParent().getParent().getRight()) //if the parent is the right child of its parent.
				{
					y = z.getParent().getParent().getLeft(); //set y to be the left child of the grandparent (used for rotation).
					if (y.getColor() == 'R') //Case 1
					{
						z.getParent().setColor('B');
						y.setColor('B');
						z.getParent().getParent().setColor('R');
						z = z.getParent().getParent();
					}
					else 
					{
						if(z == z.getParent().getLeft()) //Case 2
						{
							z = z.getParent();
							RightRotate(z);
						}
						//Case3
						z.getParent().setColor('B');
						z.getParent().getParent().setColor('R');
						LeftRotate(z.getParent().getParent());
					}
				}
			}
		}
		root.setColor('B'); //Root must always be black.
	}
	
	/*
	 * This function finds the minimum totalScore node in the tree. As a result, it will traverse all the way left.
	 * This function takes O(lgn) time on average, O(n) in worst case (if tree is entirely skewed left).
	 */
	public WebPage treeMinimum(WebPage x)
	{
		while(x.getLeft() != nil) //As long as the current node isn't null, traverse to the left child.
			x = x.getLeft();
		return x;
	}
	
	/*
	 * This function will replace a node with node v and its subtree. This is used in RBDelete.
	 * This function takes O(1) time.
	 */
	public void RBTransplant(WebPage u, WebPage v)
	{
		if(u.getParent() == nil) //If the node has no parent, it is a root. Therefore, we need to replace the root with v.
		{
			root = v;
		}
		else if(u == u.getParent().getLeft()) //If u is a left child, replace the left child of the parent with v.
		{
			u.getParent().setLeft(v);
		}
		else //if u is a right child, replace the right child of the parent with v.
			u.getParent().setRight(v);
		v.setParent(u.getParent()); //set v's parent to u's original parent.
	}
	
	/*
	 * This function removes a node from the RB Tree. It determines whether the node has children or not, and then appropriately removes the node
	 * based off 3 cases. However, it also keeps track of the color being deleted. If the color is black, then the black height of one side will be
	 * messed up, and the property violated. Therefore, we call delete fixup to fix the tree and retain the properties.
	 */
	public void RBDelete(WebPage z)
	{
		WebPage y = new WebPage();
		WebPage x = new WebPage();
		y = z; //set y to the node to be deleted.
		char yOriginalColor = y.getColor(); //keeps track of the color of the first y.
		if(z.getLeft() == nil) //if no left child, replace the node with the right child.
		{
			x = z.getRight();
			RBTransplant(z,z.getRight());
		}
		else if(z.getRight() == nil) // if no right child, replace the node with the left child.
		{
			x = z.getLeft();
			RBTransplant(z,z.getLeft());
		}
		else //find the minimum of the node z, and swap the two. then apply case 1 or 2 above.
		{
			y = treeMinimum(z.getRight()); //find minimum in subtree and set y to it.
			yOriginalColor = y.getColor();
			x = y.getRight();
			if(y.getParent() == z) //if y is a child of z, set the parent of y's child to y.
			{
				x.setParent(y);
			}
			else //do a transplant to replace y with its right child. then fix up the pointers.
			{
				RBTransplant(y,y.getRight());
				y.setRight(z.getRight());
				y.getRight().setParent(y);
			}
			RBTransplant(z,y);
			y.setLeft(z.getLeft());
			y.getLeft().setParent(y);
			y.setColor(z.getColor());
		}
		if(yOriginalColor == 'B') //if the node deleted was black, fixup the tree.
			RBDeleteFixup(x);
		pageRankCounter--; //reduce size of tree by 1, so we need to reduce total number of nodes assigned a pagerank by 1.
		resetPageRanks(root); //redo page rank values.
	}
	
	/*
	 * Called at the end of RBDelete, this function preserves the properties of the RBTree by rotations and recoloring.
	 * There are 4 cases to consider:
	 * Case 1: x's sibling w is red
	 * Case 2: x's sibling w is black, and both of w's children are black.
	 * Case 3: x's sibling w is black, w's left child is red, and w's right child is black.
	 * Case 4: x's sibling is black, w's right child is red.
	 * Depending on the case, we fix up the tree by recoloring and rotations.
	 * The while loop takes less than O(lgn) in most cases.
	 */
	public void RBDeleteFixup(WebPage x)
	{
		WebPage w = new WebPage();
		while(x != root & x.getColor() == 'B')
		{
			if(x == x.getParent().getLeft()) //If it is a left child
			{
				w = x.getParent().getRight(); //set w to be the right child.
				if(w.getColor() == 'R') //Case 1
				{
					w.setColor('B');
					x.getParent().setColor('R');
					LeftRotate(x.getParent());
					w = x.getParent().getRight();
				}
				if(w.getLeft().getColor() == 'B' && w.getRight().getColor() == 'B') //Case 2
				{
					w.setColor('R');
					x = x.getParent();
				}
				else
				{
					if(w.getRight().getColor() == 'B') //Case 3
					{
						w.getLeft().setColor('B'); 
						w.setColor('R');
						RightRotate(w);
						w = x.getParent().getRight();
					}
					//Case 4
					w.setColor(x.getParent().getColor());
					x.getParent().setColor('B');
					w.getRight().setColor('B');
					LeftRotate(x.getParent());
					x = root;
				}
			}
			else
			{
				w = x.getParent().getLeft();
				if(w.getColor() == 'R') //Case 1
				{
					w.setColor('B');
					x.getParent().setColor('R');
					RightRotate(x.getParent());
					w = x.getParent().getLeft();
				}
				if(w.getLeft().getColor() == 'B' && w.getRight().getColor() == 'B') //Case 2
				{
					w.setColor('R');
					x = x.getParent();
				}
				else 
				{
					if(w.getLeft().getColor() == 'B') //Case 3
					{
						w.getRight().setColor('B');
						w.setColor('R');
						LeftRotate(w);
						w = x.getParent().getLeft();
					}
					//Case 4
					w.setColor(x.getParent().getColor());
					x.getParent().setColor('B');
					w.getLeft().setColor('B');
					RightRotate(x.getParent());
					x = root;
				}
			}
		}
		x.setColor('B');
	}
	
	/*
	 * This function rotates a node to the left (counterclockwise). This is necessary if the right side of a subtree branches further than the left
	 * branch. The rotation then reduces the overall height of the tree to keep the self-balanced nature of the RB Tree.
	 * This takes O(1) time.
	 */
	public void LeftRotate(WebPage x)
	{
		WebPage y = new WebPage(); //Used to keep track of a node.
		y = x.getRight(); //y is a right child now. (keep track of original right child)
		x.setRight(y.getLeft()); //replace the right child of x with y's left child.
		if(y.getLeft() != nil) //if it has a left child,
		{
			y.getLeft().setParent(x); //set the parent of the left child to y's parent.
		}
		y.setParent(x.getParent()); //set y parent to x's parent.
		if(x.getParent() == nil) //if x has no parent, it is the root.
		{
			root = y; 
		}
		else if(x == x.getParent().getLeft()) //if it is a left child
		{
			x.getParent().setLeft(y);
		}
		else  //it is a right child.
		{
			x.getParent().setRight(y);
		}
		y.setLeft(x);
		x.setParent(y);
	}
	
	/*
	 * This function rotates a node to the right (clockwise). This is necessary if the left side of a subtree branches further than the right
	 * branch. The rotation then reduces the overall height of the tree to keep the self-balanced nature of the RB Tree.
	 * This takes O(1) time.
	 */
	public void RightRotate(WebPage x)
	{
		WebPage y = new WebPage(); //Used to keep track of a node.
		y = x.getLeft(); //y is a left child now. (keep track of original left child)
		x.setLeft(y.getRight()); //replace the left child of x with y's right child.
		if(y.getRight() != nil) //if it has a right child,
		{
			y.getRight().setParent(x); //set the parent of the right child to y's parent.
		}
		y.setParent(x.getParent()); //set y parent to x's parent.
		if(x.getParent() == nil) //if x has no parent, it is the root.
		{
			root = y;
		}
		else if(x == x.getParent().getRight()) //if it is a right child
		{
			x.getParent().setRight(y);
		}
		else //it is a left child.
		{
			x.getParent().setLeft(y);
		}
		y.setRight(x);
		x.setParent(y);
	}
	
	public void drawTreeHorizontal(int i, WebPage p)
	{
		{
		   if(p == nil)
			   return;
		   
		   
		   drawTreeHorizontal(i + 20, p.getRight());
		   for(int j = 0; j < i; j++)
		   {
			   System.out.print(" ");
		   }
		   if(p.getRight() == nil)
			   System.out.print(p.getRel() + "" + p.getColor());
		   else
		   	{
			   System.out.print(p.getRel() + "" + p.getColor());
		   	}
		   System.out.print("\n");
		   drawTreeHorizontal(i + 20, p.getLeft());
		}
	}
	
	/*
	 * This function will display the tree vertically. While this function is long, 
	 * most of it is to determine the number of blank spaces. Nil nodes are not presented in this display.
	 */
	public void drawTree()
	{
		int lev = 0; //used to determine the level of a node
		int lev2 = 0; //used for the max arrayLst to find the max level.
		int maxlevel = 0; //holds the max level of the tree.
		int levelcounter = 0; //used for printing spaces.
		ArrayList<WebPage> nodes = new ArrayList<WebPage>(); //used to hold the nodes of the tree.
		ArrayList<Integer> level = new ArrayList<Integer>(); //used to hold the level of each node.
		ArrayList<Integer> max = new ArrayList<Integer>(); //used to find the max level in the tree.
		findMax(getRoot(), max, lev2); //records the level of all nodes.
		for(int i = 0; i < max.size(); i++)
		{
			//finds the max level in the tree.
			if(max.get(i) > maxlevel)
			{
				maxlevel = max.get(i);
			}
		}
		//this function traverses the tree and stores the nodes and its levels.
		TraverseTree(getRoot(), nodes, level, lev, maxlevel);
		levelcounter = maxlevel;
		//The rest of this function determines the spacing and when to print the content.
		for(int j = 0; j < maxlevel; j++)
		{
			for(int k = 0; k < Math.pow(2, levelcounter); k++)
			{
				System.out.print(" ");
			}
			levelcounter = levelcounter - 1;
			for(int z = 0; z < nodes.size(); z++)
			{
				if(level.get(z) == j)
				{
					if(nodes.get(z) != nil)
					{
						System.out.print(nodes.get(z).getRel() + "" + nodes.get(z).getColor());	
						for(int k = 0; k < Math.pow(2,  levelcounter+2)-3; k++)
						{
							System.out.print(" ");
						}
					}
					else
					{
						for(int k = 0; k < Math.pow(2, levelcounter + 2); k++)
						{
							System.out.print(" ");
						}
					}			
				}
			}
			System.out.print("\n");
		}
	}
	
	/*
	 * This function will record the level of all nodes in the tree. Stops at nil.
	 */
	public void findMax(WebPage x, ArrayList<Integer> a1, int level)
	{
		a1.add(level);
		if(x != nil)
		{
			findMax(x.getLeft(), a1, level + 1); //Traverse left first
			findMax(x.getRight(), a1, level + 1); //Traverse right after
		}
	}
	
	/*
	 * This function will record the level of all nodes and all of the nodes.
	 */
	public void TraverseTree(WebPage x, ArrayList<WebPage> a1, ArrayList<Integer> a2, int level, int maxlevel)
	{
		a1.add(x);
		a2.add(level);
		if(level < maxlevel) //retrieve 2^max level - 1 nodes.
		{
			TraverseTree(x.getLeft(), a1, a2, level + 1, maxlevel); //Traverse left first
			TraverseTree(x.getRight(), a1, a2, level + 1, maxlevel); //Traverse right after
		}
	}
}
