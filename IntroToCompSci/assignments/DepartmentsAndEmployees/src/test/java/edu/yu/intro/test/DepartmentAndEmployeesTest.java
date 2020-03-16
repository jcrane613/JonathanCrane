package edu.yu.intro.test;
import edu.yu.intro.Department;
import edu.yu.intro.Employee;
import edu.yu.intro.Factory;
import org.junit.*;
import static org.junit.Assert.*;
public class DepartmentAndEmployeesTest
{
@Test
  public void DepartmentBuilderNumber()
  {
		Factory factory = new Factory();
		factory.newDepartment("GOV");
		factory.newDepartment("IT");
    assertEquals ("Making sure that the correct number is determined ", 2,factory.getNDepartments());
  }
@Test (expected = IllegalArgumentException.class)
  public void RepeatingDepartments()
  {
		Factory factory = new Factory();
		factory.newDepartment("GOV");
		factory.newDepartment("IT");
		factory.newDepartment("Research");
		factory.newDepartment("GOV");
		factory.newDepartment("POL");
		factory.newDepartment("SOCIAL");
		factory.newDepartment("REC");
		factory.newDepartment("GOV");
		factory.newDepartment("IT");
		factory.newDepartment("Research");
		factory.newDepartment("GOV");
		factory.newDepartment("POL");
		factory.newDepartment("SOCIAL");
		factory.newDepartment("REC");
		assertEquals ("Making sure that the correct number is determined in repeating names for department ", 7,factory.getNDepartments());
  }
@Test (expected= IllegalArgumentException.class)
  public void TooManyDepartmentException()
  {
		Factory factory = new Factory();
		factory.newDepartment("GOV");
		factory.newDepartment("IT");
		factory.newDepartment("Research");
		factory.newDepartment("GOV");
		factory.newDepartment("POL");
		factory.newDepartment("SOCIAL");
		factory.newDepartment("REC");
		factory.newDepartment("HOUSE");
		factory.newDepartment("CODING");
		factory.newDepartment("R&D");
		factory.newDepartment("GOV");
  }
@Test
	public void EmployesInputted()
	{
		Factory factory = new Factory();
		String tester = ("August 24.4 33.3 5 IT");
		factory.newEmployee(tester);
	}
@Test
	public void SameEmployesInputtedButDifferentDepartment()
	{
		Factory factory = new Factory();
    factory.newDepartment("IT");
    factory.newDepartment("Research");
		String tester = ("August 24.4 33.3 5 IT");
		String tester2 = ("August 24.4 33.3 5 Research");
		factory.newEmployee(tester);
		factory.newEmployee(tester2);
		Department[] scroller = factory.getDepartments();
		assertEquals("testing that only one member was inserted into a given department", 1, scroller[0].getNEmployees());
		assertEquals("testing that only one member was inserted into a given department", 1, scroller[1].getNEmployees());
	}
@Test (expected= IllegalArgumentException.class)
	public void SameEmployeeInputtedTwice()
	{
		Factory factory = new Factory();
    factory.newDepartment("IT");
		String tester = ("August 24.4 33.3 15 IT");
		String tester2 = ("August 24.4 23.3 15 IT");
		factory.newEmployee(tester);
		factory.newEmployee(tester2);
	}
@Test (expected= IllegalArgumentException.class)
	public void DepartmentOfEmployeeDoesntExist()
	{
		Factory factory = new Factory();
		factory.newDepartment("GOV");
		factory.newDepartment("PEOPLE");
		factory.newDepartment("R&D");
		factory.newDepartment("HR");
		String tester = "August 24.4 33.3 5 GOV";
		String tester2= "Jonathan 24.4 23.3 5 POL";
		String tester3 = "TALIA 24.4 23.3 5 PEOPLE";
		String tester4 = "MOSHE 24.4 23.3 5 R&D";
		factory.newEmployee(tester);
		factory.newEmployee(tester2);
		factory.newEmployee(tester3);
		factory.newEmployee(tester4);
	}
@Test
  public void DepartmentWasRead()
  {
    Department department = new Department("IT");
    assertEquals ("Testing that data inputted and Department readable", "IT", department.getName());
  }
@Test (expected= IllegalArgumentException.class)
  public void TooManyDepartmentsPerLine()
  {
		String tester = "IT Research";
    Department department = new Department(tester);
  }
@Test (expected= IllegalArgumentException.class)
	public void DoubleEmployee()
	{
		Department department = new Department("IT");
		Employee employeeInstance = new Employee("Jonathan 24.4 43.3 15 IT",1);
		Employee employeeInstance2 = new Employee("Jonathan 24.4 43.3 15 IT",2);
		department.addEmployee(employeeInstance);
		department.addEmployee(employeeInstance2);
	}
@Test
	public void GettingEmployeesArray()
	{
    Factory factory = new Factory();
		Department department = new Department("IT");
		factory.newEmployee("Jonathan 24.4 43.3 15 IT");
		factory.newEmployee("Moshe 22.4 43.3 15 IT");
    Department department2 = new Department("R&D");
		factory.newEmployee("Jonathan 24.4 43.3 15 R&D");
		factory.newEmployee("Moshe 22.4 43.3 15 R&D");
		Employee[] tester = department.getEmployees();
		Employee[] testerVersus = department2.getEmployees();
		assertArrayEquals(tester, testerVersus);
	}
@Test
	public void NumberofEmployees()
	{
		Department department = new Department("IT");
		Employee employeeInstance = new Employee("Jonathan 24.4 43.3 15 IT",1);
		Employee employeeInstance2 = new Employee("Moshe 24.4 43.3 15 IT",2);
    Employee employeeInstance3 = new Employee("Farrah 24.4 43.3 15 IT",3);
		department.addEmployee(employeeInstance);
		department.addEmployee(employeeInstance2);
    department.addEmployee(employeeInstance3);
		int tester = department.getNEmployees();
		assertEquals(3, tester);
	}
@Test
	public void NameOfDepartment()
		{
			Department department = new Department("IT");
			String tester= department.getName();
			assertEquals("IT", tester);
		}
@Test
	public void TotalGrossPay()
	{
		Department department = new Department("IT");
		Employee employeeInstance = new Employee("Jonathan 24.4 43.3 15 IT",1);
		Employee employeeInstance2 = new Employee("Moshe 24.4 43.3 15 IT",2);
		department.addEmployee(employeeInstance);
		department.addEmployee(employeeInstance2);
		double tester =department.getTotalGrossPay();
		assertEquals(2113.04, tester, .01);
	}
@Test
  public void EmployeeHasDepartment()
  {
		String tester = "Jonathan 24.4 43.3 15 IT";
    Employee employee = new Employee(tester, 0);
    assertEquals ("Testing that inputted and Department readable", "IT", employee.departmentForEmployeeMethod());
  }
@Test
  public void EmployeeHasID()
  {
		String tester = "Jonathan 24.4 43.3 15 IT";
    Employee employee = new Employee(tester, 1);
    assertEquals ("Testing that inputted and Id readable", "Jonathan", employee.getId());
  }
@Test
  public void EmployeeHasGrossPay()
  {
		String tester = "Jonathan 24.4 43.3 15 IT";
    Employee employee = new Employee(tester, 2);
    assertEquals (1056.52, employee.getGrossPay(), .01);
  }
@Test (expected= IllegalArgumentException.class)
  public void EmployeeHasTooManyValues()
  {
		String tester = "Jonathan 24.4 43.3 15 34 23 IT";
    Employee employee = new Employee(tester, 3);
  }
@Test (expected= IllegalArgumentException.class)
  public void EmployeeHrsWorkedInvalid()
  {
		String tester = "Jonathan Farrah 43.3 15 IT";
    Employee employee = new Employee(tester, 4);
  }
@Test (expected= IllegalArgumentException.class)
  public void EmployeeHrsWorkedTooLow()
  {
		String tester = "Jonathan .2 43.3 15 IT";
    Employee employee = new Employee(tester, 5);
  }
@Test (expected= IllegalArgumentException.class)
  public void EmployeeWageRateTooLow()
  {
		String tester = "Jonathan 24.4 12.2 15 IT";
    Employee employee = new Employee(tester, 6);
  }
@Test (expected= IllegalArgumentException.class)
  public void EmployeeWageRateInvalid()
  {
		String tester = "Jonathan 24.4 Farrah 15 IT";
    Employee employee = new Employee(tester, 7);
  }
@Test (expected= IllegalArgumentException.class)
  public void EmployeeDeductionsTooLow()
  {
		String tester = "Jonathan 24.4 43.3 37 IT";
    Employee employee = new Employee(tester, 8);
  }
@Test (expected= IllegalArgumentException.class)
  public void EmployeeDeductionsInvalid()
  {
		String tester = "Jonathan 24.4 43.3 Farrah IT";
    Employee employee = new Employee(tester, 9);
  }
@Test (expected= IllegalArgumentException.class)
  public void EmployeeNetPayInvalid()
  {
		String tester = "Jonathan 1.1 15.2 34 IT";
    Employee employee = new Employee(tester, 10);
  }

}
