import java.awt.*;

public class Hall
{
  private int height;
  private int width;
  private int xPos;
  private int yPos;
  private Canvas theCanvas;
  
  public Hall (int xPosPass, int yPosPass, int widthPass, int heightPass, Canvas theCanvasPass)
  {
    height = heightPass;
    width = widthPass;
    xPos = xPosPass;
    yPos = yPosPass;
    theCanvas = theCanvasPass;
  }
  
  public void drawHall()
  {
    theCanvas.setInkColor(Color.lightGray);
    theCanvas.fillRectangle(xPos, yPos, width, height);
  }
  
  public int getXPos()
  {
    //System.out.print("x = " + xPos);
    return xPos;
  }
  
  public int getYPos()
  {
    //System.out.println(", y = " + yPos);
    return yPos;
    
  }
  
  public int getHeight()
  {
    return height;
  }
  
  public int getWidth()
  {
    return width;
  }
}