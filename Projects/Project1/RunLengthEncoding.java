/* RunLengthEncoding.java */

/**
 *  The RunLengthEncoding class defines an object that run-length encodes an
 *  Ocean object.  Descriptions of the methods you must implement appear below.
 *  They include constructors of the form
 *
 *      public RunLengthEncoding(int i, int j, int starveTime);
 *      public RunLengthEncoding(int i, int j, int starveTime,
 *                               int[] runTypes, int[] runLengths) {
 *      public RunLengthEncoding(Ocean ocean) {
 *
 *  that create a run-length encoding of an Ocean having width i and height j,
 *  in which sharks starve after starveTime timesteps.
 *
 *  The first constructor creates a run-length encoding of an Ocean in which
 *  every cell is empty.  The second constructor creates a run-length encoding
 *  for which the runs are provided as parameters.  The third constructor
 *  converts an Ocean object into a run-length encoding of that object.
 *
 *  See the README file accompanying this project for additional details.
 */

public class RunLengthEncoding {

  /**
   *  Define any variables associated with a RunLengthEncoding object here.
   *  These variables MUST be private.
   */

  private int width;
  private int height;
  private int starveTime;
  private DList runDList;
  private DListNode currentNode;

  public int xLocation(int x) {
    if(x<0) {
      return width+x%width;
    }else {
      return x%width;
    }
  }

  public int yLocation(int y) {
    if(y<0) {
      return height+y%height;
    }else {
      return y%height;
    }
  }

  /**
   *  The following methods are required for Part II.
   */

  /**
   *  RunLengthEncoding() (with three parameters) is a constructor that creates
   *  a run-length encoding of an empty ocean having width i and height j,
   *  in which sharks starve after starveTime timesteps.
   *  @param i is the width of the ocean.
   *  @param j is the height of the ocean.
   *  @param starveTime is the number of timesteps sharks survive without food.
   */

  public RunLengthEncoding(int i, int j, int starveTime) {
    width = i;
    height = j;
    this.starveTime = starveTime;
    runDList = new DList();
    currentNode = runDList.head;
    
    runDList.insertEnd(new Run(Ocean.EMPTY, i*j));
  }

  /**
   *  RunLengthEncoding() (with five parameters) is a constructor that creates
   *  a run-length encoding of an ocean having width i and height j, in which
   *  sharks starve after starveTime timesteps.  The runs of the run-length
   *  encoding are taken from two input arrays.  Run i has length runLengths[i]
   *  and species runTypes[i].
   *  @param i is the width of the ocean.
   *  @param j is the height of the ocean.
   *  @param starveTime is the number of timesteps sharks survive without food.
   *  @param runTypes is an array that represents the species represented by
   *         each run.  Each element of runTypes is Ocean.EMPTY, Ocean.FISH,
   *         or Ocean.SHARK.  Any run of sharks is treated as a run of newborn
   *         sharks (which are equivalent to sharks that have just eaten).
   *  @param runLengths is an array that represents the length of each run.
   *         The sum of all elements of the runLengths array should be i * j.
   */

  public RunLengthEncoding(int i, int j, int starveTime,
                           int[] runTypes, int[] runLengths) {
    width = i;
    height = j;
    this.starveTime = starveTime;
    runDList = new DList();
    currentNode = runDList.head;

    for(int k=0; k<runTypes.length; k++) {
      runDList.insertEnd(new Run(runTypes[k], runLengths[k], starveTime));
    }
  }

  /**
   *  restartRuns() and nextRun() are two methods that work together to return
   *  all the runs in the run-length encoding, one by one.  Each time
   *  nextRun() is invoked, it returns a different run (represented as a
   *  TypeAndSize object), until every run has been returned.  The first time
   *  nextRun() is invoked, it returns the first run in the encoding, which
   *  contains cell (0, 0).  After every run has been returned, nextRun()
   *  returns null, which lets the calling program know that there are no more
   *  runs in the encoding.
   *
   *  The restartRuns() method resets the enumeration, so that nextRun() will
   *  once again enumerate all the runs as if nextRun() were being invoked for
   *  the first time.
   *
   *  (Note:  Don't worry about what might happen if nextRun() is interleaved
   *  with addFish() or addShark(); it won't happen.)
   */

  /**
   *  restartRuns() resets the enumeration as described above, so that
   *  nextRun() will enumerate all the runs from the beginning.
   */

  public void restartRuns() {
    currentNode = runDList.head;
  }

  /**
   *  nextRun() returns the next run in the enumeration, as described above.
   *  If the runs have been exhausted, it returns null.  The return value is
   *  a TypeAndSize object, which is nothing more than a way to return two
   *  integers at once.
   *  @return the next run in the enumeration, represented by a TypeAndSize
   *          object.
   */

