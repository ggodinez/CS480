import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.*;

import java.lang.Math;
import java.util.Random;


/* Predator.java
 * CS480 - PA3
 * Fall 2014
 * 
 * Frances Gail Godinez - fgodinez@bu.edu
 * 
 * Predator object that acts as the predator to the fish class
 * Moves towards the fish to "eat" it, when eaten it moves randomly
 * until fish is respawned 
 * */


public class Predator {
	
private GLUT glut; 


//PARTS FOR PREDATOR
private int predator_object, tail_object, body_object, fin_object;

//SPEED AND DIRECTION FOR PREDATOR COORDINATES
private float speed_x;
private float dir_x; 

private float speed_y; 
private float dir_y;

private float speed_z; 
private float dir_z;


//ANGLE, SPEED, AND DIRECTION FOR TAIL 
private float tail_angle;
private float tail_speed;
private float tail_direction;

//COORDINATE FOR PREDATOR 
public Coord coord;

//THIS.VIVARIUM, ALLOWS ACCESS TO OTHER CLASSES
public Vivarium viv;

//COORDINATE OF PREDATOR'S DESTINATION, AKA THE PREY
public Coord destCoord;  

//FISH CONSTRUCTOR 
public Predator(String name, Coord c, Vivarium v) {
	
	//THIS.VIVARIUM 
	viv = v; 
	
	//SETS DESTINATION COORDINATE AS FISH COORDINATES
	destCoord = viv.fish.coord;
	
	//INITIAL COORDINATES
	coord = c;
	
	//PREDATOR PARTS
	predator_object = 0;
	body_object = 0; 
	tail_object = 0;
	fin_object = 0;

	//SPEED AND DIRECTION FOR PREDATOR COORDINATES
	speed_x = 0.01f;
	dir_x = -1;

	speed_y = 0.01f;
	dir_y = -1;
	
	speed_z = 0.01f;
	dir_z = -1;
	
	//AMGLE, SPEED, AND DIRECTION FOR TAIL 
	tail_angle = 0;
	tail_speed = 1.5f; 
	tail_direction = 1; 
	
}


//FISH INITIALIZATION
public void init( GL2 gl ) {
	
	create_body(gl);
	create_tail(gl);
	create_fin(gl);

	predator_object = gl.glGenLists(1);
    gl.glNewList(predator_object, GL2.GL_COMPILE );
    create_dList(gl);

    gl.glEndList();
}

//CREATES DISPLAY LIST
public void create_dList(GL2 gl) {

    gl.glPushMatrix();
    gl.glScalef(0.8f, 0.8f, 1);    
    
    //TRANSLATES PREDATOR TO MOVE AROUND TANK
    gl.glTranslated(coord.x,coord.y,coord.z);
    
    //TURNS FISH AROUND WHEN IT REACHES END OF TANK
    if (dir_x > 1) {
    	gl.glRotatef(-180,0,1,0);
    }
    
    //CREATES BODY
    gl.glCallList( body_object );
    
    //CREATES FIN
    gl.glPushMatrix();
    gl.glCallList(fin_object);
    gl.glPopMatrix();

    //CREATES FISH TAIL 
    gl.glPushMatrix();
    gl.glRotatef(tail_angle,0,1,0);
    	gl.glCallList( tail_object );
    gl.glPopMatrix();
    
    gl.glPopMatrix();
	
}


//CREATES FIN
public void create_fin(GL2 gl){
	
	fin_object = gl.glGenLists(1);
	gl.glNewList( fin_object, GL2.GL_COMPILE);
	
	GLUT glut = new GLUT();
	
    gl.glRotatef(-90,1,0,0);
	glut.glutSolidCone(.1, .4, 50, 50);
	
    gl.glEndList();
	
}



//CREATES BODY
public void create_body(GL2 gl){
	
	body_object = gl.glGenLists(1);
	gl.glNewList(body_object, GL2.GL_COMPILE);
	
	GLUT glut = new GLUT();
	
    gl.glScalef(1.2f, 1f, 1);
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


//UPDATES MOVEMENT
public void update(GL2 gl) {
	
	move_tail();
	move();
	
    gl.glNewList( predator_object, GL2.GL_COMPILE );
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

//UPDATES PREDATOR MOVEMENT
public void move()  {
	
	
	//RANDOMNESS TO CREATE RANDOM DESTINATION, MAKES PREDATOR MOVE RANDOMLY
	
	double min = -2;
	double max = 2;
	
	double rand_x = min + Math.random( ) * (max - min);
	double rand_y = min + Math.random( ) * (max - min);
	double rand_z = min + Math.random( ) * (max - min);
	
	//VECTORS FOR RANDOMNESS
	double vecR_x = 0.0;
	double vecR_y = 0.0;
	double vecR_z = 0.0;
	
	//CALCULATES POTENTIAL FUNCTION FOR CHASING PREY
	Coord vec = potentialF(coord, destCoord, 0.4);
	
	double vecF_x = vec.x;
	double vecF_y = vec.y;
	double vecF_z = vec.z;

	//IF FISH IS EATEN, MOVE RANDOMLY 
	if (viv.eaten) {
		
		//FIND RANDOM DESTINATION WHEN PREY IS EATEN 
		Coord randDest = new Coord(rand_x,rand_y,rand_z);
		
		//VECTOR POTENTIALS FOR PREY IS SET TO ZERO 
		vecF_x = 0.0;
		vecF_y = 0.0;
		vecF_z = 0.0;
		
		
		//CALCLUATES POTENTIAL FUNCTION OF RANDOM DESTINATION
		Coord vecR = potentialF(coord, randDest, 0.2);
		
		vecR_x = vecR.x;
		vecR_y = vecR.y;
		vecR_z = vecR.z;
	}
	
	//GRADIENT SUM 
	double sum_x = vecF_x + vecR_x;
	double sum_y = vecF_y + vecR_y;
	double sum_z = vecF_z + vecR_z; 
	
	
	//CHANGES MOVEMENT DIRECTION BASED ON GRADIENT
	dir_x += sum_x;
	dir_y += sum_y;
	dir_z += sum_z;
	
	
	//UPDATES X-COORDINATE
	coord.x += speed_x*dir_x;
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

}

//DRAWS PREDATOR
public void draw(GL2 gl) {

    gl.glPushMatrix();
    gl.glColor3f( 1f, 0, 0);
    gl.glCallList( predator_object );
    gl.glPopMatrix();
    }

}