package tracker.controllers;

import tracker.model.*;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    public final MyLinkedList historyList;

    public InMemoryHistoryManager() {
        historyList = new MyLinkedList();
    }


    @Override
    public void add(Task task) {
        historyList.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    @Override
    public void remove(Task task) {
        historyList.removeNode(task);
    }

    @Override
    public String getNodeId() {
        return historyList.getNodeId();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return Objects.equals(historyList, that.historyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyList);
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "historyList=" + historyList +
                '}';
    }

    static class MyLinkedList {

        private final Map<Integer, Node> nodeMap = new HashMap<>();

        private static class Node {
            private final Task value;
            private Node next;
            private Node prev;

            public Node(Task value) {
                this.value = value;
            }
        }

        private Node head;
        private Node tail;

        void linkLast(Task task) {
            Node node = new Node(task);

            if (head == null) {
                this.head = node;
            }

            if (tail != null) {
                tail.next = node;
                node.prev = tail;
            }

            tail = node;

            Node oldValue = nodeMap.put(task.getId(), node);

            if (oldValue != null) {
                Node prev = oldValue.prev;
                Node next = oldValue.next;
                if (prev == null) {
                    head = next;
                    if (next != null) {
                        next.prev = null;
                    }
                } else {
                    prev.next = next;

                    if (next != null) {
                        next.prev = prev;
                    }
                }
            }
        }

        void removeNode(Task task) {
            Node node = nodeMap.remove(task.getId());

            if (node == null) {
                return;
            }

            Node prev = node.prev;
            Node next = node.next;
            if (prev == null) {
                head = next;
                if (next != null) {
                    next.prev = null;
                }
            } else {
                prev.next = next;

                if (next != null) {
                    next.prev = prev;
                }
            }

        }

        String getNodeId() {
            List<Integer> nodeId = new ArrayList<>();
            nodeId.addAll(nodeMap.keySet());
            String historyId = nodeId.toString();
            return historyId;
        }

        List<Task> getTasks() {
            List<Task> objects = new ArrayList<>();

            Node cur = head;

            while (cur != null) {
                objects.add(cur.value);

                cur = cur.next;
            }

            return objects;
        }

    }

}