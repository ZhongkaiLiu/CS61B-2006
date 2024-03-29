/* Set.java */

import list.*;

/**
 *  A Set is a collection of Comparable elements stored in sorted order.
 *  Duplicate elements are not permitted in a Set.
 **/
@SuppressWarnings("unchecked")
public class Set {
  protected List setList;

  /**
   * Set ADT invariants:
   *  1)  The Set's elements must be precisely the elements of the List.
   *  2)  The List must always contain Comparable elements, and those elements 
   *      must always be sorted in ascending order.
   *  3)  No two elements in the List may be equals().
   **/

  /**
   *  Constructs an empty Set. 
   *
   *  Performance:  runs in O(1) time.
   **/
  public Set() { 
    setList = new DList();
  }

  /**
   *  cardinality() returns the number of elements in this Set.
   *
   *  Performance:  runs in O(1) time.
   **/
  public int cardinality() {
    return setList.length();
  }

  /**
   *  insert() inserts a Comparable element into this Set.
   *
   *  Sets are maintained in sorted order.  The ordering is specified by the
   *  compareTo() method of the java.lang.Comparable interface.
   *
   *  Performance:  runs in O(this.cardinality()) time.
   **/
  public void insert(Comparable c) throws InvalidNodeException {
    if(setList.isEmpty()) {
      setList.insertFront(c);
    } else if(c.compareTo(setList.back().item()) > 0) {
      setList.insertBack(c);
    } else {
      ListNode current = setList.front();
      while(true) {
        if(c.compareTo(current.item()) == 0) {
          break;
        } else if(c.compareTo(current.item()) < 0) {
          current.insertBefore(c);
          break;
        }
        current = current.next();
      }
    }
  }

  /**
   *  union() modifies this Set so that it contains all the elements it
   *  started with, plus all the elements of s.  The Set s is NOT modified.
   *  Make sure that duplicate elements are not created.
   *
   *  Performance:  Must run in O(this.cardinality() + s.cardinality()) time.
   *
   *  Your implementation should NOT copy elements of s or "this", though it
   *  will copy _references_ to the elements of s.  Your implementation will
   *  create new _nodes_ for the elements of s that are added to "this", but
   *  you should reuse the nodes that are already part of "this".
   *
   *  DO NOT MODIFY THE SET s.
   *  DO NOT ATTEMPT TO COPY ELEMENTS; just copy _references_ to them.
   **/
  public void union(Set s) throws InvalidNodeException {
    if(!s.setList.isEmpty()) {
      if(this.setList.isEmpty()) {
        this.setList = s.setList;
      } else {
        ListNode thisMin = this.setList.front();
        ListNode thisMax = this.setList.back(); 
        ListNode sMin = s.setList.front();
        ListNode sMax = s.setList.back();

        if(((Comparable)sMin.item()).compareTo(thisMax.item()) > 0) {
          ListNode current = sMin;
          while(true) {
            this.setList.insertBack(current.item());
            current = current.next();
            if(current == sMax) {
              this.setList.insertBack(current.item());
              break;
            }
          }
        } else if(((Comparable)sMax.item()).compareTo(thisMin.item()) < 0) {
          ListNode current = sMax;
          while(true) {
            this.setList.insertFront(current.item());
            current = current.prev();
            if(current == sMin) {
              this.setList.insertFront(current.item());
              break;
            }
          }
        } else {
          ListNode sCurrent = sMin;
          ListNode thisCurrent = thisMin;

          while(((Comparable)sCurrent.item()).compareTo(sMax.item()) <= 0) {
            if(((Comparable)sCurrent.item()).compareTo(thisCurrent.item()) == 0) {
              if(sCurrent==sMax) {
                break;
              }
              sCurrent = sCurrent.next();
            } else if(((Comparable)sCurrent.item()).compareTo(thisCurrent.item()) < 0 ) {
              thisCurrent.insertBefore(sCurrent.item());
              if(sCurrent==sMax) {
                break;
              }
              sCurrent = sCurrent.next();
            } else {
              if(thisCurrent==thisMax) {
                thisCurrent.insertAfter(sCurrent.item());
                if(sCurrent==sMax) {
                  break;
                }
                sCurrent = sCurrent.next();
              }
              thisCurrent = thisCurrent.next();
            }
          }
        }
      }
    }
  }

