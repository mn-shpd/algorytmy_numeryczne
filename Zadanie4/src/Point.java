public class Point {

    //x - distance[km] from x^0 to x^i
    //y - elevation
    private double x;
    private double y;
    private Coordinates coordinates;

    public Point(double x, double y, Coordinates coordinates) {
        this.x = x;
        this.y = y;
        this.coordinates = coordinates;
    }

    public double getLatitude() {
        return coordinates.getLatitude();
    }

    public double getLongitude() {
        return coordinates.getLongitude();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return x + "\t" + y;
    }
}
