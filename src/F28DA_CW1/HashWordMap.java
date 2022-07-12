package F28DA_CW1;

import java.util.LinkedList;
import java.util.Iterator;

/**
 * @author Riffat Khan
 * 
 *	This class performs the implementation of the map in a hash table format.
 *	Each space in the hash table stores the word and its position as an entry
 *	with the help of the MapEntry class provided. The positions of each word is stored
 *	in a linked list format as well. The following methods perform the basic functionality
 *	of a hash table based map.
 */

public class HashWordMap implements IWordMap, IHashMonitor {

	// declaring the variables and the map
	private float maxLoadFactor;
	private int entry = 0;
	private int size = 13;
	private MapEntry hashTable[] = new MapEntry[size];
	private int numberOfProbes = 0;
	private int numberOfOperations = 0;

	// constructor with default load factor 
	public HashWordMap() {
		maxLoadFactor = 0.5f;
	}

	// constructor with specified load factor
	public HashWordMap(float maxLoadFactor) {
		this.maxLoadFactor = maxLoadFactor;
	}
	
	/**
	 * This method adds a new word or a new position if the word already exists in the map. 
	 */
	@Override
	public void addPos(String word, IPosition pos) {
		// get index to enter word in the map
		int hashIndex = hashCode(word);
		int j = 1;

		// loop until an available space in the hash table map is found
		while (hashTable[hashIndex] != null) {
			// condition to check if the word in the current hash table index is the same as the word to add
			if (hashTable[hashIndex].getMapWord().equals(word)) {
				// if it is then add the new position into the hash map
				hashTable[hashIndex].getPositions().add(pos);
				return;
			}
			
			// condition to check if a word was deleted in that space
			if (hashTable[hashIndex].getDEFUNCT()) {
				// set it to false
				hashTable[hashIndex].setDEFUNCT(false);
				// and add the new word in that space
				hashTable[hashIndex] = new MapEntry(word, pos);
				// increment entry
				entry++;
			}
			
			// if neither of the conditions are met, get a new index by performing 
			// double hashing for the word to be added in the hash table
			hashIndex = Math.abs((hashCode(word) + j*hashCodeDouble(word)) % size);
			j++;
			
			// increment number of probes
			numberOfProbes++;
		}
		
		// if it is an available space
		if (hashTable[hashIndex] == null) {
			// add the word in the hash map
			hashTable[hashIndex] = new MapEntry(word, pos);
			entry++;
		}
		
		// increment number of operations performed
		numberOfOperations++;
		// checks if table needs to be resized or not
		checkLoadFactor();
	}
	
	/**
	 * This method removes a word and its positions from the map. 
	 */
	@Override
	public void removeWord(String word) throws WordException {
		// get index to enter word in the map
		int hashIndex = hashCode(word);
		int j = 1;
		// increment number of operations performed
		numberOfOperations++;
		
		// condition to check if the word at that index does not exit
		if (hashTable[hashIndex] == null) 
			throw new WordException();
		
		// loop until the space does not have a word
		while (hashTable[hashIndex] != null) {
			// condition to check if the space does not have a deleted value and the
			// word in the current hash table index is the same as the word to remove
			if(!hashTable[hashIndex].getDEFUNCT() && hashTable[hashIndex].getMapWord().equals(word)) {
				// set the space to have a deleted value
				hashTable[hashIndex].setDEFUNCT(true);
				// decrement entry
				entry--;
			}
			
			// else get a new index by performing double hashing until the word to be
			// removed from the hash table is found
			hashIndex = Math.abs((hashCode(word) + j*hashCodeDouble(word)) % size);
			j++;
			numberOfProbes++;
		}
	}
	
	/**
	 * This method removes a specified position of a word from the map.
	 */
	@Override
	public void removePos(String word, IPosition pos) throws WordException {
		int hashIndex = hashCode(word);
		int j = 1;
		
		numberOfOperations++;
		if (hashTable[hashIndex] == null) 
			throw new WordException();
		
		while (hashTable[hashIndex] != null) {
			
			if(!hashTable[hashIndex].getDEFUNCT() && hashTable[hashIndex].getMapWord().equals(word)) {
				// removes the word entirely if it has only one position 
				if (hashTable[hashIndex].getPositions().size() == 1) {
					hashTable[hashIndex].setDEFUNCT(true);
					entry--;
				}
				
				// else remove the specified position from the linked list of positions of the word
				else hashTable[hashIndex].getPositions().remove(pos);
			}
			
			hashIndex = Math.abs((hashCode(word) + j*hashCodeDouble(word)) % size);
			j++;
			numberOfProbes++;
		}
	}
	
