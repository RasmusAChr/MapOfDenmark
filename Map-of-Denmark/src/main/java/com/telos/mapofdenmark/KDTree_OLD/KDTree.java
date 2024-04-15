package com.telos.mapofdenmark.KDTree_OLD;

//Code taken from 398-399 in Algorithms FOURTH EDITION by Sedgewick and Wayne
//Modeled after a BST (Binary Search Tree)
public class KDTree<Value> {
    private static final int K = 2; // Number of dimensions, set to 2 for 2D space

    private class KDNode {
        private double[] coordinates; // Represents the point
        private Value val;
        private KDNode left, right;

        public KDNode(double[] coordinates, Value val) {
            this.coordinates = coordinates;
            this.val = val;
        }

        // Method to get coordinate for a given dimension
        public double getCoordinate(int dimension) {
            return coordinates[dimension];
        }
    }

    private KDNode root; // Root of KD Tree

    public void put(double[] coordinates, Value val) {
        root = put(root, coordinates, val, 0);
    }

    private KDNode put(KDNode node, double[] coordinates, Value val, int depth) {
        if (node == null) {
            return new KDNode(coordinates, val);
        }

        int dim = depth % K; // Determine current dimension (x=0, y=1)
        if (coordinates[dim] < node.getCoordinate(dim)) {
            node.left = put(node.left, coordinates, val, depth + 1);
        } else {
            node.right = put(node.right, coordinates, val, depth + 1);
        }
        return node;
    }
    public Value get(double[] coordinates) {
        return get(root, coordinates, 0);
    }

    private Value get(KDNode node, double[] coordinates, int depth) {
        if (node == null) {
            return null;
        }

        // Check if we have found the target point
        if (isEqual(node.coordinates, coordinates)) {
            return node.val;
        }

        int dim = depth % K; // Determine current dimension for comparison
        if (coordinates[dim] < node.getCoordinate(dim)) {
            return get(node.left, coordinates, depth + 1);
        } else {
            return get(node.right, coordinates, depth + 1);
        }
    }
    // Helper method to check if two points are equal
    private boolean isEqual(double[] coords1, double[] coords2) {
        for (int i = 0; i < K; i++) {
            if (coords1[i] != coords2[i]) {
                return false;
            }
        }
        return true;
    }
    private int compareByDimension(KDNode a, KDNode b, int depth) {
        int dimension = depth % K; // K is the number of dimensions
        return Double.compare(a.coordinates[dimension], b.coordinates[dimension]);
    }
    /*
    Range Search: Implementing a range search involves traversing the tree and collecting nodes that
    fall within a specified range for each dimension.
    This can get complex as you consider how ranges overlap with the divisions made in the tree.
     */
}
