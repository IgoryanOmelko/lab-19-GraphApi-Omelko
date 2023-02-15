package com.example.lab19_graphapiomelko.model;

import android.util.Log;

import java.util.ArrayList;

public class Graph {
    public ArrayList<Node> nodes = new ArrayList<Node>();
    public ArrayList<Link> links = new ArrayList<Link>();
    public int LinkID = 0;
    public int NodeID = 0;
    public String Name;
    public int ID;

    public String toString() {
        return String.valueOf(ID) + " " + Name;
    }

    public void AddNode(float x, float y, String text, int graphID) {
        nodes.add(new Node(x, y, nodes.size(), text, graphID));
        NodeID++;
        for (int i=0;i<nodes.size();i++){
            nodes.get(i).id=i;
        }

    }

    public void RemoveNode(int index) {
        if (index < 0) {
            return;
        } else {
            if (links.size() > 0) {
                RemoveLinkOnNode(index);
                for (int i = 0; i < links.size(); i++) {
                    if (links.get(i).a > index) {
                        links.get(i).a--;
                    }
                    if (links.get(i).b > index) {
                        links.get(i).b--;
                    }
                }
                nodes.remove(index);
                for (int i=0;i<nodes.size();i++){
                    nodes.get(i).id=i;
                }
                NodeID = nodes.size() - 1;
            } else {
                nodes.remove(index);
                NodeID = nodes.size() - 1;
            }

        }
    }

    public void AddLink(int nodeIndex1, int nodeIndex2) {
        if (nodeIndex1 == nodeIndex2) {
            return;
        }
        if (nodeIndex1 == -1 || nodeIndex2 == -1) {
            return;
        } else {
            if (links.size() == 0) {
                Log.e("Test", String.valueOf(LinkID));
                links.add(new Link(nodeIndex1, nodeIndex2, links.size(),ID));
                LinkID++;
                for (int i=0;i<links.size();i++){
                    links.get(i).id=i;
                }
            } else {
                if ((links.get(links.size() - 1).a == nodeIndex1 && links.get(links.size() - 1).b == nodeIndex2) || (links.get(links.size() - 1).a == nodeIndex2 && links.get(links.size() - 1).b == nodeIndex1)) {
                    Log.e("Test", "Stop do it!");
                    return;
                } else {
                    Log.e("Test", String.valueOf(LinkID));
                    links.add(new Link(nodeIndex1, nodeIndex2, links.size(),ID));
                    LinkID++;
                    for (int i=0;i<links.size();i++){
                        links.get(i).id=i;
                    }
                }
            }
        }
    }

    public void AddTextOnNode(int index, String text) {
        Node n = Graph.this.nodes.get(index);
        n.text = text;
        nodes.set(index, n);
    }

    public void AddValueOnLink(int index, float valueAB, float valueBA, boolean arrows) {
        Link l = Graph.this.links.get(index);
//        l.valueAB = valueAB;
//        l.valueBA = valueBA;
        l.arrows = arrows;
        links.set(index, l);
    }

    public void RemoveLinkOnNode(int index) {
        if (index < 0) {
            return;
        } else {
            int tmp = 0;
            do {
                Log.v("Test", String.valueOf(tmp));
                if (links.get(tmp).a == index || links.get(tmp).b == index) {
                    links.remove(tmp);
                } else {
                    tmp++;
                    continue;
                }
            } while (tmp < links.size());
            if (LinkID < 0) {
                LinkID = 0;
            } else {
                LinkID = links.size() - 1;
            }

        }
    }

    /**
     * Method for removing link by index
     *
     * @param index index of link in array
     */
    public void RemoveLink(int index) {
        if (index < 0) {
            return;
        } else {
            links.remove(index);
            LinkID = links.size() - 1;
            for (int i=0;i<links.size();i++){
                links.get(i).id=i;
            }
        }
    }
}