  public TypeAndSize nextRun() {
    if(currentNode.next==runDList.head) {
      return null;
    } else {
      currentNode = currentNode.next;
      int type = currentNode.item.species;
      int size = currentNode.item.length;
      return new TypeAndSize(type, size);
    }

  }

  /**
   *  toOcean() converts a run-length encoding of an ocean into an Ocean
   *  object.  You will need to implement the three-parameter addShark method
   *  in the Ocean class for this method's use.
   *  @return the Ocean represented by a run-length encoding.
   */

  public Ocean toOcean() {
    Ocean sea = new Ocean(width, height, starveTime);
    DListNode nodePointer = runDList.head.next;
    int type = nodePointer.item.species;
    int size = nodePointer.item.length;
    int life = nodePointer.item.sharkLife;

    for(int y=0; y<height; y++) {
      for(int x=0; x<width; x++) {
        if(size==0) {
          nodePointer = nodePointer.next;
          type = nodePointer.item.species;
          size = nodePointer.item.length;
          life = nodePointer.item.sharkLife;
        }

        if(type==Ocean.FISH) {
          sea.addFish(x,y);
        } else if(type==Ocean.SHARK) {
          sea.addShark(x,y,life);
        }
        size--;
      }
    }
    return sea;
  }

  /**
   *  The following method is required for Part III.
   */

  /**
   *  RunLengthEncoding() (with one parameter) is a constructor that creates
   *  a run-length encoding of an input Ocean.  You will need to implement
   *  the sharkFeeding method in the Ocean class for this constructor's use.
   *  @param sea is the ocean to encode.
   */

  public RunLengthEncoding(Ocean sea) {
    width = sea.width();
    height = sea.height();
    starveTime = sea.starveTime();
    runDList = new DList();
    currentNode = runDList.head;

    int type = sea.cellContents(0,0);
    int size = 1;
    int life = sea.sharkFeeding(0,0);

    for(int y=0; y<height; y++) {
      for(int x=0; x<width; x++) {

        if(x==0&&y==0) {
          continue;
        }

        if(type==sea.cellContents(x,y)&&life==sea.sharkFeeding(x,y)) {
          size++;
        } else {
          runDList.insertEnd(new Run(type, size, life));
          type = sea.cellContents(x,y);
          size = 1;
          life = sea.sharkFeeding(x,y);
        }

        if(x+1==width&&y+1==height) {
          runDList.insertEnd(new Run(type, size, life));
        }
      }
    }
    check();
  }



  /**
   *  The following methods are required for Part IV.
   */

  // Return the correct DListNode of cell (x,y)
  public DListNode findNode(int x, int y) {
    DListNode nodePointer = runDList.head.next;
    int location = (xLocation(x)+1) + width * yLocation(y);

    while(nodePointer!=runDList.head) {
      location = location - nodePointer.item.length;
      if(location<=0) {
        break;
      }
      nodePointer = nodePointer.next;
    }
    return nodePointer;
  }

  // Return the index of the node in the DList
  public int nodeIndex(int x, int y) {
    DListNode nodePointer = runDList.head.next;
    int location = (xLocation(x)+1) + width * yLocation(y);
    int index = 1;

    while(nodePointer!=runDList.head) {
      location = location - nodePointer.item.length;
      if(location<=0) {
        break;
      }
      nodePointer = nodePointer.next;
      index++;
    }
    return index;
  }

  // Return the cell index inside the node, starts from 1
  public int cellIndexInNode(int x, int y) {
    DListNode nodePointer = runDList.head.next;
    int location = (xLocation(x)+1) + width * yLocation(y);

    while(nodePointer!=runDList.head) {
      location = location - nodePointer.item.length;
      if(location<=0) {
        break;
      }
      nodePointer = nodePointer.next;
    }
    return location + nodePointer.item.length;
  }

  // Return the species of the node which the cell (x,y) is in
  public int nodeContents(int x, int y) {
    return findNode(x,y).item.species;
  }


  /**
   *  addFish() places a fish in cell (x, y) if the cell is empty.  If the
   *  cell is already occupied, leave the cell as it is.  The final run-length
   *  encoding should be compressed as much as possible; there should not be
   *  two consecutive runs of sharks with the same degree of hunger.
   *  @param x is the x-coordinate of the cell to place a fish in.
   *  @param y is the y-coordinate of the cell to place a fish in.
   */

