package com.day34;

/**
 * import arraylist class
 * import list class
 * import scanner class
 */

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.day34.EmployeePayrollException.Exception;
import java.util.logging.Logger;

public class EmployeePayrollService {
    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO
    }

    Logger log = Logger.getLogger(EmployeePayrollService .class.getName());
    public List<EmployeePayrollData> employeePayrollList;
    private EmployeePayrollDbService employeePayrollDBService;
    private EmployeePayrollNewDBService employeePayrollNewDBService;
    private Map<String, Double> employeePayrollMap;

    public EmployeePayrollService () {
        employeePayrollDBService = EmployeePayrollDbService.getInstance();
        employeePayrollNewDBService = EmployeePayrollNewDBService.getInstance();
    }

    public EmployeePayrollService (List<EmployeePayrollData> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }

    public EmployeePayrollService (Map<String, Double> employeePayrollMap) {
        this();
        this.employeePayrollMap = employeePayrollMap;
    }
    public void readEmployeeData(Scanner consoleInputReader) {
        log.info("Enter employee ID : ");
        int id = Integer.parseInt(consoleInputReader.nextLine());
        log.info("Enter employee name : ");
        String name = consoleInputReader.nextLine();
        log.info("Enter employee salary : ");
        double salary = Double.parseDouble(consoleInputReader.nextLine());
        employeePayrollList.add(new EmployeePayrollData(id, name, salary));
    }

    public List<EmployeePayrollData> readEmployeepayrollData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().readData();
        if (ioService.equals(IOService.DB_IO))
            return new EmployeePayrollNewDBService().readData();
        else
            return null;
    }

    public List<EmployeePayrollData> readPayrollDataForRange(IOService ioService, LocalDate startDate,
                                                               LocalDate endDate) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollNewDBService.readData();
        return employeePayrollList;
    }

    public void writeEmployeeData(IOService ioService) {
        if (ioService.equals(IOService.CONSOLE_IO))
            log.info("Employee Payroll Data to Console\n" + employeePayrollList);
        else if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().writeData(employeePayrollList);
    }

    public void printData(IOService ioService) {
        new EmployeePayrollFileIOService().printData();
    }

    public long countEntries(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().countEntries();
        return employeePayrollList.size();
    }

    public void updateEmployeeSalary(String name, double salary) throws EmployeePayrollException {
        int result = employeePayrollNewDBService.updateEmployeeData(name, salary);
        if (result == 0) {
            throw new EmployeePayrollException(Exception.DATA_NULL, "No Data to update ");
        }
        EmployeePayrollData employeePayrollData = this.getEmployee_payroll_Data(name);
        if (employeePayrollData != null)
            employeePayrollData.salary = salary;
    }

    public void addEmployeeToPayRoll(String name, double salary, LocalDate start, String gender) {
        employeePayrollList.add(employeePayrollNewDBService.addEmployeeToPayroll(name, salary, start, gender));
    }

    private EmployeePayrollData getEmployee_payroll_Data(String name) {
        return this.employeePayrollList.stream().filter(emp_Data -> emp_Data.name.equals(name)).findFirst()
                .orElse(null);
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollList.get(0).equals(getEmployee_payroll_Data(name));
    }

    public Map<String, Double> readPayrollDataForAvgSalary(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollMap = employeePayrollDBService.get_AverageSalary_ByGender();
        return employeePayrollMap;
    }

    public Map<String, Double> readPayrollDataForSumSalary(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollMap = employeePayrollDBService.get_SumOfSalary_ByGender();
        return employeePayrollMap;
    }

    public Map<String, Double> readPayrollDataForMaxSalary(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollMap = employeePayrollNewDBService.get_Max_Salary_ByGender();
        return employeePayrollMap;
    }

    public Map<String, Double> readPayrollDataForMinSalary(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollMap = employeePayrollDBService.get_Min_Salary_ByGender();
        return employeePayrollMap;
    }

    public Map<String, Double> readPayrollDataFor_CountOfEmployee_ByGender(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollMap = employeePayrollNewDBService.get_CountOfEmployee_ByGender();
        return employeePayrollMap;
    }

    /**
     * create a main method,all program execute in main method
     * @param args no arguments,its default.
     */
    public static void main(String[] args) {
        /**
         * create a list object name as  employeePayrollList
         * here EmployeePayrollData is a class.
         */
        List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
        /**
         * create a object for  EmployeePayrollService class ,object name as employeePayrollService
         */
        EmployeePayrollService employeePayrollService = new   EmployeePayrollService(employeePayrollList);
        /**
         * create a scanner class object name as object is consoleInputReader
         */
        Scanner consoleInputReader = new Scanner(System.in);
        /**
         * calling readEmployeeData method from employeePayrollService object
         */
        employeePayrollService.readEmployeeData(consoleInputReader);
        /**
         * calling writeEmployeeData method from employeePayrollService object
         */
        employeePayrollService.writeEmployeeData(IOService.CONSOLE_IO);
    }


}