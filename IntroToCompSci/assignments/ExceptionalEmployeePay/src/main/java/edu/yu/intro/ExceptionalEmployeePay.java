package edu.yu.intro;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.*;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class ExceptionalEmployeePay
{
  public static void main(String[] args)
  {
    if (args.length != 1)
    {
      final String msg = "Usage: ExceptionalEmployeePay name_of_input file ";
      System.err.println(msg) ;
      throw new IllegalArgumentException(msg);
    }
    try
    {
      int rowNumber = 0;
      final String inputFileName = args [0];
      final File input = new File(inputFileName);

      Scanner scanner = new Scanner(input);
      Double hrsWorked = 0.0;
      Double wageRate = 0.0;
      int deductions = 0;
      Double totalGrossPay = 0.0;
      Double totalTaxesPaid = 0.0;
      String richEmployee = null;
      Double maxValue = Double.MIN_VALUE;
      System.out.println("*************************************************");
      System.out.printf("%15s %15s %14s %12s %18s%n", "Id","Gross Pay","Taxes","Net Pay","Average Pay");

      while (scanner.hasNext())
      {
        String lines = scanner.nextLine();
        String[] strs = lines.trim().split("\\s+");
        rowNumber++;
        boolean skiper = false;
        if (strs.length == 4)
        {
          String employeeId = strs[0];
          try
          {
            hrsWorked = Double.parseDouble(strs[1]);
          }
          catch (NumberFormatException e)
          {
            System.out.println("Problem at line # "+rowNumber+": Could not parse Hours Worked [" +strs[1]+ "] into valid input");
            skiper = true;
          }
          if (skiper != true)
          {
            try
            {
              wageRate = Double.parseDouble(strs[2]);
            }
            catch (NumberFormatException e)
            {
              System.out.println("Problem at line # "+rowNumber+": Could not parse Wage Rate [" +strs[2]+ "] into valid input");
              skiper = true;
            }
          }
          if (skiper != true)
          {
            try
            {
              deductions = Integer.parseInt(strs[3]);
            }
            catch (NumberFormatException e)
            {
              System.out.println("Problem at line # "+rowNumber+": Could not parse Deductions [" +strs[3]+ "] into valid input");
              skiper = true;
            }
          }
          //this code will test for specific conditions
          if (skiper != true)
          {
            if (hrsWorked < 1)
            {
              System.out.println("Problem at line # "+rowNumber+": The amount of hours recorded, "+hrsWorked+" is too small. Must be at least 1.0");
              skiper = true;
            }
          }
          if (skiper != true)
          {
            if (wageRate < 15 )
            {
              System.out.println("Problem at line # "+rowNumber+": The value of wages per hour, " +wageRate+ " is too small. Must be greater than or equal to minimum wage of $15");
              skiper = true;
            }
          }
          if (skiper != true)
          {
            if (0 >= deductions || deductions >= 35)
            {
              System.out.println("Problem at line # " +rowNumber+ ": The deduction value, "+deductions+" is out of range. Deductions must be greater than zero and less than $35");
              skiper = true;
            }
          }

          if (skiper != true)
          {
            Double grossPay = hrsWorked * wageRate; //hours worked * wage rate
            Double Taxes = grossPay * 0.15; //gross pay × tax rate of 15%
            Double netPay = grossPay - Taxes - deductions - (grossPay * 0.05); // gross pay - taxes - deductions - pension contributions of 5% of gross pay
            Double averagePay = netPay / hrsWorked; // net pay divided by hours worked
            if (netPay < 0)
            {
              //System.out.println("Error: You entered a negative number for NetPay, which was " + netPay+" on line " +rowNumber);
              System.out.printf("%s %d%s %.2f %s%n", "Problem at line #",rowNumber,": The Net Pay,",netPay, "is a negative number");
              skiper = true;
            }
            if (skiper != true)
            {
              System.out.printf("%15s %,15.2f %,14.2f  %,12.2f %,16.2f%n", employeeId, grossPay, Taxes, netPay, averagePay);
              totalGrossPay = grossPay + totalGrossPay;
              totalTaxesPaid = Taxes + totalTaxesPaid;
              //The code to figure out who gets paid the max
              if (grossPay > maxValue)
              {
                maxValue = grossPay;
                richEmployee = employeeId;
              }
            }
          }
        }
        else
        {
          System.out.println("Problem at line # "+rowNumber+ ": Expected 4 tokens per line, found " +strs.length+ ". Discarding input & advancing");
        }
      }
      System.out.println();
      System.out.println();
      System.out.println();
      System.out.printf("%40s %,.2f%n", "Total Gross Pay: ", totalGrossPay);
      System.out.printf("%40s %,.2f%n", "Total Taxes: ", totalTaxesPaid);
      System.out.printf("%40s %s%n", "Employee with Largest Gross Pay: ", richEmployee);
      System.out.println("*************************************************");
    }
    catch (FileNotFoundException e)
    {
      throw new IllegalArgumentException("File not found with the path specified");
    }
    catch (IllegalStateException e)
    {
      System.out.println("There was an issue with the Scanner since it is closed");
    }
  }
}
