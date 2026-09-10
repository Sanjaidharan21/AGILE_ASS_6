package com.devops.assessment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private static final double MAX_DTI_RATIO = 0.45; // 45% DTI threshold
    private static final double INCOME_MULTIPLIER = 10.0; // Loan cannot exceed 10x monthly income

    public static void assessLoan(LoanApplication app) {
        Customer customer = app.getCustomer();
        List<String> rejectionReasons = new ArrayList<>();
        boolean criticalViolation = false;

        // 1. Age Verification
        if (customer.getAge() < MIN_AGE) {
            rejectionReasons.add("Customer is below minimum required age of " + MIN_AGE + ".");
            criticalViolation = true;
        }

        // 2. ID Status Check
        if (!customer.hasValidGovId()) {
            rejectionReasons.add("Customer does not possess a valid government-issued ID.");
            criticalViolation = true;
        }

        // 3. Minimum Monthly Salary Check
        if (customer.getMonthlyIncome() < MIN_INCOME) {
            rejectionReasons.add("Monthly income is below the minimum threshold of $" + MIN_INCOME + ".");
            criticalViolation = true;
        }

        // 4. Debt-to-Income (DTI) Ratio Assessment
        double dti = customer.getMonthlyIncome() > 0 
                ? (customer.getExistingMonthlyObligations() / customer.getMonthlyIncome()) 
                : 1.0;
        if (dti > MAX_DTI_RATIO) {
            rejectionReasons.add("Debt-to-Income ratio (" + String.format("%.2f", dti * 100) + "%) exceeds limit of " + (MAX_DTI_RATIO * 100) + "%.");
            criticalViolation = true;
        }

        // 5. Credit Score Minimum Verification
        if (customer.getCreditScore() < MIN_CREDIT_SCORE) {
            rejectionReasons.add("Credit score (" + customer.getCreditScore() + ") is below minimum requirement of " + MIN_CREDIT_SCORE + ".");
            criticalViolation = true;
        }

        // 6. Income-based Limit Valuation
        double maxPermissibleLoan = customer.getMonthlyIncome() * INCOME_MULTIPLIER;
        if (app.getRequestedAmount() > maxPermissibleLoan) {
            rejectionReasons.add("Requested amount ($" + app.getRequestedAmount() + ") exceeds income limit ($" + maxPermissibleLoan + ").");
        }

        // Classification Rule Execution
        String riskStatus;
        if (criticalViolation || app.getRequestedAmount() > maxPermissibleLoan) {
            riskStatus = "High Risk / Rejected";
        } else if (customer.getCreditScore() >= 750 && dti <= 0.25) {
            riskStatus = "Low Risk";
        } else {
            riskStatus = "Medium Risk";
        }

        // Output Result Logging
        System.out.println("------------------------------------------------");
        System.out.println("Customer Name: " + customer.getName());
        System.out.println("Requested Amount: $" + app.getRequestedAmount());
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

public class SmartLoanSystem {
    public static void main(String[] args) {
        System.out.println("=== EXECUTION: SMART LOAN APPROVAL SYSTEM ===");
        
        Customer c1 = new Customer("John Doe", 29, true, 6000.0, 600.0, 780);      // Low Risk Approval
        Customer c2 = new Customer("Jane Miller", 24, true, 4000.0, 1400.0, 650);  // Medium Risk Approval
        Customer c3 = new Customer("Paul Vance", 20, false, 1500.0, 900.0, 520);   // Rejected / High Risk
        
        List<LoanApplication> dataset = Arrays.asList(
            new LoanApplication(c1, 20000.0),
            new LoanApplication(c2, 15000.0),
            new LoanApplication(c3, 40000.0)
        );

        for (LoanApplication app : dataset) {
            CreditAssessment.assessLoan(app);
        }
    }
}
