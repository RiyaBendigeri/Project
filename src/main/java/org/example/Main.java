package org.example;

/*

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
    List<Products>products = new ArrayList<>();


    public List<Products> getPList()
    {
        return products;
    }
    public void insertP(Products obj)
    {
        for (Categories categories : category) {
            if(categories.getID() == obj.getcatID())
            {


                    products.add(obj);

            }
        }
        System.out.println("cannot add a product which is not there in categories");

    }
    public void displayP()
    {
        System.out.println();
        System.out.println(" ID "+ "  Name  " + " Price " + " Category ID " );
        for (Products obj : products) {
            if(obj.getID() > 0)
            {
                System.out.println("  "+ obj.getID() + "  " + obj.getName() + "  " + obj.getPrice() +  "       " + obj.getcatID()  );
            }
        }
        System.out.println();
    }
    public void updateP(int id) {
        Scanner sc = new Scanner(System.in);
        String updatevalue = sc.nextLine();


        for (Products obj : products) {
            if (obj.getID() == id) {
                if (updatevalue.equals("Name")) {
                    sc = new Scanner(System.in);
                    String updatename = sc.nextLine();
                    obj.setName(updatename);
                    displayP();
                    return;
                } else if (updatevalue == "Price") {
                    sc = new Scanner(System.in);
                    int pricenew = sc.nextInt();
                    obj.setPrice(pricenew);
                    displayP();
                    return;
                } else if (updatevalue == "catID") {
                    sc = new Scanner(System.in);
                    int catnew = sc.nextInt();
                    for (Categories cat : category) {
                        {
                            if (cat.getID() == catnew) {
                                obj.setcatID(catnew);
                                displayP();
                                return;
                            }
                        }
                        System.out.println("cannot update the id as such category with id doesnt exist");
                    }
                    displayP();
                    return;
                }
            }
        }
            System.out.println(" NO SUCH OBJECT WITH ID EXISTS");


        }





    //here store all our categories
    List<Categories>category = new ArrayList<>();

    //operations defined
    public List<Categories> getList()
    {
        return category;
    }
    public void insert(Categories obj)
    {
        category.add(obj);
    }
    public void display()
    {
        System.out.println();
        System.out.println(" ID "+ "  Name  ");
        for (Categories obj : category) {
            if(obj.getID() > 0)
            {
                System.out.println(obj.getID() + "  " + obj.getName());
            }
        }
        System.out.println();
    }
    public void update(int id,String newcat)
    {
        for (Categories obj : category) {
            if(obj.getID() == id)
            {
                obj.setName(newcat);
                display();
                return;
            }
        }
        System.out.println(" NO SUCH OBJECT WITH ID EXISTS");


    }
    public void delete(int id)
    {
        for (Categories obj : category) {
            if(obj.getID() == id)
            {
                obj.deleteobj(id);
                display();
                return;
            }
        }
        System.out.println(" NO SUCH OBJECT WITH ID EXISTS");
    }
    public static void main(String[] args) {
        Main obj = new Main();
        Categories catobj = new Categories(1,"Electronics");
        obj.insert(catobj);
        catobj = new Categories(2,"Clothing");
        obj.insert(catobj);
        catobj = new Categories(3,"Books");
        obj.insert(catobj);
        catobj = new Categories(4,"Home Appliances");
        obj.insert(catobj);
        obj.display();

        obj.update(1,"Brands");
        obj.update(10,"Brands");
        obj.delete(1);

        Products p= new Products(1,"Laptop",9000, 1);
        obj.insertP(p);
        //obj.displayP();
        p= new Products(2,"Tshirt",900, 2);
        obj.insertP(p);
        //obj.displayP();
        p= new Products(3,"The one ",250, 3);
        obj.insertP(p);
        obj.displayP();
        obj.updateP(3);





    }
}
*/
//
//import java.sql.*;
//
//
//
//public class Main {
//
//
//
//    public static void main(String[] args) {
//        private static String url = "jdbc:mysql://localhost:3306/project";
//        private String username = "root";
//        String password = "cricket@2003";
//
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");  // explicitly load driver
//            Connection conn = DriverManager.getConnection(url, username, password);
//            System.out.println("Connected successfully!");
//            Statement stmt = conn.createStatement();
//            String query = "select * from categories";
//            ResultSet rs = stmt.executeQuery(query);
//            while(rs.next())
//            {
//                int id = rs.getInt("id");
//                String name = rs.getString("name");
//                System.out.println(id+name);
//            }
//
//
//            /* query = "insert into categories (id,name) values (?,?)";
//            PreparedStatement ps = conn.prepareStatement(query);
//            ps.setInt(1,4);
//            ps.setString(2,"clothing");
//            int results = ps.executeUpdate();
//            if(results > 0)
//            {
//                System.out.println("data inserted");
//            }
//            else{
//                System.out.println("not able to insert data");
//            }
//            */
//
//            //update
////            query = "update categories set name = ? where id = ?";
////            PreparedStatement ps = conn.prepareStatement(query);
////
////            ps.setString(1,"brands");
////            ps.setInt(2,2);
////            int results = ps.executeUpdate();
////            if(results > 0)
////            {
////                System.out.println("data updated");
////            }
////            else{
////                System.out.println("not able to insert data");
////            }
//
//            query = "delete from categories where id = ?";
//            PreparedStatement ps = conn.prepareStatement(query);
//
//
//            ps.setInt(1,2);
//            int results = ps.executeUpdate();
//            if(results > 0)
//            {
//                System.out.println("data deleted");
//            }
//            else{
//                System.out.println("not able to insert data");
//            }
//
//
//
//
//
//
//
//            conn.close();
//        } catch (ClassNotFoundException e) {
//            System.out.println("Driver class not found: " + e.getMessage());
//        } catch (SQLException e) {
//            System.out.println("SQL Exception: " + e.getMessage());
//        }
//    }
//}



