import com.sun.jdi.IntegerValue;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class CubicSplineInterpolation {

    private List<Point> listOfPoints;
    private List<Double> listOfHs;
    private List<Double> listOfMs;

    public void loadPoints(double lat1, double lng1, double lat2, double lng2, int numberOfSamples) throws IOException, JSONException {

        listOfPoints = new ArrayList<>();

        //Parametry request'a.
        String dataFormat = "json";
        String apiKey = "API KEY HERE";

        URL url = new URL("https://maps.googleapis.com/maps/api/elevation/" + dataFormat + "?path=" + lat1 + "," + lng1 + "|"
                            + lat2 + "," + lng2 + "&samples=" + numberOfSamples + "&key=" + apiKey);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        //Ladowanie do string'a.
        BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("UTF-8")));
        StringBuilder content = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            content.append((char) cp);
        }

        JSONObject jsonObj = new JSONObject(content.toString());
        rd.close();

        //Pobranie wynikow do tablicy JSONow.
        JSONArray array = jsonObj.getJSONArray("results");

        //Pobieranie wspolrzednych pierwszego punktu celem obliczania odleglosci tj. "x" dla wszystkich punktow.
        double firstPointLatitude = array.getJSONObject(0).getJSONObject("location").getDouble("lat");
        double firstPointLongitude = array.getJSONObject(0).getJSONObject("location").getDouble("lng");
        double firstPointElevation = array.getJSONObject(0).getDouble("elevation");

        for (int i = 0; i < array.length(); i++)
        {
            double latitude = array.getJSONObject(i).getJSONObject("location").getDouble("lat");
            double longitude = array.getJSONObject(i).getJSONObject("location").getDouble("lng");
            double elevation = array.getJSONObject(i).getDouble("elevation");
            double distance = distance(firstPointLatitude, firstPointLongitude, firstPointElevation, latitude, longitude, elevation);
            Point point = new Point(distance, elevation, new Coordinates(latitude, longitude));
            listOfPoints.add(point);
        }
    }

    //Wyciaga z listy punktow co drugi punkt i zapisuje w innej liscie, ktora zwraca.
    public List<Point> reduceListOfPoints() {

        List<Point> points = new ArrayList<>();

        for(int i = 0; i < listOfPoints.size() - 1; i += 3) {
            points.add(listOfPoints.get(i + 1));
            points.add(listOfPoints.get(i + 2));
        }

        for(int i = 0; i < points.size(); i++) {
            listOfPoints.remove(points.get(i));
        }

        return points;
    }

    //https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude

    //Liczy odleglosc pomiedzy dwoma punktami geograficznymi
    public static double distance(double lat1, double lon1, double el1, double lat2,
                                  double lon2, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance)  / 1000.0;
    }

    //Liczy od h1 do hn
    public void countHs() {

        listOfHs = new ArrayList<>();

        for(int i = 0; i < listOfPoints.size() - 1; i++) {
            listOfHs.add(listOfPoints.get(i + 1).getX() - listOfPoints.get(i).getX());
        }
//        printList(listOfHs);
    }

    //µ (liczy od 1 do n-1)
    public List<Double> countMis() {

        List<Double> listOfMis = new ArrayList<>();

        for(int i = 0; i < listOfPoints.size() - 2; i++) {
            listOfMis.add(listOfHs.get(i) / (listOfHs.get(i) + listOfHs.get(i + 1)));
        }

        listOfMis.add(0.0);
//        printList(listOfMis);

        return listOfMis;
    }

    //λ (liczy od 1 do n-1)
    public List<Double> countLambdas() {

        List<Double> listOfLambdas = new ArrayList<>();
        listOfLambdas.add(0.0);

        for(int i = 0; i < listOfPoints.size() - 2; i++) {
            listOfLambdas.add(listOfHs.get(i + 1) / (listOfHs.get(i) + listOfHs.get(i + 1)));
        }

//        printList(listOfLambdas);

        return listOfLambdas;
    }

    //δ (liczy od 1 do n-1)
    public List<Double> countDeltas() {

        List<Double> listOfDeltas = new ArrayList<>();
        listOfDeltas.add(0.0);

        for(int i = 0; i < listOfPoints.size() - 2; i++) {

            double firstMultElement = 6 / (listOfHs.get(i) + listOfHs.get(i+1));
            double firstSubtElement = (listOfPoints.get(i + 2).getY() - listOfPoints.get(i + 1).getY()) / listOfHs.get(i + 1);
            double secondSubtElement = (listOfPoints.get(i + 1).getY() - listOfPoints.get(i).getY()) / listOfHs.get(i);
            double secondMultElement = firstSubtElement - secondSubtElement;
            listOfDeltas.add(firstMultElement * secondMultElement);
        }

        listOfDeltas.add(0.0);
//        printList(listOfDeltas);

        return listOfDeltas;
    }

    //Liczy wspolczynniki wielomianu o indeksie i
    public List<Double> countCoefficients(int i){

        List<Double> listOfCoefficients = new ArrayList<>();

        //a
        listOfCoefficients.add(listOfPoints.get(i).getY());
        //b
        double firstSubtElement = (listOfPoints.get(i+1).getY() - listOfPoints.get(i).getY()) / listOfHs.get(i);
        double secondSubtElement = ( ((2.0 * listOfMs.get(i)) + listOfMs.get(i+1)) * listOfHs.get(i) ) / 6.0;
        listOfCoefficients.add(firstSubtElement - secondSubtElement);
        //c
        listOfCoefficients.add(listOfMs.get(i) / 2.0);
        //d
        listOfCoefficients.add((listOfMs.get(i+1) - listOfMs.get(i)) / (6 * listOfHs.get(i)));

        return listOfCoefficients;
    }

    //Wylicza uklad rownan zmiennych M (nalezy okreslic, ktory algorytm ma wyliczyc uklad rownan).
    public void csi(int whichMethod, int numberOfIterations) throws Exception {

        countHs();
        List<Double> listOfMis = countMis();
        List<Double> listOfLambdas = countLambdas();
        List<Double> listOfDeltas = countDeltas();

        MyMatrix MatrixM = new MyMatrix(listOfPoints.size(), listOfPoints.size() + 1);
        MatrixM.setZero();

        for(int i = 0; i < MatrixM.getRowCount(); i++) {
            for(int j = 0; j < MatrixM.getColumnCount(); j++) {
                if(j == MatrixM.getColumnCount() - 1) {
                    MatrixM.set(i, j, listOfDeltas.get(i));
                }
                else if(i == j) {
                    MatrixM.set(i, j, 2.0);
                }
                else if(i == j + 1) {
                    MatrixM.set(i, j, listOfMis.get(j));
                }
                else if(i == j - 1) {
                    MatrixM.set(i, j, listOfLambdas.get(i));
                }
            }
        }

        MyMatrix solution = null;

        switch(whichMethod) {
            case 0:
                MatrixM.partialGauss();
                solution = MatrixM.countXFromFinalMatrix(null);
                break;
            case 1:
                solution = MatrixM.jacobIteration(numberOfIterations);
                break;
            case 2:
                solution = MatrixM.gaussSeidelIteration(numberOfIterations);
                break;
        }

        listOfMs = solution.getMatrixAsList();
    }

    public double countElevation(double x) {

        if(x < 0) {
            throw new IllegalArgumentException("The distance (x) should be greater than zero.");
        }
        else if(x > listOfPoints.get(listOfPoints.size() - 1).getX()) {
            throw new IllegalArgumentException("The distance (x) cannot be greater than distance of the last point.");
        }
        else {

            List<Double> coefficients = null;
            //Indeks j punktu we wzorze wielomianu
            int j = 0;

            //Sprawdza ktory wielomian (o jakim indeksie) ma wyliczyc.
            for (int i = 1; i < listOfPoints.size(); i++) {
                if (x == 0.0) {
                    coefficients = countCoefficients(0);
                    j = 0;
                    break;
                }
                if (x <= listOfPoints.get(i).getX()) {
                    coefficients = countCoefficients(i - 1);
                    j = i - 1;
                    break;
                }

            }

            //Wylicza wysokosc
            double elevation = coefficients.get(0) + coefficients.get(1) * (x - listOfPoints.get(j).getX()) +
                                coefficients.get(2) * Math.pow(x - listOfPoints.get(j).getX(), 2) +
                                coefficients.get(3) * (Math.pow(x - listOfPoints.get(j).getX(), 2) * (x - listOfPoints.get(j).getX()));

            return elevation;
        }
    }

    public void printList(List list) {
        for(Object o : list) {
            System.out.println(o);
        }
    }

    public CubicSplineInterpolation() {};

    public CubicSplineInterpolation(List<Point> listOfPoints) {
        this.listOfPoints = listOfPoints;
    }

    public List<Point> getListOfPoints() {
        return listOfPoints;
    }

    public void setListOfPoints(List<Point> listOfPoints) {
        this.listOfPoints = listOfPoints;
    }
}
