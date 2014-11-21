import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.*;

import java.util.Random;
import java.lang.Math;

/*
 * Fish.java
 * CS480 - PA3
 * Fall 2014
 * 
 * Frances Gail Godinez - fgodinez@bu.edu
 * 
 * Fish object that acts as the 'prey' to the predator class
 * Moves away from predator (with some randomness) and moves towards food
 * if 'F' is pressed and food is present
 * 
 * 
 * */

public class Fish {
	
private GLUT glut; 


//PARTS FOR FISH
private int fish_object, tail_object, body_object; 

//SPEED AND DIRECTION FOR FISH COORDINATES
private float speed_x;
private float dir_x; 

private float speed_y; 
private float dir_y;

private float speed_z; 
private float dir_z;

//ANGLE, SPEED, DIRECTION FOR TAIL
private float tail_angle;
private float tail_speed;
private float tail_direction;

//COORDINATE FOR FISH 
public Coord coord;

//THIS.VIVARIUM, ALLOWS ACCESS TO OTHER CLASSES
public Vivarium viv; 

//COORDINATE OF DESTINATION, AKA FOOD, IF DRAWN
public Coord destCoord;

//COORDINATE OF PREDATOR
public Coord predCoord; 

//FISH CONSTRUCTOR 
public Fish(String name, Coord c, Vivarium v) {

	
	//THIS.VIVARIUM 
	viv = v; 
	
	//INITIAL COORDINATES
	coord = c;
	
	//FISH PARTS
	fish_object = 0;
	body_object = 0; 
	tail_object = 0;
	
	//SPEED AND DIRECTION FOR PREDATOR COORDINATES
	speed_x = 0.01f;
	dir_x = -1;

	speed_y = 0.01f;
	dir_y = -1;
	
	speed_z = 0.01f;
	dir_z = -1;
	
	tail_angle = 0;
	tail_speed = 1.5f; 
	tail_direction = 1; 

	
}


//FISH INITIALIZATION
public void init( GL2 gl ) {
	
	create_body(gl);
	create_tail(gl);

	fish_object = gl.glGenLists(1);
	 
    gl.glNewList(fish_object, GL2.GL_COMPILE );
    create_dList(gl);
    gl.glEndList();
    
}

//CREATES DISPLAY LIST
public void create_dList(GL2 gl) {
	
    gl.glPushMatrix();
    gl.glScalef(0.8f, 0.8f, 1);

    
    //TRANSLATES THE FISH TO MOVE AROUND TANK
    gl.glTranslated(coord.x,coord.y,coord.z);
    
    //TURNS FISH AROUND WHEN IT REACHES END OF TANK
    if (dir_x > 1) {
    	gl.glRotatef(-180,0,1,0);
    	
    }
    
    if (dir_y > 1) {
    	gl.glRotatef(-180,1,0,0);
    }
    
    gl.glCallList( body_object );

    //CREATES FISH TAIL 
    gl.glPushMatrix();
    gl.glRotatef(tail_angle,0,1,0);
    	gl.glCallList( tail_object );
    gl.glPopMatrix();
    
    gl.glPopMatrix();
	
}

//CREATES BODY
public void create_body(GL2 gl){
	
	body_object = gl.glGenLists(1);
	gl.glNewList(body_object, GL2.GL_COMPILE);
	GLUT glut = new GLUT();
    gl.glRotatef(90,0,1,0);
    glut.glutSolidSphere(0.2, 10, 5);
    gl.glEndList();
	
}

//CREATES TAIL 
public void create_tail(GL2 gl){
	
	tail_object = gl.glGenLists(1);
	gl.glNewList( tail_object, GL2.GL_COMPILE);
	GLUT glut = new GLUT();
	gl.glTranslated(0,0,0.1);
	glut.glutSolidCone(.1, .4, 50, 50);
    gl.glEndList();
	
}

//UPDATES FISH MOVEMENT
public void update(GL2 gl) {
	
	move_tail();
	move();
	
    gl.glNewList( fish_object, GL2.GL_COMPILE );
    create_dList( gl ); 
    gl.glEndList();

	
}


//UPDATES TAIL MOVEMENT
public void move_tail() {
	
	tail_angle += tail_speed*tail_direction;

	//SETS LIMIT ROTATIONS FOR TAIL MOVEMENT
	if (tail_angle > 30 || tail_angle <-30) {
		tail_direction = -tail_direction;
	} 

}


//CALCULATES POTENTIAL FUNCTIONS
public Coord potentialF(Coord p, Coord q, double s) {
	
	//EXPRESION USED FOR CALCULATING PARTIAL DERIVATIVES 
	double expr = Math.pow(p.x-q.x,2) + Math.pow(p.y-q.y,2) + Math.pow(p.z-q.z,2);
	
	//SCALAR TO DETERMINE PRIORITY 
	double scalar = s;
	
	//CALCULATES PARTIAL DERIVATIVES
	double partial_x = -2*(p.x - q.x)*Math.exp(-expr);
	double partial_y = -2*(p.y - q.y)*Math.exp(-expr);
	double partial_z = -2*(p.z - q.z)*Math.exp(-expr); 
	
	//CREATES [X,Y,Z] POTENTIALS
	Coord potential = new Coord(scalar*partial_x,scalar*partial_y,scalar*partial_z);
	
	return potential; 
}


//CHECKS IF COLLIDING
public boolean collision(Coord a, Coord b) {
	
	double xDiff = a.x - b.x;
	double yDiff = a.y - b.y;
	double zDiff = a.z - b.z;
	
	if (Math.abs(xDiff) < 0.2 & Math.abs(yDiff) < 0.2 & Math.abs(zDiff) < 0.2) {
		return true;
	}

	return false; 
}


//UPDATES FISH MOVEMENT
public void move()  {
	
	//RANDOM MODULE TO CREATE RANDOM DESTINATION, MAKES FISH MOVE RANDOMLY
	double min = -2;
	double max = 2;
	
	double rand_x = min + Math.random( ) * (max - min);
	double rand_y = min + Math.random( ) * (max - min);
	double rand_z = min + Math.random( ) * (max - min);
	
	//CREATES A RANDOOM DESTINATION FOR FISH TO MOVE TOWARDS
	Coord randDest = new Coord(rand_x,rand_y,rand_z);

	//POTENTIAL FUNCTION RESULT FOR RANDOM DESTINATION
	Coord vec_r = potentialF(coord, randDest, 0.3);

	//SETS PREDATOR COORDINATES
	predCoord = viv.pred.coord; 
	
	//POTENTIAL FUNCTION FOR AVOIDING PREDATOR 
	Coord vecP = potentialF(coord, predCoord,0.7);
	
	//IF FOOD IS DRAWN, DESTINATION COORD IS FOOD 
	double vecF_x = 0.0;
	double vecF_y = 0.0;
	double vecF_z = 0.0;

	if(viv.drawFood) {
		
		//SETS DESTINATION COORDINATE AS FOOD COORDINATE
		destCoord = viv.food.coord;
		Coord vecF = potentialF(coord, destCoord,0.65);
		
		vecF_x = vecF.x;
		vecF_y = vecF.y;
		vecF_z = vecF.z;
		
		//CHECKS IF FISH COLLIDED WITH FOOD
		boolean collided = collision(viv.fish.coord,viv.food.coord);
		
		//CHECKS IF FOOD IS AT TOP
		boolean atTop = coord.isEqual(new Coord(0,1.5,0), viv.food.coord);
		
		//IF FISH COLLIDES WITH FOOD AND FOOD IS NOT AT TOP, FOOD IS "EATEN" 
		if (collided & !atTop) {
			
			//STOPS DRAWING FOOD
			viv.drawFood = false;
			
			//SETS FOOD VECTOR AT 0
			vecF_x = 0.0;
			vecF_y = 0.0;
			vecF_z = 0.0;
			
			//RESETS FOOD COORDINATES AT TOP
			viv.food.coord = new Coord(0,1.5,0);
			
		}
		
	} else {
		
		/*FOR TESTING PURPOSES: 
		 *WHEN 'D' IS PRESS FOOD IS REMOVED 
		 *AND COORDINATE IS RESET*/
	
		vecF_x = 0.0;
		vecF_y = 0.0;
		vecF_z = 0.0;
		
		viv.food.coord = new Coord(0,1.5,0);

	}
	
	//CHECKS FOR COLLISION WITH PREDATOR, IF COLLISION HAPPENS, FISH IS EATEN 
	boolean dead = collision(viv.fish.coord,viv.pred.coord);
	
	//IF THE FISH IS EATEN, SETS EATEN TO TRUE
	if(dead) {
		viv.eaten = true; 
	} 
	
	
	//GRADIENT SUM 
	double sum_x = vec_r.x + vecF_x + (-vecP.x);
	double sum_y = vec_r.y + vecF_y + (-vecP.y);
	double sum_z = vec_r.z + vecF_z + (-vecP.z); 
	
	//CHANGES MOVEMENT DIRECTION BASED ON GRADIENT
	dir_x += sum_x;
	dir_y += sum_y;
	dir_z += sum_z;
	
	//UPDATES X-COORDINATE

	coord.x +=  speed_x*dir_x;
	
	if (coord.x > 2 || coord.x < -2) {
		  dir_x = -dir_x;
	  }
	
	//UPDATES Y-COORDINATE
	coord.y += speed_y*dir_y;

	if (coord.y > 2 || coord.y < -2) {
		  dir_y = -dir_y;
	  }
	
	//UPDATES Z-COORDINATE
	coord.z += speed_z*dir_z;

	if (coord.z > 2 || coord.z < -2) {
		  dir_z = -dir_z;
	  }
	
	System.out.println("FISH DIR.X: "+ dir_x);
	System.out.println("FISH DIR.Y: "+ dir_y);
	System.out.println("FISH DIR.Z: "+ dir_z);
	System.out.println();
}

//DRAWS FISH 
public void draw(GL2 gl) {

    gl.glPushMatrix();    
    gl.glColor3f( 0.85f, 0.55f, 0.20f);
    gl.glCallList( fish_object );
    gl.glPopMatrix();
	
	
}



}