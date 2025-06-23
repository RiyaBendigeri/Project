package org.example;

import jakarta.persistence.*;

@Entity
@Table(name="categories")
public class Categories {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String name;
    public Categories(){}
    public Categories(int id,String name)
    {
        this.id=id;
        this.name=name;
    }
    public int getID()
    {
        return(this.id);
    }
    public String getName()
    {
        return(this.name);
    }
    public void setID(int id)
    {
        this.id=id;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public void deleteobj(int id)
    {
        this.id=0;
        this.name=null;
    }
}