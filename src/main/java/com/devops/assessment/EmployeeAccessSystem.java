package com.devops.assessment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Employee {
    private String id;
    private String name;
    private int age;
    private String department;
    private boolean isActiveEmployment;
    private boolean hasValidIdStatus;
    private int securityClearanceLevel; // 1: Low, 2: Medium, 3: High

    public Employee(String id, String name, int age, String department, 
                    boolean isActiveEmployment, boolean hasValidIdStatus, int securityClearanceLevel) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.department = department;
        this.isActiveEmployment = isActiveEmployment;
        this.hasValidIdStatus = hasValidIdStatus;
        this.securityClearanceLevel = securityClearanceLevel;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getDepartment() { return department; }
    public boolean isActiveEmployment() { return isActiveEmployment; }
    public boolean hasValidIdStatus() { return hasValidIdStatus; }
    public int getSecurityClearanceLevel() { return securityClearanceLevel; }
}

class AccessController {
    private static final List<String> VALID_DEPARTMENTS = Arrays.asList("IT", "HR", "Finance", "Administration");

    public static void evaluateAccess(Employee emp, int requestedAccessLevel) {
        List<String> violations = new ArrayList<>();
        boolean hasCriticalFailure = false;

        // Rule 1: Age Check
        if (emp.getAge() < 21) {
            violations.add("Employee is under 21 years old (Age: " + emp.getAge() + ").");
            hasCriticalFailure = true;
        }

        // Rule 2: Department Check
        if (emp.getDepartment() == null || !VALID_DEPARTMENTS.contains(emp.getDepartment())) {
            violations.add("Department '" + emp.getDepartment() + "' is not authorized.");
            hasCriticalFailure = true;
        }

        // Rule 3: Active Status Check
        if (!emp.isActiveEmployment()) {
            violations.add("Employment status is inactive.");
            hasCriticalFailure = true;
        }

        // Rule 4: ID Validity Check
        if (!emp.hasValidIdStatus()) {
            violations.add("Employee ID status is invalid.");
            hasCriticalFailure = true;
        }

        // Rule 5: Security Clearance Check
        boolean clearanceFailed = emp.getSecurityClearanceLevel() < requestedAccessLevel;
        if (clearanceFailed) {
            violations.add("Insufficient security clearance (Has: Lvl " + emp.getSecurityClearanceLevel() + 
                           ", Required: Lvl " + requestedAccessLevel + ").");
        }

        // Determine Final Status Designation
        String status;
        if (violations.isEmpty()) {
            status = "Eligible";
        } else if (clearanceFailed && !hasCriticalFailure) {
            status = "Conditionally Eligible";
        } else {
            status = "Not Eligible";
        }

        // Console Results Output
        System.out.println("------------------------------------------------");
        System.out.println("Employee ID: " + emp.getId() + " | Name: " + emp.getName());
        System.out.println("Requested Level: Lvl " + requestedAccessLevel);
        System.out.println("Status: " + status);
        if (!violations.isEmpty()) {
            System.out.println("Reasons / Violations:");
            for (String reason : violations) {
                System.out.println(" - " + reason);
            }
        }
    }
}

public class EmployeeAccessSystem {
    public static void main(String[] args) {
        System.out.println("=== EXECUTION: EMPLOYEE ACCESS ELIGIBILITY SYSTEM ===");
        int targetAccessLevel = 2; // Required level 2 resource clearance

        List<Employee> dataset = Arrays.asList(
            new Employee("EMP001", "Alice Smith", 28, "IT", true, true, 3), // Fully Eligible
            new Employee("EMP002", "Bob Jones", 35, "HR", true, true, 1),  // Conditionally Eligible (Low clearance)
            new Employee("EMP003", "Charlie Brown", 19, "Marketing", false, true, 1) // Multiple Failures
        );

        for (Employee emp : dataset) {
            AccessController.evaluateAccess(emp, targetAccessLevel);
        }
    }
}
