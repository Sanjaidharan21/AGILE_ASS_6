package com.devops.assessment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ==========================================
// QUESTION 1: EMPLOYEE ACCESS SYSTEM DOMAIN
// ==========================================
class Employee {
    private String id;
    private String name;
    private int age;
    private String department;
    private boolean isActiveEmployment;
    private boolean hasValidIdStatus;
    private int securityClearanceLevel;

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

        if (emp.getAge() < 21) {
            violations.add("Employee is under 21 years old (Age: " + emp.getAge() + ").");
            hasCriticalFailure = true;
        }
        if (emp.getDepartment() == null || !VALID_DEPARTMENTS.contains(emp.getDepartment())) {
            violations.add("Department '" + emp.getDepartment() + "' is not authorized.");
            hasCriticalFailure = true;
        }
        if (!emp.isActiveEmployment()) {
            violations.add("Employment status is inactive.");
            hasCriticalFailure = true;
        }
        if (!emp.hasValidIdStatus()) {
            violations.add("Employee ID status is invalid.");
            hasCriticalFailure = true;
        }

        boolean clearanceFailed = emp.getSecurityClearanceLevel() < requestedAccessLevel;
        if (clearanceFailed) {
            violations.add("Insufficient security clearance (Has: Lvl " + emp.getSecurityClearanceLevel() + 
                           ", Required: Lvl " + requestedAccessLevel + ").");
        }

        String status;
        if (violations.isEmpty()) {
            status = "Eligible";
        } else if (clearanceFailed && !hasCriticalFailure) {
            status = "Conditionally Eligible";
        } else {
            status = "Not Eligible";
        }

        System.out.println("------------------------------------------------");
        System.out.println("Employee ID: " + emp.getId() + " | Name: " + emp.getName());
        System.out.println("Status: " + status);
        if (!violations.isEmpty()) {
            System.out.println("Reasons / Violations:");
            for (String reason : violations) {
                System.out.println(" - " + reason);
            }
        }
    }
}

// ==========================================
// QUESTION 2: SMART LOAN SYSTEM DOMAIN
// ==========================================
class Customer {
    private String name;
    private int age;
    private boolean hasValidGovId;
    private double monthlyIncome;
    private double existingMonthlyObligations;
    private int creditScore;

    public Customer(String name, int age, boolean hasValidGovId, double monthlyIncome, 
                    double existingMonthlyObligations, int creditScore) {
        this.name = name;
        this.age = age;
        this.hasValidGovId = hasValidGovId;
        this.monthlyIncome = monthlyIncome;
        this.existingMonthlyObligations = existingMonthlyObligations;
        this.creditScore = creditScore;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public boolean hasValidGovId() { return hasValidGovId; }
    public double getMonthlyIncome() { return monthlyIncome; }
    public double getExistingMonthlyObligations() { return existingMonthlyObligations; }
    public int getCreditScore() { return creditScore; }
}

class LoanApplication {
    private Customer customer;
    private double requestedAmount;

    public LoanApplication(Customer customer, double requestedAmount) {
        this.customer = customer;
        this.requestedAmount = requestedAmount;
    }

    public Customer getCustomer() { return customer; }
    public double getRequestedAmount() { return requestedAmount; }
}

class CreditAssessment {
    private static final int MIN_AGE = 21;
    private static final double MIN_INCOME = 2000.0;
    private static final int MIN_CREDIT_SCORE = 600;
    private static final double MAX_DTI_RATIO = 0.45;
    private static final double INCOME_MULTIPLIER = 10.0;

