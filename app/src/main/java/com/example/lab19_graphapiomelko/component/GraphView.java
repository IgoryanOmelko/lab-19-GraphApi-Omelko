package com.example.lab19_graphapiomelko.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.lab19_graphapiomelko.model.Graph;
import com.example.lab19_graphapiomelko.model.Link;
import com.example.lab19_graphapiomelko.model.Node;

public class GraphView extends SurfaceView {
    public Graph graph;
    Paint paint;
    int lasthit = -1;
    int lasthitLink = -1;
    int selectedIndex1 = -1;
    int selectedIndex2 = -1;
    public int selectedIndexLink = -1;
    float radius = 70.0f;
    float halfside = 35.0f;
    float lastX, lastY;
    float lenght = 50;
    double alpha;
    double betta;
    double gamma = Math.PI / 9;
    double gamma2 = 2 * Math.PI - gamma;


    public GraphView(Context context, AttributeSet attributes) {
        super(context, attributes);
        paint = new Paint();
        paint.setAntiAlias(true);
        setWillNotDraw(false);
    }

    /**
     * Method for adding node on the graph
     *
     * @param x    x coordinate of position the node
     * @param y    y coordinate of position the node
     * @param text text, that displayed on the node
     */
    public void AddNode(float x, float y, String text, int graphID) {
        graph.AddNode(100.0f, 100.0f, text, graphID);
        invalidate();
    }

    /**
     * Method for removing selected node from the graph
     */
    public void RemoveSelectedNode() {
        if (selectedIndex1 < 0) {
            return;
        } else {
            graph.RemoveNode(selectedIndex1);
            selectedIndex1 = -1;
            invalidate();
        }
    }

    public void RemoveSelectedLink() {
        if (selectedIndexLink < 0) {
            return;
        } else {
            graph.RemoveLink(selectedIndexLink);
            selectedIndexLink = -1;
            invalidate();
        }
    }

    /**
     * Method for adding text on the selected node
     *
     * @param text text, that displayed on the node
     */
    public void AddTextOnSelectedNode(String text) {
        if (selectedIndex1 < 0) {
            return;
        } else {
            graph.AddTextOnNode(selectedIndex1, text);
            selectedIndex1 = -1;
            invalidate();
        }
    }

    /**
     * Method for setting selected link
     *
     * @param valueAB value from node A to node B
     * @param valueBA value from node B to node A
     * @param arrows  value, what switch on display arrows
     */
    public void AddTextOnSelectedLink(float valueAB, float valueBA, boolean arrows) {
        if (selectedIndexLink < 0) {
            return;
        } else {
            graph.AddValueOnLink(selectedIndexLink, valueAB, valueBA, arrows);
            selectedIndex1 = -1;
            invalidate();
        }
    }

    /**
     * Method for link selected nodes. It add link on graph between nodes
     */
    public void LinkSelectedNodes() {
        if (selectedIndex1 < 0) {
            return;
        } else {
            if (selectedIndex2 < 0) {
                return;
            } else {
                graph.AddLink(selectedIndex1, selectedIndex2);
                invalidate();
            }
        }
    }

    /**
     * Method for link nodes, that had got index. Use it for link nodes when read database
     *
     * @param indexA index of first node
     * @param indexB index of second node
     */
    public void LinkNamedNodes(int indexA, int indexB) {
        selectedIndex1 = indexA;
        selectedIndex2 = indexB;
        graph.AddLink(indexA, indexB);
        invalidate();
    }

