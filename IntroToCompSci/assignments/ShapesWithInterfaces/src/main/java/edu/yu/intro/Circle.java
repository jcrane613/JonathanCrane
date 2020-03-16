package edu.yu.intro;
import java.lang.Math;
public class Circle implements Shape
{
  private double radius;
  Circle (double radiusForCircle)
  {
    if (radiusForCircle < 0)
    {
      throw new IllegalArgumentException("Error, radius was negative");
    }
    this.radius = radiusForCircle;
  }
  @Override
  public double area()
  {
    double area = Math.PI * (radius * radius);
    return area;
  }
  @Override
  public double perimeter()
  {
    double perimeter = 2 * Math.PI * radius;
    return perimeter;
  }
}
