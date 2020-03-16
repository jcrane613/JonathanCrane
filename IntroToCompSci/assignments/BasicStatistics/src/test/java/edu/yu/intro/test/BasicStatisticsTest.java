package edu.yu.intro.test;
import edu.yu.intro.BasicStatistics;
import org.junit.*;
import static org.junit.Assert.*;
public class BasicStatisticsTest
{
@Test
public void getNDatums()
{
  BasicStatistics stat = new BasicStatistics();
  stat.enter(new double[]{22.3, 872.1, 39.8, 47.6});
  stat.enter(74.3);
  stat.enter(-332.4);
  stat.enter(0);
  stat.enter(new double[]{-343.5, -7.4, 36.9, 0});
  assertEquals(11, stat.getNDatums());
}
@Test
public void getNDatumsZero()
{
  BasicStatistics stat = new BasicStatistics();
  assertEquals(0, stat.getNDatums());
}
@Test
public void getSum()
{
  BasicStatistics stat = new BasicStatistics();
  stat.enter(new double[]{22.3, 872.1, 39.8, 47.6});
  assertEquals(981.800, stat.getSum(), .01);
}
@Test
public void getSumNegative()
{
  BasicStatistics stat = new BasicStatistics();
  stat.enter(new double[]{22.3, 39.8, 47.6});
  stat.enter(new double[]{282.3, -8.1, 3.8, -37.6});
  stat.enter(-63.3);
  assertEquals(286.800, stat.getSum(), .01);
}
@Test
public void getSumNegativeAnswer()
{
  BasicStatistics stat = new BasicStatistics();
  stat.enter(new double[]{-22.3, -39.8, 47.6});
  stat.enter(new double[]{-282.3, -8.1, 3.8, -37.6});
  stat.enter(-63.3);
  assertEquals(-402.0, stat.getSum(), .01);
}
@Test
public void getSumNoInput()
{
  BasicStatistics stat = new BasicStatistics();
  assertEquals(0, stat.getSum(), .1);
}
@Test
public void getMean()
{
  BasicStatistics stat = new BasicStatistics();
  stat.enter(new double[]{22.3, 872.1, 39.8, 47.6});
  assertEquals(245.450, stat.getMean(), .01);
}
@Test
public void getMeanNegative()
{
  BasicStatistics stat = new BasicStatistics();
  stat.enter(new double[]{22.3, 8.1, 39.8, 47.6});
  stat.enter(-63.3);
  stat.enter(-783.3);
  assertEquals(-121.466, stat.getMean(), .01);
}
@Test
public void getMeanNoInput()
{
  BasicStatistics stat = new BasicStatistics();
  assertEquals(Double.NaN, stat.getMean(), .01);
}
@Test
public void getStandardDeviation()
{
  BasicStatistics stat = new BasicStatistics();
  stat.enter(new double[]{22.3, 872.1, 39.8, 47.6});
  assertEquals(361.913, stat.getStandardDeviation(), .01);
}
@Test
public void getStandardDeviationNegatives()
{
  BasicStatistics stat = new BasicStatistics();
  stat.enter(new double[]{22.3, -87.1, 12.8, -25.6});
  stat.enter(94.2);
  stat.enter(-24.6);
  stat.enter(new double[]{26.3, 45.1, 34.3, 23.7});
  stat.enter(new double[]{22.3, -2.1, 39.8, 47.6});
  stat.enter(-238.5);
  assertEquals(74.865, stat.getStandardDeviation(), .01);
}
@Test
public void getStandardDeviationNoInput()
{
  BasicStatistics stat = new BasicStatistics();
  assertEquals(Double.NaN, stat.getStandardDeviation(), .01);
}
@Test
public void getMin()
{
  BasicStatistics stat = new BasicStatistics();
  stat.enter(new double[]{22.3, 872.1, 39.8, 47.6});
  assertEquals(22.3, stat.getMin(), .01);
}
@Test
public void getMinNegative()
{
  BasicStatistics stat = new BasicStatistics();
  stat.enter(new double[]{-22.3, 872.1, 39.8, 47.6});
  stat.enter(0);
  stat.enter(-73.3);
  stat.enter(new double[]{-2.3, 87.1, 3.8, 4.6});
  stat.enter(783.3);
  stat.enter(new double[]{2.3, 72.1, 9.8, 7.6});
  assertEquals(-73.3, stat.getMin(), .01);
}
@Test
public void getMinNoInput()
{
  BasicStatistics stat = new BasicStatistics();
  assertEquals(Double.POSITIVE_INFINITY, stat.getMin(), .01);
}
@Test
public void getMax()
{
  BasicStatistics stat = new BasicStatistics();
  stat.enter(new double[]{22.3, 872.1, 39.8, 47.6});
  stat.enter(new double[]{-22.3, 872.1, 39.8, 47.6});
  stat.enter(0);
  stat.enter(-73.3);
  stat.enter(new double[]{-2.3, 5823.2, 3.8, 4.6});
  stat.enter(783.3);
  stat.enter(new double[]{2.3, 72.1, 9.7, 7.6});
  assertEquals(5823.2, stat.getMax(), .01);
}
@Test
public void getMaxNoInput()
{
  BasicStatistics stat = new BasicStatistics();
  assertEquals(Double.NEGATIVE_INFINITY, stat.getMax(), .01);
}
}
