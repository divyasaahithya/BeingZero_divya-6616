import java.util.*;
// Node class for Circular Doubly Linked List (CDLL)
class CDLLNode {
    int key, val; // Key-value pair for LRU cache
    CDLLNode prev, next; // Pointers to previous and next nodes

    // Constructor to initialize node
    public CDLLNode(int k, int v) {
        this.key = k;
        this.val = v;
        this.prev = this.next = null; // Initially, node is isolated
    }
}

// Circular Doubly Linked List (CDLL) class to manage LRU cache order
class CDLL {
    CDLLNode head; // Head of the list

    public CDLL() {
        head = null;
    }

    // Inserts a new node at the beginning of the circular doubly linked list
    CDLLNode insAtBegin(int k, int v) {
        CDLLNode nn = new CDLLNode(k, v);
        nn.next = nn; // Self-loop (only one node)
        nn.prev = nn;
        
        if (head == null) { // If list is empty, new node becomes head
            head = nn;
            return nn;
        }

        // If list is not empty, insert at the beginning
        CDLLNode last = head.prev; // Get last node
        nn.next = head; // New node's next is the current head
        head.prev = nn; // Head's previous becomes new node
        last.next = nn; // Last node's next becomes new node
        nn.prev = last; // New node's previous becomes last node
        head = nn; // Update head to the new node
        return nn;
    }

    // Prints the keys in the circular doubly linked list
    void printLL() {
        if (head == null)
            return;

        System.out.println(head.key); // Print head key
        CDLLNode curr = head.next;

        while (curr != head) { // Traverse through the list
            System.out.print(curr.key + " ");
            curr = curr.next;
        }
        System.out.println();
    }

    // Deletes the least recently used (last) node and returns its key
    int delLast() {
        if (head == null)
            return -1;

        CDLLNode last = head.prev; // Get last node
        if (last == head) { // Only one node in the list
            head = null;
            return last.key;
        }

        last.prev.next = head; // Update second last node's next to head
        head.prev = last.prev; // Update head's previous to second last node
        return last.key; // Return deleted key
    }

    // Moves a specific node to the front (most recently used)
    void moveToFront(CDLLNode nodeToMove) {
        if (nodeToMove == head) return; // If already at front, do nothing

        // Remove node from its current position
        nodeToMove.prev.next = nodeToMove.next;
        nodeToMove.next.prev = nodeToMove.prev;

        // Insert node at the front
        CDLLNode last = head.prev;
        nodeToMove.next = head;
        head.prev = nodeToMove;
        last.next = nodeToMove;
        nodeToMove.prev = last;
        head = nodeToMove; // Update head to new front
    }
}

// LRU Cache class using HashMap and CDLL
class LRUCache {
    CDLL ll; // Circular doubly linked list to store cache order
    int capacity, size; // Maximum capacity and current size of cache
    Map<Integer, CDLLNode> mp; // HashMap for quick access to cache items

    // Constructor to initialize LRU Cache with given capacity
    public LRUCache(int cap) {
        ll = new CDLL();
        this.capacity = cap;
        this.size = 0;
        mp = new HashMap<>();
    }

    // Retrieves the value for a given key if present, else returns -1
    public int get(int k) {
        if (mp.containsKey(k)) { // If key exists
            CDLLNode node = mp.get(k);
            ll.moveToFront(node); // Move node to front as it is recently used
            return node.val; // Return the value
        }
        return -1; // Key not found
    }

    // Inserts or updates a key-value pair in the cache
    public void put(int k, int v) {
        if (mp.containsKey(k)) { // If key exists, update value
            CDLLNode node = mp.get(k);
            node.val = v;
            ll.moveToFront(node); // Move node to front as it is recently used
        } else { // If key doesn't exist, insert new key-value pair
            if (size < capacity) { // If cache is not full
                CDLLNode nn = ll.insAtBegin(k, v); // Insert at beginning
                mp.put(k, nn); // Store reference in HashMap
                size++;
            } else { // If cache is full, remove least recently used (LRU) node
                int delKey = ll.delLast(); // Remove LRU node from list
                mp.remove(delKey); // Remove from HashMap
                CDLLNode nn = ll.insAtBegin(k, v); // Insert new node
                mp.put(k, nn); // Store reference in HashMap
            }
        }
    }
}
 public class leetLRU {
    public static void main(String[] args) {
        // Create a new circular doubly linked list (CDLL)
        CDLL list1 = new CDLL();

        // Insert nodes at the beginning of the list
        list1.insAtBegin(1, 10);
        list1.insAtBegin(2, 20);
        list1.insAtBegin(3, 30);

        // Print the current state of the circular doubly linked list
        System.out.print("List after insertion: ");
        list1.printLL();

        // Delete the least recently used (last) node
        int deletedKey = list1.delLast();
        System.out.println("Deleted LRU node key: " + deletedKey);

        // Print the list after deletion
        System.out.print("List after deletion: ");
        list1.printLL();

        // Move a node to the front (simulate recent usage)
        System.out.println("Moving node with key 2 to front");
        CDLLNode nodeToMove = list1.head.next; // Move second node to front
        list1.moveToFront(nodeToMove);

        // Print the list after moving a node to the front
        System.out.print("List after moving key 2 to front: ");
        list1.printLL();
    }
}

