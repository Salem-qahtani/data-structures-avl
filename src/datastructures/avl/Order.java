package datastructures.avl;

public class Order {

    private int orderId;
    private int customerId;
    private LinkedList<Integer> items;
    private double totalPrice;
    private String orderDate;
    private String status;

    // Shared in-memory index of orders by orderId for fast lookup.
    private static AVL<Order> orders = new AVL<>();

    public Order(int orderId, int customerId, LinkedList<Integer> items,
                 double totalPrice, String orderDate, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.status = status;
    }

    public int getOrderId() { return orderId; }
    public int getCustomerId() { return customerId; }
    public LinkedList<Integer> getItems() { return items; }
    public double getTotalPrice() { return totalPrice; }
    public String getOrderDate() { return orderDate; }
    public String getStatus() { return status; }

    // Returns orders ordered by orderId (AVL in-order traversal).
    public static LinkedList<Order> getOrders() {
        return orders.inOrderTraversal();
    }

    public void setOrderId(int orderId) { this.orderId = orderId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setItems(LinkedList<Integer> items) { this.items = items; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public void setStatus(String status) { this.status = status; }

    // Rebuilds the AVL index from a list and re-links each order to its customer history.
    public static void setOrders(LinkedList<Order> list) {
        orders = new AVL<>();

        if (list == null || list.empty()) {
            return;
        }

        list.findFirst();
        while (true) {
            Order o = list.retrieve();
            orders.insert(o.getOrderId(), o);

            Customer c = Customer.findCustomer(o.getCustomerId());
            if (c != null) {
                c.addOrder(o);
            }

            if (list.last()) break;
            list.findNext();
        }
    }

    public static Order findOrder(int orderId) {
        return orders.search(orderId);
    }

    // Adds the order to the global index and attaches it to the owning customer (if found).
    public static boolean addOrder(Order order) {

        if (orders.search(order.getOrderId()) != null) {
            System.out.println("Order is found!");
            return false;
        }

        orders.insert(order.getOrderId(), order);

        Customer customer = Customer.findCustomer(order.getCustomerId());
        if (customer != null) {
            customer.addOrder(order);
        }

        System.out.println("Order is added successfully!");
        return true;
    }

    public static boolean cancelOrder(int orderId) {
        Order order = findOrder(orderId);
        if (order != null) {
            order.setStatus("Cancelled");
            System.out.println("Order #" + orderId + " is cancelled!");
            return true;
        }

        System.out.println("Order not found!");
        return false;
    }

    public static boolean updateOrderStatus(int orderId, String newStatus) {
        Order order = findOrder(orderId);
        if (order != null) {
            order.setStatus(newStatus);
            System.out.println("Order #" + orderId + " new status is " + newStatus);
            return true;
        }

        System.out.println("Order not found!");
        return false;
    }

    // Assumes orderDate is stored in a lexicographically sortable format (e.g., YYYY-MM-DD).
    public static LinkedList<Order> getOrdersBetweenDates(String startDate, String endDate) {
        LinkedList<Order> results = new LinkedList<>();
        if (orders.empty()) return results;

        LinkedList<Order> all = orders.inOrderTraversal();
        if (all.empty()) return results;

        all.findFirst();
        while (true) {
            Order o = all.retrieve();

            if (o.getOrderDate().compareTo(startDate) >= 0 &&
                o.getOrderDate().compareTo(endDate) <= 0) {
                results.insert(o);
            }

            if (all.last()) break;
            all.findNext();
        }

        return results;
    }

    public String toString() {
        int itemsCount;

        if (items == null) {
            itemsCount = 0;
        } else {
            itemsCount = items.getSize();
        }

        return "Order [orderId=" + orderId +
               ", customerId=" + customerId +
               ", itemsCount=" + itemsCount +
               ", totalPrice=" + totalPrice +
               ", orderDate=" + orderDate +
               ", status=" + status + "]";
    }
}