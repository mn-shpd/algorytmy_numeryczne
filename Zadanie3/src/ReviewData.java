public class ReviewData {

    private String user_id;
    private double rating;

    ReviewData(String user_id, double rating) {
        this.user_id = user_id;
        this.rating = rating;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
