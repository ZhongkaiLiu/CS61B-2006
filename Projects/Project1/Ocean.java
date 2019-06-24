/* Ocean.java */
/**
 *  The Ocean class defines an object that models an ocean full of sharks and
 *  fish.  Descriptions of the methods you must implement appear below.  They
 *  include a constructor of the form
 *
 *      public Ocean(int i, int j, int starveTime);
 *
 *  that creates an empty ocean having width i and height j, in which sharks
 *  starve after starveTime timesteps.
 *
 *  See the README file accompanying this project for additional details.
 */

public class Ocean {

  /**
   *  Do not rename these constants.  WARNING:  if you change the numbers, you
   *  will need to recompile Test4.java.  Failure to do so will give you a very
   *  hard-to-find bug.
   */

  public final static int EMPTY = 0;
  public final static int SHARK = 1;
  public final static int FISH = 2;
  public final static int DEATH = -1;

  /**
   *  Define any variables associated with an Ocean object here.  These
   *  variables MUST be private.
   */
  private int width;
  private int height;
  private int starveTime;
  private Content[][] oceanContents;


  /**
   *  The following methods are required for Part I.
   */



  /**
   *  Ocean() is a constructor that creates an empty ocean having width i and
   *  height j, in which sharks starve after starveTime timesteps.
   *  @param i is the width of the ocean.
   *  @param j is the height of the ocean.
   *  @param starveTime is the number of timesteps sharks survive without food.
   */

  public Ocean(int i, int j, int starveTime) {
    width = i;
    height = j;
    this.starveTime = starveTime;
    oceanContents = new Content[width][height];
    for(int x=0;x<i;x++) {
      for(int y=0;y<j;y++) {
        oceanContents[x][y] = new Content();
      }
    }
  }

  /**
   *  width() returns the width of an Ocean object.
   *  @return the width of the ocean.
   */

  public int width() {
    return width;
  }

  /**
   *  height() returns the height of an Ocean object.
   *  @return the height of the ocean.
   */

  public int height() {
    return height;
  }

  /**
   *  starveTime() returns the number of timesteps sharks survive without food.
   *  @return the number of timesteps sharks survive without food.
   */

  public int starveTime() {
    return starveTime;
  }

  // return the left life of the shark at cell (x,y)
  private int sharkLife(int x, int y) {
    return this.oceanContents[x][y].sharkLife;
  }

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
   *  addFish() places a fish in cell (x, y) if the cell is empty.  If the
   *  cell is already occupied, leave the cell as it is.
   *  @param x is the x-coordinate of the cell to place a fish in.
   *  @param y is the y-coordinate of the cell to place a fish in.
   */

  public void addFish(int x, int y) {
    if(cellContents(x,y)==EMPTY) {
      oceanContents[xLocation(x)][yLocation(y)].setFish();
    }
  }

  /**
   *  addShark() (with two parameters) places a newborn shark in cell (x, y) if
   *  the cell is empty.  A "newborn" shark is equivalent to a shark that has
   *  just eaten.  If the cell is already occupied, leave the cell as it is.
   *  @param x is the x-coordinate of the cell to place a shark in.
   *  @param y is the y-coordinate of the cell to place a shark in.
   */

  public void addShark(int x, int y) {
    if(cellContents(x,y)==EMPTY) {
      oceanContents[xLocation(x)][yLocation(y)].setShark(starveTime);
    }
  }

  /**
   *  cellContents() returns EMPTY if cell (x, y) is empty, FISH if it contains
   *  a fish, and SHARK if it contains a shark.
   *  @param x is the x-coordinate of the cell whose contents are queried.
   *  @param y is the y-coordinate of the cell whose contents are queried.
   */

  public int cellContents(int x, int y) {
    int content = oceanContents[xLocation(x)][yLocation(y)].species;
    if(content==EMPTY) {
      return EMPTY;
    } else if (content==SHARK) {
      return SHARK;
    } else{
      return FISH;
    }
  }

  //This method return an array which contains the numbers of EMPTY, SHARK, FISH in the neighbor cells
  public int[] neighborContents(int x, int y) {
    int numberOfEmpty = 0;
    int numberOfFish = 0;
    int numberOfShark = 0;
    int[] efsArray = new int[3];
    for(int i=x-1;i<=x+1;i++) {
      for(int j=y-1;j<=y+1;j++) {
        if(i==x&&j==y) {
          continue;
        }
        switch(cellContents(i,j)) {
          case EMPTY:
            numberOfEmpty++;
            break;
          case FISH:
            numberOfFish++;
            break;
          case SHARK:
            numberOfShark++;
            break;
        }
      }
    }
    efsArray[0] = numberOfEmpty;
    efsArray[1] = numberOfShark;
    efsArray[2] = numberOfFish;
    return efsArray;
  }

