package com.example.lab19_graphapiomelko.model;

public class Node {
    public float x, y;
    public int id;
    public String text;
    public int GraphID;

    public Node(float x, float y, int id, String text, int GraphID) {
        this.x = x;
        this.y = y;
        this.id=id;
        this.text=text;
        this.GraphID=GraphID;
    }
}
