import java.util.*;

class TNode {
    boolean isEow; // Marks end of word
    int value; // Stores associated value
    TNode[] children;
    static final int ALPHA_SIZE = 26;

    public TNode() {
        isEow = false;
        value = 0; // Default value
        children = new TNode[ALPHA_SIZE];
    }
}

class TrieMap {
    TNode root;

    public TrieMap() {
        root = new TNode();
    }

    // Insert key-value pair into Trie
    void insert(String key, int value) {
        TNode temp = root;
        for (char ch : key.toCharArray()) {
            int idx = ch - 'a';
            if (temp.children[idx] == null) {
                temp.children[idx] = new TNode();
            }
            temp = temp.children[idx];
        }
        temp.isEow = true;
        temp.value = value; // Store the value at the end of the word
    }

    // Get value associated with key
    Integer get(String key) {
        TNode temp = root;
        for (char ch : key.toCharArray()) {
            int idx = ch - 'a';
            if (temp.children[idx] == null) return null;
            temp = temp.children[idx];
        }
        return temp.isEow ? temp.value : null;
    }

    // Check if a key exists
    boolean containsKey(String key) {
        return get(key) != null;
    }

    // Retrieve all key-value pairs
    Map<String, Integer> getAllKeyValues() {
        Map<String, Integer> result = new HashMap<>();
        getAllKeyValuesHelper(root, new StringBuilder(), result);
        return result;
    }

    private void getAllKeyValuesHelper(TNode node, StringBuilder path, Map<String, Integer> result) {
        if (node.isEow) {
            result.put(path.toString(), node.value);
        }
        for (int i = 0; i < TNode.ALPHA_SIZE; i++) {
            if (node.children[i] != null) {
                path.append((char) (i + 'a'));
                getAllKeyValuesHelper(node.children[i], path, result);
                path.setLength(path.length() - 1);
            }
        }
    }
}

// Main class to test Trie as a Map
public class TrieAsMap {
    public static void main(String[] args) {
        TrieMap trie = new TrieMap();
        trie.insert("apple", 100);
        trie.insert("banana", 200);
        trie.insert("orange", 300);

        // Retrieve values
        System.out.println("apple: " + trie.get("apple")); // 100
        System.out.println("banana: " + trie.get("banana")); // 200
        System.out.println("grape: " + trie.get("grape")); // null

        // Check key existence
        System.out.println("Contains orange? " + trie.containsKey("orange")); // true
        System.out.println("Contains grape? " + trie.containsKey("grape")); // false

        // Retrieve all key-value pairs
        System.out.println("\nAll key-value pairs:");
        for (Map.Entry<String, Integer> entry : trie.getAllKeyValues().entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