  public void addFish(int x, int y) {
    DListNode node = findNode(x,y);
    int index = cellIndexInNode(x,y);

    if(nodeContents(x,y)==Ocean.EMPTY) {
      if(node.item.length==1) {
        if(node.prev.item.species==Ocean.FISH&&node.next.item.species==Ocean.FISH) {
          node.prev.item.length = node.prev.item.length + node.next.item.length + 1;
          runDList.removeNode(node.next);
          runDList.removeNode(node);
        } else if(node.prev.item.species==Ocean.FISH) {
          node.prev.item.length++;
          runDList.removeNode(node);
        } else if(node.next.item.species==Ocean.FISH) {
          node.next.item.length++;
          runDList.removeNode(node);
        } else {
          node.item.species = Ocean.FISH;
        }
      }
      else {
        if(index==1) {
          if(node.prev.item.species==Ocean.FISH) {
            node.prev.item.length++;
            node.item.length--;
          } else {
            node.item.length--;
            runDList.insertAfter(node.prev, new Run(Ocean.FISH, 1));
          }
        } else if(index==node.item.length) {
          if(node.next.item.species==Ocean.FISH) {
            node.next.item.length++;
            node.item.length--;
          } else {
            node.item.length--;
            runDList.insertAfter(node, new Run(Ocean.FISH, 1));
          }
        } else {
          runDList.insertAfter(node, new Run(Ocean.FISH, 1));
          runDList.insertAfter(node.next, new Run(Ocean.EMPTY, node.item.length - index));
          node.item.length = index-1;
        }
      }
    }
    check();
  }

  /**
   *  addShark() (with two parameters) places a newborn shark in cell (x, y) if
   *  the cell is empty.  A "newborn" shark is equivalent to a shark that has
   *  just eaten.  If the cell is already occupied, leave the cell as it is.
   *  The final run-length encoding should be compressed as much as possible;
   *  there should not be two consecutive runs of sharks with the same degree
   *  of hunger.
   *  @param x is the x-coordinate of the cell to place a shark in.
   *  @param y is the y-coordinate of the cell to place a shark in.
   */

  public void addShark(int x, int y) {
    DListNode node = findNode(x,y);
    int index = cellIndexInNode(x,y);

    if(nodeContents(x,y)==Ocean.EMPTY) {
      if(node.item.length==1) {
        if(node.prev.item.species==Ocean.SHARK&&node.prev.item.sharkLife==starveTime&&node.next.item.species==Ocean.SHARK&&node.next.item.sharkLife==starveTime) {
          node.prev.item.length = node.next.item.length + 1;
          runDList.removeNode(node.next);
          runDList.removeNode(node);
        } else if(node.prev.item.species==Ocean.SHARK&&node.prev.item.sharkLife==starveTime) {
          node.prev.item.length++;
          runDList.removeNode(node);
        } else if(node.next.item.species==Ocean.SHARK&&node.next.item.sharkLife==starveTime) {
          node.next.item.length++;
          runDList.removeNode(node);
        } else {
          node.item.species = Ocean.FISH;
          node.item.sharkLife = starveTime;
        }
      }
      else {
        if(index==1) {
          if(node.prev.item.species==Ocean.SHARK&&node.prev.item.sharkLife==starveTime) {
            node.prev.item.length++;
            node.item.length--;
          } else {
            node.item.length--;
            runDList.insertAfter(node.prev, new Run(Ocean.SHARK, 1, starveTime));
          }
        } else if(index==node.item.length) {
          if(node.next.item.species==Ocean.SHARK&&node.next.item.sharkLife==starveTime) {
            node.next.item.length++;
            node.item.length--;
          } else {
            node.item.length--;
            runDList.insertAfter(node, new Run(Ocean.SHARK, 1, starveTime));
          }
        } else {
          runDList.insertAfter(node, new Run(Ocean.SHARK, 1, starveTime));
          runDList.insertAfter(node.next, new Run(Ocean.EMPTY, node.item.length - index));
          node.item.length = index-1;
        }
      }
    }
    check();
  }

  /**
   *  check() walks through the run-length encoding and prints an error message
   *  if two consecutive runs have the same contents, or if the sum of all run
   *  lengths does not equal the number of cells in the ocean.
   */

  public void check() {
    DListNode nodePointer = runDList.head.next;
    int size = 0;

    while(nodePointer!=runDList.head) {
      if(nodePointer.next!=runDList.head) {
        if(nodePointer.item.species==nodePointer.next.item.species&&nodePointer.item.sharkLife==nodePointer.next.item.sharkLife) {
          System.out.println("Two consecutive runs have the same contents!");
          System.exit(1);
        }
      }
      size = size + nodePointer.item.length;
      nodePointer = nodePointer.next;
    }

    if(size!=width*height) {
      System.out.println("The sum of all run lengths does not equal the number of cells in the ocean!");
      System.out.println("Sum of run lengths = "+size+"; Number of cells = "+width*height);
      Thread.dumpStack();
      System.exit(1);
    }

  }
}

