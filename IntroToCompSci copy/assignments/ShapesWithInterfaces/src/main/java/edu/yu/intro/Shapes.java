package edu.yu.intro;
import java.lang.Math;
import edu.yu.intro.Shape;
import edu.yu.intro.Polygon;
public class Shapes
{
  public static Shape newCircle(final double radius)
  {
    Shape circle = new Circle(radius);
    return circle;
  }
  public static Shape newRectangle(final double width, final double height)
  {
    Shape rectangle = new Rectangle(width, height);
    return rectangle;
  }
  public static Shape newTriangle(final double s1, final double s2, final double s3)
  {
    Shape triangle = new Triangle(s1, s2, s3);
    return triangle;
  }
}
