import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.*;

public class Food {
	
	
	public Coord coord;
	
	private int cube;
	
	private int object;
	
	private float speed;
	
	
	
	public Food(String name, Coord c) {
		

		coord = c;
		
		speed = 0.01f;
		
		cube = 0; 
		
		object = 0; 
		
	}
	
	public void init(GL2 gl) {
		
		cube = gl.glGenLists(1);
		gl.glNewList( cube, GL2.GL_COMPILE);
		
		GLUT glut = new GLUT();

	    gl.glTranslated(coord.x,coord.y,coord.z);
		glut.glutSolidCube(0.1f);
	
	    gl.glEndList();
		
	}
	

	public void update(GL2 gl) {
        
		//WHEN CUBE REACHES BOTTOM, IT STOPS
		if (coord.y > -1.9) {
	    coord.y -= speed;
		}
		
	    gl.glNewList( cube, GL2.GL_COMPILE);
		
		GLUT glut = new GLUT();

	    gl.glTranslated(coord.x,coord.y,coord.z);
		glut.glutSolidCube(0.1f);
	
	    gl.glEndList();
	    
	
	    
	    
	}
	
	
	public void drawCube(GL2 gl) {
		
	}
	
	
	

	
	public void draw(GL2 gl) {
	    gl.glPushMatrix();
	    
	    gl.glColor3f(0.55f, 0.55f, 0f);
	    
	    gl.glCallList( cube );

	    gl.glPopMatrix();
		
	}
	
	
}