package edu.yu.intro;

import java.util.Scanner; // program uses class Scanner

public class OneEmployeePay

{
  // main method begins execution of Java application
      public static void main(String[] args)
      {
        Scanner s = new Scanner(System.in); // create a Scanner to obtain input from the command window

        String employeeId; // The name of the Employee
        double hrsWorked; // The amount of hours that the employee worked
        double wageRate; // How much money he makes per hour of work
        int deductions; // things you bought which cost money
        double GrossPay;//  hours worked * wage rate
        double Taxes;//  gross pay × tax rate of 15%
        double NetPay; // gross pay - taxes - deductions - pension contributions of 5% of gross pay
        double AveragePay; //net pay divided by hours worked

        System.out.println("Enter employeeId, hrsWorked, wageRate, deductions: ");


        employeeId = s.next(); // read 1st number from the user
        hrsWorked = s.nextDouble(); // read 2nd number from the user
        wageRate = s.nextDouble(); // read 3rd number from the user
        deductions = s.nextInt(); // read 4th number from the user

        GrossPay = hrsWorked * wageRate; //hours worked * wage rate
        Taxes = GrossPay * 0.15; //gross pay × tax rate of 15%
        NetPay = GrossPay - Taxes - deductions - (GrossPay * 0.05); // gross pay - taxes - deductions - pension contributions of 5% of gross pay
        AveragePay = NetPay / hrsWorked; // net pay divided by hours worked

        System.out.println("*************************************************");
        //Input values
        System.out.println("INPUT...");
        System.out.printf("%-20s %s%n", "Employee Id:", employeeId);
        System.out.printf("%-20s %.2f%n", "Hours Worked:", hrsWorked);
        System.out.printf("%-20s %.2f%n", "Wage Rate:", wageRate);
        System.out.printf("%-20s %d%n%n%n%n", "Deductions:", deductions);

        //Output Values
        System.out.println("OUTPUT...");
        System.out.printf("%-20s %.2f%n", "Gross Pay:", GrossPay);
        System.out.printf("%-20s %.2f%n", "Taxes:", Taxes);
        System.out.printf("%-20s %.2f%n", "Net Pay:", NetPay);
        System.out.printf("%-20s %.2f%n", "Average Pay:", AveragePay);

        System.out.println("*************************************************");

  }
}
