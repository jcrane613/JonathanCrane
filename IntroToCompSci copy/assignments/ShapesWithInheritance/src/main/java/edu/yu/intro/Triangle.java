package edu.yu.intro;
import java.lang.Math;

public class Triangle extends Shape
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
  this.side1 = s1;
  if (s2 < 0)
  {
    throw new IllegalArgumentException("Error, side 2 was negative");
  }
  this.side2 = s2;
  if (s3 < 0)
  {
    throw new IllegalArgumentException("Error, side 3 was negative");
  }
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
  double perimeter = (side1 + side2 + side3);
  return perimeter;
}
}
