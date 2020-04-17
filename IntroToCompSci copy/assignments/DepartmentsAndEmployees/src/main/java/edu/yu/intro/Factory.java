package edu.yu.intro;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.*;
import java.util.Scanner;
import java.io.FileNotFoundException;
/** @author Jonathan Crane


*/
public class Factory
{
  public final static int MAX_DEPARTMENTS = 10;
  public Department[] departmentInstances = new Department[10];
  public int counterForDepartments = 0;
  public int employeeRowNumber = 0;
  public Factory()
  {}
  /** Returns the array of departments constructed by this factory instance.
  *
  * @return Department array
  */
  public Department[] getDepartments()
  {
    return departmentInstances;
  }
  public int getNDepartments()
  {
    return counterForDepartments;
  }
  /** Creates a Department instance from the parameter’s tokens and adds
  * instance to array.
  * @param input Enter a token of one String that will be a Department name
  * @return new Department instance
  * @throws IllegalArgumentException if mismatch on expected number of tokens, token type,
  * or if a department with the (implicitly) specified name has
  * already been created. See the requirements document for the specific
  * syntax and semantics.
  */
  public Department newDepartment(final String input)
  {
    if (counterForDepartments >= MAX_DEPARTMENTS)
    {
      throw new IllegalArgumentException("Error: Too many departments in the file. No more than 10 may be inputted");
    }
    Department departmentInstance = new Department(input);
    String nameOfThisInstance = departmentInstance.getName();
    for (int i = 0; i<counterForDepartments;i++)
    {
      String knownDepartment = departmentInstances[i].getName();
      if (nameOfThisInstance.equals(knownDepartment))
      {
        throw new IllegalArgumentException("The department " +nameOfThisInstance+ " is repeated. Discarding the second one and continuing");
      }
    }
    departmentInstances[counterForDepartments] = departmentInstance;
    counterForDepartments++;
    return departmentInstance;
  }
  /** Creates an Employee instance from the parameter’s tokens and adds
  * instance to appropriate Department.
  * @param input Enter a String consisiting of a string, double, double, integar and a string
  * @return new Employee instance
  * @throws IllegalArgumentException if mismatch on expected number of
  tokens ,
  * token types , or if the input references an unknown department , or if an
  * Employee with the (implicitly) specified name has already been created.
  See
  * the requirements document for the specific syntax and semantics.
  */
  public Employee newEmployee(final String input)
  {
    employeeRowNumber++;
    Employee employeeInstance = new Employee(input,employeeRowNumber);
    String nameOfThisDepartment = employeeInstance.departmentForEmployeeMethod();
    for (int i = 0; i<counterForDepartments;i++)
    {
      String knownDepartment = departmentInstances[i].getName();
      if (nameOfThisDepartment.equals(knownDepartment))
      {
        departmentInstances[i].addEmployee(employeeInstance);
        break;
      }
      if (i+1 == counterForDepartments)
      {
        throw new IllegalArgumentException("Problem at line #"+employeeRowNumber+" The department for employee " +employeeInstance.getId()+ " does not match any known departments");
      }
    }
    return employeeInstance;
  }
  public static void main (String[] args)
  {
    if (args.length != 2)
    {
      final String msg = "Usage: DepartmentsAndEmployees employeeInputFile deptInputFile";
      System.err.println(msg);
      throw new IllegalArgumentException(msg);
    }
    try
    {
      Factory factory = new Factory();
      final String deptInputFile = args[1];
      File deptInput = new File(deptInputFile);
      Scanner deptInputScanner = new Scanner(deptInput);
      while (deptInputScanner.hasNext())
      {
        try
        {
          String lines = deptInputScanner.nextLine();
          factory.newDepartment(lines);
        }
        catch (IllegalArgumentException e)
        {
          if (e.getMessage().equals("Error: Too many departments in the file. No more than 10 may be inputted"))
            {
              throw new IllegalArgumentException(e.getMessage());
            }
          System.out.println(e.getMessage());
        }
      }
      final String employeeInputFile = args[0];
      File employeeInput = new File(employeeInputFile);
      Scanner employeeInputScanner = new Scanner(employeeInput);
      while (employeeInputScanner.hasNext())
      {
        try
        {
          String lines = employeeInputScanner.nextLine();
          factory.newEmployee(lines);
        }
        catch (IllegalArgumentException e)
        {
          if (e.getMessage().equals("Error: No more than 100 employees may be in any department"))
            {
              throw new IllegalArgumentException(e.getMessage());
            }
          System.out.println(e.getMessage());
        }
      }
      System.out.println("*************************************************");
      System.out.printf("%15s %15s %14s %15s%n", "Department","# Employees","Total Gross Pay","Average Gross Pay");
        Department[] roller = factory.getDepartments();
        for (int p = 0; p < factory.getNDepartments(); p++)
        {
          try
          {
            String name = roller[p].getName();
            int number = roller[p].getNEmployees();
            double grosses = roller[p].getTotalGrossPay();
            double averageGroses;
            if (number != 0)
            {
              averageGroses = (grosses/number);
            }
            else
            {
              averageGroses = 0;
            }
            System.out.printf("%15s %15d %,14.2f %,15.2f%n",name, number, grosses, averageGroses);
          }
          catch (IllegalArgumentException e)
          {
            System.out.println(e.getMessage());
          }
        }
        System.out.println("*************************************************");
    }
    catch (FileNotFoundException e)
    {
      System.out.println("File not found with the path specified");
    }
    catch (IllegalArgumentException e)
    {
      System.out.println(e.getMessage());
    }
  }
}
