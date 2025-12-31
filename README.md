# Data Structures – AVL Tree (Java)

A Java project that demonstrates the implementation and usage of an AVL Tree, along with supporting custom data structures.  
The project includes a small demo application that loads data from CSV files and performs common operations such as search, sorting, filtering, and relationships between entities.

---

## Features

- AVL Tree implementation (self-balancing binary search tree)
- Insert, delete, search, and range query operations
- Custom Linked List used instead of Java built-in collections
- Domain models:
  - Product
  - Customer
  - Order
  - Review
- CSV-based data loading
- Demo main program showcasing the system

---

## Project Structure

data-structures-avl/
├── src/
│   └── datastructures/avl/
│       ├── AVL.java
│       ├── LinkedList.java
│       ├── Product.java
│       ├── Customer.java
│       ├── Order.java
│       ├── Review.java
│       ├── SimpleCSVReader.java
│       └── Main.java
├── data/
│   ├── products.csv
│   ├── customers.csv
│   ├── orders.csv
│   └── reviews.csv
└── .gitignore

---

## How to Run

1. Clone the repository:
   git clone https://github.com/Salem-qahtani/data-structures-avl.git

2. Open the project in Eclipse or any Java IDE.

3. Ensure the data folder exists in the project root and contains:
   - products.csv
   - customers.csv
   - orders.csv
   - reviews.csv

4. Run the Main.java file.

The application will load the CSV files and execute a demo showcasing different operations.

---

## Demo Operations

The demo application (Main.java) includes examples of:
- Searching products by name
- Filtering products by price range
- Retrieving top-rated products
- Finding customers and displaying order history
- Adding and editing reviews
- Finding common high-rated products between customers

---

## Notes

- This project avoids Java built-in collections to focus on data structure implementation.
- The Main class serves as a demo and testing entry point.
- CSV files can be replaced with custom data as long as the format is preserved.

---

## Author

Salem Al-Qahtani

---

## License

This project is provided for educational purposes.
