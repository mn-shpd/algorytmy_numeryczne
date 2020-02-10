import java.io.*;
import java.util.Random;

public class MyMatrix {

    private int rowCount;
    private int columnCount;
    private double[][] matrix;

    public MyMatrix(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.matrix = new double[rowCount][columnCount];
    }

    //Wstawia wartosc do macierzy pod konkretne indeksy
    public void set(int rowNum, int colNum, double value) {
        matrix[rowNum][colNum] = value;
    }

    //Wypisuje macierz w konsoli
    public void print() {

        for(int i = 0; i < rowCount; i++) {
            for(int j = 0; j < columnCount; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    //Wstawia zero na miejsce kazdego elementu macierzy.
    public void setZero() {

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                matrix[i][j] = 0.0;
            }
        }
    }

    //Dodaje do konkretnego elementu macierzy wartosc podana w argumencie.
    public void addSingleValue(int rowNumber, int columnNumber, double value) {
        matrix[rowNumber][columnNumber] += value;
    }

    //Transpozycja
    public MyMatrix transposition() {

        MyMatrix newMatrix = new MyMatrix(columnCount, rowCount);

        for(int i = 0; i < columnCount; i++) {
            for(int j = 0; j < rowCount; j++) {
                newMatrix.set(i, j, this.getValue(j, i));
            }
        }
        return newMatrix;
    }

    public void makeItUnit() {

        for(int i = 0; i < rowCount; i++) {
            for(int j = 0; j < columnCount; j++) {
                if(i == j) {
                    this.set(i, j, 1.0);
                }
                else {
                    this.set(i, j, 0.0);
                }
            }
        }
    }

    //Mozenie macierzy przez liczbe
    public MyMatrix multiplyByNumber(double number) {

        MyMatrix newMatrix = new MyMatrix(rowCount, columnCount);

        for(int i = 0; i < rowCount; i++) {
            for(int j = 0; j < columnCount; j++) {
                newMatrix.set(i, j, this.matrix[i][j] * number);
            }
        }
        return newMatrix;
    }

    //Dodawanie dwoch macierzy
    public MyMatrix add(MyMatrix secondMatrix) {

        if(this.rowCount != secondMatrix.getRowCount() || this.columnCount != secondMatrix.getColumnCount()) {
            System.out.println("Invalid matrix. Columns and rows are not equal!");
            return null;
        } else {

            MyMatrix newMatrix = new MyMatrix(this.rowCount, this.columnCount);
            for (int i = 0; i < newMatrix.getRowCount(); i++) {
                for (int j = 0; j < newMatrix.getColumnCount(); j++) {
                    newMatrix.set(i, j, this.getValue(i, j) + secondMatrix.getValue(i, j));
                }
            }
            return newMatrix;
        }
    }

    //Wstaw losowe wartosci
    public void setRandomValues() {

        Random random = new Random();
        double randomValue;

        for(int i = 0; i < this.getRowCount(); i++) {
            for(int j = 0; j < this.getColumnCount(); j++) {
                do {
                    randomValue = random.nextDouble();
                } while(randomValue == 0.0);
//                int randomValue = random.nextInt(65536) + 65536;
//                this.set(i, j, (double) randomValue / 65536);
                this.matrix[i][j] = randomValue;
            }
        }
    }

    //Wytnij z macierzy podmacierz
    public MyMatrix cut(int lowerRow, int upperRow, int lowerColumn, int upperColumn) {
        if(lowerRow < 0 || lowerRow > rowCount - 1) {
            System.out.println("Invalid argument (lowerRow is out of range).");
            return null;
        }
        if(upperRow < 0 || upperRow > rowCount - 1) {
            System.out.println("Invalid argument (upperRow is out of range).");
            return null;
        }
        if(lowerColumn < 0 || lowerColumn > columnCount - 1) {
            System.out.println("Invalid argument (lowerColumn is out of range).");
            return null;
        }
        if(upperColumn < 0 || upperColumn > columnCount - 1) {
            System.out.println("Invalid argument (upperColumn is out of range).");
            return null;
        }
        if(lowerRow > upperRow) {
            System.out.println("lowerRow cannot be greater than upperRow.");
            return null;
        }
        if(lowerColumn > upperColumn) {
            System.out.println("lowerColumn cannot be greater than upperColumn.");
            return null;
        }

        MyMatrix newMatrix = new MyMatrix(upperRow - lowerRow + 1, upperColumn - lowerColumn + 1);

        for(int i = 0; i < newMatrix.getRowCount(); i++) {
            for(int j = 0; j < newMatrix.getColumnCount(); j++) {
                newMatrix.set(i, j, this.matrix[i + lowerRow][j + lowerColumn]);
            }
        }

        return newMatrix;
    }

    //Mnozenie dwoch macierzy
    public MyMatrix multiply(MyMatrix secondMatrix) {

        if(this.columnCount != secondMatrix.getRowCount()) {
            System.out.println("Invalid matrix. Columns and rows are not equal!");
            return null;
        } else {

            //Tworzenie macierzy wynikowej.
            MyMatrix newMatrix = new MyMatrix(this.rowCount, secondMatrix.getColumnCount());
            //Wyzerowanie macierzy wynikowej.
            newMatrix.setZero();

            for(int i = 0; i < secondMatrix.getColumnCount(); i++) {   //SecondMatrix column iterator
                for(int j = 0; j < this.rowCount; j++) { //FirstMatrix row iterator
                    for (int k = 0; k < this.columnCount; k++) {  //FirstMatrix column iterator / SecondMatrix row iterator
                        newMatrix.addSingleValue(j, i, this.matrix[j][k] * secondMatrix.getValue(k, i));
                    }
                }
            }
            return newMatrix;
        }
    }

    //Tworzy nowa macierz skladajaca sie z macierzy A i wektora B
    public MyMatrix mergeWithB(MyMatrix vectorB) {

        MyMatrix newMatrix = new MyMatrix(rowCount, columnCount+1);

        for(int i = 0; i < rowCount; i++) {
            for(int j = 0; j < columnCount + 1; j++) {
                if(j == columnCount) {
                    newMatrix.set(i, j, vectorB.getValue(i, 0));
                }
                else {
                    newMatrix.set(i, j, this.getValue(i, j));
                }
            }
        }
        return newMatrix;
    }

    //Sprawdza czy element na przekatnej macierzy dla konkretnej kolumny jest zerem i jesli jest to zamienia wiersze tak zeby tego zera nie bylo
    //W przypadku gdy nie ma z czym zamienic to nic nie robi i zwraca false. Zwraca true gdy element na przekatnej nie jest zerem lub gdy zamienil (mozna liczyc).
    public boolean swapIfZero(int columnNum) {

        if( (columnNum < 0) || (columnNum >= this.columnCount) ) {
            throw new IllegalArgumentException(("Overrange columnNum argument."));

        } else {

            //Row iterator
            int i = columnNum;
            //Jesli i przekroczy zakres wierszy, to wtedy cala kolumna jest zerowa (metoda zwraca false).

            if (this.matrix[columnNum][columnNum] == 0) {

                do {
                    i++;
                    if(i == this.rowCount) {
                        return false;
                    }
                } while (this.matrix[i][columnNum] == 0);

            } else {
                return true;
            }

            this.swapRows(columnNum, i);
            return true;

        }
    }

    public void basicGauss() {

        for (int k = 0; k < this.columnCount - 2; k++) {

            if (this.swapIfZero(k)) {
                for (int i = k + 1; i < this.rowCount; i++) {
                    this.subtractMultipliedRow(k, i, this.matrix[i][k] / this.matrix[k][k]);
                }
            }
        }
    }

    //To samo co basicGauss tylko przed jakimikolwiek operacjami na macierzy wyszukuje w danej kolumnie najwieksza wartosc i podmienia
    //tak zeby w danej iteracji najwyzej byla najwieksza wartosc.
    public void partialGauss() {

        for (int k = 0; k < this.columnCount - 2; k++) {

            //Wyszukuje najwieksza bezwzgledna wartosc w kolumnie
            int rowNum = absoluteBiggestValueInColumn(k, k);

            //Jezeli najwieksza wartosc bezwzgledna to 0, to znaczy, ze cala kolumna jest zerowa. Wtedy przechodzi dalej.
            if(this.matrix[rowNum][k] == 0) {
                continue;
            }

            //Jezeli najwieksza wartosc jest na innym miejscu niz na przekatnej to zamienia wiersze.
            if (rowNum != k) {
                this.swapRows(k, rowNum);
            }

            for (int i = k + 1; i < this.rowCount; i++) {
                this.subtractMultipliedRow(k, i, this.matrix[i][k] / this.matrix[k][k]);
            }
        }
    }

    public int[] completeGauss() {

        int[] changesOfVariablesOrder = new int[this.columnCount-1];
        for(int i = 0; i < this.columnCount - 1; i++) {
            changesOfVariablesOrder[i] = i;
        }

        for (int k = 0; k < this.columnCount - 2; k++) {

            //Wyszukuje najwieksza bezwzgledna wartosc w kolumnie
            int rowNum = absoluteBiggestValueInColumn(k, k);

            //Jezeli najwieksza wartosc bezwzgledna to 0, to znaczy, ze cala kolumna jest zerowa. Wtedy przechodzi dalej.
            if(this.matrix[rowNum][k] == 0) {
                continue;

            } else {

                //Wyszukuje najwieksza bezwzgledna wartosc w podmacierzy zaczynajac sie od elementu o indeksach (k,k)
                ElementCoordinates biggestValue = absoluteBiggestValueInRange(k, k);
                if (biggestValue.getI() != k) {
                    swapRows(k, biggestValue.getI());
                }
                if (biggestValue.getJ() != k) {
                    swapColumns(k, biggestValue.getJ());
                    int temp = changesOfVariablesOrder[k];
                    changesOfVariablesOrder[k] = changesOfVariablesOrder[biggestValue.getJ()];
                    changesOfVariablesOrder[biggestValue.getJ()] = temp;
                }

                for (int i = k + 1; i < this.rowCount; i++) {

                    this.subtractMultipliedRow(k, i, this.matrix[i][k] / this.matrix[k][k]);

                }
            }
        }
        return changesOfVariablesOrder;
    }

    //Metoda tworzy nowy wektor finalny X z macierzy schodkowej
    //Liczy sumy na wierszach zaczynajac od dolu, wymnazajac kolejne wspolczynniki przez X-ksy wyliczone w poprzednich iteracjach.
    //Sumy odejmuje sie od elementu wektora B lezacego w tym samym wierszu i taki wynik dzielony jest przez wspolczynnik stojacy przy jeszcze nieznanym X.
    //Jesli do metody przekazano tablice z ulozeniem zmiennych, to na koniec metody nastepuje zamiana kolejnosci elementow nowego wektora X.
    public MyMatrix countXFromFinalMatrix(int[] variableOrder) {

        MyMatrix newVectorX = new MyMatrix(this.rowCount, 1);

        newVectorX.set(this.rowCount-1, 0,this.getValue(this.getRowCount()-1, this.getColumnCount()-1) / this.getValue(this.getRowCount()-1, this.getColumnCount()-2));

        for(int i = this.getRowCount()-2; i >= 0; i--) {

            double sum = 0.0;

            for (int j = this.getColumnCount() - 2; j > i; j--) {

                sum += this.getValue(i, j) * newVectorX.getValue(j, 0);
            }

            double x = (this.getValue(i, this.getColumnCount() - 1) - sum) / this.getValue(i, i);
            newVectorX.set(i, 0, x);

        }

        if(variableOrder != null) {

            for(int i = 0; i < this.getColumnCount() - 1; i++) {
                for(int j = 0; j < this.getColumnCount() - 1; j++) {
                    if(variableOrder[j] == i) {
                        newVectorX.swapRows(i, j);
                        int temp = variableOrder[i];
                        variableOrder[i] = i;
                        variableOrder[j] = temp;
                    }
                }
            }
        }
        return newVectorX;
    }
    
    //Odejmuje wiersz pomnozony przez liczbe od innego wiersza.
    public void subtractMultipliedRow(int firstRowNum, int secondRowNum, double value) {

        for(int j = 0; j < this.columnCount; j++) {

            double temp = -1 * this.matrix[firstRowNum][j] * value;
            this.addSingleValue(secondRowNum, j, temp);
        }
    }

    //Zamienia wiersze
    public void swapRows(int firstRowNum, int secondRowNum) {

        for(int j = 0; j < this.columnCount; j++) {

            double temp = this.matrix[firstRowNum][j];
            this.matrix[firstRowNum][j] = this.matrix[secondRowNum][j];
            this.matrix[secondRowNum][j] = temp;
        }
    }
    
    //Zamienia kolumny
    public void swapColumns(int firstColumnNum, int secondColumnNum) {

        for(int i = 0; i < this.rowCount; i++) {

            double temp = this.matrix[i][firstColumnNum];
            this.matrix[i][firstColumnNum] = this.matrix[i][secondColumnNum];
            this.matrix[i][secondColumnNum] = temp;
        }
    }

    //Zwraca numer wiersza, w ktorym znajduje sie najwieksza wartosc w danej kolumnie.
    //Poszukiwania w kolumnie rozpoczyna od wiersza przekazanego jako argument.
    public int absoluteBiggestValueInColumn(int rowNum, int columnNum) {

        int rowNumTemp = rowNum;

        if ((columnNum < 0) || (columnNum >= this.columnCount)) {
            throw new IllegalArgumentException(("Overrange columnNum argument."));
        } else if((rowNum < 0) || (rowNum >= this.rowCount)) {
            throw new IllegalArgumentException(("Overrange rowNum argument."));
        } else {

            double temp = Math.abs(this.matrix[rowNum][columnNum]);

            for (int i = rowNum + 1; i < this.rowCount; i++) {

                double temp2 = Math.abs(this.matrix[i][columnNum]);

                if (temp2 > temp) {
                    temp = temp2;
                    rowNumTemp = i;
                }
            }
        }
        return rowNumTemp;
    }

    //Zwraca obiekt posiadajacy pola ze wspolrzednymi najwiekszego elementu w macierzy dla zakresu wierszy od rowNum do rowCount - 1
    //i kolumn od columnNum do columnCount - 2 (z pominieciem wektora B).
    public ElementCoordinates absoluteBiggestValueInRange(int rowNum, int columnNum) {

        ElementCoordinates biggestValueCoordinates = new ElementCoordinates(rowNum, columnNum);

        double temp = Math.abs(this.matrix[rowNum][columnNum]);

        for(int i = rowNum; i < this.rowCount; i++) {
            for(int j = columnNum; j < this.columnCount - 1; j++) {
                if (Math.abs(this.matrix[i][j]) > temp) {
                    temp = Math.abs(this.matrix[i][j]);
                    biggestValueCoordinates.setI(i);
                    biggestValueCoordinates.setJ(j);
                }
            }
        }

        return biggestValueCoordinates;
    }

    //Liczy usredniony blad bezwzgledny (usredniona roznice pomiedzy wejsciowym wektorem X a finalnym wektorem X)
    public double countAverageDifference(MyMatrix secondMatrix) {

        double sum = 0.0;

        for (int i = 0; i < this.rowCount; i++) {
            sum += Math.abs(this.matrix[i][0] - secondMatrix.matrix[i][0]);
        }

        return sum / (double)this.rowCount;
    }


    public void readFromFile(String path) throws IOException {

        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        if((line = br.readLine()) != null) {
            for (int i = 0; i < this.getRowCount(); i++) {
                for (int j = 0; j < this.getColumnCount(); j++) {

                    double temp = Double.parseDouble(line);
                    matrix[i][j] = temp;
                    line = br.readLine();
                }
            }
        }
    }

    public double getValue(int i, int j) {
        return this.matrix[i][j];
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

}
