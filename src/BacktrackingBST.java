

import java.sql.SQLOutput;
import java.util.NoSuchElementException;

public class BacktrackingBST implements Backtrack, ADTSet<BacktrackingBST.Node> {
    private Stack stack;
    private Stack redoStack;
    private BacktrackingBST.Node root = null;
    private boolean backtracking;

    // Do not change the constructor's signature
    public BacktrackingBST(Stack stack, Stack redoStack) {
        this.stack = stack;
        this.redoStack = redoStack;
        backtracking = false;
    }

    public Node getRoot() {
        if (root == null) {
            throw new NoSuchElementException("empty tree has no root");
        }
        return root;
    }

    public Node search(int k) {
        Node treeNode = root;
        while (treeNode != null) {
            int key = treeNode.getKey();
            if (key == k)
                return treeNode;
            else if (key < k) {
                if (treeNode.right == null)
                    return null;
                treeNode = treeNode.right;
            } else {
                if (treeNode.left == null)
                    return null;
                treeNode = treeNode.left;
            }
        }
        return null;
    }

    public void insert(Node node) {
        argCheck(node);
        System.out.println("called to insert " + node.getKey());
        if (search(node.key) != null)
            return;
        Node y = null;
        Node x = root;
        while (x != null) {
            y = x;
            if (node.key < x.key)
                x = x.left;
            else
                x = x.right;
        }
        node.parent = y;
        if (y == null)
            root = node;
        else if (node.key < y.key)
            y.left = node;
        else
            y.right = node;
        if (!backtracking) {
            stack.push(new Node(node));
            System.out.println("pushing " + node.getKey());
        }
    }

