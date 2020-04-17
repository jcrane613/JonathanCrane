package edu.yu.intro.test;
import edu.yu.intro.AckermannFn;
import org.junit.*;
import static org.junit.Assert.*;
public class AckermannFnTest
{
@Test
  public void ackermann00 ()
  {
    assertEquals ("Testing the ackermann function for 0,1", 2L, AckermannFn.ackermann(0L,1L));
  }
@Test
  public void ackermann12 ()
  {
    assertEquals ("Testing the ackermann function for 1,2", 4L, AckermannFn.ackermann(1L,2L));
  }
@Test
  public void ackermann21 ()
  {
    assertEquals ("Testing the ackermann function for 2,2", 7L, AckermannFn.ackermann(2L,2L));
  }
@Test
  public void ackermann23 ()
  {
    assertEquals ("Testing the ackermann function for 2,3", 9L, AckermannFn.ackermann(2L,3L));
  }
@Test
  public void ackermann32 ()
  {
    assertEquals ("Testing the ackermann function for 3,2", 29L, AckermannFn.ackermann(3L,2L));
  }
@Test
  public void ackermann31 ()
  {
    assertEquals ("Testing the ackermann function for 3,1", 13L, AckermannFn.ackermann(3L,1L));
  }
@Test (expected = IllegalArgumentException.class)
  public void ackermanNegativeN()
  {
    AckermannFn.ackermann(0L,-1L);
  }
@Test (expected = IllegalArgumentException.class)
public void ackermannNegativeM()
  {
    AckermannFn.ackermann(-1L,0L);
  }
}
