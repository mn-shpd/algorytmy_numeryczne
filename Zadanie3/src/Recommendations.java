import java.awt.geom.Arc2D;
import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Recommendations {

    //Lista z produktami i z nimi zwiazanymi danymi (user, rating, group)
    private List<ProductData> productsDataList = new ArrayList<>();
    //Lista uzytkownikow
    private List<String> usersList = new ArrayList<>();
    //Lista produktow
    private List<Integer> productsList = new ArrayList<>();
    //Macierz ocen produktow przez poszczegolnych uzytkownikow
    private MyMatrix ratings;

    //Laduje podana ilosc produktow z okreslonej grupy
    public void loadProductData(int productAmount, int minimumReviewsAmount, String group, String path) throws IOException {

        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        Pattern idPattern = Pattern.compile("Id:");
        Pattern groupPattern = Pattern.compile("group: " + group);
        Pattern reviewsPattern = Pattern.compile("reviews:");
        Pattern customerPattern = Pattern.compile("cutomer: ");
        int loadedProductsCounter = 0;

        while((line = br.readLine()) != null && loadedProductsCounter < productAmount) {

            Matcher matcher = idPattern.matcher(line);
            if(matcher.find()) {
                int product_id = Integer.parseInt(line.substring(6));

                br.readLine();
                br.readLine();
                line = br.readLine();

                matcher = groupPattern.matcher(line);
                if(matcher.find()) {
                    matcher = reviewsPattern.matcher(line);
                    while(!matcher.find()) {
                        line = br.readLine();
                        matcher = reviewsPattern.matcher(line);
                    }

                    //Jak znalazlem linie z reviews, to tworze liste z danymi review.
                    List<ReviewData> reviews = new ArrayList<>();

                    line = br.readLine();
                    matcher = customerPattern.matcher(line);

                    while(matcher.find()) {

                        boolean flag = false;
                        String customer_id = line.substring(line.indexOf("cutomer: ") + 9, line.indexOf(" rating:") - 1);
                        double rating = Double.parseDouble(line.substring(line.indexOf("rating: ") + 8, line.indexOf(" votes:") - 1));

                        //Sprawdza czy uzytkownik juz ocenial produkt. Jesli tak, to aktualizuje ocene (nie dodaje nowej).
                        for(ReviewData rd : reviews) {
                            if(rd.getUser_id().equals(customer_id)) {
                                rd.setRating(rating);
                                flag = true;
                            }
                        }

                        if(!flag) {
                            reviews.add(new ReviewData(customer_id, rating));
                        }

                        line = br.readLine();
                        matcher = customerPattern.matcher(line);
                    }

                    if(reviews.size() >= minimumReviewsAmount) {
                        productsDataList.add(new ProductData(product_id, group, reviews));
                        loadedProductsCounter++;
                    }
                }
            }

        }

    }

    public void fillUsersAndProductsLists() {

            for(ProductData pd : productsDataList) {

                int product_id = pd.getProduct_id();

                if(!productsList.contains(product_id)) {
                    //System.out.println("Dodalem product: " + product_id);
                    productsList.add(product_id);
                }

                List<ReviewData> reviews = pd.getReviews();

                for(ReviewData rd : reviews) {

                    String user_id = rd.getUser_id();

                    if (!usersList.contains(user_id)) {
                        //System.out.println("Dodalem user: " + user_id + " z produktu: " + product_id);
                        usersList.add(user_id);
                    }
                }
            }
    }

    //Redukuje liczbe userow do takich, ktorzy ocenili podane jako parametr minimum wczytanych produtkow.
    public void filterUsersList(int minimumProductsReviewed) {

        if(minimumProductsReviewed < 0) {
            System.out.println("Parameter minimumProductsReviewed cannot be less than zero.");
        }
        if(minimumProductsReviewed > productsList.size()) {
            System.out.println("Parameter minimumProductsReviewed cannot be greater than amount of loaded products.");
        }

        Iterator iterator = usersList.iterator();
        while(iterator.hasNext()) {
            String user_id = (String) iterator.next();
            int reviewCounter = 0;
            for(ProductData pd : productsDataList) {
                List<ReviewData> reviews = pd.getReviews();
                for(ReviewData rd : reviews) {
                    if(rd.getUser_id().equals(user_id)) {
                        reviewCounter++;
                    }
                }
            }
            if(reviewCounter < minimumProductsReviewed) {
                iterator.remove();
            }
        }
    }

    //Redukuje liczbe userow do okreslonej ilosci, wybierajac tych, ktorzy ocenili najwiecej z wczytanych produktow.
    public void filterUsersList2(int maximumAmountOfUsers) {

        if(maximumAmountOfUsers < 0) {
            throw new IllegalArgumentException("Parameter maximumAmountOfUsers cannot be less than zero.");
        }
        if(maximumAmountOfUsers > usersList.size()) {
            throw new IllegalArgumentException("Parameter maximumAmountOfUsers cannot be greater than amount of loaded users.");
        }

        List<String> newUsersList = new ArrayList<>();
        List<Integer> usersReviewsList = new ArrayList<>();
        boolean flag;

        for(String user_id : usersList) {
            flag = true;
            int reviewCounter = 0;
            for(ProductData pd : productsDataList) {
                List<ReviewData> reviews = pd.getReviews();
                for(ReviewData rd : reviews) {
                    if(rd.getUser_id().equals(user_id)) {
                        reviewCounter++;
                    }
                }
            }
            //Jesli uzytkownik nie ocenil zadnego produktu z wczytanych, to petla pomija go
            if(reviewCounter == 0) {
                continue;
            }
            if(usersReviewsList.size() == 0) {
                usersReviewsList.add(reviewCounter);
                newUsersList.add(user_id);
            }
            for(int i = 0; i < usersReviewsList.size(); i++) {
                if(reviewCounter >= usersReviewsList.get(i)) {
                    usersReviewsList.add(i, reviewCounter);
                    newUsersList.add(i, user_id);
                    flag = false;
                    break;
                }
            }
            if(flag) {
                usersReviewsList.add(reviewCounter);
                newUsersList.add(user_id);
            }
        }

        usersList = new ArrayList<>(newUsersList.subList(0, maximumAmountOfUsers));

    }

    public void fillRatingsMatrix() {

        ratings = new MyMatrix(usersList.size(), productsList.size());
        ratings.setZero();
        //Lista ocen z pojedynczego produktu
        List<ReviewData> reviews = null;

        for(int j = 0; j < productsList.size(); j++) {

            for(ProductData pd : productsDataList) {
                if(productsList.get(j) == pd.getProduct_id()) {
                    reviews = pd.getReviews();
                    break;
                }
            }

            for(ReviewData rd : reviews) {
                for(int i = 0; i < usersList.size(); i++) {
                    if(rd.getUser_id().equals(usersList.get(i))) {
                        ratings.set(i, j, rd.getRating());
                    }
                }
            }
        }
    }

    public Stats als(int d, Double lambda, int numberOfIterations) throws Exception {

        MyMatrix P = new MyMatrix(d, productsList.size());
        MyMatrix U = new MyMatrix(d, usersList.size());
        MyMatrix ratingsCalculated = null;

        P.setRandomValues();
        U.setRandomValues();

        //Do testow
        double actualObjectiveFunction = 0.0;
        double previousObjectiveFunction;
        double objectiveFunctionDiff;
        long startTime, endTime, time = 0;
        Stats stats = null;

        for (int i = 0; i < numberOfIterations; i++) {

            startTime = System.currentTimeMillis();

            for (int u = 0; u < usersList.size(); u++) {

                //Lista indeksow produktow, ktorym uzytkownik u wystawil ocene.
                List<Integer> I_u = new ArrayList<>();

                for (int j = 0; j < ratings.getColumnCount(); j++) {
                    if (ratings.getValue(u, j) != 0.0) {
                        I_u.add(j);
                    }
                }

                //Macierz zawierajaca kolumny z macierzy P o indeksach wyliczonych wyzej (produkty dla ktorych uzytkownik wystawil ocene).
                MyMatrix P_I_u = new MyMatrix(P.getRowCount(), I_u.size());

                for (int k = 0; k < P_I_u.getRowCount(); k++) {
                    for (int l = 0; l < P_I_u.getColumnCount(); l++) {
                        P_I_u.set(k, l, P.getValue(k, I_u.get(l)));
                    }
                }

                //Macierz jednostkowa
                MyMatrix E = new MyMatrix(d, d);
                E.makeItUnit();

                //Przemnozenie macierzy jednostkowej przez lambde.
                E = E.multiplyByNumber(lambda);

                //Transpozycja macierzy P_I_u.
                MyMatrix T_P_I_u = P_I_u.transposition();

                //Mnozenie macierzy P_I_u i T_P_I_u.
                MyMatrix A_u = P_I_u.multiply(T_P_I_u);
                A_u = A_u.add(E);

                //Macierz V_u reprezentujaca sume kolumn z macierzy P pomnozonych przez odpowiednie oceny uzytkownika.
                MyMatrix V_u = new MyMatrix(d, 1);
                V_u.setZero();

                for (int k = 0; k < I_u.size(); k++) {
                    V_u = V_u.add(P.cut(0, d - 1, I_u.get(k), I_u.get(k)).multiplyByNumber(ratings.getValue(u, I_u.get(k))));
                }

                A_u = A_u.mergeWithB(V_u);
//                int[] temp = A_u.completeGauss();
//                MyMatrix solution = A_u.countXFromFinalMatrix(temp);
                A_u.partialGauss();
                MyMatrix solution = A_u.countXFromFinalMatrix(null);

                for (int k = 0; k < d; k++) {
                    U.set(k, u, solution.getValue(k, 0));
                }
            }

            for (int p = 0; p < productsList.size(); p++) {

                //Lista indeksow uzytkownikow przez ktorych produkt p zostal oceniony.
                List<Integer> I_p = new ArrayList<>();

                for (int j = 0; j < ratings.getRowCount(); j++) {
                    if (ratings.getValue(j, p) != 0.0) {
                        I_p.add(j);
                    }
                }

                //Macierz zawierajaca kolumny z macierzy U o indeksach wyliczonych wyzej (uzytkownicy ktorzy ocenili produkt p).
                MyMatrix U_I_p = new MyMatrix(U.getRowCount(), I_p.size());

                for (int k = 0; k < U_I_p.getRowCount(); k++) {
                    for (int l = 0; l < U_I_p.getColumnCount(); l++) {
                        U_I_p.set(k, l, U.getValue(k, I_p.get(l)));
                    }
                }

                //Macierz jednostkowa
                MyMatrix E = new MyMatrix(d, d);
                E.makeItUnit();

                //Przemnozenie macierzy jednostkowej przez lambde.
                E = E.multiplyByNumber(lambda);

                //Transpozycja macierzy U_I_p.
                MyMatrix T_U_I_p = U_I_p.transposition();

                //Mnozenie macierzy U_I_p i T_U_I_p.
                MyMatrix B_p = U_I_p.multiply(T_U_I_p);
                B_p = B_p.add(E);

                //Macierz W_p reprezentujaca sume kolumn z macierzy U pomnozonych przez odpowiednie oceny uzytkownika.
                MyMatrix W_p = new MyMatrix(d, 1);
                W_p.setZero();

                for (int k = 0; k < I_p.size(); k++) {
                    W_p = W_p.add(U.cut(0, d - 1, I_p.get(k), I_p.get(k)).multiplyByNumber(ratings.getValue(I_p.get(k), p)));
                }

                B_p = B_p.mergeWithB(W_p);
//                int[] temp = B_p.completeGauss();
//                MyMatrix solution = B_p.countXFromFinalMatrix(temp);
                B_p.partialGauss();
                MyMatrix solution = B_p.countXFromFinalMatrix(null);

                for (int k = 0; k < d; k++) {
                    P.set(k, p, solution.getValue(k, 0));
                }
            }

            //Wynikowa macierz ocen
            ratingsCalculated = U.transposition().multiply(P);

            endTime = System.currentTimeMillis();
            time += (endTime - startTime);

            previousObjectiveFunction = actualObjectiveFunction;
            actualObjectiveFunction = objectiveFunction(ratingsCalculated, U, P, lambda);
            objectiveFunctionDiff = Math.abs(actualObjectiveFunction - previousObjectiveFunction);

            if (i > 0 && previousObjectiveFunction < actualObjectiveFunction) {
                throw new Exception("Cos poszlo nie tak.");
            }

            if (objectiveFunctionDiff < 0.01) {
                stats = new Stats(i + 1, time, actualObjectiveFunction);
                System.out.println("----------------------");
                ratingsCalculated.print();
                break;
            }

        }
    return stats;
}

    public double objectiveFunction(MyMatrix ratingsCalculated, MyMatrix U, MyMatrix P, double lambda) {

        double firstSumElement = 0.0;

        for(int i = 0; i < ratings.getRowCount(); i++) {
            for(int j = 0; j < ratings.getColumnCount(); j++) {
                if(ratings.getValue(i, j) != 0.0) {
                    firstSumElement += Math.pow(ratings.getValue(i, j) - ratingsCalculated.getValue(i, j), 2);
                }
            }
        }

        double secondSumElement = 0.0;

        for(int j = 0; j < U.getColumnCount(); j++) {
            for(int i = 0; i < U.getRowCount(); i++) {
                secondSumElement += Math.pow(U.getValue(i, j), 2);
            }
        }

        for(int j = 0; j < P.getColumnCount(); j++) {
            for(int i = 0; i < P.getRowCount(); i++) {
                secondSumElement += Math.pow(P.getValue(i, j), 2);
            }
        }

        secondSumElement *= lambda;

        return firstSumElement + secondSumElement;
    }

    public void ratingsPartiallySetZero() {

        ratings.set(2, 1, 0.0);
        ratings.set(3, 4, 0.0);
        ratings.set(8, 1, 0.0);
        ratings.set(5, 0, 0.0);
        ratings.set(1, 3, 0.0);
        ratings.set(0, 3, 0.0);
        ratings.set(2, 7, 0.0);
        ratings.set(7, 7, 0.0);
        ratings.set(4, 9, 0.0);
        ratings.set(9, 9, 0.0);
    }

    public List<ProductData> getProductsDataList() {
        return productsDataList;
    }

    public List<String> getUsersList() {
        return usersList;
    }

    public List<Integer> getProductList() {
        return productsList;
    }

    public MyMatrix getRatings() {
        return ratings;
    }

    public void setRatings(MyMatrix ratings) {
        this.ratings = ratings;
    }
}
