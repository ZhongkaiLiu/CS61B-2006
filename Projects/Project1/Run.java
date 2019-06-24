public class Run {
  public int species;
  public int length;
  public int sharkLife;

  public Run() {
    species = Ocean.EMPTY;
    length = 1;
    sharkLife = Ocean.DEATH;
  }

  public Run(int t, int l) {
    if(t!=Ocean.SHARK) {
      species = t;
      length = l;
    } else {
      System.out.println("RunClass Error:  A SHARK requires the input of sharkLife!");
      System.exit(1);
    }
  }

  public Run(int t, int l, int life) {
    species = t;
    length = l;
    if(species==Ocean.SHARK) {
      sharkLife = life;
    } else {
      sharkLife = Ocean.DEATH;
    }
  }

}