    /**
     * Method for get number of link in link array after link was clicked
     *
     * @param x is x coordinate
     * @param y is y coordinate
     * @return index of clicked link
     */
    public int GetLinkAtXY(float x, float y) {
        for (int i = 0; i < graph.links.size(); i++) {
            Link l = graph.links.get(i);
            Node na = graph.nodes.get(l.a);
            Node nb = graph.nodes.get(l.b);
            float bx = (na.x + nb.x) * 0.5f;//middle point of line between nodes A and B
            float by = (na.y + nb.y) * 0.5f;
            if (x >= bx - halfside && x <= bx + halfside && y >= by - halfside && y <= by + halfside) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Method for get number of node in node array after node was clicked
     *
     * @param x is x coordinate
     * @param y is y coordinate
     * @return index of clicked node
     */
    public int GetNodeAtXY(float x, float y) {
        for (int i = graph.nodes.size() - 1; i >= 0; i--) {
            Node n = graph.nodes.get(i);
            float dx = x - n.x;
            float dy = y - n.y;
            if (dx * dx + dy * dy <= radius * radius) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Method for selecting and moving the node, that we clicked
     *
     * @param event type of event, thay we call when we click on display
     * @return event of GraphView
     */
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int i = GetNodeAtXY(x, y);
                int j = GetLinkAtXY(x, y);
                lasthit = i;
                lasthitLink = j;
                if (j < 0) {
                    selectedIndexLink = -1;
                } else {
                    selectedIndexLink = j;
                }
                if (i < 0) {
                    selectedIndex1 = -1;
                    selectedIndex2 = -1;
                } else {
                    if (selectedIndex1 >= 0) {
                        selectedIndex2 = i;
                    } else {
                        selectedIndex1 = i;
                    }
                }
                lastX = x;
                lastY = y;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                if (lasthit >= 0) {
                    Node n = graph.nodes.get(lasthit);
                    n.x += x - lastX;
                    n.y += y - lastY;
                    invalidate();
                }
                lastX = x;
                lastY = y;
                return true;
        }
        return super.onTouchEvent(event);
    }

    protected float[] drawArrows(float xc0, float xc1, float yc0, float yc1, double alpha) {
        float xp0;
        float xp1;
        float xp2;
        float yp0;
        float yp1;
        float yp2;
        if (xc1 >= xc0) {
            if (yc1 >= yc0) {
                xp0 = xc0 + radius * (float) Math.sin(alpha);
                yp0 = yc0 + radius * (float) Math.cos(alpha);
            } else {
                xp0 = xc0 + radius * (float) Math.sin(alpha);
                yp0 = yc0 - radius * (float) Math.cos(alpha);
            }
        } else {
            if (yc1 >= yc0) {
                xp0 = xc0 - radius * (float) Math.sin(alpha);
                yp0 = yc0 + radius * (float) Math.cos(alpha);
            } else {
                xp0 = xc0 - radius * (float) Math.sin(alpha);
                yp0 = yc0 - radius * (float) Math.cos(alpha);
            }
        }
        if (xp0 >= xc0) {
            if (yp0 > yc0) {
                xp1 = xp0 + lenght * (float) Math.sin(gamma + alpha);
                yp1 = yp0 + lenght * (float) Math.cos(gamma + alpha);
                xp2 = xp0 + lenght * (float) Math.sin(gamma2 + alpha);
                yp2 = yp0 + lenght * (float) Math.cos(gamma2 + alpha);
            } else {
                xp1 = xp0 + lenght * (float) Math.sin(gamma + alpha);
                yp1 = yp0 - lenght * (float) Math.cos(gamma + alpha);
                xp2 = xp0 + lenght * (float) Math.sin(gamma2 + alpha);
                yp2 = yp0 - lenght * (float) Math.cos(gamma2 + alpha);
            }
        } else {
            if (yp0 >= yc0) {
                xp1 = xp0 - lenght * (float) Math.sin(gamma + alpha);
                yp1 = yp0 + lenght * (float) Math.cos(gamma + alpha);
                xp2 = xp0 - lenght * (float) Math.sin(gamma2 + alpha);
                yp2 = yp0 + lenght * (float) Math.cos(gamma2 + alpha);
            } else {
                xp1 = xp0 - lenght * (float) Math.sin(gamma + alpha);
                yp1 = yp0 - lenght * (float) Math.cos(gamma + alpha);
                xp2 = xp0 - lenght * (float) Math.sin(gamma2 + alpha);
                yp2 = yp0 - lenght * (float) Math.cos(gamma2 + alpha);
            }
        }
        float[] coords = new float[6];
        coords[0] = xp0;
        coords[1] = yp0;
        coords[2] = xp1;
        coords[3] = yp1;
        coords[4] = xp2;
        coords[5] = yp2;
        return coords;
    }

    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.rgb(255, 255, 255));
//        paint.setColor(Color.BLACK);
//        paint.setTextSize(25);
//        //canvas.drawText(graph.Name,0,25,paint);
//        for (int i = 0; i < graph.links.size(); i++) {
//            Link l = graph.links.get(i);
//            Node na = graph.nodes.get(l.a);
//            Node nb = graph.nodes.get(l.b);
//            if (i == selectedIndexLink) {
//                paint.setColor(Color.argb(255, 255, 0, 255));
//            } else {
//                paint.setColor(Color.argb(127, 0, 0, 0));
//            }
//            canvas.drawLine(na.x, na.y, nb.x, nb.y, paint);
//            float bx = (na.x + nb.x) * 0.5f;
//            float by = (na.y + nb.y) * 0.5f;
//            float xc0 = bx - halfside;
//            float xc1 = bx + halfside;
//            float yc0 = by - halfside;
//            float yc1 = by + halfside;
//
//            double a, b, c;
//
//            if (nb.x >= na.x) {
//                a = nb.x - na.x;
//            } else {
//                a = na.x - nb.x;
//            }
//            if (nb.y >= na.y) {
//                b = nb.y - na.y;
//            } else {
//                b = na.y - nb.y;
//            }
//            c = Math.sqrt(a * a + b * b);
//            alpha = Math.asin(a / c);
//            betta = Math.asin(a / b);
//
//            betta = Math.asin(a / b);
//
//            float[] coords = new float[6];
//
////            if (l.arrows && (l.valueAB > 0 && l.valueBA > 0)) {
////                coords = drawArrows(na.x, nb.x, na.y, nb.y, alpha);
////                canvas.drawLine(coords[0], coords[1], coords[2], coords[3], paint);
////                canvas.drawLine(coords[0], coords[1], coords[4], coords[5], paint);
////                coords = drawArrows(nb.x, na.x, nb.y, na.y, alpha);
////                canvas.drawLine(coords[0], coords[1], coords[2], coords[3], paint);
////                canvas.drawLine(coords[0], coords[1], coords[4], coords[5], paint);
////            }
////            if (l.arrows && (l.valueAB > 0 && l.valueBA == 0)) {
////                coords = drawArrows(na.x, nb.x, na.y, nb.y, alpha);
////                canvas.drawLine(coords[0], coords[1], coords[2], coords[3], paint);
////                canvas.drawLine(coords[0], coords[1], coords[4], coords[5], paint);
////            }
////            if (l.arrows && (l.valueAB == 0 && l.valueBA > 0)) {
////                coords = drawArrows(nb.x, na.x, nb.y, na.y, alpha);
////                canvas.drawLine(coords[0], coords[1], coords[2], coords[3], paint);
////                canvas.drawLine(coords[0], coords[1], coords[4], coords[5], paint);
////            }
////
////
////            canvas.drawRect(xc0, yc0, xc1, yc1, paint);
////            if (l.valueAB > 0) {
////                float xv = (graph.nodes.get(l.b).x + graph.nodes.get(l.a).x) / 2;
////                float yv = (graph.nodes.get(l.b).y + graph.nodes.get(l.a).y) / 2;
////                if (xv < 0) {
////                    xv *= -1;
////                }
////                if (yv < 0) {
////                    yv *= -1;
////                }
////                paint.setTextSize(25);
////                paint.setColor(Color.BLACK);
////                canvas.drawText(String.valueOf(l.valueAB), xv - String.valueOf(l.valueAB).length() * 5, yv, paint);
////            }
////            if (l.valueBA > 0) {
////                float xv = (graph.nodes.get(l.b).x + graph.nodes.get(l.a).x) / 2;
////                float yv = ((graph.nodes.get(l.b).y + graph.nodes.get(l.a).y) / 2) + 22;
////                if (xv < 0) {
////                    xv *= -1;
////                }
////                if (yv < 0) {
////                    yv *= -1;
////                }
////                paint.setTextSize(25);
////                paint.setColor(Color.BLACK);
////                canvas.drawText(String.valueOf(l.valueBA), xv - String.valueOf(l.valueBA).length() * 5, yv, paint);
////            }
//        }
//        for (int i = 0; i < graph.nodes.size(); i++) {
//            Node n = graph.nodes.get(i);
//            paint.setStyle(Paint.Style.FILL);
//            if (i == selectedIndex1) {
//                paint.setColor(Color.argb(50, 127, 0, 255));
//            } else {
//                if (i == selectedIndex2) {
//                    paint.setColor(Color.argb(50, 255, 0, 50));
//                } else {
//                    paint.setColor(Color.argb(50, 0, 127, 255));
//                }
//            }
//            canvas.drawCircle(n.x, n.y, radius, paint);
//            paint.setStyle(Paint.Style.STROKE);
//            if (i == selectedIndex1) {
//                paint.setColor(Color.rgb(127, 0, 255));
//            } else {
//                if (i == selectedIndex2) {
//                    paint.setColor(Color.rgb(255, 0, 50));
//                } else {
//                    paint.setColor(Color.rgb(0, 127, 255));
//                }
//            }
//            canvas.drawCircle(n.x, n.y, radius, paint);
//            paint.setTextSize(50);
//            paint.setColor(Color.BLACK);
//            canvas.drawText(n.text, n.x - n.text.length() * 10, n.y + 145, paint);
        }
    }

