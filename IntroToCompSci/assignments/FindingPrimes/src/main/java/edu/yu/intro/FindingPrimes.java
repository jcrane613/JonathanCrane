package edu.yu.intro;
import java.util.Scanner;
import java.lang.Math;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.InputMismatchException;

public class FindingPrimes
{
  public final static int MAX_LIMIT = 10000;
  public static boolean isPrime(final int N)
  {
    if (N < 2 || N > MAX_LIMIT)
    {
      throw new IllegalArgumentException("Error: number supplied is out of bounds. Must be between 2 and 10,000");
    }
    else
    {
      double numberTest = Math.sqrt(N);
      for (int i = 2; i <= numberTest; i++)
      {
        if ((N % i) == 0)
        {
          return false;
        }
      }
    }
    return true;
  }
  public static int[] primes (final int N)
  {
    if (N < 2 || N > MAX_LIMIT)
    {
      throw new IllegalArgumentException("Error: number supplied is out of bounds. Must be between 2 and 10,000");
    }
    int counter1 = 0;
    for (int j = 2; j <= N; j++)
    {
      if (isPrime(j))
      {
        counter1++;
      }
    }
    int[] findPrimesArray = new int[counter1];
    int counter2 = 0;
    for (int p = 2; p <= N; p++)
    {
      if (isPrime(p))
      {
        findPrimesArray[counter2]=p;
        counter2++;
      }
    }
    return findPrimesArray;
  }
  public static void main (final String[] args)
  {
    try
    {
      System.out.print("Nu? Enter the upper limit number that you want to search for primes: ");
      Scanner scanner = new Scanner(System.in);
      int N = scanner.nextInt();
      if (N < 2 || N > MAX_LIMIT)
      {
        throw new IllegalArgumentException("Error: number supplied is out of bounds. Must be between 2 and 10,000");
      }
      else
      {
        System.out.println();
        System.out.println("*************************************************");
        System.out.println("INPUT ...");
        System.out.println("Searched for primes up to: "+N);
        System.out.println("OUTPUT ...");
        System.out.println("Found " +(primes(N)).length+ " primes from 2..."+N);
        int[] testArray = primes(N);
        for (int p = 0; p < primes(N).length; p++)
        {
          System.out.print(testArray[p]+",");
        }
        System.out.println();
        //System.out.println(Arrays.toString(primes(N)));
      }
    }
    catch (InputMismatchException e)
    {
      throw new IllegalArgumentException("Error, the input needs to be an integar");
    }
  }
}
