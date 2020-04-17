package edu.yu.intro.test;
import edu.yu.intro.FindingPrimes;
import org.junit.*;
import static org.junit.Assert.*;
public class FindingPrimesTest
{
@Test
  public void testPrimeTable()
  {
    int[] expectedOutput = {2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97,101,103,107,109,113,127,131,137,139,149,151,157,163,167,173, 179,181,191,193,197,199,211,223,227,229,233,239,241,251,257,263,269,271,277,281,283,293,307,311,313,317,331,337,347,349};
    int[] methodOutput = (FindingPrimes.primes(350));
    assertArrayEquals(expectedOutput, methodOutput);
  }
@Test
  public void ELi()
  {
    assertEquals("poop",20 , FindingPrimes.primes(71).length);
  }
@Test
  public void primeCheckerTrue()
  {
    assertTrue(FindingPrimes.isPrime(17));
  }
@Test
  public void primeCheckerFalse()
  {
    assertFalse(FindingPrimes.isPrime(16));
  }
@Test  (expected = IllegalArgumentException.class)
  public void primeCheckerTooBig()
  {
    assertFalse(FindingPrimes.isPrime(30000));
  }
@Test  (expected = IllegalArgumentException.class)
  public void primeCheckerTooSmall()
  {
    assertFalse(FindingPrimes.isPrime(-2));
  }
@Test  (expected = IllegalArgumentException.class)
  public void primeArrayTooBig()
  {
    assertEquals("Finding the prime value of 30,000",0,FindingPrimes.primes(30000));
  }
@Test  (expected = IllegalArgumentException.class)
  public void primeArrayTooSmall()
  {
    assertEquals("Finding the prime value of -2",0 ,FindingPrimes.primes(-2));
  }
}
