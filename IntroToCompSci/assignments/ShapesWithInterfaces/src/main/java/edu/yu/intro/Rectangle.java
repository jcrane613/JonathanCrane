package edu.yu.intro;

public class Rectangle implements Shape,Polygon
{
  private double width;
  private double height;
  Rectangle(double widthForRectangle, double heightForRectangle)
  {
    if (widthForRectangle < 0)
    {
      throw new IllegalArgumentException("Error, width was negative");
    }
    this.width = widthForRectangle;
    if (heightForRectangle < 0)
    {
      throw new IllegalArgumentException("Error, height was negative");
    }
    this.height = heightForRectangle;
  }
  @Override
  public double area()
  {
    double area = height * width;
    return area;
  }
  @Override
  public double perimeter()
  {
    double perimeter = 2 * (height + width);
    return perimeter;
  }
  @Override
  public int nSides()
  {
    return 4;
  }
}
