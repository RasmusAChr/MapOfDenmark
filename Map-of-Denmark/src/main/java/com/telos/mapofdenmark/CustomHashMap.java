package com.telos.mapofdenmark;

import java.io.Serializable;

public class CustomHashMap<K, V> implements Serializable {
    private static final int INITIAL_CAPACITY = 1 << 16;
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private Entry[] hashtable;

    public CustomHashMap(){
        hashtable = new Entry[INITIAL_CAPACITY];
    }

    public CustomHashMap(Integer capacity){
        int cap = tableSizeFor(capacity);
        hashtable = new Entry[cap];
    }

    private int tableSizeFor(Integer cap){
        int n = -1 >>> Integer.numberOfLeadingZeros(cap-1);
        return n < 0 ? 1 : (n >= MAXIMUM_CAPACITY ? MAXIMUM_CAPACITY: n + 1);
    }

    class Entry<K, V>{
        public K key;
        public V value;
        public Entry next;

        Entry(K key, V value){
            this.key = key;
            this.value = value;
        }
    }
    final int hash(Object key){
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public void put(K key, V value){
        int hashCode = hash(key) & (hashtable.length - 1);
        Entry node = hashtable[hashCode];
        if(node == null){
            Entry newNode = new Entry(key, value);
            hashtable[hashCode] = newNode;
        }else{
            Entry prevNode = node;
            while (node != null){
                if(node.key == key){
                    node.value = value;
                    return;
                }
                prevNode = node;
                node = node.next;
            }
            Entry newNode = new Entry(key, value);
            prevNode.next = newNode;
        }
    }

    public V get(K key){
        int hashCode = hash(key) & (hashtable.length - 1);
        Entry node = hashtable[hashCode];
        while(node != null){
            if(node.key.equals(key)){
                return (V) node.value;
            }
            node = node.next;
        }
        return null;
    }
    public boolean containsKey(K key){
        return this.get(key) != null;
    }
}