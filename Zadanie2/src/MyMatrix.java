import java.io.*;

public class MyMatrix<T> {

    private int rowCount;
    private int columnCount;
    private T[][] matrix;
    private Calculator<T> calculator;

    public MyMatrix(Calculator<T> calculator, int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.calculator = calculator;
        this.matrix = (T[][]) new Number[rowCount][columnCount];
    }

    //Wstawia wartosc do macierzy pod konkretne indeksy
    public void set(int rowNum, int colNum, T value) {
        matrix[rowNum][colNum] = value;
    }

    //Wypisuje macierz w konsoli
    public void print() {
        for(int i = 0; i < rowCount; i++) {
            for(int j = 0; j < columnCount; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("");
        }
    }

    //Wstawia zero na miejsce kazdego elementu macierzy.
    public void setZero() {

        for (int i = 0; i < this.rowCount; i++) {
            for (int j = 0; j < this.columnCount; j++) {
                matrix[i][j] = calculator.returnZero();
            }
        }
    }

    //Dodaje do konkretnego elementu macierzy wartosc podana w argumencie.
    public void addSingleValue(int rowNumber, int columnNumber, T value) {

        matrix[rowNumber][columnNumber] = calculator.add(matrix[rowNumber][columnNumber], value);

    }

    //Mnozenie dwoch macierzy
    public MyMatrix<T> multiply(MyMatrix<T> secondMatrix) {

        if(this.columnCount != secondMatrix.rowCount) {
            System.out.println("Invalid matrix. Columns and rows are not equal!");
            return null;
        } else {

            //Tworzenie macierzy wynikowej.
            MyMatrix<T> newMatrix = new MyMatrix<T>(calculator, this.rowCount, secondMatrix.columnCount);
            //Wyzerowanie macierzy wynikowej.
            newMatrix.setZero();

            for(int i = 0; i < secondMatrix.getColumnCount(); i++) {   //SecondMatrix column iterator
                for(int j = 0; j < this.rowCount; j++) { //FirstMatrix row iterator
                    for (int k = 0; k < this.columnCount; k++) {  //FirstMatrix column iterator / SecondMatrix row iterator
                        newMatrix.addSingleValue(j, i, calculator.multiply(this.matrix[j][k], secondMatrix.matrix[k][i]));
                    }
                }
            }
            return newMatrix;
        }
    }

    //Tworzy nowa macierz skladajaca sie z macierzy A i wektora B
    public MyMatrix<T> mergeWithB(MyMatrix<T> vectorB) {

        MyMatrix<T> newMatrix = new MyMatrix<T>(calculator, this.rowCount, this.columnCount+1);

        for(int i = 0; i < this.rowCount; i++) {
            for(int j = 0; j < this.columnCount+1; j++) {
                if(j == this.columnCount) {
                    newMatrix.matrix[i][j] = vectorB.matrix[i][0];
                }
                else {
                    newMatrix.matrix[i][j] = this.matrix[i][j];
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

            if (calculator.compareTo(this.matrix[columnNum][columnNum], calculator.returnZero()) == 0) {

                do {
                    i++;
                    if(i == this.rowCount) {
                        return false;
                    }
                } while (calculator.compareTo(this.matrix[i][columnNum], calculator.returnZero()) == 0);

            } else {
                return true;
            }

            this.swapRows(columnNum, i);
            return true;

        }
    }

    public void basicGauss() {

        for (int k = 0; k < this.columnCount - 1; k++) {

            if (this.swapIfZero(k)) {
                for (int i = k + 1; i < this.rowCount; i++) {
                    this.subtractMultipliedRow(k, i, calculator.divide(this.matrix[i][k], this.matrix[k][k]));
                }
            }
        }
    }

    //To samo co basicGauss tylko przed jakimikolwiek operacjami na macierzy wyszukuje w danej kolumnie najwieksza wartosc i podmienia
    //tak zeby w danej iteracji najwyzej byla najwieksza wartosc.
    public void partialGauss() {

        for (int k = 0; k < this.columnCount - 1; k++) {

            //Wyszukuje najwieksza bezwzgledna wartosc w kolumnie
            int rowNum = absoluteBiggestValueInColumn(k, k);

            //Jezeli najwieksza wartosc bezwzgledna to 0, to znaczy, ze cala kolumna jest zerowa. Wtedy przechodzi dalej.
            if(calculator.compareTo(this.matrix[rowNum][k], calculator.returnZero()) == 0) {
                continue;
            }

            //Jezeli najwieksza wartosc jest na innym miejscu niz na przekatnej to zamienia wiersze.
            if(rowNum != k) {
                swapRows(k, rowNum);
            }

            for (int i = k + 1; i < this.rowCount; i++) {
                this.subtractMultipliedRow(k, i, calculator.divide(this.matrix[i][k], this.matrix[k][k]));
            }
        }
    }

    public int[] completeGauss() {

        int[] changesOfVariablesOrder = new int[this.columnCount-1];
        for(int i = 0; i < this.columnCount - 1; i++) {
            changesOfVariablesOrder[i] = i;
        }

        for (int k = 0; k < this.columnCount - 1; k++) {

            //Wyszukuje najwieksza bezwzgledna wartosc w kolumnie
            int rowNum = absoluteBiggestValueInColumn(k, k);

            //Jezeli najwieksza wartosc bezwzgledna to 0, to znaczy, ze cala kolumna jest zerowa. Wtedy przechodzi dalej.
            if(calculator.compareTo(this.matrix[rowNum][k], calculator.returnZero()) == 0) {
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

                    this.subtractMultipliedRow(k, i, calculator.divide(this.matrix[i][k], this.matrix[k][k]));

                }
            }
        }
        return changesOfVariablesOrder;
    }

    //Metoda tworzy nowy wektor finalny X z macierzy schodkowej
    //Liczy sumy na wierszach zaczynajac od dolu, wymnazajac kolejne wspolczynniki przez X-ksy wyliczone w poprzednich iteracjach.
    //Sumy odejmuje sie od elementu wektora B lezacego w tym samym wierszu i taki wynik dzielony jest przez wspolczynnik stojacy przy jeszcze nieznanym X.
    //Jesli do metody przekazano tablice z ulozeniem zmiennych, to na koniec metody nastepuje zamiana kolejnosci elementow nowego wektora X.
    public MyMatrix<T> countXFromFinalMatrix(int[] variableOrder) {

        MyMatrix<T> newVectorX = new MyMatrix<T>(calculator, this.rowCount, 1);

        newVectorX.matrix[this.rowCount-1][0] = calculator.divide(this.matrix[this.rowCount-1][columnCount-1], this.matrix[this.rowCount-1][this.columnCount-2]);

        for(int i = this.rowCount-2; i >= 0; i--) {

            T sum = calculator.returnZero();

            for (int j = columnCount - 2; j > i; j--) {

                sum = calculator.add(sum, calculator.multiply(this.matrix[i][j], newVectorX.matrix[j][0]));
            }

            T x = calculator.divide(calculator.subtract(this.matrix[i][columnCount - 1], sum), this.matrix[i][i]);
            newVectorX.matrix[i][0] = x;

        }

        if(variableOrder != null) {

            for(int i = 0; i < this.columnCount - 1; i++) {
                for(int j = 0; j < this.columnCount - 1; j++) {
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
    public void subtractMultipliedRow(int firstRowNum, int secondRowNum, T value) {

        for(int j = 0; j < this.columnCount; j++) {

            //Odejmuje od zera, zeby uzyskac ujemna wartosc
            T temp = calculator.subtract(calculator.returnZero(), calculator.multiply(this.matrix[firstRowNum][j], value));
            this.addSingleValue(secondRowNum, j, temp);
        }
    }

    //Zamienia wiersze
    public void swapRows(int firstRowNum, int secondRowNum) {

        for(int j = 0; j < this.columnCount; j++) {

            T temp = this.matrix[firstRowNum][j];
            this.matrix[firstRowNum][j] = this.matrix[secondRowNum][j];
            this.matrix[secondRowNum][j] = temp;
        }
    }
    
    //Zamienia kolumny
    public void swapColumns(int firstColumnNum, int secondColumnNum) {

        for(int i = 0; i < this.rowCount; i++) {

            T temp = this.matrix[i][firstColumnNum];
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


            T temp = calculator.abs(this.matrix[rowNum][columnNum]);

            for (int i = rowNum + 1; i < this.rowCount; i++) {

                if (calculator.compareTo(calculator.abs(this.matrix[i][columnNum]), temp) == 1) {
                    temp = this.matrix[i][columnNum];
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

        T temp = calculator.abs(this.matrix[rowNum][columnNum]);

        for(int i = rowNum; i < this.rowCount; i++) {
            for(int j = columnNum; j < this.columnCount - 1; j++) {
                if (calculator.compareTo(calculator.abs(this.matrix[i][j]), temp) == 1) {
                    temp = this.matrix[i][j];
                    biggestValueCoordinates.setI(i);
                    biggestValueCoordinates.setJ(j);
                }
            }
        }

        return biggestValueCoordinates;
    }

    //Liczy usredniony blad bezwzgledny (usredniona roznice pomiedzy wejsciowym wektorem X a finalnym wektorem X)
    public T countAverageDifference(MyMatrix<T> secondMatrix) {

        T sum = calculator.returnZero();

        for (int i = 0; i < this.rowCount; i++) {
            sum = calculator.add(sum, calculator.abs(calculator.subtract(this.matrix[i][0], secondMatrix.matrix[i][0])));
        }

        return calculator.divide(sum, calculator.convertInt(this.rowCount));
    }


    public void readFromFile(String path) throws IOException {

        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        if((line = br.readLine()) != null) {
            for (int i = 0; i < this.getRowCount(); i++) {
                for (int j = 0; j < this.getColumnCount(); j++) {

                    T temp = calculator.parse(line);
                    matrix[i][j] = temp;
                    line = br.readLine();
                }
            }
        }
    }

    public Float getFloatValueOfMatrix(int i, int j) {
        return (Float)this.matrix[i][j];
    }

    public Double getDoubleValueOfMatrix(int i, int j) {
        return (Double)this.matrix[i][j];
    }

    public MyOwnType getMyOwnTypeValueOfMatrix(int i, int j) {
        return (MyOwnType)this.matrix[i][j];
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

    public T[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(T[][] matrix) {
        this.matrix = matrix;
    }

}