  // This method return the correct content of a FISH cell in the next step
  public Content FishCellNext(int x, int y) {
    int numberOfNeighborEmpty = neighborContents(x,y)[0];
    int numberOfNeighborShark = neighborContents(x,y)[1];
    int numberOfNeighborFish = neighborContents(x,y)[2];

    Content content = new Content();
    if(numberOfNeighborShark==0) {
      content.setFish();
    } else if(numberOfNeighborShark==1) {
      content.setEmpty();
    } else {
      content.setShark(starveTime);
    }
    return content;
  }

  // This method return the correct content of a SHARK cell in the next step
  public Content SharkCellNext(int x, int y) {
    int numberOfNeighborEmpty = neighborContents(x,y)[0];
    int numberOfNeighborShark = neighborContents(x,y)[1];
    int numberOfNeighborFish = neighborContents(x,y)[2];


    Content content = new Content();
    if(numberOfNeighborFish==0) {
      int life = sharkLife(x,y) - 1;
      if(life==DEATH) {
        content.setEmpty();
      } else {
        content.setShark(life);
      }
    } else{
      content.setShark(starveTime);
    }
    return content;
  }

  // This method return the correct content of a EMPTY cell in the next step
  public Content EmptyCellNext(int x, int y) {
    int numberOfNeighborEmpty = neighborContents(x,y)[0];
    int numberOfNeighborShark = neighborContents(x,y)[1];
    int numberOfNeighborFish = neighborContents(x,y)[2];

    Content content = new Content();
    if(numberOfNeighborFish<2) {
      content.setEmpty();
    } else {
        if(numberOfNeighborShark<=1) {
        content.setFish();
      } else {
        content.setShark(starveTime);
      }
    }
    return content;
  }

  /**
   *  timeStep() performs a simulation timestep as described in README.
   *  @return an ocean representing the elapse of one timestep.
   */

  public Ocean timeStep() {
    Ocean nextOcean = new Ocean(width, height, starveTime);
    for(int x=0;x<width;x++) {
      for(int y=0;y<height;y++) {
        if(cellContents(x,y)==FISH) {
          nextOcean.oceanContents[x][y] = FishCellNext(x,y);
        } else if(cellContents(x,y)==SHARK) {
          nextOcean.oceanContents[x][y] = SharkCellNext(x,y);
        } else if(cellContents(x,y)==EMPTY) {
          nextOcean.oceanContents[x][y] = EmptyCellNext(x,y);
        }
      }
    }
    return nextOcean;
  }

  /**
   *  The following method is required for Part II.
   */

  /**
   *  addShark() (with three parameters) places a shark in cell (x, y) if the
   *  cell is empty.  The shark's hunger is represented by the third parameter.
   *  If the cell is already occupied, leave the cell as it is.  You will need
   *  this method to help convert run-length encodings to Oceans.
   *  @param x is the x-coordinate of the cell to place a shark in.
   *  @param y is the y-coordinate of the cell to place a shark in.
   *  @param feeding is an integer that indicates the shark's hunger.  You may
   *         encode it any way you want; for instance, "feeding" may be the
   *         last timestep the shark was fed, or the amount of time that has
   *         passed since the shark was last fed, or the amount of time left
   *         before the shark will starve.  It's up to you, but be consistent.
   */

  public void addShark(int x, int y, int feeding) {
    if(cellContents(x,y)==EMPTY) {
      oceanContents[xLocation(x)][yLocation(y)].setShark(feeding);
    }
  }

  /**
   *  The following method is required for Part III.
   */

  /**
   *  sharkFeeding() returns an integer that indicates the hunger of the shark
   *  in cell (x, y), using the same "feeding" representation as the parameter
   *  to addShark() described above.  If cell (x, y) does not contain a shark,
   *  then its return value is undefined--that is, anything you want.
   *  Normally, this method should not be called if cell (x, y) does not
   *  contain a shark.  You will need this method to help convert Oceans to
   *  run-length encodings.
   *  @param x is the x-coordinate of the cell whose contents are queried.
   *  @param y is the y-coordinate of the cell whose contents are queried.
   */

  public int sharkFeeding(int x, int y){
    return oceanContents[xLocation(x)][yLocation(y)].sharkLife;
  }

}

// Content Class
class Content{
  int species;
  int sharkLife;

  public Content() {
    species = Ocean.EMPTY;
    sharkLife = Ocean.DEATH;
  }

  public void setEmpty() {
    species = Ocean.EMPTY;
    sharkLife = Ocean.DEATH;
  }

  public void setShark(int life) {
    species = Ocean.SHARK;
    sharkLife = life;
  }

  public void setFish() {
    species = Ocean.FISH;
    sharkLife = Ocean.DEATH;
  }
}