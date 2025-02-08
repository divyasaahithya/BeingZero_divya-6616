import java.util.*;

// Doubly Linked List Node
class DLLNode {
    int key, value;
    DLLNode prev, next;

    public DLLNode(int key, int value) {
        this.key = key;
        this.value = value;
        this.prev = this.next = null;
    }
}

// LRU Cache Implementation
class LRUCache {
    private final int capacity;
    private final Map<Integer, DLLNode> cacheMap;
    private final DLLNode head, tail; // Dummy head & tail for easy manipulation

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cacheMap = new HashMap<>();
        
        head = new DLLNode(-1, -1); // Dummy head
        tail = new DLLNode(-1, -1); // Dummy tail
        head.next = tail;
        tail.prev = head;
    }

    // Move node to the front (most recently used)
    private void moveToFront(DLLNode node) {
        removeNode(node);
        addToFront(node);
    }

    // Add node to the front of the list
    private void addToFront(DLLNode node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    // Remove a node from the list
    private void removeNode(DLLNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // Remove and return the least recently used node
    private DLLNode removeLRU() {
        DLLNode lruNode = tail.prev;
        removeNode(lruNode);
        return lruNode;
    }

    // Get value for a key, move the accessed node to front
    public int get(int key) {
        if (!cacheMap.containsKey(key)) return -1;

        DLLNode node = cacheMap.get(key);
        moveToFront(node);
        return node.value;
    }

    // Insert or update a key-value pair in the cache
    public void put(int key, int value) {
        if (cacheMap.containsKey(key)) {
            DLLNode node = cacheMap.get(key);
            node.value = value; // Update value
            moveToFront(node);
        } else {
            if (cacheMap.size() >= capacity) {
                DLLNode lruNode = removeLRU();
                cacheMap.remove(lruNode.key);
            }
            DLLNode newNode = new DLLNode(key, value);
            addToFront(newNode);
            cacheMap.put(key, newNode);
        }
    }
}

// Main class for testing
public class Lru {
    public static void main(String[] args) {
        LRUCache cache = new LRUCache(3); // LRU Cache with capacity 3

        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);

        System.out.println(cache.get(1)); // Output: 10 (1 is recently used)
        
        cache.put(4, 40); // Evicts key 2 (Least Recently Used)
        System.out.println(cache.get(2)); // Output: -1 (2 is evicted)
        System.out.println(cache.get(3)); // Output: 30 (3 is still in cache)

        cache.put(5, 50); // Evicts key 1
        System.out.println(cache.get(1)); // Output: -1 (1 is evicted)
        System.out.println(cache.get(4)); // Output: 40 (Still in cache)
    }
}
