package datastructures.avl;

public class AVLNode<T> {
    public int key;
    public T data;
    public AVLNode<T> left, right;
    public int height;  
    
    
    public AVLNode(int k, T val) {
        key = k;
        data = val;
        left = right = null;
        height = 1;  
    }
    
    
    public AVLNode(int k, T val, AVLNode<T> l, AVLNode<T> r) {
        key = k;
        data = val;
        left = l;
        right = r;
        height = 1;  
    }
}
