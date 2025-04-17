package lib;

public final class TaxFunction {
    private static final int PTKP_BASE       = 54_000_000;
    private static final int PTKP_MARRIED    = 4_500_000;
    private static final int PTKP_CHILD      = 4_500_000;
    private static final double TAX_RATE     = 0.05;
    private static final int MAX_CHILDREN    = 3;

    private TaxFunction() { /* utility class */ }

    public static int calculateTax(
        int monthlySalary,
        int otherMonthlyIncome,
        int numberOfMonthWorking,
        int deductible,
        boolean isMarried,
        int numberOfChildren
    ) {
        int annualIncome   = computeAnnualIncome(monthlySalary, otherMonthlyIncome, numberOfMonthWorking);
        int ptkp           = computePTKP(isMarried, numberOfChildren);
        int taxableIncome  = annualIncome - deductible - ptkp;

        if (taxableIncome <= 0) {
            return 0;
        }

        return applyTaxRate(taxableIncome);
    }

    private static int computeAnnualIncome(int salary, int otherIncome, int months) {
        return (salary + otherIncome) * months;
    }

    private static int computePTKP(boolean married, int children) {
        int total = PTKP_BASE;
        if (married) {
            total += PTKP_MARRIED;
        }
        int validChildren = Math.min(children, MAX_CHILDREN);
        total += PTKP_CHILD * validChildren;
        return total;
    }

    private static int applyTaxRate(int income) {
        return (int) Math.round(TAX_RATE * income);
    }
}
