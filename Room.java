import java.awt.*;

public class Room 
{
  private int number;
  private double xPos;
  private double yPos;
  private double doorX;
  private double doorY;
  private double height;
  private double width;
  private double doorWidth;
  private double doorHeight;
  private Canvas theCanvas;
  private int nearNode;
   
  public Room(int numberPass, double xPosPass, double yPosPass, double doorXPass, double doorYPass, 
               double heightPass, double widthPass, double doorWidthPass, double doorHeightPass, Canvas canvasPass, int nearNodePass)
  {
    number = numberPass;
    xPos = xPosPass;
    yPos = yPosPass;
    doorX = doorXPass;
    doorY = doorYPass;
    height = heightPass;
    width = widthPass;
    doorWidth = doorWidthPass;
    doorHeight = doorHeightPass;
    theCanvas = canvasPass;
    nearNode = nearNodePass;
  }
  
  public void drawRoom()
  {
    theCanvas.setInkColor(Color.black);
    theCanvas.drawRectangle((int)xPos, (int)yPos, (int)height, (int)width);
    theCanvas.setInkColor(Color.blue);
    theCanvas.drawRectangle((int)doorX, (int)doorY, (int)doorWidth, (int)doorHeight);
    theCanvas.setInkColor(Color.black);
    theCanvas.drawString("" + number, (int)(xPos + 5), (int)(yPos + 20));
  }
  
  public int getDoorX()
  {
    return (int)doorX;
  }
  
  public int getDoorY()
  {
    return (int)doorY;
  }
  
  public int getNearNode()
  {
    return nearNode;
  }
}







