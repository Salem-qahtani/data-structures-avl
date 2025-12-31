package datastructures.avl;

public class AVL<T> {

    AVLNode<T> root, current;

    public AVL() {
        root = current = null;
    }

    public boolean empty() {
        return root == null;
    }

    public boolean full() {
        return false;
    }

    public T retrieve() {
        return current.data;
    }

    // Updates the value for the current node by removing its key then reinserting the new (key, data).
    // Assumes current is valid and represents the entry to be updated.
    public boolean update(int key, T data) {
        remove_key(current.key);
        return insert(key, data);
    }

    // Detaches the subtree rooted at "current" from the tree.
    public void deleteSubtree() {
        if(current == root) {
            current = root = null;
        }
        else {
            AVLNode<T> p = current;
            find(Relative.Parent);
            if(current.left == p)
                current.left = null;
            else
                current.right = null;
            current = root;
        }
    }

    // Navigational helper for moving the "current" pointer around the tree.
    public boolean find(Relative rel) {
        switch (rel) {
            case Root:
                current = root;
                return true;
            case Parent:
                if(current == root)
                    return false;
                current = findparent(current, root);
                return true;
            case LeftChild:
                if(current.left == null)
                    return false;
                current = current.left;
                return true;
            case RightChild:
                if(current.right == null)
                    return false;
                current = current.right;
                return true;
            default:
                return false;
        }
    }

    private AVLNode<T> findparent(AVLNode<T> p, AVLNode<T> t) {
        if(t == null)
            return null;
        if(t.right == null && t.left == null)
            return null;
        else if(t.right == p || t.left == p)
            return t;
        else {
            AVLNode<T> q = findparent(p, t.left);
            if (q != null)
                return q;
            else
                return findparent(p, t.right);
        }
    }

    private int getHeight(AVLNode<T> node) {
        if (node == null) return 0;
        return node.height;
    }

