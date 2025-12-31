package datastructures.avl;

public class Customer {

    private int customerId;
    private String name;
    private String email;
    private LinkedList<Order> orders;

    // Shared AVL tree used as an in-memory index for fast customer lookup by ID
    private static AVL<Customer> customers = new AVL<>();

    public Customer(int customerId, String name, String email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.orders = new LinkedList<>();
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LinkedList<Order> getOrders() {
        return orders;
    }

    // Returns customers ordered by customerId (AVL in-order traversal)
    public static LinkedList<Customer> getCustomers() {
        return customers.inOrderTraversal();
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOrders(LinkedList<Order> orders) {
        this.orders = orders;
    }

    // Rebuilds the AVL index from an existing list (used after loading persisted data)
    public static void setCustomers(LinkedList<Customer> list) {
        customers = new AVL<>();

        if (list == null || list.empty()) {
            return;
        }

        list.findFirst();
        while (true) {
            Customer c = list.retrieve();
            customers.insert(c.getCustomerId(), c);

            if (list.last()) break;
            list.findNext();
        }
    }

    public void addOrder(Order order) {
        this.orders.insert(order);
    }

    // Creates a review associated with this customer and delegates storage to Review
    public void addReview(int reviewId, int productId, int rating, String comment) {
        Review review = new Review(reviewId, productId, this.customerId, rating, comment);
        Review.addReview(review);
    }

    // Prints all orders belonging to this customer
    public void orderHistory() {
        if (orders.empty()) {
            System.out.println("No orders found for, " + name);
            return;
        }

        System.out.println("Order History for " + name + ":");
        System.out.println("--------------------------------");

        int count = 1;
        orders.findFirst();
        while (true) {
            Order order = orders.retrieve();
            System.out.println("#" + count + " " + order.toString());
            count++;

            if (orders.last()) break;
            orders.findNext();
        }
    }

    // Adds customer only if the ID does not already exist in the AVL tree
    public static boolean addCustomer(Customer customer) {

        Customer existing = customers.search(customer.getCustomerId());
        if (existing != null) {
            System.out.println("Customer already exists!");
            return false;
        }

        customers.insert(customer.getCustomerId(), customer);
        System.out.println("Customer registered successfully!");
        return true;
    }

    public static Customer findCustomer(int customerId) {
        return customers.search(customerId);
    }

    // Returns customers sorted alphabetically by name (case-insensitive)
    public static LinkedList<Customer> getAllCustomersSortedByName() {
        LinkedList<Customer> result = new LinkedList<>();

        if (customers == null || customers.empty()) {
            return result;
        }

        LinkedList<Customer> all = customers.inOrderTraversal();
        int size = all.getSize();
        if (size == 0) return result;

        Customer[] arr = new Customer[size];
        all.findFirst();
        for (int i = 0; i < size; i++) {
            arr[i] = all.retrieve();
            if (!all.last()) {
                all.findNext();
            }
        }

        // Manual sort kept explicit for assignment constraints
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (arr[j].getName().compareToIgnoreCase(arr[j + 1].getName()) > 0) {
                    Customer temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }

        for (int i = 0; i < size; i++) {
            result.insert(arr[i]);
        }

        return result;
    }

    public String toString() {
        return "Customer[ID=" + customerId + ", Name=" + name + ", Email=" + email + ", Orders=" + orders + "]";
    }
}
