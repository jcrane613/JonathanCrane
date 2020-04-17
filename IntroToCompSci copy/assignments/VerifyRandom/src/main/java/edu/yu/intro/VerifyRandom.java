package edu.yu.intro;

import java.util.Random;

public class VerifyRandom
{
  // main method begins execution of Java application
  public static void main(String[] args)
  {
    int die1 = 0;
    int die2 = 0;   // The values rolled on the two dice.
    int countRolls = 0;   // Used to count the number of rolls.
    Random randomNumbers = new Random();
    int rolls;
    double predicted = (1.0/36.0);

    for (rolls= 0; rolls < 10000; rolls++)
    {
      die1 = 4;
      die2 = 3;
      while ( die1 != 1 || die2 != 1 )
      {
        die1 = 1 + randomNumbers.nextInt(6);
        die2 = 1 + randomNumbers.nextInt(6);
        countRolls++;
      }
     }
           //System.out.println("jonathan:" +predicted);

        //  System.out.println("countRolls is: " + countRolls);
          double probability= (10000.0 / countRolls);
          double ratio = (Math.abs(probability - predicted) / (predicted));

         System.out.printf("%-64s %.3f%n", "Average simulated probability: ", probability);
         System.out.printf("%-64s %d%n", "Number of Simulations: " , rolls);
         System.out.printf("%-64s %.3f%n", "Difference (as a ratio) with respect to predicted probability: " , ratio);
       }

}
