# Programming-Assignment-3
Internal Process (But with an RB Tree this time)

Search Engine Internal Process:
1. Based on scores of 30 URLs that you retrieved from your web crawler, use Red-Black Tree
to manipulate the data. The following section lists the programming requirements for RB
Tree. (MUST Index, total score, PageRank, color, and URL. If not, your will lose 50 points.)

Programming Requirements
1. The Google Search Engine Internal Process MUST be written in Java and it is required to use
pseudo codes in the textbook. (Please be noted that you MUST use the pseudo codes
provided in textbook to write your Java codes. Any other codes will be treated as failing
requirements and will receive 0 points.)
2. You need to use a Web Crawler to enter keywords and then each keyword will receive 30
URLs for further Google Internal Process. (You will receive 0 if you hardcode 30 URLs in
your program/code.)
3. You must follow the four PageRank factors from your PA1 to calculate scores for PageRank.
4. Your simulation application MUST at least contain the following functions for URL RB
Tress:
a) Build up a URL RB Tree. The color property of each node must be presented.
(MUST follow RB Tree properties specified in textbook and ppt slides. Your own
tree structure will not be accepted.) (A better way to verify if your URL RB tree is
correct or not, just draw the RB tree.)
b) Allow users to insert any URL to the RB Tree based on its total score. (Must list the
URLs showing that the node has been inserted into RB Tree and must draw the RB
tree to show the result of the color property changes of URL nodes) (MUST show
Index, total score, PageRank, color, and URL. If not, your will lose 50 points.)
c) Allow users to search a URL by entering a PageRank or total score.
d) Allow users to delete any URL from the URL RB Tree by entering a PageRank or
total score. (Must list the URLs showing that the node has been deleted from the RB
Tree and must draw the RB tree to show the result of the color property changes of
URL nodes) (MUST show Index, total score, PageRank, color, and URL. If not, your
will lose 50 points.)
e) Allow users to make a sorted list of URLs based on the total score (MUST show
Index, total score, PageRank, color, and URL. If not, your will lose 50 points.)
5. Each java file/class/subroutine/function call must contain a header comment in the beginning
of it. (You will lose 20 points for not putting comments in your program.)
