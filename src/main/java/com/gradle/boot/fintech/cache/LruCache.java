package com.gradle.boot.fintech.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Getter
@Setter
public class LruCache<T> {

    @Value("${cache.course.size}")
    private int capacity;
    private int size;
    private final Map<String, Node> cache;
    private final DoublyLinkedList queue;
    private final ReentrantLock lock;

    public LruCache() {
        lock = new ReentrantLock();
        cache = new ConcurrentHashMap<>();
        queue = new DoublyLinkedList();
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public T get(final String key) {
        lock.lock();
        try {
            Node node = cache.get(key);
            if (node == null) {
                return null;
            }
            queue.modeNodeToFirst(node);
            return node.value;
        } finally {
            lock.unlock();
        }
    }

    public void put(String key, T value) {
        lock.lock();
        try {
            Node currentNode = cache.get(key);
            if (currentNode != null) {
                currentNode.value = value;
                queue.modeNodeToFirst(currentNode);
                return;
            }

            if (size == capacity) {
                String rearNodeKey = queue.getLastKey();
                queue.removeNodeFromLast();
                cache.remove(rearNodeKey);
                size--;
            }

            Node node = new Node(key, value);
            queue.addNodeToFirst(node);
            cache.put(key, node);
            size++;
        } finally {
            lock.unlock();
        }
    }

    public class Node {
        String key;
        T value;
        Node next, prev;

        public Node(String key, T value) {
            this.key = key;
            this.value = value;
        }
    }

    @Getter
    @Setter
    public class DoublyLinkedList {
        private Node first, last;

        private void addNodeToFirst(Node node) {
            if (last == null) {
                first = last = node;
                return;
            }
            node.next = first;
            first.prev = node;
            first = node;
        }

        public void modeNodeToFirst(Node node) {
            if (first == node) return;

            if (node == last) {
                last = last.prev;
                last.next = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }

            node.prev = null;
            node.next = first;
            first.prev = node;
            first = node;
        }

        private void removeNodeFromLast() {
            if (last == null) return;

            if (first == last) {
                first = last = null;
            } else {
                last = last.prev;
                last.next = null;
            }
        }

        private String getLastKey() {
            return last.key;
        }
    }
}
