package edu.yu.intro;
import edu.yu.intro.Factory;
public class Department
{
  public final static int MAX_EMPLOYEES = 100;
  public int counterForEmployees = 0;
  public Employee[] employees = new Employee[100];
  public String department;
  public Department (String lines)
  {
    String[] wordList = lines.trim().split("\\s+");
    if (wordList.length != 1)
    {
      throw new IllegalArgumentException("Too many departments per line. No more than 1 may be inputted");
    }
    this.department = lines;
  }
  public void addEmployee(Employee input)
  {
    if (counterForEmployees >= MAX_EMPLOYEES)
    {
      throw new IllegalArgumentException("Error: No more than 100 employees may be in any department");
    }
    String testerVersus = input.getId();
    for (int i =0; i<counterForEmployees; i++)
    {
      String tester = employees[i].getId();
      if (tester.equals(testerVersus))
      {
        throw new IllegalArgumentException("The employeeId "+testerVersus+ " already exists. Employee names can't be repeated, discarding data and moving on");
      }
    }
    this.employees[counterForEmployees] = input;
    counterForEmployees++;
  }
  public Employee[] getEmployees()
  {
    return employees;
  }
  public int getNEmployees()
  {
    return counterForEmployees;
  }
  public String getName()
  {
    return department;
  }
  public double getTotalGrossPay()
  {
    double totalValue = 0.0;
    for (int i = 0; i<counterForEmployees; i++)
    {
      double total = employees[i].getGrossPay();
      totalValue += total;
    }
    return totalValue;
  }
}
