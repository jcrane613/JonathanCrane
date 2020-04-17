package edu.yu.intro.test;
import edu.yu.intro.Rational;
import org.junit.*;
import static org.junit.Assert.*;
public class RationalTest
{
@Test
  public void rationalPrintTest()
  {
    Rational rational = new Rational();
    assertEquals ("Testing the state of the numbers set in the class", "The numerator is 0 and the denominator is 1 this is a Positive rational", rational.printRational());
  }
@Test
  public void rationalPrintTestNegative()
  {
    Rational rational = new Rational(-3,7);
    assertEquals ("Testing the state of the rational -3/7", "The numerator is -3 and the denominator is 7 this is a Negative rational", rational.printRational());
  }
@Test
  public void rationalPrintTestPositive()
  {
    Rational rational = new Rational(-3,-7);
    assertEquals ("Testing the state of the rational -3/-7", "The numerator is -3 and the denominator is -7 this is a Positive rational", rational.printRational());
  }
@Test
  public void getNumeratorTest()
  {
    Rational rational = new Rational();
    assertEquals ("Numerator has the number 0", 0, rational.getNumerator());
  }
@Test
  public void getDenominatorTest()
  {
    Rational rational = new Rational();
    assertEquals ("Denominator has the number 1", 1, rational.getDenominator());
  }
@Test
  public void contructorForNumeratorDenominator()
  {
    Rational rational2 = new Rational(3,2);
    assertEquals("Getting the numerator for 3,2", 3, rational2.getNumerator());
    assertEquals("Getting the denominator for 3,2", 2, rational2.getDenominator());
  }
@Test
  public void contructorForNumerator0()
  {
    Rational rational2 = new Rational(0,2);
    assertEquals("Getting the numerator for 0,2", 0, rational2.getNumerator());
    assertEquals("Getting the denominator for 0,2", 2, rational2.getDenominator());
  }
@Test(expected= IllegalArgumentException.class)
  public void contructorForDenominator0()
  {
    Rational rational2 = new Rational(3,0);
    assertEquals("Getting the numerator for 3,0", 3, rational2.getNumerator());
    assertEquals("Getting the denominator for 3,0", 0, rational2.getDenominator());
  }
@Test
  public void negateTestBothPositive()
  {
    Rational rational2 = new Rational(4,5);
    rational2.negate();
    assertEquals("Negating the numerator of 4,5", -4, rational2.getNumerator());
    assertEquals("keeping the denominator the same for 4,5", 5, rational2.getDenominator());
  }
@Test
  public void negateTestNegative()
  {
    Rational rational2 = new Rational(-4,5);
    rational2.negate();
    assertEquals("Negating the numerator of -4,5", 4, rational2.getNumerator());
  }
@Test(expected= IllegalArgumentException.class)
  public void negateTestPositiveNumerator()
  {
    Rational rational2 = new Rational(4,0);
    rational2.negate();
    assertEquals("Negating the numerator of 4,0: should throw an error becasue of the denominator", -4, rational2.getNumerator());
  }
@Test
  public void invertTestBothPositive()
  {
    Rational rational2 = new Rational(4,5);
    rational2.invert();
    assertEquals("Inverting the numerator from 4 to 5", 5, rational2.getNumerator());
    assertEquals("Inverting the denominator from 5 to 4", 4, rational2.getDenominator());
  }
@Test
  public void invertTestNegativeDenominator()
  {
    Rational rational2 = new Rational(7,-3);
    rational2.invert();
    assertEquals("Inverting the numerator from 7 to -3", -3, rational2.getNumerator());
    assertEquals("Inverting the denominator from -3 to 7", 7, rational2.getDenominator());
  }
@Test (expected= UnsupportedOperationException.class)
  public void invertTestDenominatorIsZero()
  {
    Rational rational2 = new Rational(0,3);
    rational2.invert();
    assertEquals("Inverting the numerator from 0 to 3", 3, rational2.getNumerator());
    assertEquals("Inverting the denominator from 3 to 0: Error, the denominator cannot be zero", 0, rational2.getDenominator());
  }
@Test
  public void toDoubleBothPositive()
  {
    Rational rational2 = new Rational(5,7);
    double test = rational2.toDouble();
    assertEquals(.714,test,.01);
  }
@Test
  public void toDoubleNumeratorNegative()
  {
    Rational rational2 = new Rational(-6,16);
    double test = rational2.toDouble();
    assertEquals(-0.375,test,.01);
  }
@Test
  public void reduceTestBothPositive()
  {
    Rational rational2 = new Rational(14,21);
    Rational testReduce = rational2.reduce();
    assertEquals("Reducing 14", 2, testReduce.getNumerator());
    assertEquals("Reducing 21", 3, testReduce.getDenominator());
  }
@Test
  public void reduceTestBothPositiveLarger()
  {
    Rational rational2 = new Rational(52,130);
    Rational testReduce = rational2.reduce();
    assertEquals("Reducing 52", 2, testReduce.getNumerator());
    assertEquals("Reducing 130", 5, testReduce.getDenominator());
  }
@Test
  public void reduceTestNumeratorNegative()
  {
    Rational rational2 = new Rational(-38,14);
    Rational testReduce = rational2.reduce();
    assertEquals("Reducing -38", -19, testReduce.getNumerator());
    assertEquals("Reducing 14", 7, testReduce.getDenominator());
  }
@Test
  public void reduceTestNumeratorZero()
  {
    Rational rational2 = new Rational(0,130);
    Rational testReduce = rational2.reduce();
    assertEquals("Reducing 0", 0, testReduce.getNumerator());
    assertEquals("Reducing 130", 1, testReduce.getDenominator());
  }
@Test
  public void addTestBothPositive()
  {
    Rational rational1 = new Rational(3,6);
    Rational rational2 = new Rational(2,5);
    Rational rational3 = rational1.add(rational2);
    assertEquals("Adding 3+2", 9, rational3.getNumerator());
    assertEquals("Adding 6+5", 10, rational3.getDenominator());
  }
@Test
  public void addTestNumeratorNegative()
  {
    Rational rational1 = new Rational(1,3);
    Rational rational2 = new Rational(-1,4);
    Rational rational3 = rational1.add(rational2);
    assertEquals("Adding 1-1", 1, rational3.getNumerator());
    assertEquals("Adding 3+4", 12, rational3.getDenominator());
  }
@Test
  public void addTestBothNumeratorNegative()
  {
    Rational rational1 = new Rational(-1,3);
    Rational rational2 = new Rational(-1,4);
    Rational rational3 = rational1.add(rational2);
    assertEquals("Adding -1-2", -7, rational3.getNumerator());
    assertEquals("Adding 3+4", 12, rational3.getDenominator());
  }
@Test
  public void gcdTestBothPositive()
  {
    Rational rational = new Rational();
    assertEquals ("Greatest common denominator for 16 and 32", 16, rational.gcd(16,32));
  }
@Test
  public void gcdTestDenominatorNegative()
  {
    Rational rational = new Rational();
    assertEquals ("Greatest common denominator for 0 and -17", 17, rational.gcd(0,-17));
  }
}
