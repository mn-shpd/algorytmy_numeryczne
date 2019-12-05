import java.io.IOException;
import java.util.concurrent.TimeUnit;
//
public class Main {

    public static void countStatsForFloat(int GaussOption) throws IOException {

        switch (GaussOption) {
            case 0:
                System.out.println("BasicGauss");
                break;
            case 1:
                System.out.println("PartialGauss");
                break;
            case 2:
                System.out.println("CompleteGauss");
                break;
        }

        System.out.println("R.M.\tTIME\tAVG.DIFF.");

        for(int i = 20; i <= 200; i += 20) {

            MyMatrix<Float> floatMatrixA = new MyMatrix<Float>(new CalculatorFloat(), i, i);
            floatMatrixA.readFromFile("FloatValues.txt");
            MyMatrix<Float> floatVectorX = new MyMatrix<Float>(new CalculatorFloat(), i, 1);
            floatVectorX.readFromFile("FloatValuesX.txt");
            MyMatrix<Float> floatVectorB = floatMatrixA.multiply(floatVectorX);
            MyMatrix<Float> temp = floatMatrixA.mergeWithB(floatVectorB);

            long start = System.nanoTime();

            MyMatrix<Float> newVectorX = null;

            switch(GaussOption) {
                case 0:
                    temp.basicGauss();
                    newVectorX = temp.countXFromFinalMatrix(null);
                    break;
                case 1:
                    temp.partialGauss();
                    newVectorX = temp.countXFromFinalMatrix(null);
                    break;
                case 2:
                    int[] variableOrder = temp.completeGauss();
                    newVectorX = temp.countXFromFinalMatrix(variableOrder);
                    break;
            }

            long time = System.nanoTime() - start;
            System.out.println(i + "\t" + TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS) + "\t" + floatVectorX.countAverageDifference(newVectorX));

        }
    }

    public static void countStatsForDouble(int GaussOption) throws IOException {

        switch (GaussOption) {
            case 0:
                System.out.println("BasicGauss");
                break;
            case 1:
                System.out.println("PartialGauss");
                break;
            case 2:
                System.out.println("CompleteGauss");
                break;
        }

        System.out.println("R.M.\tTIME\tAVG.DIFF.");

        for(int i = 20; i <= 200; i += 20) {

            MyMatrix<Double> doubleMatrixA = new MyMatrix<Double>(new CalculatorDouble(), i, i);
            doubleMatrixA.readFromFile("DoubleValues.txt");
            MyMatrix<Double> doubleVectorX = new MyMatrix<Double>(new CalculatorDouble(), i, 1);
            doubleVectorX.readFromFile("DoubleValuesX.txt");
            MyMatrix<Double> doubleVectorB = doubleMatrixA.multiply(doubleVectorX);
            MyMatrix<Double> temp = doubleMatrixA.mergeWithB(doubleVectorB);

            long start = System.nanoTime();

            MyMatrix<Double> newVectorX = null;

            switch(GaussOption) {
                case 0:
                    temp.basicGauss();
                    newVectorX = temp.countXFromFinalMatrix(null);
                    break;
                case 1:
                    temp.partialGauss();
                    newVectorX = temp.countXFromFinalMatrix(null);
                    break;
                case 2:
                    int[] variableOrder = temp.completeGauss();
                    newVectorX = temp.countXFromFinalMatrix(variableOrder);
                    break;
            }
            long time = System.nanoTime() - start;
            System.out.println(i + "\t" + TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS) + "\t" + doubleVectorX.countAverageDifference(newVectorX));

        }
    }

    public static void countStatsForMyOwnType(int GaussOption) throws IOException {

        switch (GaussOption) {
            case 0:
                System.out.println("BasicGauss");
                break;
            case 1:
                System.out.println("PartialGauss");
                break;
            case 2:
                System.out.println("CompleteGauss");
                break;
        }

        System.out.println("R.M.\tTIME\tAVG.DIFF.");

        for(int i = 20; i <= 200; i += 20) {

            MyMatrix<MyOwnType> myOwnTypeMatrixA = new MyMatrix<MyOwnType>(new CalculatorMyOwnType(), i, i);
            myOwnTypeMatrixA.readFromFile("MyOwnTypeValues.txt");
            MyMatrix<MyOwnType> myOwnTypeVectorX = new MyMatrix<MyOwnType>(new CalculatorMyOwnType(), i, 1);
            myOwnTypeVectorX.readFromFile("MyOwnTypeValuesX.txt");
            MyMatrix<MyOwnType> myOwnTypeVectorB = myOwnTypeMatrixA.multiply(myOwnTypeVectorX);
            MyMatrix<MyOwnType> temp = myOwnTypeMatrixA.mergeWithB(myOwnTypeVectorB);

            MyMatrix<MyOwnType> newVectorX = null;

            long start = System.nanoTime();

            switch(GaussOption) {
                case 0:
                    temp.basicGauss();
                    newVectorX = temp.countXFromFinalMatrix(null);
                    break;
                case 1:
                    temp.partialGauss();
                    newVectorX = temp.countXFromFinalMatrix(null);
                    break;
                case 2:
                    int[] variableOrder = temp.completeGauss();
                    newVectorX = temp.countXFromFinalMatrix(variableOrder);
                    break;
            }

            long time = System.nanoTime() - start;
            System.out.println(i + "\t" + TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS) + "\t" + myOwnTypeVectorX.countAverageDifference(newVectorX));

        }
    }

    public static void main(String[] args) throws IOException {

//        Generator.generateValuesToFile(500, 1);

        //GaussOption (0 - basic, 1 - partial, 2 - complete)


//        countStatsForFloat(2);
//        countStatsForFloat(1);
//        countStatsForFloat(0);
//
//        countStatsForDouble(2);
//        countStatsForDouble(1);
//        countStatsForDouble(0);

//        countStatsForMyOwnType(2);
//        countStatsForMyOwnType(1);
//        countStatsForMyOwnType(0);

/*
        //MNOŻENIE MACIERZY PRZEZ WEKTOR (WYSZCZEGOLNIONY W SPRAWOZDANIU)

        MyMatrix<Float> matrix1 = new MyMatrix<Float>(new CalculatorFloat(), 3, 3);
        MyMatrix<Float> matrix2 = new MyMatrix<Float>(new CalculatorFloat(), 3, 1);

        matrix1.set(0, 0, 2.0f);
        matrix1.set(0, 1, 4.0f);
        matrix1.set(0, 2, 2.0f);
        matrix1.set(1, 0, -4.0f);
        matrix1.set(1, 1, 6.0f);
        matrix1.set(1, 2, 8.0f);
        matrix1.set(2, 0, 1.0f);
        matrix1.set(2, 1, 1.0f);
        matrix1.set(2, 2, 1.0f);

        matrix2.set(0, 0, 2.0f);
        matrix2.set(1, 0, 2.0f);
        matrix2.set(2, 0, 2.0f);

        matrix1.print();
        System.out.println("");
        matrix2.print();
        System.out.println("");
        MyMatrix<Float> matrix3 = matrix1.multiply(matrix2);
        matrix3.print();


        //DODAWANIE WARTOSCI DO POJEDYNCZEGO ELEMENTU MACIERZY
        matrix3.addSingleValue(0,0, 2.0f);
        System.out.println("");
        matrix3.print();
*/
/*
        //OPERACJA DODAWANIA OBIEKTOW O WLASNYM TYPIE
        MyOwnType mojtypek = new MyOwnType(new BigInteger("2"), new BigInteger("5"));
        MyOwnType mojtypek2 = new MyOwnType(new BigInteger("1"), new BigInteger("5"));
        CalculatorMyOwnType calculator = new CalculatorMyOwnType();
        MyOwnType mojtypek3 = calculator.add(mojtypek, mojtypek2);
        System.out.println(mojtypek3);
        //
*/
/*
        MyMatrix<MyOwnType> matrix1 = new MyMatrix<MyOwnType>(new CalculatorMyOwnType(),2, 3);
        MyMatrix<MyOwnType> matrix2 = new MyMatrix<MyOwnType>(new CalculatorMyOwnType(),3, 2);

        matrix1.set(0, 0, new MyOwnType(new BigInteger("1"), new BigInteger("3")));
        matrix1.set(0, 1, new MyOwnType(new BigInteger("2"), new BigInteger("3")));
        matrix1.set(0, 2, new MyOwnType(new BigInteger("3"), new BigInteger("4")));
        matrix1.set(1, 0, new MyOwnType(new BigInteger("1"), new BigInteger("4")));
        matrix1.set(1, 1, new MyOwnType(new BigInteger("5"), new BigInteger("3")));
        matrix1.set(1, 2, new MyOwnType(new BigInteger("2"), new BigInteger("5")));

        matrix2.set(0, 0, new MyOwnType(new BigInteger("1"), new BigInteger("3")));
        matrix2.set(0, 1, new MyOwnType(new BigInteger("2"), new BigInteger("3")));
        matrix2.set(1, 0, new MyOwnType(new BigInteger("3"), new BigInteger("4")));
        matrix2.set(1, 1, new MyOwnType(new BigInteger("1"), new BigInteger("4")));
        matrix2.set(2, 0, new MyOwnType(new BigInteger("5"), new BigInteger("3")));
        matrix2.set(2, 1, new MyOwnType(new BigInteger("2"), new BigInteger("5")));

        matrix1.print();
        matrix2.print();

        MyMatrix<MyOwnType> matrix3 = matrix1.multiply(matrix2);
        matrix3.print();

*/

    }
}
