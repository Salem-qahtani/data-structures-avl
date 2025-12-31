package datastructures.avl;

import java.io.BufferedReader;
import java.io.FileReader;

public class SimpleCSVReader {

    private static String[] splitCSV(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }

    private static String clean(String s) {
        s = s.trim();
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            s = s.substring(1, s.length() - 1);
        }
        return s;
    }

    public static datastructures.avl.LinkedList<Product> readProducts(String file) {
    	datastructures.avl.LinkedList<Product> list = new datastructures.avl.LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] v = splitCSV(line);
                int productId = Integer.parseInt(clean(v[0]));
                String name   = clean(v[1]);
                double price  = Double.parseDouble(clean(v[2]));
                int stock     = Integer.parseInt(clean(v[3]));
                Product p = new Product(productId, name, price, stock);
                list.insert(p);
            }
        } catch (Exception e) {
            System.out.println("Error reading products: " + e.getMessage());
        }
        return list;
    }

    public static datastructures.avl.LinkedList<Customer> readCustomers(String file) { 
    	datastructures.avl.LinkedList<Customer> list = new datastructures.avl.LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] v = splitCSV(line);
                int id = Integer.parseInt(clean(v[0]));
                String name = clean(v[1]);
                String email = clean(v[2]);
                list.insert(new Customer(id, name, email));
            }
        } catch (Exception e) {
            System.out.println("Error reading customers: " + e.getMessage());
        }
        return list;
    }

    public static datastructures.avl.LinkedList<Order> readOrders(String file) { 
    	datastructures.avl.LinkedList<Order> list = new datastructures.avl.LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] v = splitCSV(line);
                int orderId    = Integer.parseInt(clean(v[0]));
                int customerId = Integer.parseInt(clean(v[1]));
                String productIdsStr = clean(v[2]);
                datastructures.avl.LinkedList<Integer> items = new datastructures.avl.LinkedList<>();
                if (!productIdsStr.isEmpty()) {
                    String[] ids = productIdsStr.split(";");
                    for (int i = 0; i < ids.length; i++) {
                        String s = ids[i].trim();
                        if (!s.isEmpty()) items.insert(Integer.parseInt(s));
                    }
                }
                double totalPrice = Double.parseDouble(clean(v[3]));
                String orderDate  = clean(v[4]);
                String status     = clean(v[5]);
                list.insert(new Order(orderId, customerId, items, totalPrice, orderDate, status));
            }
        } catch (Exception e) {
            System.out.println("Error reading orders: " + e.getMessage());
        }
        return list;
    }

    public static datastructures.avl.LinkedList<Review> readReviews(String file) {  
    	datastructures.avl.LinkedList<Review> list = new datastructures.avl.LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] v = splitCSV(line);
                int reviewId   = Integer.parseInt(clean(v[0]));
                int productId  = Integer.parseInt(clean(v[1]));
                int customerId = Integer.parseInt(clean(v[2]));
                int rating     = Integer.parseInt(clean(v[3]));
                String comment = clean(v[4]);
                list.insert(new Review(reviewId, productId, customerId, rating, comment));
            }
        } catch (Exception e) {
            System.out.println("Error reading reviews: " + e.getMessage());
        }
        return list;
    }
}
