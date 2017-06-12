package ddd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arc on 11/11/2016.
 */
public class TreeTest {
    public static void main(String args[]) {
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        node3.setLeft(node1);
        node3.setRight(node2);
        Node node4 = new Node(4);
        node4.setLeft(node3);
        Node node5 = new Node(5);
        node4.setRight(node5);
        Node node6 = new Node(6);
        Node node7 = new Node(7);
        node5.setLeft(node6);
        node5.setRight(node7);

        printTree(node4,0);
        for(List<Integer> list:vaList){
          for(Integer val:list){
            System.out.print(val);
          }
          System.out.println();
        }
    }
    private static List<List<Integer>> vaList=new ArrayList<>();
    public static void printTree(Node node,int deep){
      List<Integer> deepList;
      if(vaList.size()<=deep){
        deepList=new ArrayList<>();
        vaList.add(deepList);
      }else {
        deepList=vaList.get(deep);
      }
      deepList.add(node.getValue());
      if(node.getLeft()!=null){
        printTree(node.getLeft(),deep+1);
      }
      if(node.getRight()!=null){
        printTree(node.getRight(),deep+1);
      }
    }
}


class Node {

    private Integer value;
    private Node left;
    private Node right;

    public Node(Integer value) {
        this.value = value;
    }

    public Node getLeft() {
        return left;
    }

    public Node setLeft(Node left) {
        this.left = left;
        return this;
    }

    public Node getRight() {
        return right;
    }

    public Node setRight(Node right) {
        this.right = right;
        return this;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

}