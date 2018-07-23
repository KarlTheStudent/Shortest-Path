public class Node 
{
  private int xPos;
  private int yPos;
  private int index;
  
  public Node(int x, int y, int nc)
  {
    xPos = x;
    yPos = y;
    index = nc;
    
  }
  public int getNodeX()
  {
    return xPos;
  }
  
  public int getNodeY()
  {
    return yPos;
  }
  
  // In this 2D array the second teir array at each indicies contains the nodes which are adjacent to the node with the
  // corrosponding index.  The node numbers are commented next to each adacency array.
  public int[] getAdjNodes(int i)
  {
    int adjArray [][] = {
      {1,14},//0
      {0,2},//1
      {1,3},//2
      {2,4},//3
      {3,5,19},//4
      {4,6},//5
      {5,7},//6
      {6,8},//7
      {7,9},//8
      {8,10,24},//9
      {9,11},//10
      {10,12},//11
      {11,13},//12
      {12,29},//13
      {0,15},//14
      {14,16},//15
      {15,17},//16
      {16,18},//17
      {17,34},//18
      {4,20},//19
      {19,21},//20
      {20,22},//21
      {21,23},//22
      {22,38},//23
      {9,25},//24
      {24,26},//25
      {25,27},//26
      {26,28},//27
      {27,43},//28
      {13,30},//29
      {29,31},//30
      {30,32},//31
      {31,33},//32
      {32,47},//33
      {18,35},//34
      {34,36},//35
      {35,37},//36
      {36,38},//37
      {23,37,39},//38
      {38,40},//39
      {39,41,48},//40
      {40,42},//41
      {41,43},//42
      {28,42,44},//43
      {43,45},//44
      {44,46},//45
      {45,47},//46
      {33,46},//47
      {40,49},//48
      {48,50},//49
      {49}//50
    };
  return adjArray[i];}
}