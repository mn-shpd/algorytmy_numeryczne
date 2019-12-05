public class CalculatorFloat implements Calculator<Float> {

    @Override
    public Float add(Float a, Float b) {
        return a + b;
    }

    @Override
    public Float subtract(Float first, Float second) {
        return first - second;
    }

    @Override
    public Float multiply(Float first, Float second) {
        return first * second;
    }

    @Override
    public Float divide(Float first, Float second) {
        return first / second;
    }

    @Override
    public int compareTo(Float first, Float second) {
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
    public Float returnZero() {
        return 0.0f;
    }

    @Override
    public Float abs(Float x) {
        return Math.abs(x);
    }

    @Override
    public Float convertInt(int x) {
        return Float.valueOf(x);
    }

    @Override
    public Float parse(String s) {
        return Float.parseFloat(s);
    }
}
