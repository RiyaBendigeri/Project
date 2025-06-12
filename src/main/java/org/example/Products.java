package org.example;

public class Products {
    private int id;
    private String name;
    private int price;
    private int catID;

    Products(int id,String name,int price,int catID)
    {
        this.id=id;
        this.name=name;
        this.price=price;
        this.catID=catID;

    }
    int getID()
    {
        return(this.id);
    }
    String getName()
    {
        return(this.name);
    }
    int getPrice()
    {
        return(this.price);
    }
    int getcatID()
    {
        return(this.catID);
    }

    void setID(int id)
    {
        this.id=id;
    }
    void setName(String name)
    {
        this.name=name;
    }
    void setPrice(int Price)
    {
        this.price=Price;
    }
    void setcatID(int catID)
    {
        this.catID=catID;
    }

}
