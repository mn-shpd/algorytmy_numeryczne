import java.io.IOException;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class Main {

    public static Recommendations prepareData(int productAmount, int minimumReviewsAmount, String group) throws IOException {

        Recommendations recommendations = new Recommendations();
        recommendations.loadProductData(productAmount, minimumReviewsAmount, group, "amazon-meta/amazon-meta.txt");
        recommendations.fillUsersAndProductsLists();
        System.out.println(recommendations.getUsersList().size());
        recommendations.filterUsersList2(10);
        System.out.println(recommendations.getUsersList().size());
        recommendations.fillRatingsMatrix();

        recommendations.getRatings().print();

//        recommendations.ratingsPartiallySetZero();



        System.out.println("Amount of loaded products: " + recommendations.getProductList().size());
        System.out.println("Amount of loaded users: " + recommendations.getUsersList().size());

        return recommendations;
    }

    public static void main(String[] args) throws Exception {

        Recommendations recommendations = prepareData(10,100, "Book");

//        int d = 3;
//
//        Stats stats = recommendations.als(d, 0.1, 1000000);
//        System.out.println("Value of D parameter: " + d);
//        System.out.println("Average number of iterations: " + stats.getIterations());
//        System.out.println("Average time needed to calculate ratings [ms]: " + stats.getTime());
//        System.out.println("Average objectiveFunction value: " + stats.getObjectiveFunctionResult());

        //Dla testow algorytm liczy 40 razy dla jednej wartosci d. Pozniej wyniki sa usrednione.
        int amountOfAttempts = 40;

        for (int d = 1; d <= 10; d++) {

            int iterationSum = 0;
            long timeSum = 0;
            Double objectiveFunctionSum = 0.0;
            Stats stats;

            for (int i = 1; i <= amountOfAttempts; i++) {
                stats = recommendations.als(d, 0.1, 1000000);
                iterationSum += stats.getIterations();
                timeSum += stats.getTime();
                objectiveFunctionSum += stats.getObjectiveFunctionResult();
            }

            System.out.println("Value of D parameter: " + d);
            System.out.println("Average number of iterations: " + iterationSum / amountOfAttempts);
            System.out.println("Average time needed to calculate ratings [ms]: " + timeSum / amountOfAttempts);
            System.out.println("Average objectiveFunction value: " + objectiveFunctionSum / amountOfAttempts);
        }
    }
}
