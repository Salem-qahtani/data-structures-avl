package datastructures.avl;

public class Product {

    private int productId;
    private String name;
    private double price;
    private int stock;
    private LinkedList<Review> reviews;

    // Shared in-memory index of products by productId for fast lookup.
    private static AVL<Product> products = new AVL<>();

    public Product(int productId, String name, double price, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.reviews = new LinkedList<>();
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public LinkedList<Review> getReviews() { return reviews; }

    public void setProductId(int productId) { this.productId = productId; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setReviews(LinkedList<Review> reviews) { this.reviews = reviews; }

    // Returns products ordered by productId (AVL in-order traversal).
    public static LinkedList<Product> getAllProducts() {
        return products.inOrderTraversal();
    }

    // Rebuilds the AVL index from an existing list (used after loading persisted data).
    public static void setAllProducts(LinkedList<Product> list) {
        products = new AVL<>();

        if (list == null || list.empty()) {
            return;
        }

        list.findFirst();
        while (true) {
            Product p = list.retrieve();
            products.insert(p.getProductId(), p);

            if (list.last()) break;
            list.findNext();
        }
    }

    public static Product findProduct(int productId) {
        return products.search(productId);
    }

    public static boolean addProduct(Product product) {
        if (products.search(product.getProductId()) != null) {
            System.out.println("Product is found!");
            return false;
        }
        return products.insert(product.getProductId(), product);
    }

    public static boolean deleteProduct(int productId) {
        return products.delete(productId);
    }

    public static boolean updatePrice(int productId, double newPrice) {
        Product product = products.search(productId);
        if (product != null) {
            product.setPrice(newPrice);
            return true;
        }
        return false;
    }

    public static boolean updateStock(int productId, int newStock) {
        Product product = products.search(productId);
        if (product != null) {
            product.setStock(newStock);
            return true;
        }
        return false;
    }

    // Case-insensitive substring search on product name.
    public static LinkedList<Product> productSearch(String searchTerm) {
        LinkedList<Product> results = new LinkedList<>();
        if (products.empty()) return results;

        String lowerSearch = searchTerm.toLowerCase();

        LinkedList<Product> all = products.inOrderTraversal();
        if (all.empty()) return results;

        all.findFirst();
        while (true) {
            Product p = all.retrieve();
            if (p.getName().toLowerCase().contains(lowerSearch)) {
                results.insert(p);
            }
            if (all.last()) break;
            all.findNext();
        }

        return results;
    }

    public double getAverageRating() {
        if (reviews.empty()) return 0.0;

        double sum = 0;
        int count = 0;

        reviews.findFirst();
        while (true) {
            Review review = reviews.retrieve();
            sum += review.getRating();
            count++;

            if (reviews.last()) break;
            reviews.findNext();
        }
        return sum / count;
    }

    public static LinkedList<Product> getOutOfStockProducts() {
        LinkedList<Product> outOfStock = new LinkedList<>();
        if (products.empty()) return outOfStock;

        LinkedList<Product> all = products.inOrderTraversal();
        if (all.empty()) return outOfStock;

        all.findFirst();
        while (true) {
            Product p = all.retrieve();
            if (p.getStock() == 0) {
                outOfStock.insert(p);
            }
            if (all.last()) break;
            all.findNext();
        }
        return outOfStock;
    }

    // Returns up to 3 products with the highest average rating (descending).
    public static LinkedList<Product> getTop3ByRating() {
        LinkedList<Product> top3 = new LinkedList<>();
        if (products.empty()) return top3;

        LinkedList<Product> all = products.inOrderTraversal();
        int size = all.getSize();
        if (size == 0) return top3;

        Product[] arr = new Product[size];

        all.findFirst();
        for (int i = 0; i < size; i++) {
            arr[i] = all.retrieve();
            if (!all.last()) all.findNext();
        }

        // Manual sort kept explicit for assignment constraints.
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (arr[j].getAverageRating() < arr[j + 1].getAverageRating()) {
                    Product temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }

        int limit = Math.min(3, size);
        for (int i = 0; i < limit; i++) {
            top3.insert(arr[i]);
        }

        return top3;
    }

    public static LinkedList<Product> getProductsWithinPriceRange(double minPrice, double maxPrice) {
        LinkedList<Product> result = new LinkedList<>();
        if (products.empty()) return result;

        LinkedList<Product> all = products.inOrderTraversal();
        if (all.empty()) return result;

        all.findFirst();
        while (true) {
            Product p = all.retrieve();
            if (p.price >= minPrice && p.price <= maxPrice) {
                result.insert(p);
            }

            if (all.last()) break;
            all.findNext();
        }

        return result;
    }

    public void addReview(Review review) {
        this.reviews.insert(review);
    }
}
