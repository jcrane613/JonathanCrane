package edu.yu.intro;
import java.lang.Math;

public class Rational
{
  private int numerator;
  private int denominator;
  public Rational()
  {
    numerator = 0;
    denominator = 1;
  }
  public String printRational()
  {
    double sum = ((double)numerator)/((double)denominator);
    if (sum >= 0)
    {
      return ("The numerator is "+numerator+ " and the denominator is "+denominator+" this is a Positive rational");
    }
    return ("The numerator is "+numerator+ " and the denominator is "+denominator+" this is a Negative rational");
  }
  public int getNumerator()
  {
    return numerator;
  }
  public int getDenominator()
  {
    return denominator;
  }
  public Rational (int numerator, int denominator)
  {
    if (denominator == 0)
    {
      throw new IllegalArgumentException("Error: denominator cannot be zero");
    }
    this.numerator = numerator;
    this.denominator = denominator;
  }
  public void negate()
  {
    numerator = numerator * (-1);
  }
  public void invert()
  {
    int newNumerator= denominator;
    int newDenominator= numerator;
    numerator = newNumerator;
    denominator = newDenominator;
    if (denominator == 0)
    {
      throw new UnsupportedOperationException("Error: denominator cannot be zero");
    }
  }
  public double toDouble()
  {
    double sum = ((double)numerator/(double)denominator);
    return sum;
  }
  public Rational reduce()
  {
    int num = numerator;
    int den = denominator;
    int gcdForReduce= gcd(num,den);
    int newNumerator = num/gcdForReduce;
    int newDenominator = den/gcdForReduce;
    return new Rational(newNumerator,newDenominator);
  }
  public int gcd(int numerator, int denominator)
  {
    if (denominator == 0)
    {
    return Math.abs(numerator);
    }
    else
    {
      return gcd(denominator, numerator%denominator);
    }
  }
  public Rational add(final Rational that)
  {
    int num1 = this.numerator;
    int den1 = this.denominator;
    int num2 = that.numerator;
    int den2 = that.denominator;
    int quotient = den1 * den2;
    int newNumerator = (num1 * den2) + (num2 * den1);
    int newDenominator = quotient;
    int lowestForm = gcd(newNumerator, newDenominator);
    int finalNumerator =newNumerator/lowestForm;
    int finalDenominator =newDenominator/lowestForm;
    return new Rational (finalNumerator, finalDenominator);
  }
  public static void main (final String[] args)
  {
    Rational rational4 = new Rational();
    System.out.println(rational4.printRational());
  }
}
