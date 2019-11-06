public class Lnx {

    public static double wariant1(double x, int m) {

        if(x <= -1  || x > 1) {
            System.out.println("Niepoprawny argument.");
            return -1;
        }

        double suma = 0;
        double y = 0;    //temp x
        int k = 0;       //licznik poteg

        for(int n = 1; n <= m; n++) {

            k = n;
            y = x;
            while(k > 1) {
                y *= x;
                k--;
            }

            if(n % 2 == 1) {
                suma = suma + (y / n);
            }
            else {
                suma = suma - (y / n);
            }
        }
        return suma;
    }

    public static double wariant2(double x, int m) {

        if(x <= -1  || x > 1) {
            System.out.println("Niepoprawny argument.");
            return -1;
        }

        double suma = 0;
        double y = 0;    //temp x
        int k = 0;       //licznik poteg

        for(int n = m; n >= 1; n--) {

            k = n;
            y = x;
            while(k > 1) {
                y *= x;
                k--;
            }

            if(n % 2 == 1) {
                suma = suma + (y / n);
            }
            else {
                suma = suma - (y / n);
            }
        }
        return suma;
    }

    public static double wariant3(double x, int m) {

        if(x <= -1  || x > 1) {
            System.out.println("Niepoprawny argument.");
            return -1;
        }

        double suma = 0;
        double aktualnyElSumy = x;
        int n = 1;

        while(n <= m) {
            suma += aktualnyElSumy;
            aktualnyElSumy = ((-1) * aktualnyElSumy * x * n) / (n + 1);
            n++;
        }

        return suma;
    }

    public static double wariant4(double x, int m) {

        if(x <= -1  || x > 1) {
            System.out.println("Niepoprawny argument.");
            return -1;
        }

        double[] tablicaSkladnikowSumy = new double[m];
        double suma = 0;
        double aktualnyElSumy = x;
        int n = 1;

        while(n <= m) {
            tablicaSkladnikowSumy[n-1] = aktualnyElSumy;
            aktualnyElSumy = ((-1) * aktualnyElSumy * x * n) / (n + 1);
            n++;
        }

        for(int i = m-1; i >= 0; i--) {
            suma += tablicaSkladnikowSumy[i];
        }

        return suma;
    }

}