	/**
	 * This following method returns an iterator to iterate through the words present in the map.
	 */
	@Override
	public Iterator<String> words() {
		LinkedList<String> words = new LinkedList<>();
		// add each word from the map into a linked list
		for (MapEntry e : hashTable) {
			if (e != null && !e.getDEFUNCT()) 
				words.add(e.getMapWord());
		}
		
		return words.iterator();
	}
	
	/**
	 * This following method returns an iterator to iterate through the positions of a word in the map.
	 */
	@Override
	public Iterator<IPosition> positions(String word) throws WordException {
		int hashIndex = hashCode(word);
		int j = 1;
		
		while (hashTable[hashIndex] != null) {
		
			if (!hashTable[hashIndex].getDEFUNCT() && hashTable[hashIndex].getMapWord().equals(word))
				return hashTable[hashIndex].getPositions().iterator();
		
			hashIndex = Math.abs((hashCode(word) + j*hashCodeDouble(word)) % size);
			j++;
			numberOfProbes++;
		}
		
		throw new WordException();
	}
	
	/**
	 * This method returns the number of words present in the map.
	 */
	@Override
	public int numberOfEntries() {
		return entry;
	}
	
	/**
	 * This method returns the maximum load factor of the map.
	 */
	@Override
	public float getMaxLoadFactor() {
		return maxLoadFactor;
	}
	
	/**
	 * This method returns the current load factor of the map.
	 */
	@Override
	public float getLoadFactor() {
		return (float) entry / size;
	}
	
	/**
	 * This method returns the average number of probes.
	 */
	@Override
	public float averNumProbes() {
		return (float) numberOfProbes / numberOfOperations;
	}
	
	/**
	 * This method computes a hash code using the polynomial accumulation and returns it.
	 */
	@Override
	public int hashCode(String s) {
		// initialize variables
		int sum = 0, ch = 0;
		while (ch < s.length()) {
			// multiply the character value with the power of the prime number 31
			// and accumulate the computed value in sum
			sum += s.charAt(ch) * (long) Math.pow(31, ch);
			ch++;
		}
		// return the hash code with mod size for the value to be within the size of the table 
		return (int) (Math.abs(sum) % java.lang.Integer.MAX_VALUE) % size; 
	}
	
	// The following are additional helper methods for the hash map to function properly 
	
	// This method computes another hash code with a different prime number 
	// using the polynomial accumulation and returns it.
	private int hashCodeDouble(String s) {
		int sum = 0, ch = 0, hash = 0;
		while (ch < s.length()) {
			sum += s.charAt(ch) * (long) Math.pow(29, ch);
			ch++;
		}
		hash = (int) (Math.abs(sum) % java.lang.Integer.MAX_VALUE) % size;
		if (hash == 0) return 1; else return hash; 
	}

	
	// this method gets the current load factor and compares it to see 
	// if the hash map needs to be resized.
	private void checkLoadFactor() {
		if (getLoadFactor() > maxLoadFactor) 
			resize();
	}
	
	// method to check if this number is a prime number
	// Reference -> https://www.geeksforgeeks.org/recursive-program-prime-number/
	private boolean isPrime(int n, int i) {
 
        // Base cases
        if (n <= 2)
            return (n == 2) ? true : false;
        if (n % i == 0)
            return false;
        if (i * i > n)
            return true;
      
        // Check for next divisor
        return isPrime(n, i + 1);
    }
		
	// method to get the next prime number
	private int getPrime(int n){
		// get the next prime number near the double of n 
		int nextPrime = 2*n;
			
		// find the next prime number if the double is not a prime number
		while(!isPrime(nextPrime, 2))
			nextPrime++;
		
		return nextPrime;
	}

	// method used to resize the table and add the prev values in the new table again
	// when the current load factor is more than the max
	private void resize() {
		size = getPrime(size);
		MapEntry[] copy = hashTable.clone();
		hashTable = new MapEntry[size];
		
		for (MapEntry e : copy) {
			
			if (e != null && !e.getDEFUNCT()) {
				int hashIndex = hashCode(e.getMapWord()); 
				int j = 1;
				
				while (hashTable[hashIndex] != null) {
					
					if (hashTable[hashIndex].getDEFUNCT()) {
						hashTable[hashIndex].setDEFUNCT(false);
						hashTable[hashIndex] = e;
					}
					hashIndex = Math.abs((hashCode(e.getMapWord()) + j*hashCodeDouble(e.getMapWord())) % size);
					j++;
					numberOfProbes++;
				}
				if (hashTable[hashIndex] == null) 
					hashTable[hashIndex] = e;
			}
		}
	}
}