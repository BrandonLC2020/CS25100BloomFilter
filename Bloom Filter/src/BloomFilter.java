import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Bloom Filter
 *
 * This program implements a bloom filter. It constructs a bloom filter containing a bit array (represented as
 * a boolean array) of given length and its two main operations, add and test, which use a given number of hash
 * functions to perform their task and/or compute their results.
 *
 * @author Brandon Lamer-Connolly, CS 25100
 */

public class BloomFilter {

    private boolean[] bitArray; // boolean array used to represent the bloom filter's bit array
    private int capacity; // length of the boolean array
    private int numHashFunctions; // number of hash functions used

    /**
     * Bloom Filter constructor
     *
     * @param capacity the length of the bit array
     * @param numHashFunctions inputted number of hash functions to implement (assumed to not be zero)
     */
    public BloomFilter(int capacity, int numHashFunctions) {
        bitArray = new boolean[capacity];
        for (int i = 0; i < capacity; i++) {
            bitArray[i] = false;
        }
        this.capacity = capacity;
        this.numHashFunctions = numHashFunctions;
    }

    /**
     * the first hash function used to add a given element to the bit array
     * @param input the given element to be added to the bit array represented as a String
     * @return the first hashCode for the given element to be inserted
     * @throws NoSuchAlgorithmException
     */
    public int hashFunctionA(String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        byte[] bits = messageDigest.digest(input.getBytes());
        String hash = new String(bits);

        int hashModded = hash.hashCode() % capacity;

        if (hashModded < 0) {
            return hashModded + capacity;
        } else {
            return hashModded;
        }
    }

    /**
     * the first hash function used to add a given element to the bit array
     * @param input the given element to be added to the bit array represented as an integer
     * @return the first hashCode for the given element to be inserted
     * @throws NoSuchAlgorithmException
     */
    public int hashFunctionA(int input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        String inputStr = "" + input;
        byte[] bits = messageDigest.digest(inputStr.getBytes());
        String hash = new String(bits);

        int hashModded = hash.hashCode() % capacity;

        if (hashModded < 0) {
            return hashModded + capacity;
        } else {
            return hashModded;
        }
    }

    /**
     * the second hash function used to add a given element to the bit array
     * @param input the given element to be added to the bit array represented as a String
     * @return the second hashCode for the given element to be inserted
     * @throws NoSuchAlgorithmException
     */
    public int hashFunctionB(String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        byte[] bitsFirst = messageDigest.digest(input.getBytes());
        String hashFirst = new String(bitsFirst);
        int hashedFirst = hashFirst.hashCode();

        String hashedTwiceStr = "" + hashedFirst;
        byte[] bitsSecond = messageDigest.digest(hashedTwiceStr.getBytes());
        String hashSecond = new String(bitsSecond);
        int hashedSecond = hashSecond.hashCode();

        int doubleHashedModded = hashedSecond % capacity;

        if (doubleHashedModded < 0) {
            return doubleHashedModded + capacity;
        } else {
            return doubleHashedModded;
        }
    }

    /**
     * the second hash function used to add a given element to the bit array
     * @param input the given element to be added to the bit array represented as an integer
     * @return the second hashCode for the given element to be inserted
     * @throws NoSuchAlgorithmException
     */
    public int hashFunctionB(int input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        String inputStr = "" + input;
        byte[] bitsFirst = messageDigest.digest(inputStr.getBytes());
        String hashFirst = new String(bitsFirst);
        int hashedFirst = hashFirst.hashCode();

        String hashedTwiceStr = "" + hashedFirst;
        byte[] bitsSecond = messageDigest.digest(hashedTwiceStr.getBytes());
        String hashSecond = new String(bitsSecond);
        int hashedSecond = hashSecond.hashCode();

        int doubleHashedModded = hashedSecond % capacity;

        if (doubleHashedModded < 0) {
            return doubleHashedModded + capacity;
        } else {
            return doubleHashedModded;
        }
    }

