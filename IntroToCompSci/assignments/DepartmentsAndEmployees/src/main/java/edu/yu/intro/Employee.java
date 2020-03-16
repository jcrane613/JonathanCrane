package edu.yu.intro;
import java.io.*;
import java.util.Scanner;
public class Employee
{
  public String employeeId;
  public double hrsWorked;
  public double wageRate;
  public int deductions;
  public String departmentForEmployee;
  public double grossPay;
  public int rowNumber;

  public Employee (String lines, int numberJonathan)
  {
    this.rowNumber = numberJonathan;
    String[] strs = lines.trim().split("\\s+");
    this.employeeId = strs[0];
    if (strs.length == 5)
    {
      try
      {
        this.hrsWorked = Double.parseDouble(strs[1]);
        if (hrsWorked < 1)
        {
          throw new IllegalArgumentException("Problem at line #"+this.rowNumber+": The amount of hours recorded, "+hrsWorked+" is too small. Must be at least 1.0");
          //throw new IllegalArgumentException("The amount of hours recorded, "+hrsWorked+" is too small. Must be at least 1.0 for employee "+employeeId);
        }
      }
      catch (NumberFormatException e)
      {
        throw new IllegalArgumentException("Problem at line #"+this.rowNumber+": Could not parse Hours Worked [" +strs[1]+ "] into valid input");
        //throw new IllegalArgumentException("Could not parse Hours Worked [" +strs[1]+ "] into valid input for employee "+employeeId);
      }
      try
      {
        this.wageRate = Double.parseDouble(strs[2]);
        if (wageRate < 15 )
        {
          throw new IllegalArgumentException("Problem at line #"+this.rowNumber+": The value of wages per hour, " +wageRate+ " is too small. Must be greater than or equal to minimum wage of $15");
          //throw new IllegalArgumentException("The value of wages per hour, " +wageRate+ " is too small. Must be greater than or equal to minimum wage of $15 for employee "+employeeId);
        }
      }
      catch (NumberFormatException e)
      {
          throw new IllegalArgumentException("Problem at line #"+this.rowNumber+": Could not parse Wage Rate [" +strs[2]+ "] into valid input");
          //throw new IllegalArgumentException("Could not parse Wage Rate [" +strs[2]+ "] into valid input for employee "+employeeId);
      }
      try
      {
        this.deductions = Integer.parseInt(strs[3]);
        if (0 >= deductions || deductions >= 35)
        {
          throw new IllegalArgumentException("Problem at line #" +this.rowNumber+ ": The deduction value, "+deductions+" is out of range. Deductions must be greater than zero and less than $35");
          //throw new IllegalArgumentException("The deduction value, "+deductions+" is out of range. Deductions must be greater than zero and less than $35 for employee "+employeeId);
        }
      }
      catch (NumberFormatException e)
      {
        throw new IllegalArgumentException("Problem at line #"+this.rowNumber+": Could not parse Deductions [" +strs[3]+ "] into valid input");
        //throw new IllegalArgumentException("Could not parse Deductions [" +strs[3]+ "] into valid input for employee "+employeeId);
      }
      this.grossPay = hrsWorked * wageRate; //hours worked * wage rate
      Double Taxes = grossPay * 0.15; //gross pay × tax rate of 15%
      Double netPay = grossPay - Taxes - deductions - (grossPay * 0.05); // gross pay - taxes - deductions - pension contributions of 5% of gross pay
      if (netPay < 0)
      {
        throw new IllegalArgumentException("Error: You entered a negative number for NetPay, which was " + netPay+" on line" +this.rowNumber);
        //throw new IllegalArgumentException("%s %d%s %.2f %s%n", "Problem at line #"+rowNumber+": The Net Pay,"+netPay+ "is a negative number");
        //throw new IllegalArgumentException("The Net Pay,"+netPay+ "is a negative number for employee "+employeeId);
      }
      this.departmentForEmployee = strs[4];
    }
    else
    {
      throw new IllegalArgumentException("Problem at line #"+this.rowNumber+ ": Expected 5 tokens per line, found " +strs.length+ ". Discarding input & advancing");
      //throw new IllegalArgumentException("Expected 5 tokens per line, found " +strs.length+ ". Discarding input & advancing for employee "+employeeId);
    }
  }
  public String departmentForEmployeeMethod()
  {
    return departmentForEmployee;
  }
  public String getId()
  {
    return employeeId;
  }
  public double getGrossPay()
  {
    return grossPay;
  }
}
