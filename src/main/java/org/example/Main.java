package org.example;



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
