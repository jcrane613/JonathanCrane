package edu.yu.intro;
import java.lang.Math;
public class Triangle implements Shape, Polygon
{
  private double side1;
  private double side2;
  private double side3;
  Triangle(double s1, double s2, double s3)
  {
    if (s1 < 0)
    {
      throw new IllegalArgumentException("Error, side 1 was negative");
    }
    if (s2 < 0)
    {
      throw new IllegalArgumentException("Error, side 2 was negative");
    }
    if (s3 < 0)
    {
      throw new IllegalArgumentException("Error, side 3 was negative");
    }
    this.side1 = s1;
    this.side2 = s2;
    this.side3 = s3;
  }
  @Override
  public double area()
  {
    double s = ((side1 + side2 + side3)/2.0d);
    double x = (s * (s-side1) * (s-side2) * (s-side3));
    double Area= Math.sqrt(x);
    return Area;
  }
  @Override
  public double perimeter()
  {
    double perimeter = side1 + side2 + side3;;
    return perimeter;
  }
  @Override
  public int nSides()
  {
    return 3;
  }
}