    private int getBalanceFactor(AVLNode<T> node) {
        if (node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    private void updateHeight(AVLNode<T> node) {
        if (node != null) {
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        }
    }

    // Performs a right rotation around y and returns the new subtree root.
    private AVLNode<T> rightRotate(AVLNode<T> y) {
        AVLNode<T> x = y.left;
        AVLNode<T> T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // Performs a left rotation around x and returns the new subtree root.
    private AVLNode<T> leftRotate(AVLNode<T> x) {
        AVLNode<T> y = x.right;
        AVLNode<T> T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // Searches for a key and updates "current":
    // - if found: current points to the matching node
    // - if not found: current points to the last visited node (insertion position parent)
    public boolean findkey(int tkey) {
        AVLNode<T> p = root, q = root;
        if(empty())
            return false;
        while(p != null) {
            q = p;
            if(p.key == tkey) {
                current = p;
                return true;
            }
            else if(tkey < p.key)
                p = p.left;
            else
                p = p.right;
        }
        current = q;
        return false;
    }

    public boolean insert(int k, T val) {
        if (findkey(k)) {
            return false;
        }

        root = insertRecursive(root, k, val);
        findkey(k);
        return true;
    }

    // Standard AVL insert with rebalancing on the way back up.
    private AVLNode<T> insertRecursive(AVLNode<T> node, int key, T value) {
        if (node == null) {
            return new AVLNode<>(key, value);
        }

        if (key < node.key) {
            node.left = insertRecursive(node.left, key, value);
        } else if (key > node.key) {
            node.right = insertRecursive(node.right, key, value);
        } else {
            node.data = value;
            return node;
        }

        updateHeight(node);
        int balance = getBalanceFactor(node);

        if (balance > 1 && key < node.left.key)
            return rightRotate(node);

        if (balance < -1 && key > node.right.key)
            return leftRotate(node);

        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public boolean remove_key(int tkey) {
        BoolFlag removed = new BoolFlag(false);
        root = removeRecursive(root, tkey, removed);
        current = root;
        return removed.getValue();
    }

    // Standard AVL delete with rebalancing; BoolFlag is used to bubble up "was removed" state.
    private AVLNode<T> removeRecursive(AVLNode<T> node, int key, BoolFlag flag) {
        if (node == null) {
            return null;
        }

        if (key < node.key) {
            node.left = removeRecursive(node.left, key, flag);
        } else if (key > node.key) {
            node.right = removeRecursive(node.right, key, flag);
        } else {
            flag.setValue(true);

            if (node.left == null || node.right == null) {
                AVLNode<T> temp = (node.left != null) ? node.left : node.right;

                if (temp == null) {
                    node = null;
                } else {
                    node = temp;
                }
            } else {
                AVLNode<T> temp = findMin(node.right);
                node.key = temp.key;
                node.data = temp.data;

                node.right = removeRecursive(node.right, temp.key, new BoolFlag(false));
            }
        }

        if (node == null) {
            return null;
        }

        updateHeight(node);
        int balance = getBalanceFactor(node);

        if (balance > 1 && getBalanceFactor(node.left) >= 0)
            return rightRotate(node);

        if (balance > 1 && getBalanceFactor(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && getBalanceFactor(node.right) <= 0)
            return leftRotate(node);

        if (balance < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    private AVLNode<T> findMin(AVLNode<T> p) {
        if (p == null) return null;
        while (p.left != null) {
            p = p.left;
        }
        return p;
    }

    public LinkedList<T> inOrderTraversal() {
        LinkedList<T> result = new LinkedList<>();
        inOrderRecursive(root, result);
        return result;
    }

    private void inOrderRecursive(AVLNode<T> node, LinkedList<T> result) {
        if (node == null) return;
        inOrderRecursive(node.left, result);
        result.insert(node.data);
        inOrderRecursive(node.right, result);
    }

    public T search(int key) {
        return findkey(key) ? current.data : null;
    }

    public boolean delete(int key) {
        return remove_key(key);
    }

    public int getSize() {
        return countNodesInSubtree(root);
    }

    private int countNodesInSubtree(AVLNode<T> node) {
        if (node == null) return 0;
        return 1 + countNodesInSubtree(node.left) + countNodesInSubtree(node.right);
    }

    public LinkedList<Integer> getKeysSorted() {
        LinkedList<Integer> result = new LinkedList<>();
        getKeysRecursive(root, result);
        return result;
    }

    private void getKeysRecursive(AVLNode<T> node, LinkedList<Integer> result) {
        if (node == null) return;
        getKeysRecursive(node.left, result);
        result.insert(node.key);
        getKeysRecursive(node.right, result);
    }

    // Returns all values with keys in [minKey, maxKey] (inclusive).
    public LinkedList<T> rangeQuery(int minKey, int maxKey) {
        LinkedList<T> result = new LinkedList<>();
        rangeQueryRecursive(root, minKey, maxKey, result);
        return result;
    }

    private void rangeQueryRecursive(AVLNode<T> node, int minKey, int maxKey, LinkedList<T> result) {
        if (node == null) return;

        if (node.key > minKey)
            rangeQueryRecursive(node.left, minKey, maxKey, result);

        if (node.key >= minKey && node.key <= maxKey)
            result.insert(node.data);

        if (node.key < maxKey)
            rangeQueryRecursive(node.right, minKey, maxKey, result);
    }

    public LinkedList<T> getAllValues() {
        return inOrderTraversal();
    }

    public T findMin() {
        if (root == null) return null;
        return findMin(root).data;
    }

    public T findMax() {
        if (root == null) return null;
        AVLNode<T> node = root;
        while (node.right != null) node = node.right;
        return node.data;
    }

    public String toString() {
        return "AVL[size=" + getSize() + ", root=" + (root != null ? root.key : "null") + "]";
    }

    private static class BoolFlag {
        private boolean value;
        public BoolFlag(boolean v) { value = v; }
        public boolean getValue() { return value; }
        public void setValue(boolean v) { value = v; }
    }
}
