package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name="products")
public class Products {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String name;
    @Column
    private int price;
    @Column
    private int catid;
    public Products(){}
    public Products(int id,String name,int price,int catid)
    {
        this.id=id;
        this.name=name;
        this.price=price;
        this.catid=catid;

    }
    public int getID()
    {
        return(this.id);
    }
    public String getName()
    {
        return(this.name);
    }
    public int getPrice()
    {
        return(this.price);
    }
    public int getcatid()
    {
        return(this.catid);
    }

    public void setID(int id)
    {
        this.id=id;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public void setPrice(int Price)
    {
        this.price=Price;
    }
    public void setcatid(int catid)
    {
        this.catid=catid;
    }

}
