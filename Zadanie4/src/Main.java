import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void printList(List list) {
        for(Object o : list) {
            System.out.println(o);
        }
    }

    public static void main(String[] args) throws Exception {

        //Trasa plaska (okolo 19,5 km)
        //40.740488, -113.867667, 40.736066, -113.697379,
        //Trasa z wyraznym wzniesieniem (okolo 13,5 km)
        //49.151592, 19.797682, 49.270740, 19.842228,
        //Trasa z wieloma wniesieniami (okolo 45 km)
        //45.042119, 6.418056, 45.286274, 6.876964,
        //Trasa z depresja (okolo 15 km)
        //43.019260, 89.135147, 42.882669, 89.144656,

        CubicSplineInterpolation algorytm = new CubicSplineInterpolation();
        algorytm.loadPoints(49.151592, 19.797682, 49.270740, 19.842228, 100);

        //Rzeczywista wysokosc dla wszystkich punktow.
        List<Point> listOfPointsBeforeReduction = new ArrayList<>(algorytm.getListOfPoints());
        printList(listOfPointsBeforeReduction);

        //Redukcja punktow (wybiera co trzeci indeks) i zwraca liste usunietych punktow.
        List<Point> takenPoints = algorytm.reduceListOfPoints();

        // 0 - Partial Gauss
        // 1 - Jacob Iteration
        // 2 - Gauss-Seidel Iteration

        algorytm.csi(1, 20);

        System.out.println("---------------");

        //TESTY

        //Wysokosci wyliczone z powstalych wielomianow dla wszystkich punktow.
        for(int i = 0; i < listOfPointsBeforeReduction.size(); i++) {

            double calculatedElevation = algorytm.countElevation(listOfPointsBeforeReduction.get(i).getX());
            System.out.println(listOfPointsBeforeReduction.get(i).getX() + "\t" + calculatedElevation);
        }

        //Lista punktow po redukcji (tylko wezly biorace udzial w algorytmie CSI).
        List<Point> listOfPointsAfterReduction = algorytm.getListOfPoints();

        //Sredni blad interpolacji w wezlach.
        double sum = 0;

        for(int i = 0; i < listOfPointsAfterReduction.size(); i++) {

            double calculatedElevation = algorytm.countElevation(listOfPointsAfterReduction.get(i).getX());
            sum += Math.abs(listOfPointsAfterReduction.get(i).getY() - calculatedElevation);
        }

        System.out.println("Usredniony blad interpolacji w wezlach: " + sum / listOfPointsAfterReduction.size());

        //Sredni blad interpolacji w punktach pomiedzy wezlami.
        sum = 0;

        for(int i = 0; i < takenPoints.size(); i++) {
            double calculatedElevation = algorytm.countElevation(takenPoints.get(i).getX());
            sum += Math.abs(takenPoints.get(i).getY() - calculatedElevation);
        }

        System.out.println("Usredniony blad interpolacji w punktach pomiedzy wezlami: " + sum / takenPoints.size());

        //C2 WSPOLCZYNNIKI TRZECH PIERWSZYCH WIELOMIANOW

//        CubicSplineInterpolation algorytm = new CubicSplineInterpolation();
//        algorytm.loadPoints(45.042119, 6.418056, 45.286274, 6.876964, 100);
//        algorytm.reduceListOfPoints();
//
//        algorytm.csi(2, 20);
//
//        printList(algorytm.countCoefficients(0));
//        System.out.println("--------------");
//        printList(algorytm.countCoefficients(1));
//        System.out.println("--------------");
//        printList(algorytm.countCoefficients(2));

        //TEST ZALEZNOSC CZASU WYKONYWANIA OD LICZBY WEZLOW

//        long time;
//
//        CubicSplineInterpolation algorytm = new CubicSplineInterpolation();
//        algorytm.loadPoints(45.042119, 6.418056, 45.286274, 6.876964, 500);
//
//        for(int i = 50; i <= 500; i += 50) {
//            algorytm = new CubicSplineInterpolation();
//            algorytm.loadPoints(45.042119, 6.418056, 45.286274, 6.876964, i);
//            algorytm.csi(0, 20);
//        }
//
//        for(int i = 50; i <= 500; i += 50) {
//
//            algorytm = new CubicSplineInterpolation();
//            algorytm.loadPoints(45.042119, 6.418056, 45.286274, 6.876964, i);
//            long startTime = System.nanoTime();
//            algorytm.csi(0, 20);
//
//            System.out.println((System.nanoTime() - startTime));
//        }

        //E3 TEST ZALEZNOSC CZASU WYKONYWANIA OD ILOSCI ITERACJI

//        int numberOfIterations = 30;
//
//        CubicSplineInterpolation algorytm = new CubicSplineInterpolation();
//        algorytm.loadPoints(45.042119, 6.418056, 45.286274, 6.876964, 500);
//
//        for(int i = 1; i < numberOfIterations; i++) {
//            algorytm = new CubicSplineInterpolation();
//            algorytm.loadPoints(45.042119, 6.418056, 45.286274, 6.876964, 500);
//            algorytm.csi(1, i);
//        }
//
//        for(int i = 1; i <= numberOfIterations; i+=1) {
//
//            algorytm = new CubicSplineInterpolation();
//            algorytm.loadPoints(45.042119, 6.418056, 45.286274, 6.876964, 500);
//
//            long startTime = System.nanoTime();
//
//            algorytm.csi(1, i);
//
//            System.out.println(i + "\t" + (System.nanoTime() - startTime));
//        }
    }
}
