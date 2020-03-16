package edu.yu.intro;
public class AckermannFn
{
  //public static void main(String[] args)
  //{
  //  System.out.println(ackermann(3,2));
  //}
  public static long ackermann (long m, long n)
  {
    if (m < 0 || n < 0)
    {
      throw new IllegalArgumentException("Error: the numbers inputted must both be positive");
    }
    else if (m == 0)
    {
      return n+1;
    }
    else if (n == 0)
    {
      return ackermann(m-1,1L);
    }
    else
    {
      return (ackermann(m-1, ackermann(m,n-1)));
    }
  }
}
