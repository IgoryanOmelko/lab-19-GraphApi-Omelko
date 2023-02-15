package com.example.lab19_graphapiomelko.model;

public class Link {
    public int a, b, id;//a and b is id of node. id is id of link
    public float value = 0 ;
    public int graphID;
    public boolean arrows;
    public boolean isValue=false;

    public Link(int a, int b, int id,int graphID) {
        this.a = a;
        this.b = b;
        this.id = id;
        this.graphID=graphID;
    }
}
