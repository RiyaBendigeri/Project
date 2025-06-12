package org.example;

public class Categories {
    private int id;
    private String name;
    Categories(int id,String name)
    {
        this.id=id;
        this.name=name;
    }
    int getID()
    {
        return(this.id);
    }
    String getName()
    {
        return(this.name);
    }
    void setID(int id)
    {
        this.id=id;
    }
    void setName(String name)
    {
        this.name=name;
    }
    void deleteobj(int id)
    {
        this.id=0;
        this.name=null;
    }
}