    public static void assessLoan(LoanApplication app) {
        Customer customer = app.getCustomer();
        List<String> rejectionReasons = new ArrayList<>();
        boolean criticalViolation = false;

        if (customer.getAge() < MIN_AGE) {
            rejectionReasons.add("Customer is below minimum required age of " + MIN_AGE + ".");
            criticalViolation = true;
        }
        if (!customer.hasValidGovId()) {
            rejectionReasons.add("Customer does not possess a valid government-issued ID.");
            criticalViolation = true;
        }
        if (customer.getMonthlyIncome() < MIN_INCOME) {
            rejectionReasons.add("Monthly income is below the minimum threshold of $" + MIN_INCOME + ".");
            criticalViolation = true;
        }

        double dti = customer.getMonthlyIncome() > 0 ? (customer.getExistingMonthlyObligations() / customer.getMonthlyIncome()) : 1.0;
        if (dti > MAX_DTI_RATIO) {
            rejectionReasons.add("Debt-to-Income ratio (" + String.format("%.2f", dti * 100) + "%) exceeds limit of " + (MAX_DTI_RATIO * 100) + "%.");
            criticalViolation = true;
        }
        if (customer.getCreditScore() < MIN_CREDIT_SCORE) {
            rejectionReasons.add("Credit score (" + customer.getCreditScore() + ") is below minimum requirement of " + MIN_CREDIT_SCORE + ".");
            criticalViolation = true;
        }

        double maxPermissibleLoan = customer.getMonthlyIncome() * INCOME_MULTIPLIER;
        if (app.getRequestedAmount() > maxPermissibleLoan) {
            rejectionReasons.add("Requested amount ($" + app.getRequestedAmount() + ") exceeds maximum income limit ($" + maxPermissibleLoan + ").");
        }

        String riskStatus;
        if (criticalViolation || app.getRequestedAmount() > maxPermissibleLoan) {
            riskStatus = "High Risk / Rejected";
        } else if (customer.getCreditScore() >= 750 && dti <= 0.25) {
            riskStatus = "Low Risk";
        } else {
            riskStatus = "Medium Risk";
        }

        System.out.println("------------------------------------------------");
        System.out.println("Customer Name: " + customer.getName());
        System.out.println("Requested Amount: $" + app.getRequestedAmount());
        System.out.println("Max Permissible Loan: $" + maxPermissibleLoan);
        System.out.println("Risk Status: " + riskStatus);
        
        if (!rejectionReasons.isEmpty()) {
            System.out.println("Rejection Reasons:");
            for (String reason : rejectionReasons) {
                System.out.println(" - " + reason);
            }
        } else {
            System.out.println("Application Status: Approved.");
        }
    }
}

// ==========================================
// MAIN RUNNER PROGRAM WITH DEFAULT INPUTS
// ==========================================
public class App {
    public static void main(String[] args) {
        System.out.println("=== EXECUTION PART 1: EMPLOYEE ACCESS EVALUATION ===");
        List<Employee> mockEmployees = Arrays.asList(
            new Employee("EMP01", "Alice", 25, "IT", true, true, 3),        // Eligible
            new Employee("EMP02", "Bob", 30, "HR", true, true, 1),          // Conditionally Eligible
            new Employee("EMP03", "Charlie", 19, "Marketing", false, false, 1) // Multiple Failures
        );
        for (Employee e : mockEmployees) {
            AccessController.evaluateAccess(e, 2);
        }

        System.out.println("\n=== EXECUTION PART 2: SMART LOAN ASSESSMENT ===");
        Customer c1 = new Customer("John Doe", 29, true, 6000.0, 600.0, 780);      // Low Risk
        Customer c2 = new Customer("Jane Miller", 24, true, 4000.0, 1400.0, 650);  // Medium Risk
        Customer c3 = new Customer("Paul Vance", 20, false, 1500.0, 900.0, 520);   // High Risk / Rejection
        
        List<LoanApplication> mockLoans = Arrays.asList(
            new LoanApplication(c1, 20000),
            new LoanApplication(c2, 15000),
            new LoanApplication(c3, 40000)
        );
        for (LoanApplication loan : mockLoans) {
            CreditAssessment.assessLoan(loan);
        }
    }
}
