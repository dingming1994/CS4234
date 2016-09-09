/**
 * Q1: ABCD all need to give the optimal solution, where E aims to find a 2-OPT solution
 *
 * Q2:
 * Step 1, run Dijkstra Algorithm on first K node to find the APSP among K*K graph.
 * Step 2, build an complete K*K graph with edge weight equals to the shortest distance.
 * Step 3, run MST on resulting K*K Graph. the MST is thus the solution.
 * Complexity: Step 1: O(KTlogN) Step 2: O(K*2) Step 3: O(KlogK)
 * Since step 1 donimate 2 and 3, the complexity of algorithm is O(KTlogN).
 *
 * Q3:
 * All the following test case is generated with random number using Excel.
 *
 * TEST CASE 1:
 * The test case can be found in
 *5 4 8

 OPT = 7, APPROX = 7 RATIO = 1

 * TEST CASE 2:
 15 5 20

 OPT = 161, APPROX = 170 RATIO = 1.056

 * TEST CASE 3:
 *
 30 15 187

 OPT = 386, APPROX = 394 RATIO = 1.021

 * TEST CASE 3:
 *
 50 20 641

 OPT = 552, APPROX = 561 RATIO = 1.016

  Hence we can state that with random data, the approximation ratio is really low,
 mostly below 1.1

 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;


public class E {

    public static void main(String[] args) {
        int n, k, t;
        int x, y, w;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        String[] strs;
        try {

            while ((str = br.readLine()) != null) {
                if (str.length() > 0) {
                    strs = str.split(" ");
                    n = Integer.parseInt(strs[0]);
                    k = Integer.parseInt(strs[1]);
                    t = Integer.parseInt(strs[2]);
                    Graph g = new Graph();
                    for (int i = 0; i < n; i++) {
                        if (i == 0) g.addNode(i, 0);
                        if (i > 0) g.addNode(i, -1);
                    }
                    for (int i = 0; i < t; i++) {
                        str = br.readLine();
                        strs = str.split(" ");
                        x = Integer.parseInt(strs[0]);
                        y = Integer.parseInt(strs[1]);
                        w = Integer.parseInt(strs[2]);
                        Node nx = g.getNode(x);
                        nx.addEdge(new Edge(x, y, w));
                        Node ny = g.getNode(y);
                        ny.addEdge(new Edge(y, x, w));
                    }

                    // if k = 1, output 0
                    if (k == 1) {
                        System.out.println(0);
                        continue;
                    }
                    // Dijkstra APSP
                    int dis[][] = new int[n][n];
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++)
                            dis[i][j] = -1;
                        dis[i][i] = 0;
                    }

                    for (int tt = 0; tt < k; tt++) {
                        for (int i = 0; i < n; i++) {
                            if (i == tt) g.getNode(i).sd = 0;
                            else g.getNode(i).sd = -1;
                        }
                        Boolean isVisited[] = new Boolean[n];
                        Arrays.fill(isVisited, Boolean.FALSE);
                        PriorityQueue<Node> pq = new PriorityQueue<>(n, new QComparator());
                        pq.add(g.getNode(tt));
                        isVisited[tt] = true;
                        while (!pq.isEmpty()) {
                            Node nd = pq.poll();
                            for (Edge e : nd.connected) {
                                Node ynd = g.getNode(e.y);
                                if ((!isVisited[ynd.index]) && (ynd.sd < 0) || (nd.sd + e.w < ynd.sd)) {
                                    if (pq.contains(ynd)) pq.remove(ynd);
                                    ynd.sd = nd.sd + e.w;
                                    pq.add(ynd);
                                    isVisited[ynd.index] = true;
                                }
                            }
                        }
                        for (int i = 0; i < n; i++)
                            dis[tt][i] = g.getNode(i).sd;
                    }

                    // MST on k*k graph
                    Graph newG = new Graph();
                    for (int i = 0; i < k; i++) {
                        if (i == 0) newG.addNode(i, 0);
                        if (i > 0) newG.addNode(i, -1);

                        for (int j = 0; j < k; j++)
                            if (i != j)
                                newG.getNode(i).addEdge(new Edge(i, j, dis[i][j]));
                    }

                    Boolean isVisited[] = new Boolean[k];
                    Arrays.fill(isVisited, Boolean.FALSE);
                    PriorityQueue<Node> pq = new PriorityQueue<>(k, new QComparator());
                    pq.add(newG.getNode(0));
                    while (!pq.isEmpty()) {
                        Node nd = pq.poll();
                        isVisited[nd.index] = true;
                        for (Edge e : nd.connected) {
                            Node ynd = newG.getNode(e.y);
                            if ((!isVisited[ynd.index]) && ((ynd.sd < 0) || (e.w < ynd.sd))) {
                                if (pq.contains(ynd)) pq.remove(ynd);
                                ynd.sd = e.w;
                                pq.add(ynd);
                            }
                        }
                    }
                    int ans = 0;
                    int isCon = 1;
                    for (Node nd : newG.nodeList) {
                        ans += nd.sd;
                        if (nd.sd == -1) isCon = 0;
                    }
                    if (isCon == 0) System.out.println(-1);
                    else System.out.println(ans);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//class Edge {
//    int x, y, w;
//
//    Edge(int x1, int y1, int w1) {
//        x = x1;
//        y = y1;
//        w = w1;
//    }
//
//}
//
//class Node {
//    int index;
//    ArrayList<Edge> connected;
//    int sd;
//
//    Node(int idx, int sd) {
//        index = idx;
//        connected = new ArrayList<>();
//        this.sd = sd;
//    }
//
//    public void addEdge(Edge e) {
//        connected.add(e);
//    }
//}
//
//class Graph {
//    ArrayList<Node> nodeList;
//
//    Graph() {
//        nodeList = new ArrayList<>();
//    }
//
//    public void addNode(int idx, int sd) {
//        Node n = new Node(idx, sd);
//        nodeList.add(n);
//    }
//
//    public Node getNode(int idx) {
//        for (Node n : nodeList) {
//            if (n.index == idx) return n;
//        }
//        return null;
//    }
//}
//
//
//class QComparator implements Comparator<Node> {
//    @Override
//    public int compare(Node x, Node y) {
//        if (x.sd < y.sd) {
//            return -1;
//        }
//        if (x.sd > y.sd) {
//            return 1;
//        }
//        return 0;
//    }
//}
//
