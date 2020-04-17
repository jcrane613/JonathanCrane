package edu.yu.intro;

public class DozensOfEggsVariables
{
  // main method begins execution of Java application
      public static void main(String[] args)
      {
            int EggsToPack= 18972; // Amount of Eggs to Pack
            int BoxesOfEggs; // Amount of Boxes of Eggs (each box will have 12 eggs)
            int ExtraEggs; // The amount of leftover eggs that were not packed
            double Price; // The cost of packing all of the eggs it cost $.72 per BoxesOfEggs

            BoxesOfEggs= (EggsToPack / 12);
            ExtraEggs= (EggsToPack % 12);
            Price= (BoxesOfEggs * .72);

            System.out.printf("Your number of eggs (%,d) gets packed into %,d boxes and %d extras %n", EggsToPack, BoxesOfEggs, ExtraEggs);
            System.out.printf("Total packaging cost: $%.2f%n", Price);
      }
}
