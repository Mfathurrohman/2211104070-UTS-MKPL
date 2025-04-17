package lib;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Employee {
    public enum Gender { MALE, FEMALE }

    private final String employeeId;
    private final String firstName;
    private final String lastName;
    private final String idNumber;
    private final String address;
    private final LocalDate joinedDate;
    private final boolean isForeigner;
    private final Gender gender;

    private int monthlySalary;
    private int otherMonthlyIncome;
    private int annualDeductible;

    private Dependent spouse;
    private final List<Dependent> children = new ArrayList<>();

    private Employee(Builder b) {
        this.employeeId      = b.employeeId;
        this.firstName       = b.firstName;
        this.lastName        = b.lastName;
        this.idNumber        = b.idNumber;
        this.address         = b.address;
        this.joinedDate      = b.joinedDate;
        this.isForeigner     = b.isForeigner;
        this.gender          = b.gender;
    }

    public static class Builder {
        private final String employeeId, firstName, lastName, idNumber, address;
        private final LocalDate joinedDate;
        private final boolean isForeigner;
        private final Gender gender;

        public Builder(String employeeId,
                       String firstName,
                       String lastName,
                       String idNumber,
                       String address,
                       LocalDate joinedDate,
                       boolean isForeigner,
                       Gender gender) {
            this.employeeId  = Objects.requireNonNull(employeeId);
            this.firstName   = Objects.requireNonNull(firstName);
            this.lastName    = Objects.requireNonNull(lastName);
            this.idNumber    = Objects.requireNonNull(idNumber);
            this.address     = Objects.requireNonNull(address);
            this.joinedDate  = Objects.requireNonNull(joinedDate);
            this.isForeigner = isForeigner;
            this.gender      = Objects.requireNonNull(gender);
        }

        public Employee build() {
            return new Employee(this);
        }
    }

    public static class Dependent {
        private final String name, idNumber;
        public Dependent(String name, String idNumber) {
            this.name     = Objects.requireNonNull(name);
            this.idNumber = Objects.requireNonNull(idNumber);
        }
        public String getName()     { return name; }
        public String getIdNumber() { return idNumber; }
    }

    public void setMonthlySalary(int grade) {
        switch (grade) {
            case 1: monthlySalary = 3_000_000; break;
            case 2: monthlySalary = 5_000_000; break;
            case 3: monthlySalary = 7_000_000; break;
            default: throw new IllegalArgumentException("Grade invalid: " + grade);
        }
        if (isForeigner) monthlySalary = (int)(monthlySalary * 1.5);
    }

    public void setAnnualDeductible(int deductible) {
        this.annualDeductible = deductible;
    }

    public void setAdditionalIncome(int income) {
        this.otherMonthlyIncome = income;
    }

    public void setSpouse(String spouseName, String spouseIdNumber) {
        this.spouse = new Dependent(spouseName, spouseIdNumber);
    }

    public void addChild(String childName, String childIdNumber) {
        children.add(new Dependent(childName, childIdNumber));
    }

    public int getAnnualIncomeTax() {
        LocalDate now = LocalDate.now();
        int monthsWorked =
            now.getYear() == joinedDate.getYear()
                ? now.getMonthValue() - joinedDate.getMonthValue()
                : 12;

        boolean hasSpouse = (spouse != null);
        int numChildren   = children.size();

        return TaxFunction.calculateTax(
            monthlySalary,
            otherMonthlyIncome,
            monthsWorked,
            annualDeductible,
            hasSpouse,
            numChildren
        );
    }

    // (Getter minimal—sesuaikan kebutuhan)
    public String getEmployeeId()    { return employeeId; }
    public LocalDate getJoinedDate() { return joinedDate; }
    public List<Dependent> getChildren() {
        return Collections.unmodifiableList(children);
    }
    public Dependent getSpouse() { return spouse; }
}
