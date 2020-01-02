public class Stats {

    private int iterations;
    private long time;
    private Double objectiveFunctionResult;

    public Stats(int iterations, long time, Double objectiveFunctionResult) {
        this.iterations = iterations;
        this.time = time;
        this.objectiveFunctionResult = objectiveFunctionResult;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Double getObjectiveFunctionResult() {
        return objectiveFunctionResult;
    }

    public void setObjectiveFunctionResult(Double objectiveFunctionResult) {
        this.objectiveFunctionResult = objectiveFunctionResult;
    }
}
