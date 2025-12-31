package datastructures.avl;

public class Review {

    private int reviewId;
    private int productId;
    private int customerId;
    private int rating;
    private String comment;

    // Central in-memory list of all reviews across the system.
    private static LinkedList<Review> reviews = new LinkedList<>();

    public Review(int reviewId, int productId, int customerId, int rating, String comment) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.customerId = customerId;
        this.rating = rating;
        this.comment = comment;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getProductId() {
        return productId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public static LinkedList<Review> getReviews() {
        return reviews;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Rebuilds the global reviews list from persisted data and re-links each review to its product.
    public static void setReviews(LinkedList<Review> list) {
        reviews = new LinkedList<>();

        if (list == null || list.empty()) {
            return;
        }

        list.findFirst();
        while (true) {
            Review r = list.retrieve();

            reviews.insert(r);

            Product p = Product.findProduct(r.getProductId());
            if (p != null) {
                p.addReview(r);
            }

            if (list.last()) break;
            list.findNext();
        }
    }

    // Validates rating and entity existence before adding, then attaches the review to the product as well.
    public static boolean addReview(Review review) {
        if (review.getRating() < 1 || review.getRating() > 5) {
            System.out.println("Invalid rating! It must be between 1 and 5.");
            return false;
        }

        Product product = Product.findProduct(review.getProductId());
        if (product == null) {
            System.out.println("Product not found! Cannot add review.");
            return false;
        }

        Customer customer = Customer.findCustomer(review.getCustomerId());
        if (customer == null) {
            System.out.println("Customer not found! Cannot add review.");
            return false;
        }

        reviews.insert(review);
        product.addReview(review);
        System.out.println("Review added successfully!");
        return true;
    }

    public static boolean editReview(int reviewId, int newRating, String newComment) {

        if (reviews.empty()) {
            return false;
        }

        reviews.findFirst();
        while (true) {
            Review r = reviews.retrieve();

            if (r.getReviewId() == reviewId) {
                if (newRating >= 1 && newRating <= 5) {
                    r.setRating(newRating);
                }
                if (newComment != null && !newComment.isEmpty()) {
                    r.setComment(newComment);
                }
                return true;
            }

            if (reviews.last())
                break;

            reviews.findNext();
        }

        return false;
    }

    // Finds products reviewed by both customers and returns those with overall average rating > 4.0.
    public static LinkedList<Product> getCommonHighRatedProducts(int customerId1, int customerId2) {
        LinkedList<Product> result = new LinkedList<>();

        LinkedList<Integer> firstCustomerProducts = new LinkedList<>();
        LinkedList<Integer> secondCustomerProducts = new LinkedList<>();

        if (reviews.empty())
            return result;

        reviews.findFirst();
        while (true) {
            Review r = reviews.retrieve();

            if (r.getCustomerId() == customerId1 && !contains(firstCustomerProducts, r.getProductId())) {
                firstCustomerProducts.insert(r.getProductId());
            }

            if (r.getCustomerId() == customerId2 && !contains(secondCustomerProducts, r.getProductId())) {
                secondCustomerProducts.insert(r.getProductId());
            }

            if (reviews.last())
                break;
            reviews.findNext();
        }

        if (!firstCustomerProducts.empty()) {
            firstCustomerProducts.findFirst();
            while (true) {
                int productId = firstCustomerProducts.retrieve();

                if (contains(secondCustomerProducts, productId)) {
                    Product p = Product.findProduct(productId);
                    if (p != null && p.getAverageRating() > 4.0) {
                        result.insert(p);
                    }
                }

                if (firstCustomerProducts.last())
                    break;
                firstCustomerProducts.findNext();
            }
        }

        return result;
    }

    // Helper to avoid duplicate product IDs in intermediate lists.
    private static boolean contains(LinkedList<Integer> list, int value) {
        if (list.empty()) return false;

        list.findFirst();
        while (true) {
            if (list.retrieve() == value)
                return true;
            if (list.last())
                break;
            list.findNext();
        }
        return false;
    }

    public static LinkedList<Review> getReviewsByCustomer(int customerId) {
        LinkedList<Review> customerReviews = new LinkedList<>();

        if (reviews.empty()) {
            return customerReviews;
        }

        reviews.findFirst();
        while (true) {
            Review r = reviews.retrieve();

            if (r.getCustomerId() == customerId) {
                customerReviews.insert(r);
            }

            if (reviews.last()) {
                break;
            }
            reviews.findNext();
        }

        return customerReviews;
    }

    public String toString() {
        return "Review [reviewId=" + reviewId + ", productId=" + productId + ", customerId=" + customerId + ", rating="
                + rating + ", comment=" + comment + "]";
    }

}
