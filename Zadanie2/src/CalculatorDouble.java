public class CalculatorDouble implements Calculator<Double> {

    @Override
    public Double add(Double a, Double b) {
        return a + b;
    }

    @Override
    public Double subtract(Double first, Double second) {
        return first - second;
    }

    @Override
    public Double multiply(Double first, Double second) {
        return first * second;
    }

    @Override
    public Double divide(Double first, Double second) {
        return first / second;
    }

    @Override
    public int compareTo(Double first, Double second) {
        if(first > second) {
            return 1;
        }
        if(first < second) {
            return -1;
        }
        else {
            return 0;
        }
    }

    @Override
    public Double returnZero() {
        return 0.0d;
    }

    @Override
    public Double abs(Double x) {
        return Math.abs(x);
    }

    @Override
    public Double convertInt(int x) {
        return Double.valueOf(x);
    }

    @Override
    public Double parse(String s) {
        return Double.parseDouble(s);
    }
}
