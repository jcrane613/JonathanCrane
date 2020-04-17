package edu.yu.intro.test;
import edu.yu.intro.Shapes;
import edu.yu.intro.Shape;
import edu.yu.intro.Polygon;
import edu.yu.intro.Triangle;
import edu.yu.intro.Circle;
import edu.yu.intro.Rectangle;
import org.junit.*;
import static org.junit.Assert.*;
public class ShapesWithInterfacesTest
{
  @Test
    public void CircleArea()
    {
      Shape circle = Shapes.newCircle(2.6);
      assertEquals(21.24,circle.area(),.01);
    }
  @Test
    public void CirclePerimeter()
    {
      Shape circle = Shapes.newCircle(2.6);
      assertEquals(16.34,circle.perimeter(),.01);
    }
  @Test (expected =IllegalArgumentException.class)
    public void CircleInvalid()
    {
      Shape circle = Shapes.newCircle(-3.2);
    }
  @Test
    public void RectangleArea()
    {
      Shape rectangle = Shapes.newRectangle(2.6, 3.7);
      assertEquals(9.62,rectangle.area(),.01);
    }
  @Test
    public void RectanglePerimeter()
    {
      Shape rectangle = Shapes.newRectangle(2.6, 3.7);
      assertEquals(12.6,rectangle.perimeter(),.01);
    }
  @Test (expected =IllegalArgumentException.class)
    public void RectangleNegativeWidth()
    {
      Shape rectangle = Shapes.newRectangle(2.6,-17);
    }
  @Test (expected =IllegalArgumentException.class)
    public void RectangleNegativeHeight()
    {
      Shape rectangle = Shapes.newRectangle(-17,2.6);
    }
  @Test
    public void RectangleSides()
    {
      Rectangle rectangle = (Rectangle)Shapes.newRectangle(17,12.6);
      assertEquals("N sides",4, rectangle.nSides());
    }
  @Test
    public void TriangleArea()
    {
      Shape triangle = Shapes.newTriangle(15.4, 13.6, 3.4);
      assertEquals(20.768,triangle.area(),.01);
    }
  @Test
    public void TrianglePerimeter()
    {
      Shape triangle = Shapes.newTriangle(9.4, 5.2, 7.9);
      assertEquals(22.5,triangle.perimeter(),.01);
    }
  @Test (expected =IllegalArgumentException.class)
    public void TriangleNegativeFirst()
    {
      Shape triangle = Shapes.newTriangle(-17,12.6,23);
    }
  @Test (expected =IllegalArgumentException.class)
    public void TriangleNegativeSecond()
    {
      Shape triangle = Shapes.newTriangle(17,-12.6,23);
    }
  @Test (expected =IllegalArgumentException.class)
    public void TriangleNegativeLast()
    {
      Shape triangle = Shapes.newTriangle(17,12.6,-23);
    }
  @Test
    public void TriangleSides()
    {
      Triangle triangle = (Triangle)Shapes.newTriangle(17,12.6, 23);
      assertEquals("N sides",3, triangle.nSides());
    }
}
