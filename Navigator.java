/* This program draws a map of a building and then finds the shortest path between two rooms
 * provided by the user.  It accomplishes this by filling the halls with nodes and searching 
 * down all possible paths from the starting room and the destination room one step at a time
 * until the paths connect.  The searched paths are shown on the map in green.  Once cantact is
 * made the connecting path is backtracked and highlighted in magenta.  
 * 
 * My main goal was to develop the step method to search outwards one node at a time and 
 * backTrack method to trace the shortest path between two rooms by recusivly going backward 
 * through all of the calls to the step function. 
 * 
 * Somethings shortcomings of this program that could be fun to fix are that the node adjecency 
 * array and nearest node to each room are hard coded so if the map changes this would need to be 
 * manually updated and the room numbers are identical to their position in the array, code would 
 * need to be added to search for a room in the array if the numbers were not aligned.
 *
 * ... and I should have made it bigger :( */

import java.util.Scanner;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class Navigator
{
  public static void main(String[] args)
  {
    boolean runSlow = true;  //this controls some sleeps that make it easier to see how the program works
    boolean consoleOut = true; //this controlls some print statements that show what happens in each step.
    int stepCount = 0;
    
    // this ArrayList keeps track of which nodes have been visited during each step
    ArrayList<Integer> visited = new ArrayList<Integer>(25);
    // this ArrayList keeps track of which nodes have been connected during the backTrack phase
    ArrayList<Integer> connected = new ArrayList<Integer>(25);
    Hall[] h = new Hall[7]; //all Halls are created as indices in this array
    Room[] r = new Room[23]; //all Rooms are created as indices in this array
    Node[] n = new Node[51]; //all Rooms are created as indices in this array
     
    //the draw map function creates the map and return the canvas so that the path can be drawn on it by other functions
    Canvas myCanvas = drawMap(h, r, n, consoleOut);
    
    int currentRoom = -1;
    int destination = -1;
      
    while (currentRoom < 0 || currentRoom > 22 || destination < 0 || destination > 22)
    {
      //get current room number and destination, pass this data to the step function to start the search
      System.out.println("Rooms are numbered 1-22. Enter 0 if you are at the entrance.");
      System.out.print("What room number are you at?");
      Scanner getRoomNumber = new Scanner(System.in);
      currentRoom = getRoomNumber.nextInt();
      System.out.print("What room number would are you going to?");
      destination = getRoomNumber.nextInt();
      if (currentRoom < 0 || currentRoom > 22 || destination < 0 || destination > 22)
      {
        System.out.println("Room numbers out of range.  Try again.\n");
      }
    }
    
    // start and finish contain the node nearest the door of each selected room.  The nearNode has been hardcoded into 
    // each room object.  These are both created as int arrays because as the search progress we will have multiple nodes 
    // that we are searching from on both the forward and backward searching sides.
    int[] start = {r[currentRoom].getNearNode()}; //the room numbers are the same as the index of each room
    int[] finish = {r[destination].getNearNode()};
    if(consoleOut) { System.out.println("Start node: " + start[0]);
                     System.out.println("Start node: " + finish[0]);}
    
    //draw a line from the door of the selected rooms to the node nearest to the door.
    myCanvas.setInkColor(Color.green);
    myCanvas.drawLine(r[currentRoom].getDoorX(), r[currentRoom].getDoorY(), n[start[0]].getNodeX(), n[start[0]].getNodeY());
    myCanvas.drawLine(r[destination].getDoorX(), r[destination].getDoorY(), n[finish[0]].getNodeX(), n[finish[0]].getNodeY());
    
    // take the first step out from the start and finish nodes.  This step will go to all nodes adjecent 
    // to the start and finish.
    step(start, finish, visited, connected, n, myCanvas, runSlow, consoleOut, stepCount);
    if (consoleOut) {System.out.println("Backtracking complete.");}
    System.out.println("Path has been drawn.");
  }
  
  public static int[] step(int[] fwdCurr, int[]backCurr, ArrayList<Integer> visited, ArrayList<Integer> connected, Node[] n, Canvas myCanvas, 
                             Boolean runSlow, boolean consoleOut, int stepCount)
  {
    /* The step method is the primary mechanism of this program.  There are forward nodes which begin at the start node, stored in fwdCurr, 
     * and there are backwards node which staet at the finish and work backwards. In each step first we check if the any of the forward nodes
     * are the same as or one away from any of the backwaards nodes.  If this is the case we have found the shortest path between the start
     * and finish nodes and we start the backTrack method.  If the forward searching nodes and the backward searching nodes have not met then 
     * all of the current nodes (fwdCurr and backCurr) progress one step further away from where they started.  The visited ArrayList keeps 
     * track of the where we have been so we never visit the same node twice and each call to step creates an copy of the visited array at that step
     * which is used during the backtracking process.  The node adjancency array tells us what nodes we can progress to.
     * If the path was not found step calls for the next step.  Once the path is found the steps complete recursively and each returns the 
     * forward and back nodes from that step that were part of the path and connects them with the nodes adjacent nodes visited during that step 
     * via the backTrack method.
     * */
    if (consoleOut) {
        stepCount++;
        System.out.print ("Step " + stepCount + "\nForward nodes visited: ");
        for (int i=0; i < fwdCurr.length; i++)
        {System.out.print(fwdCurr[i]+ ", ");}
        System.out.print ("\nBackward nodes visited: ");
        for (int j =0; j < backCurr.length; j++)
        {System.out.print(backCurr[j] + ", ");}
        System.out.println("");
      }
    
    //if one of the nodes adjecent to the fwdCurr nodes is in the backCurr array fwdConnect gets the number of that node.  Otherwise it gets -1.
    int fwdConnect = isAdjInArray(fwdCurr, backCurr, n)[0];
    //if one of the nodes in the fwdCurr array is also in the backCurr array fwdBackSameNode gets that nodes number.  Otherwise it gets -1.
    int fwdBackSameNode = fwdIsInBack(fwdCurr, backCurr);
    if( fwdConnect != -1)
    {
      // Path found beacause a back node was adjcent to a forward node so connect the back and the adjacent nodes and start the backtrack 
      //process to draw the shortest path and return the nodes most recently added to the path.
      int backConnect = isAdjInArray(fwdCurr, backCurr, n)[1];
      if (consoleOut) {System.out.println("Path found! Forward node " + fwdConnect + " is adjacent to node " + backConnect);}
      myCanvas.setInkColor(Color.magenta);
      myCanvas.drawLine(n[fwdConnect].getNodeX(), n[fwdConnect].getNodeY(), n[backConnect].getNodeX(), n[backConnect].getNodeY());
      visited.add(fwdConnect);
      connected.add(fwdConnect);
      connected.add(backConnect);
      int[] returnArray = backTrack(backConnect, fwdConnect, visited, connected, n, myCanvas, runSlow, consoleOut, stepCount);
      return returnArray;
    }
    else if(fwdBackSameNode != -1)
    {
      //Path found beacause one of the forward nodes was the same as one of the back nodes start the backtrack 
      //process to draw the shortest path and return the nodes most recently added to the path.
      if (consoleOut) {System.out.println("Path found! Forward and backward nodes connected at node " + fwdBackSameNode);} 
      visited.add(fwdBackSameNode);
      connected.add(fwdBackSameNode); 
      int[] returnArray = backTrack(fwdBackSameNode, fwdBackSameNode, visited, connected, n, myCanvas, runSlow, consoleOut, stepCount);
      return returnArray;
    }
    else
    {
      /* Add the current forward moving nodes and the current backward moving nodes to the list of visited nodes, 
       * create the next arrays of forward and back moving nodes and call the function with these in order to 
       * advance the nodes*/
      
      ArrayList<Integer> nextFwd = new ArrayList<Integer>(25);
      ArrayList<Integer> nextBack = new ArrayList<Integer>(25);
      myCanvas.setInkColor(Color.green);
      
      /* This loop goes through the array of forward moving nodes and adds them to the visited array. The for each node 
       * adjacent to a forward moving node it first checks if that node has already been visited and if not that node gets
       * added to the nextFwd array which will be the fwnCurr array for the next step */
      for(int i = 0; i < fwdCurr.length; i++) 
      {
        visited.add(fwdCurr[i]);
        int[] adjArr = n[fwdCurr[i]].getAdjNodes(fwdCurr[i]); //puts nodes adjacent to the current forward moving node in an array
        for(int j = 0; j < adjArr.length; j++)
        {
          if (!itIsIn(adjArr[j], visited))
          {
            nextFwd.add(adjArr[j]);
            myCanvas.drawLine(n[fwdCurr[i]].getNodeX(), n[fwdCurr[i]].getNodeY(), n[adjArr[j]].getNodeX(), n[adjArr[j]].getNodeY());
            if (runSlow) { try{TimeUnit.MILLISECONDS.sleep(300);}catch(InterruptedException e){} }
          } 
        }
      }
      // This loop does the same as the one above for the array of backward searching nodes.
      for(int i = 0; i < backCurr.length; i++)
      {
        visited.add(backCurr[i]);
        int[] adjArr = n[backCurr[i]].getAdjNodes(backCurr[i]); //puts nodes adjacent to the current forward moving node in an array
        for(int j = 0; j < adjArr.length; j++)
        {
          if (!itIsIn(adjArr[j], visited))
          {
            nextBack.add(adjArr[j]);
            myCanvas.drawLine(n[backCurr[i]].getNodeX(), n[backCurr[i]].getNodeY(), n[adjArr[j]].getNodeX(), n[adjArr[j]].getNodeY());
            if (runSlow) { try{TimeUnit.MILLISECONDS.sleep(300);}catch(InterruptedException e){} }
          } 
        }
      }
      //System.out.println("step " + localCounter + "visited size = " + visited.size());
      
      ArrayList<Integer> localVisited; // Local visited preserves the visited nodes for this step to be used in the backTrack method.
      localVisited = new ArrayList<Integer>(visited);
      int[] fwdPass = convertIntegers(nextFwd); // ArrayLists had to be converted to arrays in order to pass them.
      int[] backPass = convertIntegers(nextBack);
      int[] returnArray = step(fwdPass, backPass, visited, connected, n, myCanvas, runSlow, consoleOut, stepCount);
      //System.out.println("step " + localCounter + "visited size = " + localVisited.size());
      //System.out.println("connected size = " + connected.size());
      /* backTrack takes the two nodes returned by the final step connects them to the adjacent visited nodes and returns the 
       * newly connected nodes to the previous step*/
      int[] nextReturn = backTrack(returnArray[1], returnArray[0], localVisited, connected, n, myCanvas, runSlow, consoleOut, stepCount);
      return nextReturn;
    }
  }
    
  public static int[] backTrack(int backNode, int fwdNode, ArrayList<Integer> visited, ArrayList<Integer> connected, Node[] n, Canvas myCanvas, 
                                Boolean runSlow, boolean consoleOut, int stepCount)
  {
    int[] fwdAdjArray = n[fwdNode].getAdjNodes(fwdNode);
    int[] backAdjArray = n[backNode].getAdjNodes(backNode);
    int[] returnArray = {-1,-2};
    
    
    if (consoleOut) {System.out.println(" Back Tracking step " + stepCount + ":");}
    
    for(int i =0; i < fwdAdjArray.length; i++)
    {
      if(itIsIn(fwdAdjArray[i], visited) && !itIsIn(fwdAdjArray[i], connected) && returnArray[0]==-1)
      {
        
        myCanvas.setInkColor(Color.magenta);
        myCanvas.drawLine(n[fwdNode].getNodeX(), n[fwdNode].getNodeY(), n[fwdAdjArray[i]].getNodeX(), n[fwdAdjArray[i]].getNodeY());
        myCanvas.drawOval(n[fwdNode].getNodeX()-1, n[fwdNode].getNodeY()-1, 4, 4);
        myCanvas.drawOval(n[fwdAdjArray[i]].getNodeX()-1, n[fwdAdjArray[i]].getNodeY()-1, 4, 4);
        if (runSlow) { try{TimeUnit.MILLISECONDS.sleep(300);}catch(InterruptedException e){} }
        if (consoleOut) {System.out.println("Connecting forward node " + fwdNode + " back to node " + fwdAdjArray[i]);}
        connected.add(fwdAdjArray[i]);
        returnArray[0] = fwdAdjArray[i];
      }
    }
    for(int i =0; i < backAdjArray.length; i++)
    {
      if(itIsIn(backAdjArray[i], visited) && !itIsIn(backAdjArray[i], connected) && returnArray[1]==-2)
      {
        myCanvas.setInkColor(Color.magenta);
        myCanvas.drawLine(n[backNode].getNodeX(), n[backNode].getNodeY(), n[backAdjArray[i]].getNodeX(), n[backAdjArray[i]].getNodeY());
        myCanvas.drawOval(n[backAdjArray[i]].getNodeX()-1, n[backAdjArray[i]].getNodeY()-1, 4, 4);
        myCanvas.drawOval(n[backNode].getNodeX()-1, n[backNode].getNodeY()-1, 4, 4);
        if (runSlow) { try{TimeUnit.MILLISECONDS.sleep(300);}catch(InterruptedException e){} }
        if (consoleOut) {System.out.println("Connecting backward node " + backNode + " back to node " + backAdjArray[i]);}
        connected.add(backAdjArray[i]);
        returnArray[1] = backAdjArray[i];
      }
    }
    return returnArray;
  }
  
  /* Checks if the nodes adjacent to the forward node are in the backward array.  
  * If adjecent nodes are found it returns an array of the nodes that are adjacent 
  * back to the step method to be connected. Otherwise it returns {-1,-1}*/
  public static int[] isAdjInArray(int [] stepNodes, int[] checkNodes, Node[] n)
  {
    int[] returnVals = {-1, -1};
    for(int i=0; i < stepNodes.length; i++)
    {
      int node = stepNodes[i];
      int[] adjs = n[node].getAdjNodes(node);
      for (int j=0; j < adjs.length; j++)
      {
          for( int k=0; k < checkNodes.length; k++)
        {
            if (checkNodes[k] == adjs[j])
            {
              returnVals[0] = stepNodes[i];
              returnVals[1] = checkNodes[k];
              return returnVals; //returns the forward and backward nodes that are adjacent
            }
        }
      }
    }
    return returnVals;
  }
  
  /* If a forward node and a back node are the same this method returns that common node 
   * else it returns -1*/
  public static int fwdIsInBack(int[] fwdCurr, int[] backCurr)
  {
    for(int i = 0; i < fwdCurr.length; i++)
    {
      for(int j =0; j < backCurr.length; j++)
      {
        if (fwdCurr[i] == backCurr[j])
        {
          return fwdCurr[i];
        }
      }
    } 
    return -1;
  }
 
  /* checks if a given value is in an ArrayList in this program this is used mainly
   * to check if an adj node has already been visited so that we don't get to many nodes running around*/
  public static boolean itIsIn(int it, ArrayList<Integer> arrayList) 
  { 
    for(int i = 0; i < arrayList.size(); i++)
    {
      int check = arrayList.get(i);
      if(it == check)
      {
        return true;
      }
    }
    return false;
  }
  
  //Converts an ArrayList of integers to an array.
  public static int[] convertIntegers(ArrayList<Integer> integers)
  {
    int[] ret = new int[integers.size()];
    for (int i=0; i < ret.length; i++)
    {
        ret[i] = integers.get(i).intValue();
    }
    return ret;
  }
  
  /* this method draws the initial map.  All map objects are created in arrays to allow searching through them*/ 
  public static Canvas drawMap(Hall[] h, Room[] r, Node[] n, boolean consoleOut)
  {
    int windowWidth = 500;
    int windowHeight = 500;
    double doorWidth = 10;
    double doorHeight = 2;
    
    Canvas myCanvas = new Canvas("A Map");
    myCanvas.setSize(windowWidth,windowHeight);
    myCanvas.setVisible(true);
    
     //Create the hallways in an array of Hall objects
    
    //Hall parameters
    //Hall (xPos, yPos, width, height, Canvas)
    h[0] = new Hall(110, 110, 280, 20, myCanvas);
    h[1] = new Hall(110, 130, 20, 100, myCanvas);
    h[2] = new Hall(196, 130, 20, 100, myCanvas);
    h[3] = new Hall(283, 130, 20, 100, myCanvas);
    h[4] = new Hall(370, 130, 20, 100, myCanvas);
    h[5] = new Hall(110, 230, 280, 20, myCanvas);
    h[6] = new Hall(230, 250, 20, 60, myCanvas);
    
    //Draw all halls
    for (int i = 0; i < h.length; i++)
    {
      h[i].drawHall();
    }
    
    //Create all the rooms in an array of rooms
    
    
    // Room parameters
    // Room(num, Xpos, Ypos, doorx, doorY, width, height, doorWidth, doorHieght, Canvas, nearestNode)
    
    r[0] = new Room(0, 230, 310, 231, 311, 20, 3, 18, 1, myCanvas, 50);//***Entrance***//
    r[1]= new Room(1, 50, 50, 115, 110, 80, 60, doorWidth, doorHeight, myCanvas, 0);
    r[2] = new Room(2, 130, 50, 140, 110, 60, 60, doorWidth, doorHeight, myCanvas, 1);
    r[3] = new Room(3, 190, 50, 200, 110, 60, 60, doorWidth, doorHeight, myCanvas, 4);
    r[4] = new Room(4, 250, 50, 260, 110, 60, 60, doorWidth, doorHeight, myCanvas, 7);
    r[5] = new Room(5, 310, 50, 320, 110, 60, 60, doorWidth, doorHeight, myCanvas, 10);
    r[6] = new Room(6, 370, 50, 375, 110, 80, 60, doorWidth, doorHeight, myCanvas, 13);
    r[7] = new Room(7, 50, 110, 110, 150, 60, 70, doorHeight, doorWidth, myCanvas, 15);
    r[8] = new Room(8, 130, 130, 150, 130, 66, 50, doorWidth, doorHeight, myCanvas, 2);
    r[9] = new Room(9, 216, 130, 230, 130, 66, 50, doorWidth, doorHeight, myCanvas, 6);
    r[10] = new Room(10, 303, 130, 315, 130, 66, 50, doorWidth, doorHeight, myCanvas, 10);
    r[11] = new Room(11, 390, 110, 390, 150, 60, 70, doorHeight, doorWidth, myCanvas, 30);
    r[12] = new Room(12, 50, 180, 110, 200, 60, 70, doorHeight, doorWidth, myCanvas, 17);
    r[13] = new Room(13, 130, 180, 150, 230, 66, 50, doorWidth, doorHeight, myCanvas, 35);
    r[14] = new Room(14, 216, 180, 230, 230, 66, 50, doorWidth, doorHeight, myCanvas, 39);
    r[15] = new Room(15, 303, 180, 315, 230, 66, 50, doorWidth, doorHeight, myCanvas, 44);
    r[16] = new Room(16, 390, 180, 390, 200, 60, 70, doorHeight, doorWidth, myCanvas, 32);
    r[17] = new Room(17, 50, 250, 115, 250, 80, 60, doorWidth, doorHeight, myCanvas, 34);
    r[18] = new Room(18, 130, 250, 140, 250, 60, 60, doorWidth, doorHeight, myCanvas, 35);
    r[19] = new Room(19, 190, 250, 230, 270, 40, 60, doorHeight, doorWidth, myCanvas, 49);
    r[20] = new Room(20, 250, 250, 260, 250, 60, 60, doorWidth, doorHeight, myCanvas, 41);
    r[21] = new Room(21, 310, 250, 320, 250, 60, 60, doorWidth, doorHeight, myCanvas, 44);
    r[22] = new Room(22, 370, 250, 375, 250, 80, 60, doorWidth, doorHeight, myCanvas, 47);
    
    //draw all the rooms
    for(int i = 0; i < r.length; i++) 
    {
      r[i].drawRoom();
    }
    
    //Create all nodes in an array of node objects
    //Nodes are spaced 20 pixels apart and have access to an array which lists 
    //adjacent nodes for each node.
    

    int nodeCounter = 0;
   
    for (int i = 0; i < h.length; i++)
    {
      int x = h[i].getXPos();
      int y = h[i].getYPos();
      int height = h[i].getHeight();
      int width = h[i].getWidth();
      
      if (height == 20)
      {
        width += x;
        x += 10;
        y += 10;
        while (x < width)
        {
          n[nodeCounter] = new Node(x, y, nodeCounter);
          myCanvas.drawOval(x, y, 2, 2);
          if (consoleOut) {myCanvas.drawString("" + nodeCounter, x, y);} 
          x += 20;
          nodeCounter++;
        }

      }
      if (width == 20)
      {
        height += y;
        y += 10;
        x += 10;
        while (y < height)
        {
          n[nodeCounter] = new Node(x, y, nodeCounter);
          if(consoleOut) {myCanvas.drawString("" + nodeCounter, x, y);}
          myCanvas.drawOval(x, y, 2, 2);
          y += 20;
          nodeCounter++;
        }
      }
    }
    return myCanvas;
  }
}