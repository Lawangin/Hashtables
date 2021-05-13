package main;

import java.util.Iterator;

public class HashTableLinearProbing<K, V> extends HashTableOpenAddressing<K, V> {

    @Override
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Cannot put null arguments");
        }

        V oldValue = null;

        Entry<K, V> newEntry = new Entry<K, V>(key, value);
        int index = super.getHashIndex(key);

        index = linearProbe(index, key);

        if (table[index] == null || table[index].isRemoved()) {
            table[index] = newEntry;
            numEntries++;
        } else {
            oldValue = table[index].getValue();
            table[index] = newEntry;
        }

        if (isFull()) {

            enlargeHashTable();

        }

        return oldValue;
    }

    @Override
    public V remove(K key) {
        V removedValue = null;
        // calculate the initial index
        int index = getHashIndex(key);

        // find the location of the key using linear probing
        int location = linearProbe(index, key);

        // if Key found; flag entry as removed and return its value
        if (location >= 0 && location < table.length && contains(key)) {
            removedValue = table[location].getValue();
            table[location].setToRemoved();
            // decrement the number of elments
            numEntries--;

            return removedValue;
        }

        // else key not found; return null
        return null;
    }

    @Override
    public V getValue(K key) {
        int index = getHashIndex(key);

        index = linearProbe(index, key);

        Entry<K, V> item = table[index];

        if ((item != null) && item.isIn())
            return item.getValue();

        return null;
    }

    @Override
    public boolean contains(K key) {

        return getValue(key) != null;
    }

    public int linearProbe(int index, K keyIn) {

        boolean found = false;

        int removedStateIndex = -1; // Index of first removed location


        if (table[index] == null)    // The hash index is available

            return index;


        while (!found && table[index] != null) {

            if (table[index].isIn()) {

                if (keyIn.equals(table[index].getKey()))

                    found = true;       // Key found

                else                     // Follow probe sequence

                    index = (index + 1) % table.length;

            } else {                              // Skip entries that were removed

                // Save index of first location in removed state

                if (removedStateIndex == -1)

                    removedStateIndex = index;

                index = (index + 1) % table.length;

            }

        }


        if (found || (removedStateIndex == -1))

            return index;             // Index of either key or null

        else

            return removedStateIndex; // Index of an available location

    }

    public static void main(String[] args){
        HashTableLinearProbing<String, Integer> purchases = new HashTableLinearProbing<>();

        String[] names = {"Pax", "Eleven", "Angel", "Abigail", "Jack"};
        purchases.put(names[0], 654);
        purchases.put(names[1], 341);
        purchases.put(names[2], 70);
        purchases.put(names[3], 867);
        purchases.put(names[4], 5309);

        System.out.println("Contents with linear probing:\n" + purchases.toString());

        System.out.println("Replaced old value was " + purchases.put(names[1], 170));
        System.out.println("Contents after changing Eleven to 170:\n" + purchases.toString());

        System.out.println("Calling getValue() on Pax, Eleven, & Angel:");
        System.out.println("\tPax: " + purchases.getValue(names[0]));
        System.out.println("\tEleven: " + purchases.getValue(names[1]));
        System.out.println("\tAngel: " + purchases.getValue(names[2]));

        purchases.remove(names[0]);
        purchases.remove(names[2]);
        System.out.println("Contents after removing Pax & Angel:\n" + purchases);

        purchases.put("Gino", 348);
        System.out.println("Contents after adding Gino:\n" + purchases);

        Iterator<String> keyIter = purchases.getKeyIterator();
        Iterator<Integer> valueIter = purchases.getValueIterator();
        System.out.println("Contents of the hash table:");
        while(keyIter.hasNext())
            System.out.println("Key-" + keyIter.next() + " : Value-" + valueIter.next());
    }
}