    public void delete(Node node) {
        argCheck(node);
        if (node == root) {
            deleteRoot();
            return;
        }
        boolean selfIsALeftChild = node.parent.left == node;
        if (node.left == null && node.right == null) { // node is a leaf
            if (selfIsALeftChild)
                node.parent.left = null;
            else {
                node.parent.right = null;
            }
        } else if (node.left == null) { // node has only right child
            node.right.parent = node.parent;
            if (selfIsALeftChild) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
        } else if (node.right == null) { // node has only left child
            node.left.parent = node.parent;
            if (selfIsALeftChild) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }
        } else { // node has two children
            Node y = successor(node);
            node.key = y.key;
            node.value = y.value;
            delete(y);
        }
    }

    private void deleteRoot() {
        if (root.left == null && root.right == null) // root is a leaf
            root = null;
        else if (root.left == null) {    // root has only right child
            root = root.right;
            root.parent = null;
        } else if (root.right == null) {  // root has only left child
            root = root.left;
            root.parent = null;
        } else {                          // root has 2 children
            Node y = successor(root);
            root.key = y.key;
            root.value = y.value;
            delete(y);
        }
    }

    public Node minimum() {
        return findMin(root);
    }

    public Node maximum() {
        return findMax(root);
    }

    public Node successor(Node node) {
        if (node.right != null) {
            return findMin(node.right);
        }
        Node y = node.parent;
        while (y != null && node == y.right) {
            node = y;
            y = y.parent;
        }
        return y;
    }

    public Node predecessor(Node node) {
        if (node.left != null)
            return findMax(node.left);
        Node y = node.parent;
        while (y != null && node == y.left) {
            node = y;
            y = y.parent;
        }
        return y;
    }

    @Override
    public void backtrack() {
        System.out.println("called to backtrack");
        backtracking = true;
        if (!stack.isEmpty()) {
            Node node = (Node) stack.pop();
//            System.out.println("popping " + node.getKey());
            Node currentNode = search(node.getKey());
            if (currentNode != null) {
                // backtracking insertion
                // node is always inserted as a leaf, so deletion is direct.
//                System.out.println("backtracking insertion, deleting " + node.getKey());
                delete(currentNode);
            } else {
                // backtracking deletion. node = deleted node.
                if (node.parent==null){
                    backtrackRootDeletion(node);
                    return;
                }
                boolean selfWasALeftChild = node.parent.left == node;
                if (node.left==null && node.right == null){ // node was a leaf
                    insert(node);
                }
                else if (node.left == null){                // node had only right child
                    node.right.parent = node;
                    if (selfWasALeftChild){
                        node.parent.left = node;
                    }
                    else{
                        node.parent.right = node;
                    }
                }
                else if (node.right == null){               // node had only left child
                    node.left.parent = node;
                    if (selfWasALeftChild){
                        node.parent.left = node;
                    }
                    else{
                        node.parent.right = node;
                    }
                }
                else{                                       // node had two children

                }
                if (node.getKey() < node.parent.getKey()) {
                    node.parent.left = node;
                } else {
                    node.parent.right = node;
                }
                if (node.left != null)
                    node.left.parent = node;
                if (node.right != null)
                    node.right.parent = node;
            }
            backtracking = false;
        }
    }

    public void backtrackRootDeletion(Node previousRoot){
        if (previousRoot.left == null && previousRoot.right == null){       // root was a leaf
            insert(previousRoot);
        }
        else if (previousRoot.left == null || previousRoot.right == null){  // root had only right child or left child
            root.parent=previousRoot;
            root = previousRoot;
        }
        else {                                                              // root had two children

        }
    }

    @Override
    public void retrack() {
        // TODO: implement your code here
    }

    public String toString() {
        return toString2(root, "", 0);
    }

    private String toString2(Node node, String acc, int depth) {
        if (node == null)
            return "Tree is empty";
        if (node.left != null) {
            acc = toString2(node.left, acc, depth + 1);
        }
        acc = acc + twodSpaces(depth) + node.key + "\n";
        if (node.right != null)
            acc = toString2(node.right, acc, depth + 1);
        return acc;
    }

    private String twodSpaces(int d) {
        String spaces = "";
        for (int i = 0; i < 2 * d; i = i + 1)
            spaces = spaces + " ";
        return spaces;
    }

    public void printPreOrder() {
        String output = "";
        Stack printStack = new Stack();
        printStack.push(root);
        while (!printStack.isEmpty()) {
            Node treeNode = (Node) printStack.pop();
            while (treeNode != null) {
                output += treeNode.key + " ";
                if (treeNode.right != null) {
                    printStack.push(treeNode.right);
                }
                treeNode = treeNode.left;
            }
        }
        System.out.println(output.substring(0, output.length() - 1));
    }

    @Override
    public void print() {
        printPreOrder();
    }

    public Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public Node findMax(Node node) {
        while (node.right != null)
            node = node.right;
        return node;
    }

    public void argCheck(Object obj) {
        if (obj == null)
            throw new IllegalArgumentException("unable to perform operation on null object");
    }

    public static class Node {
        // These fields are public for grading purposes. By coding conventions and best practice they should be private.
        public BacktrackingBST.Node left;
        public BacktrackingBST.Node right;

        private BacktrackingBST.Node parent;
        private int key;
        private Object value;

        public Node(int key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Node(Node node) {
            this.key = node.getKey();
            this.value = node.getValue();
            this.left = node.left;
            this.right = node.right;
            this.parent = node.parent;
        }

        public int getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

    }

    public static void main(String[] args) {
        BacktrackingBST tree = new BacktrackingBST(new Stack(), new Stack());
        tree.insert(new Node(8, "8"));
        tree.insert(new Node(7, "7"));
        tree.insert(new Node(2, "2"));
        tree.insert(new Node(6, "6"));
        tree.insert(new Node(1, "1"));
        tree.insert(new Node(4, "4"));
        tree.insert(new Node(5, "5"));
        System.out.println(tree);
        tree.delete(tree.search(2));
        System.out.println(tree);
        tree.delete(tree.search(5));
        System.out.println(tree);
        tree.delete(tree.search(4));
        System.out.println(tree);
        tree.delete(tree.search(1));
        System.out.println(tree);
        tree.insert(new Node(1, "1"));
        System.out.println(tree);
        tree.delete(tree.search(1));
        System.out.println(tree);
    }
}
