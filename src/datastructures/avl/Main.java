package datastructures.avl;

public class Main {

    private static void title(String t) {
        System.out.println("\n=== " + t + " ===");
    }

    private static void printProducts(LinkedList<Product> list) {
        if (list == null || list.empty()) {
            System.out.println("(empty)");
            return;
        }
        list.findFirst();
        while (true) {
            Product p = list.retrieve();
            System.out.println("ID=" + p.getProductId() +
                    ", Name=" + p.getName() +
                    ", Price=" + p.getPrice() +
                    ", Rating=" + p.getAverageRating());
            if (list.last()) break;
            list.findNext();
        }
    }

    private static void printCustomers(LinkedList<Customer> list) {
        if (list == null || list.empty()) {
            System.out.println("(empty)");
            return;
        }
        list.findFirst();
        while (true) {
            Customer c = list.retrieve();
            System.out.println("ID=" + c.getCustomerId() + ", Name=" + c.getName());
            if (list.last()) break;
            list.findNext();
        }
    }

    private static void printReviews(LinkedList<Review> list) {
        if (list == null || list.empty()) {
            System.out.println("(empty)");
            return;
        }
        list.findFirst();
        while (true) {
            System.out.println(list.retrieve());
            if (list.last()) break;
            list.findNext();
        }
    }

    private static String joinPath(String folder, String fileName) {
        if (folder == null || folder.isEmpty()) return fileName;
        // allow both "data" and "data/" and Windows "data\"
        char last = folder.charAt(folder.length() - 1);
        if (last == '/' || last == '\\') return folder + fileName;
        return folder + "/" + fileName;
    }

    public static void main(String[] args) {

        // Demo main class to showcase usage of the core logic.
        // Users can replace CSV files in /data or provide their own folder path.

        // Default folder inside the repository:
        //   data/products.csv
        //   data/customers.csv
        //   data/orders.csv
        //   data/reviews.csv
        String dataFolder = "data";

        String PRODUCTS  = joinPath(dataFolder, "products.csv");
        String CUSTOMERS = joinPath(dataFolder, "customers.csv");
        String ORDERS    = joinPath(dataFolder, "orders.csv");
        String REVIEWS   = joinPath(dataFolder, "reviews.csv");

        // Read CSV files
        LinkedList<Product>  prods = SimpleCSVReader.readProducts(PRODUCTS);
        LinkedList<Customer> custs = SimpleCSVReader.readCustomers(CUSTOMERS);
        LinkedList<Order>    ords  = SimpleCSVReader.readOrders(ORDERS);
        LinkedList<Review>   revs  = SimpleCSVReader.readReviews(REVIEWS);

        // Basic check
        if (prods == null || custs == null || ords == null || revs == null) {
            System.out.println("Failed to load one or more CSV files.");
            System.out.println("Expected files in: " + dataFolder);
            System.out.println(" - " + PRODUCTS);
            System.out.println(" - " + CUSTOMERS);
            System.out.println(" - " + ORDERS);
            System.out.println(" - " + REVIEWS);
            System.out.println("Fix the paths or place the files in a /data folder inside the project.");
            return;
        }

        // Set global data
        Product.setAllProducts(prods);
        Customer.setCustomers(custs);
        Order.setOrders(ords);
        Review.setReviews(revs);

        System.out.println("Loaded: Products=" + prods.getSize() +
                " | Customers=" + custs.getSize() +
                " | Orders=" + ords.getSize() +
                " | Reviews=" + revs.getSize());

        // === Demo calls ===

        title("Search Products: 'TV'");
        printProducts(Product.productSearch("TV"));

        title("Products Price 100 - 500");
        printProducts(Product.getProductsWithinPriceRange(100, 500));

        title("Top 3 Products");
        printProducts(Product.getTop3ByRating());

        title("Find Customer 201");
        Customer c201 = Customer.findCustomer(201);
        if (c201 != null) {
            LinkedList<Customer> one = new LinkedList<>();
            one.insert(c201);
            printCustomers(one);
        } else {
            System.out.println("Customer 201 not found.");
        }

        title("Order History for Customer 201");
        if (c201 != null) {
            c201.orderHistory();
        }

        title("Add/Edit Review (Customer 201 → Product 101)");
        if (c201 != null) {
            c201.addReview(9000, 101, 5, "Great item!");
            Review.editReview(9000, 4, "Edited review");
            printReviews(Review.getReviewsByCustomer(201));
        } else {
            System.out.println("Skipping review demo because customer 201 was not found.");
        }

        title("Common High-Rated Products (201 & 221)");
        printProducts(Review.getCommonHighRatedProducts(201, 221));

        System.out.println("\n=== DONE (DEMO) ===");
    }
}

