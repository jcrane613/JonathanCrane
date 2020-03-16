package edu.yu.intro;

import java.util.Scanner; // program uses class Scanner

public class GrossesAndDozensWithIO

{
  // main method begins execution of Java application
      public static void main(String[] args)
      {
        System.out.print("Nu? Enter number of eggs to be converted: ");

        Scanner s = new Scanner(System.in); // create a Scanner to obtain input from the command window

        int AmountOfEggs = s.nextInt();
        int Gross = AmountOfEggs / 144; //
        int dozens = AmountOfEggs / 12; //
        int extras = AmountOfEggs % 12; //

        System.out.println();
        System.out.println("*************************************************");
        System.out.println();
        System.out.println("INPUT ...");
        System.out.printf("%-25s %,d%n", "Number of eggs:", AmountOfEggs);
        System.out.println("OUTPUT ...");
        System.out.printf("%-25s %,d%n", "Number of Gross: ", Gross);
        System.out.printf("%-25s %,d%n", "Number of Dozens: ", dozens);
        System.out.printf("%-25s %d%n", "Number of Extras: ", extras);
        System.out.println("*************************************************");

      }
}