import java.sql.*;

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/project";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "cricket@2003";

    public static void main(String[] args) {
        Main main = new Main();


        Connection conn = main.getConnection();

        if (conn != null) {

            //main.insertCategory(conn, 5, "Furniture");
            main.updateCategory(conn, 5, "Brands");
            main.fetchCategories(conn);
            main.deleteCategory(conn, 5);

            //main.insertProducts(conn,2,"Laptop",2000,5);
            main.updateProducts(conn,2,"lapitopi");
            main.fetchProducts(conn);
            //main.deleteProduct(conn,2);
            //main.deleteCategory(conn, 5);
            //main.fetchCategories(conn);
            //main.fetchProducts(conn);
            main.closeConnection(conn);

        }
    }

    private Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Error: " + e.getMessage());
            return null;
        }
    }

    private void fetchCategories(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM categories";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " " + rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching categories: " + e.getMessage());
        }
    }

    private void insertCategory(Connection conn, int id, String name) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO categories (id, name) VALUES (?, ?)")) {
            ps.setInt(1, id);
            ps.setString(2, name);
            int result = ps.executeUpdate();
            System.out.println(result > 0 ? "Category inserted successfully!" : "Insertion failed.");
        } catch (SQLException e) {
            System.out.println("Insert Error: " + e.getMessage());
        }
    }

    private void updateCategory(Connection conn, int id, String newName) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE categories SET name = ? WHERE id = ?")) {
            ps.setString(1, newName);
            ps.setInt(2, id);
            int result = ps.executeUpdate();
            System.out.println(result > 0 ? "Category updated successfully!" : "Update failed.");
        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
        }
    }

    private void deleteCategory(Connection conn, int id) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM categories WHERE id = ?")) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            System.out.println(result > 0 ? "Category deleted successfully!" : "Deletion failed.");
        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }
    private void fetchProducts(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM products";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " " + rs.getString("name")+" " + rs.getInt("price")+" " + rs.getInt("catID"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
    }

    private void insertProducts(Connection conn, int id, String name,int price,int catid) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO products (id, name,price,catid) VALUES (? , ? , ? , ?)")) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setInt(3, price);
            ps.setInt(4, catid);
            int result = ps.executeUpdate();
            System.out.println(result > 0 ? "Product inserted successfully!" : "Insertion failed.");
        } catch (SQLException e) {
            System.out.println("Insert Error: " + e.getMessage());
        }
    }

    private void updateProducts(Connection conn, int id, String name) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE products SET name = ? WHERE id = ?")) {
            ps.setString(1, name);
            ps.setInt(2, id);
            int result = ps.executeUpdate();
            System.out.println(result > 0 ? "Product updated successfully!" : "Update failed.");
        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
        }
    }

    private void deleteProduct(Connection conn, int id) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM products WHERE id = ?")) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            System.out.println(result > 0 ? "Product deleted successfully!" : "Deletion failed.");
        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }

    private void closeConnection(Connection conn) {
        try {
            if (conn != null) conn.close();
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
