

public class Coord
{
  public double x, y, z;

  public Coord()
  {
    x = y = z = 0.0;
  }

  public Coord( double _x, double _y , double _z)
  {
    x = _x; 
    y = _y;
    z = _z;
  }
  
  public String toString() {
	  return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	  
  }
  
  //CALCULATES A VECTOR FROM A TO B GIVEN TWO POINTS
  public Coord calculateVector(Coord a, Coord b) {
	  double vec_x = a.x - b.x;
	  double vec_y = a.y - b.y;
	  double vec_z = a.z - b.z;
	  
	  Coord vector = new Coord(vec_x, vec_y, vec_z);
	  
	  return vector;
}

	//CHECKS IF TWO COORDINATES ARE EQUAL
	
	public boolean isEqual(Coord a, Coord b) {
		return (a.x == b.x & a.y == b.y & a.z == b.z);
		
	}
	
}
