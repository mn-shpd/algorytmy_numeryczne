public class Main {

    public static void main(String[] args) {

        int licznik = 0;
        double[] sumyBledow = new double[4];
        double[] wyniki = new double[5];
        double[] roznice = new double[5];
        int[] ktoryLepszy = new int[5];

        for (int j = 0; j < 4; j++) {         //Wyzerowanie sum bledow i licznika, która funkcja lepsza
                sumyBledow[j] = 0;
                ktoryLepszy[j] = 0;
        }

        for (double i = -0.999998; i <= 1; i += 0.000002) {

            licznik++;

            wyniki[0] = Lnx.wariant1(i, 70);
            wyniki[1] = Lnx.wariant2(i, 70);
            wyniki[2] = Lnx.wariant3(i, 70);
            wyniki[3] = Lnx.wariant4(i, 70);
            wyniki[4] = Math.log(i + 1);

            for (int j = 0; j < 4; j++) {
                    ////////////ROZNICA POMIEDZY WYNIKIEM A FUNKCJA BIBLIOTECZNA
                    roznice[j] = Math.abs(wyniki[4] - wyniki[j]);
                    sumyBledow[j] += roznice[j];
            }

            //HIPOTEZA NR 3 - KTORY WARIANT WYPADL LEPIEJ (GDZIE MNIEJSZA ROZNICA?)
            if (roznice[0] < roznice[2]) {     /////TaylorPrzod lepszy
                ktoryLepszy[0] += 1;
            } else if (roznice[0] > roznice[2]) {    /////PoprzedniPrzod lepszy
                ktoryLepszy[2] += 1;
            }

            if (roznice[1] < roznice[3]) {   /////TaylorTyl lepszy
                ktoryLepszy[1] += 1;
            } else if (roznice[1] > roznice[3]) {   /////PoprzedniTyl lepszy
                ktoryLepszy[3] += 1;
            }
            ////////////////////////////////////////////////////

            if (licznik == 50000) {

                //////////USREDNIONY BLAD BEZWZGLEDNY DLA KAZDEGO PRZEDZIALU/////////////
                for (int j = 0; j < 4; j++) {
                    System.out.print((sumyBledow[j] / 50000.0) + "\t");
                }

                System.out.println("");

                for (int j = 0; j < 4; j++) {
                    sumyBledow[j] = 0;
                }

                licznik = 0;
            }
        }

        System.out.print(ktoryLepszy[0]+"\t"+ktoryLepszy[2]+"\t"+ktoryLepszy[1]+"\t"+ktoryLepszy[3]+"\t");
    }
}