    /**
     * the i-th hash function used to add a given element to the bit array
     * @param i the i-th iteration of a hash function for a given element
     * @param inputA the returned int from hashFunctionA(the given element)
     * @param inputB the returned int from hashFunctionB(the given element)
     * @return the i-th hashCode for the given element to be inserted
     */
    public int hashFunctionAtI(int i, int inputA, int inputB) {
        int hashResult = (inputA + inputB * i) % capacity;

        if (hashResult < 0) {
            return hashResult + capacity;
        } else {
            return hashResult;
        }
    }

    /**
     * the test operation for the Bloom Filter
     * checks to see if at indices (given by the hash function(s) for the given element as an integer)
     * in the boolean array are set to true (bit 1) or false (bit 0) to determine if the given element is
     * probably in the data structure or definitely not in the data structure
     *
     * @param element the given element being tested to see if it's probably in the data structure or definitely not
     *                in the data structure
     * @return true if the given element is probably in the data structure and false if the given element is definitely
     * not in the data structure
     * @throws NoSuchAlgorithmException
     */
    public boolean test(int element) throws NoSuchAlgorithmException {
        if (numHashFunctions == 1) { // assume it won't be zero at any point
            int index = hashFunctionA(element);
            if (bitArray[index]) {
                return true;
            } else {
                return false;
            }
        } else if (numHashFunctions == 2) {
            int indexA = hashFunctionA(element);
            int indexB = hashFunctionB(element);
            if (bitArray[indexA] || bitArray[indexB]) {
                return true;
            } else {
                return false;
            }
        } else {
            int indexA = hashFunctionA(element);
            int indexB = hashFunctionB(element);
            if (bitArray[indexA] || bitArray[indexB]) {
                return true;
            } else {
                for (int i = 3; i <= numHashFunctions; i++) {
                    int indexI = hashFunctionAtI(i, indexA, indexB);
                    if (bitArray[indexI]) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    /**
     * the test operation for the Bloom Filter
     * checks to see if at indices (given by the hash function(s) for the given element as a String)
     * in the boolean array are set to true (bit 1) or false (bit 0) to determine if the given element is
     * probably in the data structure or definitely not in the data structure
     *
     * @param element the given element being tested to see if it's probably in the data structure or definitely not
     *                in the data structure
     * @return true if the given element is probably in the data structure and false if the given element is definitely
     * not in the data structure
     * @throws NoSuchAlgorithmException
     */
    public boolean test(String element) throws NoSuchAlgorithmException {
        if (numHashFunctions == 1) { // won't be zero for sure
            int index = hashFunctionA(element);
            if (bitArray[index]) {
                return true;
            } else {
                return false;
            }
        } else if (numHashFunctions == 2) {
            int indexA = hashFunctionA(element);
            int indexB = hashFunctionB(element);
            if (bitArray[indexA] || bitArray[indexB]) {
                return true;
            } else {
                return false;
            }
        } else {
            int indexA = hashFunctionA(element);
            int indexB = hashFunctionB(element);
            if (bitArray[indexA] || bitArray[indexB]) {
                return true;
            } else {
                for (int i = 3; i <= numHashFunctions; i++) {
                    int indexI = hashFunctionAtI(i, indexA, indexB);
                    if (bitArray[indexI]) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    /**
     * the add operation for the Bloom Filter
     * implements however many hash functions have been specified for the bloom filter to get multiple hash codes for
     * the given element (represented as a string) and will "add" (set boolean at the index to TRUE) the element at
     * each of the indices calculated from each hash function
     *
     * @param element the given element to be added into the data structure
     * @throws NoSuchAlgorithmException
     */
    public void add(String element) throws NoSuchAlgorithmException {
        // assume numHashFunctions cannot be 0
        if (numHashFunctions == 1) { // only use hashFunctionA
            int index = hashFunctionA(element);
            if (!bitArray[index]) {
                bitArray[index] = true;
            }
        } else if (numHashFunctions == 2) { // use both hashFunctionA and hashFunctionB
            int indexA = hashFunctionA(element);
            if (!bitArray[indexA]) {
                bitArray[indexA] = true;
            }
            int indexB = hashFunctionB(element);
            if (!bitArray[indexB]) {
                bitArray[indexB] = true;
            }
        } else {
            // use both hashFunctionA and hashFunctionB and then use their results for each subsequent i-th hashFunction
            int indexA = hashFunctionA(element);
            if (!bitArray[indexA]) {
                bitArray[indexA] = true;
            }
            int indexB = hashFunctionB(element);
            if (!bitArray[indexB]) {
                bitArray[indexB] = true;
            }
            for (int i = 3; i <= numHashFunctions; i++) {
                int indexI = hashFunctionAtI(i, indexA, indexB);
                if (!bitArray[indexI]) {
                    bitArray[indexI] = true;
                }
            }
        }
    }

    /**
     * the add operation for the Bloom Filter
     * implements however many hash functions have been specified for the bloom filter to get multiple hash codes for
     * the given element (represented as an integer) and will "add" (set boolean at the index to TRUE) the element at
     * each of the indices calculated from each hash function
     *
     * @param element the given element to be added into the data structure
     * @throws NoSuchAlgorithmException
     */
    public void add(int element) throws NoSuchAlgorithmException {
        //  comments from the above add method apply similarly in this add method
        if (numHashFunctions == 1) {
            int index = hashFunctionA(element);
            if (!bitArray[index]) {
                bitArray[index] = true;
            }
        } else if (numHashFunctions == 2) {
            int indexA = hashFunctionA(element);
            if (!bitArray[indexA]) {
                bitArray[indexA] = true;
            }
            int indexB = hashFunctionB(element);
            if (!bitArray[indexB]) {
                bitArray[indexB] = true;
            }
        } else {
            int indexA = hashFunctionA(element);
            if (!bitArray[indexA]) {
                bitArray[indexA] = true;
            }
            int indexB = hashFunctionB(element);
            if (!bitArray[indexB]) {
                bitArray[indexB] = true;
            }
            for (int i = 3; i <= numHashFunctions; i++) {
                int indexI = hashFunctionAtI(i, indexA, indexB);
                if (!bitArray[indexI]) {
                    bitArray[indexI] = true;
                }
            }
        }
    }

    /**
     * toString method for the bloom filter
     * represents the bloom filter as a string where the first line displays the capacity of the bloom filter and
     * the number of hash functiosn implemented in the data structure and where the second line prints the bit array
     *
     * @return the string representation of the bloom filter
     */
    public String toString() {
        StringBuilder arrayString = new StringBuilder();
        arrayString.append("[ ");
        for (int i = 0; i < capacity-1; i++) {
            if (bitArray[i]) {
                arrayString.append("1, ");
            } else {
                arrayString.append("0, ");
            }
        }
        if (bitArray[capacity-1]) {
            arrayString.append("1 ]");
        } else {
            arrayString.append("0 ]");
        }
        String result = String.format("capacity: %d | num of hash functions: %d\n%s\n", capacity, numHashFunctions, arrayString.toString());
        return result;

    }

    // used for testing purposes
    public static void main(String[] args) throws NoSuchAlgorithmException {
        BloomFilter test = new BloomFilter(50, 4);
        for (int i = 1; i <= 10; i ++) {
            test.add(i);
        }
        String[] letters = new String[]{"a", "b", "c", "d", "e", "f"};
        for (int i = 0; i < letters.length; i++) {
            test.add(letters[i]);
        }

        System.out.println(test.toString());
        System.out.println(test.test(0));

        BloomFilter emptyTest = new BloomFilter(20, 3);
        emptyTest.add(2);
        System.out.println(emptyTest.toString());
        System.out.println(emptyTest.test(0));
    }

}

/*
Source for Getting the i-th Hash Function(s)
http://willwhim.wpengine.com/2011/09/03/producing-n-hash-functions-by-hashing-only-once/

Source for Bloom Filter General Information and Implementation Descriptions
https://www.jasondavies.com/bloomfilter/
*/


