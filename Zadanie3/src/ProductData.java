import java.util.List;

public class ProductData {

    private int product_id;
    private String group;
    List<ReviewData> reviews;

    public ProductData(int product_id, String group, List<ReviewData> reviews) {
        this.product_id = product_id;
        this.group = group;
        this.reviews = reviews;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<ReviewData> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewData> reviews) {
        this.reviews = reviews;
    }

//    public String toString() {
//        return "user_id: " + user_id + "\nproduct_id: " + product_id + "\nrating: " + rating + "\ngroup: " + group + "\n";
//    }
}