  /**
   *  intersect() modifies this Set so that it contains the intersection of
   *  its own elements and the elements of s.  The Set s is NOT modified.
   *
   *  Performance:  Must run in O(this.cardinality() + s.cardinality()) time.
   *
   *  Do not construct any new ListNodes during the execution of intersect.
   *  Reuse the nodes of "this" that will be in the intersection.
   *
   *  DO NOT MODIFY THE SET s.
   *  DO NOT CONSTRUCT ANY NEW NODES.
   *  DO NOT ATTEMPT TO COPY ELEMENTS.
   **/
  public void intersect(Set s) throws InvalidNodeException {
    ListNode thisMin = this.setList.front();
    ListNode thisMax = this.setList.back(); 
    ListNode sMin = s.setList.front();
    ListNode sMax = s.setList.back();

    if(s.setList.isEmpty()||this.setList.isEmpty()) {
      this.setList = new DList();
    } else if((((Comparable)sMin.item()).compareTo(thisMax.item()) > 0)||(((Comparable)sMax.item()).compareTo(thisMin.item()) < 0)) {
        this.setList = new DList();
    } else {
      ListNode current = thisMin;
      ListNode next;
      ListNode sCurrent = sMin;

      while(((Comparable)current.item()).compareTo(thisMax.item()) <= 0) {
        if(((Comparable)current.item()).compareTo(sMin.item()) < 0 || ((Comparable)current.item()).compareTo(sMax.item()) > 0 || ((Comparable)current.item()).compareTo(sCurrent.item()) < 0) {
          if(current==thisMax) {
            current.remove();
            break;
          }
          next = current.next();
          current.remove();
          current = next;
        } else if(((Comparable)current.item()).compareTo(sCurrent.item()) == 0) {
          if(current==thisMax) {
            break;
          }
          next = current.next();
          current = next;
          sCurrent = sCurrent.next();
        } else if(((Comparable)current.item()).compareTo(sCurrent.item()) > 0) {
          sCurrent = sCurrent.next();
        }
      }
    }
  }

  /**
   *  toString() returns a String representation of this Set.  The String must
   *  have the following format:
   *    {  } for an empty Set.  No spaces before "{" or after "}"; two spaces
   *            between them.
   *    {  1  2  3  } for a Set of three Integer elements.  No spaces before
   *            "{" or after "}"; two spaces before and after each element.
   *            Elements are printed with their own toString method, whatever
   *            that may be.  The elements must appear in sorted order, from
   *            lowest to highest according to the compareTo() method.
   *
   *  WARNING:  THE AUTOGRADER EXPECTS YOU TO PRINT SETS IN _EXACTLY_ THIS
   *            FORMAT, RIGHT UP TO THE TWO SPACES BETWEEN ELEMENTS.  ANY
   *            DEVIATIONS WILL LOSE POINTS.
   **/
  public String toString() {
    String result = "{  ";
    ListNode current = setList.front();
    try {
      while (true) {
        result = result + current.item() + "  ";
        if(current==setList.back()) {
          break;
        }
        current = current.next();
      }
    } catch(InvalidNodeException err) {
      System.out.println("called on invalid node");
    } finally {
      return result + "}";
    }
  }

  public static void main(String[] argv) {
    try {
      Set s = new Set();
      s.insert(new Integer(3));
      s.insert(new Integer(4));
      s.insert(new Integer(3));
      System.out.println("Set s = " + s);

      Set s2 = new Set();
      s2.insert(new Integer(4));
      s2.insert(new Integer(5));
      s2.insert(new Integer(5));
      System.out.println("Set s2 = " + s2);

      Set s3 = new Set();
      s3.insert(new Integer(5));
      s3.insert(new Integer(3));
      s3.insert(new Integer(8));
      System.out.println("Set s3 = " + s3);

      s.union(s2);
      System.out.println("After s.union(s2), s = " + s);

      s.intersect(s3);
      System.out.println("After s.intersect(s3), s = " + s);

      System.out.println("s.cardinality() = " + s.cardinality());
      // You may want to add more (ungraded) test code here.
    } catch(InvalidNodeException err) {
      System.out.println("called on invalid node");
    }

  }
}
