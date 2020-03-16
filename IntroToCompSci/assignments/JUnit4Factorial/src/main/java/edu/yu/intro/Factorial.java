package edu.yu.intro;
import java.util.*;
import java.util.Arrays;
public class Factorial
{
  public static void main(String[] args)
  {
    System.out.println("********************");
    long[] testArray = factorialTable();
    for (int counter = 0; counter <= 20; counter++)
    {
      System.out.println("factorial ("+counter+") =" +testArray[counter]);
    }
    System.out.println("********************");
  }
  public static long factorial (final int n)
  {
    if (n < 0 || n >= 21)
    {
      throw new IllegalArgumentException("Error: factorial must be between 0 and 20");
    }
    else if (n <= 1)
    {
      return 1;
    }
    else
    {
      return (n * factorial(n-1));
    }
  }
  public static long[] factorialTable()
  {
    long[] factorialArray = new long[21];
    for (int i = 0; i <= 20; i++)
    {
        factorialArray[i] = factorial(i);
        //System.out.println("the factorial"+i+ "is the number"+factorialArray[i]);
    }
    return factorialArray;
  }
}
