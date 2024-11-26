package p4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ChromaticTree implements Iterable<String> {

    private int height = 0;

    @Override
    public Iterator<String> iterator() {
        return new ChromaticTreeIterator(root);
    }

    private static class ChromaticTreeIterator implements Iterator<String> {
    private final Stack<Node> stack = new Stack<>();

    public ChromaticTreeIterator(Node root) {
        pushLeft(root);
    }

    private void pushLeft(Node node) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public String next() {
        Node node = stack.pop();
        if (node.right != null) {
            pushLeft(node.right);
        }
        return "<"+node.name+", "+node.color+">";  // Append a space after the node name
    }
}


    private final Node root;

    public ChromaticTree(String inputFile) throws FileNotFoundException {
        Scanner scnr = new Scanner(new File(inputFile));

        String line = scnr.nextLine();
        String[] tokens = line.split(" ");
        String city = tokens[0];
        String color = tokens[1];
        root = new Node(city, color);

        while (scnr.hasNext()) {
            line = scnr.nextLine();
            tokens = line.split(" ");
            city = tokens[0];
            color = tokens[1];
            String path = tokens[2];

            Node temp = new Node(city, color);
            Node current = root;

            char[] splitPath = path.toCharArray();

            if (height < splitPath.length) {
                height = splitPath.length;
            }

            for (int i = 0; i < splitPath.length; i++) {
                if (i == splitPath.length - 1) {
                    if (splitPath[i] == 'L') {
                        current.left = temp;
                    } else {
                        current.right = temp;
                    }
                    temp.parent = current;
                } else {
                    if (splitPath[i] == 'L') {
                        current = current.left;
                    } else {
                        current = current.right;
                    }
                }
            }
        }
        scnr.close();
    }

    public int computeHeight() {
        return height;
    }

    public int countTriChromaticGroups() {
        return isTrichromatic(root);
    }

    public String getReverseElevatorOrder() {
        return inOrderReverse(root);
    }

    public String printPathsBackToTheRoot() {
        return printToRoot(root);
    }

    public String findFirstCommonCity(String cityA, String cityB) {
        if (PrintPath(cityA) == null || PrintPath(cityB) == null) {
            return null;
        }

        String[] pathA = Objects.requireNonNull(PrintPath(cityA)).split(" ");
        String[] pathB = Objects.requireNonNull(PrintPath(cityB)).split(" ");
        String currentA;
        String currentB;
        for (String s : pathA) {
            currentA = s;
            for (String string : pathB) {
                currentB = string;
                if (currentA.equals(currentB)) {
                    return currentA;
                }
            }
        }

        return null;
    }

    private String printToRoot(Node node) {
        StringBuilder rightToLeftTree = new StringBuilder();
        ArrayList<Node> queue = new ArrayList<>();

        rightToLeftTree.append(node.name).append("\n");
        queue.add(node.left);
        queue.add(node.right);

        Node current;

        while (!queue.isEmpty()) {
            current = queue.get(0);

            if (current.left != null) {
                queue.add(current.left);
            }

            if (current.right != null) {
                queue.add(current.right);
            }

            queue.remove(0);
            while (!current.equals(root)) {

                rightToLeftTree.append(current.name).append(" ");
                current = current.parent;

            }
            rightToLeftTree.append(node.name).append("\n");
        }

        return rightToLeftTree.toString();
    }

    private String inOrderReverse(Node node) {
        StringBuilder rightToLeftTree = new StringBuilder();
        ArrayList<Node> queue = new ArrayList<>();

        rightToLeftTree.append(node.name).append(" ");
        queue.add(node.right);
        queue.add(node.left);

        while (!queue.isEmpty()) {
            node = queue.get(0);

            if (node.right != null) {
                queue.add(node.right);
            }
            if (node.left != null) {
                queue.add(node.left);
            }
            queue.remove(0);


            rightToLeftTree.append(node.name).append(" ");

        }

        return rightToLeftTree.toString();
    }

    private int isTrichromatic(Node node) {
        int totalTrichromatics = 0;

        if (node.left != null) {
            totalTrichromatics += isTrichromatic(node.left);
        }

        // CHECK HERE
        boolean matchFound = false;
        if (node.left != null && node.right != null) {
            //compare parent to both
            if (Objects.equals(node.color, node.left.color) || Objects.equals(node.color, node.right.color)) {
                matchFound = true;
            }
            //compare left to both
            if (Objects.equals(node.right.color, node.left.color)) {
                matchFound = true;
            }
            if (!matchFound) {
                totalTrichromatics += 1;

            }
        }

        if (node.right != null) {
            totalTrichromatics += isTrichromatic(node.right);
        }


        return totalTrichromatics;
    }

    private String PrintPath(String city) {

        // 1. Check if the city exists
        //  if NOT, return NULL            // else, RETURN the PATH
        Node node = root;

        StringBuilder path = new StringBuilder();
        ArrayList<Node> queue = new ArrayList<>();

        queue.add(node.left);
        queue.add(node.right);

        Node current = null;
        boolean found = false;
        while (!queue.isEmpty()) {
            current = queue.get(0);

            if (Objects.equals(current.name, city)) {
                found = true;
                break;
            }

            if (current.left != null) {
                queue.add(current.left);
            }

            if (current.right != null) {
                queue.add(current.right);
            }

            queue.remove(0);

        }


        if (!found) {
            return null;
        } else {

            while (!current.equals(root)) {

                path.append(current.name).append(" ");
                current = current.parent;

            }
            path.append(root.name);
            return path.toString();
        }

    }

    public static class Node {

        private final String name;
        private final String color;
        protected Node left;
        protected Node right;
        protected Node parent;

        public Node(String name, String color) {
            this.name = name;
            this.color = color;
        }

    }

}
