

import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import java.util.*;

/* Vivarium.java
 * CS480 - PA3 
 * Fall 2014
 * 
 * Frances Gail Godinez - fgodinez@bu.edu
 * 
 * Vivarium class that contains a Fish object (prey) and Predator object.
 * The predator chases the fish while the fish avoids the predator.
 * 
 * KEYS:
 * 'R' - Resets viewing quaternion
 * 'F' - Drops food
 * 'S' - Spawns fish after being "eaten" by predator
 * 
 * DEBUGGING KEYS:
 * 'D' - Removes food
 * 'T' - Removes fish (as if eaten)
 * 
 * KNOWN BUGS:
 * -- Fish and predator occasionally move rapidly when in close proximity or left to run
 *    without collision
 *    
 * -- Fish will not always pursue food when food is present (higher priority 
 *    is given to avoiding preadtor)
 *    
 * -- Occasionally will give a "NullPointerException" at random and not run 
 *    
 * -- Minor bug: Tank changes to darker colour when food is called
 * 
 * */

public class Vivarium
{
  private Tank tank;
  
  
  //CREATES FISH 
  public Fish fish;
  
  //CREATES PREDATOR
  public Predator pred;
  
  //CREATES FOOD
  public Food food;
  
  //BOOLEAN TO INDICATE FOOD SHOULD BE DRAWN
  public boolean drawFood;
  
  //BOOLEAN TO INDICATE PREY IS EATEN
  public boolean eaten;

  
  public Vivarium()
  
  {

	Coord fishCoord = new Coord(1,2,1); //FISH COORDINATES
	Coord predCoord = new Coord(2,0,2); //PREDATOR COORDINATES
	Coord foodCoord = new Coord(0,1.5,0); //FOOD COORDINATE AT TOP OF TANK
	
	//DRAW FOOD BOOLEAN IS FALSE UNTIL 'F' IS PRESSED
	drawFood = false;
	
	//EATEN FOR PREY IS FALSE UNTIL COLLISION IS DETECTED
	eaten = false; 
	
    tank = new Tank( 4.0f, 4.0f, 4.0f );

    //FISH, PREDATOR, AND FOOD CONSTRUCTOR 
    fish = new Fish("fish", fishCoord,this);
    pred = new Predator("predator", predCoord, this);
    food = new Food("food", foodCoord);
    
    
    
  }
  
  //KEEPS TRACK OF VIVARIUM OBJECTS
  public void VivariumObjects () {
	  
  }


  public void init( GL2 gl )
  {
    tank.init( gl );
    
    fish.init(gl);

    pred.init(gl);
    
    food.init(gl);
    

    
  }

  public void update( GL2 gl )
  {
    tank.update( gl );
    
    //FISH IS ONLY UPDATED IF NOT EATEN
    if (!eaten) {
    	fish.update(gl);
    }
    
    pred.update(gl);
    
    //DROPS FOOD ONCE DRAWN
    if(drawFood) {
    	food.update(gl);
    }

  }

  public void draw( GL2 gl )
  {
    tank.draw( gl );
    
    //IF THE FISH ISN'T EATEN, IT WILL BE DRAWN
    if(!eaten) {
    	fish.draw(gl);
    }
    
    pred.draw(gl);
    
    //IF 'F' IS PRESSED, FOOD IS DRAWN
    if (drawFood) {
    	food.draw(gl);
    }
    

  }
}
