package edu.yu.intro;
public class BasicStatistics
{
private double[] inputArray = new double[1];
private int counter= 0;
public void enter ( double num )
{
  double[] newInputArray = new double[2*inputArray.length];
  for (int i =0; i<inputArray.length; i++)
  {
    newInputArray[i]=inputArray[i];
  }
  inputArray = newInputArray;
  inputArray[counter]=num;
  counter++;
}
public void enter ( double [] data )
{
  double[] newInputArray =new double[((2 * data.length) + inputArray.length)];
  for (int i =0; i< data.length; i++)
  {
    newInputArray[i]=data[i];
    counter++;
  }
  for (int i =0; i<inputArray.length; i++)
  {
    newInputArray[i+data.length]=inputArray[i];
  }
  inputArray=newInputArray;
}

public int getNDatums ()
{
  return counter;
}

public double getSum ()
{
  double sum =0.0;
  for (int i = 0; i<counter;i++)
  {
    sum += inputArray[i];
  }
  return sum;
}

public double getMean ()
{
  if (counter == 0)
  {
    return Double.NaN;
  }
  double mean = (getSum()/((double)getNDatums()));
  return mean;
}

public double getStandardDeviation ()
{
  if (counter == 0)
  {
    return Double.NaN;
  }
  int N = counter;
  double variance =0.0;
  for (int d =0; d<N; d++)
  {
    double newValue = inputArray[d] - getMean();
    variance += newValue * newValue;
  }
  return Math.sqrt(variance / N);
}

public double getMin ()
{
  if (counter == 0)
  {
    return Double.POSITIVE_INFINITY;
  }
  double min = 0.0;
  double minValue = Double.MAX_VALUE;
  for (int i = 0; i<counter; i++)
  {
    if (inputArray[i]<minValue)
    {
      minValue = inputArray[i];
      min = inputArray[i];
    }
  }
  return min ;
}

public double getMax ()
{
  if (counter == 0)
  {
    return Double.NEGATIVE_INFINITY;
  }
  double max = 0.0;
  double maxValue = Double.MIN_VALUE;
  for (int i = 0; i<counter; i++)
  {
    if (inputArray[i]>maxValue)
    {
      maxValue = inputArray[i];
      max = inputArray[i];
    }
  }
  return max ;
}

public static void main ( final String [] args )
{
  BasicStatistics stat = new BasicStatistics();
  stat.enter(new double[]{22.3, 872.1, 39.8, 47.6});
  System.out.printf("%-20s %d%n","Number of datums:", stat.getNDatums());
  System.out.printf("%-20s %,.3f%n", "Min of datums:", stat.getMin());
  System.out.printf("%-20s %,.3f%n", "Max of datums:", stat.getMax());
  System.out.printf("%-20s %,.3f%n", "Sum of datums:", stat.getSum());
  System.out.printf("%-20s %,.3f%n", "Mean of datums:", stat.getMean());
  System.out.printf("%-20s %,.3f%n", "StdDev of datums:", stat.getStandardDeviation());
}
}
