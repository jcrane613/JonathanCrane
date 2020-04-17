package edu.yu.intro;

import java.util.Random;

public class ShareSameBirthday

{
  // main method begins execution of Java application
      public static void main(String[] args)
      {

        Random randomNumbers = new Random();


int[] array1 = new int[10];
int i1 = 0;
int count1 = 0;


for (int rolls= 0; rolls < 10000; rolls++)
 {
        for (i1 = 0; i1 < array1.length; i1++)
          {
            array1[i1] = 1 + randomNumbers.nextInt(365);
            //System.out.println("Values: " +i+ "inside" +array[i]);
            //System.out.println("Array Number " +i1+ " value inside " +array1[i1]);
          }
boolean breaker = false;
        for  (i1 = 0; i1 < array1.length; i1++)
          {

              for (int k1 = (i1 + 1); k1 < array1.length; k1++)
                {

                if (array1[i1] == array1[k1]) {
              //  System.out.println("the values of i1: " +i1+ " the value of k1: " +k1);
              //  System.out.println("the values of i1: " +array1[i1]+ " the value of k1: " +array1[k1]);
                count1++;
                breaker= true;
                break;

              }
              }
              if (breaker == true) {break;}
            }
   }

int[] array2 = new int[23];
int i2 = 0;
int count2 = 0;

for (int rolls2= 0; rolls2 < 10000; rolls2++)
{
        for (i2 = 0; i2 < array2.length; i2++)
          {
            array2[i2] = 1 + randomNumbers.nextInt(365);
            //System.out.println("Values: " +i+ "inside" +array[i]);

            //System.out.println("Array Number " +i+ " value inside " +array[i]);
          }
        boolean breaker2 = false;
        for  (i2 = 0; i2 < array2.length; i2++){

              for (int k2 = (i2 + 1); k2 < array2.length; k2++){
                if (array2[i2] == array2[k2]) {
                //System.out.println("Success");
                //System.out.println("the values of i:" +i2+ "the value of" +k2);
              //  System.out.println("the values of i2: " +array2[i2]+ " the value of k2: " +array2[k2]);
              count2++;
              breaker2 = true;
                break;
              }

            } if(breaker2 == true) {break;}
            }
}

int[] array3 = new int[70];
int i3 = 0;
int count3 = 0;

for (int rolls3= 0; rolls3 < 10000; rolls3++)
{
        for (i3 = 0; i3 < array3.length; i3++)
          {
            array3[i3] = 1 + randomNumbers.nextInt(365);
            //System.out.println("Values: " +i+ "inside" +array[i]);

            //System.out.println("Array Number " +i+ " value inside " +array[i]);
          }
          boolean breaker3 = false;
        for  (i3 = 0; i3 < array3.length; i3++){
              for (int k3 = (i3 + 1); k3 < array3.length; k3++){
                if (array3[i3] == array3[k3]) {

              //  System.out.println("Success");
              //System.out.println("the values of i:" +i3+ "the value of" +k3);
               //System.out.println("the values of i3: " +array3[i3]+ " the value of k3: " +array3[k3]);
               count3++;
               breaker3 = true;
               break;
              }

            } if(breaker3 == true){break;}

            }
}

System.out.println("*************************************************");
System.out.println("Number of experiments run: 10,000");
System.out.println("(For popualation size 10): simulated 'shared birthday'");
System.out.printf("    %s %.3f%n", "frequency is " , (count1/ 10000.0));
System.out.println("(For popualation size 23): simulated 'shared birthday'");
System.out.printf("    %s %.3f%n", "frequency is " , (count2/ 10000.0));
System.out.println("(For popualation size 70): simulated 'shared birthday'");
System.out.printf("    %s %.3f%n", "frequency is " , (count3/ 10000.0));
System.out.println("*************************************************");
//System.out.println( "count1: " +count1+ " count2: "+count2+" count 3: " +count3);
        }

  }
