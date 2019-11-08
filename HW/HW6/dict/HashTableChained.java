/* HashTableChained.java */

package dict; // package statement must be on the first line.
import list.*; // then other packages can be imported.
import java.math.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  /**
   *  Place any data fields here.
   **/

  protected int size;
  protected int sizeBucket;
  public List[] defTable;


  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
    sizeBucket = Prime.primeBetween(sizeEstimate, sizeEstimate * 2);
    defTable = new DList[sizeBucket];
    for(int i=0; i<sizeBucket; i++) {
      defTable[i] = new DList();
    }
    size = 0;
  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
    sizeBucket = 101;
    defTable = new DList[sizeBucket];
    for(int i=0; i<sizeBucket; i++) {
      defTable[i] = new DList();
    }
    size = 0;
  }
  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
    int a = 1021;
    int b = 11287;
    int p = Prime.primeLarge(sizeBucket);
    //System.out.println("code = "+ code + ", p = "+p);

    int index = (Math.abs(a * code + b) % p) % sizeBucket;
    return index;
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    return size;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
   if(size==0) {
    return true;
   } else {
    return false;
   }
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
    int hashCode = key.hashCode();
    int index = compFunction(hashCode);

    Entry entry = new Entry();
    entry.key = key;
    entry.value = value;

    defTable[index].insertFront(entry);
    size++;
    return entry;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
    int hashCode = key.hashCode();
    int index = compFunction(hashCode);

    if(defTable[index].isEmpty()) {
      return null;
    }

    Entry entry = new Entry();
    ListNode current = defTable[index].front();

    try{
      while(true) {
        if(((Entry) current.item()).key().equals(key)) {
          entry.key = key;
          entry.value = ((Entry) current.item()).value();
          return entry;
        }
        if(current==defTable[index].back()) {
          break;
        }
        current = current.next();
      }
    } catch(Exception err) {
      System.out.println(err);
    }
    return null;
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
    int hashCode = key.hashCode();
    int index = compFunction(hashCode);

    if(defTable[index].isEmpty()) {
      return null;
    }

    Entry entry = new Entry();
    ListNode current = defTable[index].front();
    try{
      while(true) {
        if(( (Entry) current.item()).key().equals(key)) {
          entry.key = key;
          entry.value = ((Entry) current.item()).value();
          current.remove();
          size--;
          return entry;
        }
        if(current==defTable[index].back()) {
          break;
        }
        current = current.next();
      }
    } catch(Exception err) {
      System.out.println(err);
    }
    return null;
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
    defTable = new DList[sizeBucket];
    for(int i=0; i<sizeBucket; i++) {
      defTable[i] = new DList();
    }
    size = 0;
  }

  public int[] collisions() {
    int[] collisionsArray = new int[defTable.length];
    int num0 = 0;
    int num1 = 0;
    int num2 = 0;
    for(int i=0; i<defTable.length; i++) {
      collisionsArray[i] = defTable[i].length();
      if(collisionsArray[i]==0) {
        num0++;
      } else if(collisionsArray[i]==1) {
        num1++;
      } else{
        num2++;
      }
      //System.out.println("i = " + i + ", " + collisionsArray[i]);
    }
    //System.out.println(((double) num0) + ", " + ((double) num1) + ", " + ((double) num2)) ;
    return collisionsArray;
  }

}
