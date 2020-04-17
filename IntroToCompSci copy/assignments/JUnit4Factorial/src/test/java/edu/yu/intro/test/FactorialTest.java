package edu.yu.intro.test;
import edu.yu.intro.Factorial;
import org.junit.*;
import static org.junit.Assert.*;
public class FactorialTest
{
@Test
  public void factorial0 ()
  {
    assertEquals ("testing factorial 0", 1, Factorial.factorial(0));
  }
@Test
  public void factorial1()
  {
    assertEquals ("testing factorial 1", 1, Factorial.factorial(1));
  }
@Test
  public void factorial9()
  {
    assertEquals("testing factorial 9",362880, Factorial.factorial(9));
  }
@Test
  public void factorial20()
  {
    assertEquals("testing factorial 20", 2432902008176640000L, Factorial.factorial(20));
  }
@Test (expected = IllegalArgumentException.class)
  public void negativeInputRejected()
  {
    Factorial.factorial(-1);
  }
@Test (expected = IllegalArgumentException.class)
  public void longOutOfRange()
  {
    Factorial.factorial(21);
  }
@Test
  public void testFactorialTable()
  {
    long[] expectedOutput = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};
    long[] methodOutput = Factorial.factorialTable();
    assertArrayEquals(expectedOutput, methodOutput);
  }
}
