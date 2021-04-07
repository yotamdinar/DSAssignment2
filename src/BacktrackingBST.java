

import java.util.NoSuchElementException;

public class BacktrackingBST implements Backtrack, ADTSet<BacktrackingBST.Node> {
    private Stack stack;
    private Stack redoStack;
    private BacktrackingBST.Node root = null;

    // Do not change the constructor's signature
    public BacktrackingBST(Stack stack, Stack redoStack) {
        this.stack = stack;
        this.redoStack = redoStack;
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
            if (treeNode.getKey() == k)
                return treeNode;
            else if (treeNode.getKey() < k) {
                if (treeNode.right != null)
                    treeNode = treeNode.right;
                else
                    return null;
            } else {
                if (treeNode.left != null)
                    treeNode = treeNode.left;
                else
                    return null;
            }
        }
        return null;
    }

    public void insert(Node node) {
        argCheck(node);
        if (search(node.key) == null)
            return;
        stack.push(node);
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
    }

    public void delete(Node node) {
        argCheck(node);
        stack.push(node);
        boolean selfIsLeftChild = (node.parent.left != null && node.parent.left == node);
        if (node.left == null & node.right == null) { // node is a leaf
            setParent(node, selfIsLeftChild);
        } else if (node.left == null) { // node has only right child
            node.right.parent = node.parent;
            setParent(node, selfIsLeftChild);
        } else if (node.right == null) { // node has only left child
            node.left.parent = node.parent;
            setParent(node, selfIsLeftChild);
        } else { // node has two children
            Node y = successor(node);
            int key = y.key;
            delete(y);
            if (selfIsLeftChild)
                node.parent.left.key = key;
            else
                node.parent.right.key = key;
        }

    }

    private void setParent(Node node, boolean condition) {
        if (condition)
            node.parent.left = node.left;
        else
            node.parent.right = node.right;
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
        if (!stack.isEmpty()){
            Node node = (Node)stack.pop();
            if (search(node.getKey())!=null) {
                redoStack.push(node);
                delete(node);
            }
            else{
            }
        }
    }

    @Override
    public void retrack() {
        // TODO: implement your code here
    }

    public void printPreOrder() {
        String output = "";
        stack.push(root);
        while (!stack.isEmpty()) {
            Node treeNode = (Node) stack.pop();
            while (treeNode != null) {
                output += treeNode.key + " ";
                if (treeNode.right != null) {
                    stack.push(treeNode.right);
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

        public int getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

    }

    public static void main(String[] args) {
        BacktrackingBST tree = new BacktrackingBST(new Stack(), new Stack());
        tree.insert(new Node(5, "abc"));
        tree.insert(new Node(4, "abc"));
        tree.insert(new Node(2, "abc"));
        tree.insert(new Node(3, "abc"));
        tree.insert(new Node(1, "abc"));
        tree.insert(new Node(6, "abc"));
        tree.insert(new Node(8, "abc"));
        tree.insert(new Node(7, "abc"));
        tree.delete(tree.search(3));
        tree.print();
    }

}
