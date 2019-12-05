import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Random;

public class Generator {

    //https://howtodoinjava.com/java/io/java-write-to-file/
    //https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it-in-java
    //https://stackoverflow.com/questions/27976857/how-to-get-random-number-with-negative-number-in-range

    public static void generateValuesToFile(int rowCount, int columnCount) throws FileNotFoundException {

        PrintWriter floatWriter = new PrintWriter("FloatValuesX.txt");
        PrintWriter doubleWriter = new PrintWriter("DoubleValuesX.txt");
        PrintWriter MyOwnTypeWriter = new PrintWriter("MyOwnTypeValuesX.txt");
        int min = (int) Math.pow(2, 16);
        int max = (int) Math.pow(2, 16) - 1;
        double divisor = Math.pow(2, 16);

        for(int i = 0; i < rowCount * columnCount; i++) {
            int randomInt = new Random().nextInt(max + min) - min;
            floatWriter.println(randomInt / (float) divisor);
            doubleWriter.println(randomInt / divisor);
            MyOwnTypeWriter.println(new MyOwnType(BigInteger.valueOf(randomInt), BigInteger.valueOf((int)divisor)));
        }

        floatWriter.close();
        doubleWriter.close();
        MyOwnTypeWriter.close();
    }
}
