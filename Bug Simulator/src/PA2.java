/**
 * PA2.java - driver for the bug model simulation
 * 
 * History:
 * 
 *  11 October 2014
 *  
 * - modified to simulate an insect (cockroach)
 * 
 * (Frances Gail Godinez <fgodinez@bu.edu>)
 * 
 * 
 * 19 February 2011
 * 
 * - added documentation
 * 
 * (Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>)
 * 
 * 16 January 2008
 * 
 * - translated from C code by Stan Sclaroff
 *
 * (Tai-Peng Tian <tiantp@gmail.com>)
 * 
 * 
 */


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;//for new version of gl
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;//for new version of gl
import com.jogamp.opengl.util.gl2.GLUT;//for new version of gl

/**
 * The main class which drives the bug model simulation.
 * 
 * @author Tai-Peng Tian <tiantp@gmail.com>
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since Spring 2008
 */
public class PA2 extends JFrame implements GLEventListener, KeyListener,
    MouseListener, MouseMotionListener {

  /**
   * A leg/antenna which has a body joint, a middle joint, and a distal joint.
   * 
   * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
   * @since Spring 2011
   */
	
  
  private class Leg {
	  
    /** The distal joint of this body. */
    private final Component distalJoint;
    
    /** The list of all the joints in this body. */
    private final List<Component> joints;
    
    /** The middle joint of this body. */
    private final Component middleJoint;
    
    /** The body joint of this body. */
    private final Component bodyJoint;


    //INITIATIONS LEGS/LIMBS (INCLUDING ATTENAS - PART OF LEGS CLASS FOR SIMPLIFCATION)
    public Leg(final Component bodyJoint, final Component middleJoint,
        final Component distalJoint) {
      this.bodyJoint = bodyJoint;
      this.middleJoint = middleJoint;
      this.distalJoint = distalJoint;

      this.joints = Collections.unmodifiableList(Arrays.asList(this.bodyJoint,
          this.middleJoint, this.distalJoint));
    }

    //RETURNS DISTAL JOINTS
    Component distalJoint() {
      return this.distalJoint;
    }

    //RETURNS JOINTS
    List<Component> joints() {
      return this.joints;
    }

    //RETURNS MIDDLE JOINT
    Component middleJoint() {
      return this.middleJoint;
    }
    
    //RETURNS BODY JOINT
    Component bodyJoint() {
      return this.bodyJoint;
    }
  }

  /** The color for components which are selected for rotation. */
  public static final FloatColor ACTIVE_COLOR = FloatColor.RED;
  
  
  /** The default width of the created window. */
  public static final int DEFAULT_WINDOW_HEIGHT = 512;
  
  /** The default height of the created window. */
  public static final int DEFAULT_WINDOW_WIDTH = 512;
  
  /** The height of the distal joint on each of the legs. */
  public static final double DISTAL_JOINT_HEIGHT = 0.2;
  
  /** The radius of each joint which comprises the leg. */
  public static final double LEG_RADIUS = 0.04;
 
  /** The radius of the body. */
  public static final double BODY_RADIUS = 0.5;
  
  /** The color for components which are not selected for rotation. */
  public static final FloatColor INACTIVE_COLOR = FloatColor.BROWN;

  /** The initial position of the top level component in the scene. */
  public static final Point3D INITIAL_POSITION = new Point3D(0, 0, 0);
  
  /** The height of the middle joint on each of the legs. */
  public static final double MIDDLE_JOINT_HEIGHT = 0.5;
  
  /** The height of the body joint on each of the legs. */
  public static final double BODY_JOINT_HEIGHT = 0.25;
  
  /** The angle by which to rotate the joint on user request to rotate. */
  public static final double ROTATION_ANGLE = 2.0;
  
  /** Randomly generated serial version UID. */
  private static final long serialVersionUID = -7060944143920496524L;
  
  /**The height of the cockroach's antenna **/
  public static final double ANTENNA_HEIGHT = 1;

  /**
   * Runs the bug simulation in a single JFrame.
   * 
   * @param args
   *          This parameter is ignored.
   */
  public static void main(final String[] args) {
    new PA2().animator.start();
  }

  /**
   * The animator which controls the framerate at which the canvas is animated.
   */
  final FPSAnimator animator;
  
  /** The canvas on which we draw the scene. */
  private final GLCanvas canvas;
  
  /** The capabilities of the canvas. */
  private final GLCapabilities capabilities = new GLCapabilities(null);
  
  /** The legs/antennas on the body to be modeled. */
  private final Leg[] legs;
  
  /** The OpenGL utility object. */
  private final GLU glu = new GLU();
  
  /** The OpenGL utility toolkit object. */
  private final GLUT glut = new GLUT();
  
  /** The body to be modeled. */
  private final Component body;
 
  /** The last x and y coordinates of the mouse press. */
  private int last_x = 0, last_y = 0;
  
  /** Whether the world is being rotated. */
  private boolean rotate_world = false;
  
  /** The axis around which to rotate the selected joints. */
  private Axis selectedAxis = Axis.X;
  
  /** The set of components which are currently selected for rotation. */
  
  private final Set<Component> selectedComponents = new HashSet<Component>(18);
  
  /**
   * The set of legs/atennas which have been selected for rotation.
   * 
   * Selecting a joint will only affect the joints in this set of selected
   * legs/antennas
   **/
  private final Set<Leg> selectedLegs = new HashSet<Leg>(6);
  
  /** Whether the state of the model has been changed. */
  private boolean stateChanged = true;
  
  /**
   * The top level component in the scene which controls the positioning and
   * rotation of everything in the scene.
   */
  private final Component topLevelComponent;
  
  /** The quaternion which controls the rotation of the world. */
  private Quaternion viewing_quaternion = new Quaternion();
  
  /** The set of all components. */
  private final List<Component> components;
  
  public static String ANTENNA1_DIST_NAME = "antenna1 distal";
  public static String ANTENNA2_DIST_NAME = "antenna2 distal";
  
  public static String ANTENNA1_MID_NAME = "antenna1 mid";
  public static String ANTENNA2_MID_NAME = "antenna2 mid";
  
  public static String ANTENNA1_BODY_NAME = "antenna1 body";
  public static String ANTENNA2_BODY_NAME = "antenna2 body";
 
  public static String LEG4_BODY_NAME = "leg4 body";
  public static String LEG4_MIDDLE_NAME = "leg4 middle";
  public static String LEG4_DISTAL_NAME = "leg4 distal";
  
  public static String LEG2_BODY_NAME = "leg2 body";
  public static String LEG2_MIDDLE_NAME = "leg2 middle";
  public static String LEG2_DISTAL_NAME = "leg2 distal";
  
  public static String LEG3_BODY_NAME = "leg3 body";
  public static String LEG3_MIDDLE_NAME = "leg3 middle";
  public static String LEG3_DISTAL_NAME = "leg3 distal";
  
  public static String LEG1_BODY_NAME = "leg1 body";
  public static String LEG1_MIDDLE_NAME = "leg1 middle";
  public static String LEG1_DISTAL_NAME = "leg1 distal";
  
  public static String LEG5_BODY_NAME = "leg5 body";
  public static String LEG5_MIDDLE_NAME = "leg5 middle";
  public static String LEG5_DISTAL_NAME = "leg5 distal";
  
  public static String LEG6_BODY_NAME = "leg6 body";
  public static String LEG6_MIDDLE_NAME = "leg6 middle";
  public static String LEG6_DISTAL_NAME = "leg6 distal";
  
  
  public static String BODY_NAME = "body";
  public static String TOP_LEVEL_NAME = "top level";

  /**
   * Initializes the necessary OpenGL objects and adds a canvas to this JFrame.
   */
  public PA2() {
    this.capabilities.setDoubleBuffered(true);

    this.canvas = new GLCanvas(this.capabilities);
    this.canvas.addGLEventListener(this);
    this.canvas.addMouseListener(this);
    this.canvas.addMouseMotionListener(this);
    this.canvas.addKeyListener(this);
    // this is true by default, but we just add this line to be explicit
    this.canvas.setAutoSwapBufferMode(true);
    this.getContentPane().add(this.canvas);

    // refresh the scene at 60 frames per second
    this.animator = new FPSAnimator(this.canvas, 60);

    this.setTitle("CS480: Bug Simulator");
    this.setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
    
    
    /**ANNTENAS DECLARED BELOW**/
    
    //ANTENNA JOINTS CONNECTED TO THE BODY
    final Component antenna_body1 = new Component(new Point3D(0.7, 0, 0.45),
    		new RoundedCylinder(LEG_RADIUS,
            BODY_JOINT_HEIGHT, this.glut), ANTENNA1_BODY_NAME);
    
    final Component antenna_body2 = new Component(new Point3D(0.7, 0, 0.60),
    		new RoundedCylinder(LEG_RADIUS,
            BODY_JOINT_HEIGHT, this.glut), ANTENNA2_BODY_NAME);
    
    //MIDDLE ANTENNA JOINTS
    final Component antenna_mid1 = new Component(new Point3D(0, 0,
            BODY_JOINT_HEIGHT), new RoundedCylinder(0.025,
            MIDDLE_JOINT_HEIGHT, this.glut), ANTENNA1_MID_NAME);
    
    final Component antenna_mid2 = new Component(new Point3D(0, 0,
            BODY_JOINT_HEIGHT), new RoundedCylinder(0.025,
            MIDDLE_JOINT_HEIGHT, this.glut), ANTENNA2_MID_NAME);
    
    //DISTAL ANTENNA JOINTS
    final Component antenna_distal1 = new Component(new Point3D(0, 0,
            MIDDLE_JOINT_HEIGHT), new RoundedCylinder(0.02,
            ANTENNA_HEIGHT, this.glut), ANTENNA1_DIST_NAME);
    
    final Component antenna_distal2 = new Component(new Point3D(0, 0,
            MIDDLE_JOINT_HEIGHT), new RoundedCylinder(0.02,
           ANTENNA_HEIGHT, this.glut), ANTENNA2_DIST_NAME);
    
    
    /**ALL JOINTS DECLARED BELOW**/
    
    //DISTAL JOINTS
    final Component distal1 = new Component(new Point3D(0, 0,
        -MIDDLE_JOINT_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        -DISTAL_JOINT_HEIGHT, this.glut), LEG1_DISTAL_NAME);
    
    final Component distal2 = new Component(new Point3D(0, 0,
        -MIDDLE_JOINT_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        -DISTAL_JOINT_HEIGHT, this.glut), LEG2_DISTAL_NAME);
    
    final Component distal3 = new Component(new Point3D(0, 0,
        -MIDDLE_JOINT_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        -DISTAL_JOINT_HEIGHT, this.glut), LEG3_DISTAL_NAME);
 
    final Component distal4 = new Component(new Point3D(0, 0,
        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        DISTAL_JOINT_HEIGHT, this.glut), LEG4_DISTAL_NAME);
    
    
    final Component distal5 = new Component(new Point3D(0, 0,
        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        DISTAL_JOINT_HEIGHT, this.glut), LEG5_DISTAL_NAME);
    
    
    final Component distal6 = new Component(new Point3D(0, 0,
            MIDDLE_JOINT_HEIGHT), new RoundedCylinder(LEG_RADIUS,
            DISTAL_JOINT_HEIGHT, this.glut), LEG6_DISTAL_NAME);
   
    

    //MIDDLE JOINTS
    final Component middle1 = new Component(new Point3D(0, 0,
        -BODY_JOINT_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        -MIDDLE_JOINT_HEIGHT, this.glut), LEG1_MIDDLE_NAME);
    
    final Component middle2 = new Component(new Point3D(0, 0,
        -BODY_JOINT_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        -MIDDLE_JOINT_HEIGHT, this.glut), LEG2_MIDDLE_NAME);
    
    final Component middle3 = new Component(new Point3D(0, 0,
        -BODY_JOINT_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        -MIDDLE_JOINT_HEIGHT, this.glut), LEG3_MIDDLE_NAME);
    
    final Component middle4 = new Component(new Point3D(0, 0,
        BODY_JOINT_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        MIDDLE_JOINT_HEIGHT, this.glut), LEG4_MIDDLE_NAME);
        
    
    final Component middle5 = new Component(new Point3D(0, 0,
        BODY_JOINT_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        MIDDLE_JOINT_HEIGHT, this.glut), LEG5_MIDDLE_NAME);

    final Component middle6 = new Component(new Point3D(0, 0,
            BODY_JOINT_HEIGHT), new RoundedCylinder(LEG_RADIUS,
            MIDDLE_JOINT_HEIGHT, this.glut), LEG6_MIDDLE_NAME);
    


    // all the body joints, displaced by various amounts from the body
    
    final Component body1 = new Component(new Point3D(0.3, 0, 0.15),
        new RoundedCylinder(LEG_RADIUS, -BODY_JOINT_HEIGHT, this.glut),
        LEG1_BODY_NAME);
    
    final Component body2 = new Component(new Point3D(0.0, 0, 0.15),
        new RoundedCylinder(LEG_RADIUS, -BODY_JOINT_HEIGHT, this.glut),
        LEG2_BODY_NAME);
    
    final Component body3 = new Component(new Point3D(-0.3, 0, 0.15),
        new RoundedCylinder(LEG_RADIUS, -BODY_JOINT_HEIGHT, this.glut),
        LEG3_BODY_NAME);
    
    
    final Component body4 = new Component(new Point3D(0.3, 0,0.85),
        new RoundedCylinder(LEG_RADIUS, BODY_JOINT_HEIGHT, this.glut),
        LEG4_BODY_NAME);
    

    final Component body5 = new Component(new Point3D(0.0, 0, 0.85),
       new RoundedCylinder(LEG_RADIUS, BODY_JOINT_HEIGHT, this.glut),
        LEG5_BODY_NAME);
   
   
    final Component body6 = new Component(new Point3D(-0.3, 0, 0.85),
    	       new RoundedCylinder(LEG_RADIUS, BODY_JOINT_HEIGHT, this.glut),
    	        LEG6_BODY_NAME);
    	   
    
    //PUT TOGETHER THE LEGS/ANTENNAS FOR EASIER SELECTION BY KEYBOARD INPUT
    this.legs = new Leg[] { 
    		
    	//LEGS	
    	new Leg(body1, middle1, distal1),
        new Leg(body2, middle2, distal2),
        new Leg(body3, middle3, distal3),
        new Leg(body4, middle4, distal4),
        new Leg(body5, middle5, distal5),
        new Leg(body6, middle6, distal6),
        
        //ANTENNAS
        new Leg(antenna_body1, antenna_mid1, antenna_distal1),
        new Leg(antenna_body2, antenna_mid2, antenna_distal2),};

    
    //THE COCKROACH'S BODY
    this.body = new Component(new Point3D(0, 0, 0), new Body(
        BODY_RADIUS, this.glut), BODY_NAME);


    // the top level component which provides an initial position and rotation
    // to the scene (but does not cause anything to be drawn)
    this.topLevelComponent = new Component(INITIAL_POSITION, TOP_LEVEL_NAME);

    //INITIATIONS BODY AS TOP LVL COMPONENT AND ALL LEGS/ANTENNAS AS CHILDREN
    this.topLevelComponent.addChild(this.body);
    this.body.addChildren(body1, body2, body3, body4, body5,body6, antenna_body1, antenna_body2);
    
    //ADDS BODY JOINT TO COCKROACH
    body1.addChild(middle1);
    body2.addChild(middle2);
    body3.addChild(middle3);
    body4.addChild(middle4);
    body5.addChild(middle5);
    body6.addChild(middle6);
    
    //ADDS ATENNAS TO BODY
    antenna_body1.addChild(antenna_mid1);
    antenna_body2.addChild(antenna_mid2);
    
    //ADDS MIDDLE ANNTENA JOINTS
    antenna_mid1.addChild(antenna_distal1);
    antenna_mid2.addChild(antenna_distal2);
    
    //ADDS DISTAL JOINT TO MIDDLE JOINTS
    middle1.addChild(distal1);
    middle2.addChild(distal2);
    middle3.addChild(distal3);
    middle4.addChild(distal4);
    middle5.addChild(distal5);
    middle6.addChild(distal6);

    // ROTATES COCKROACH BODY FOR BETTER VIEWING
    this.topLevelComponent.rotate(Axis.Y, -30);
    this.topLevelComponent.rotate(Axis.X, 30);

    //CALLS "DEFAULT" TEST CASE TO ROTATE JOINTS TO RESEMBLE COCKROACH LEGS
    this.setModelState(this.testCases.def());
    
    //SETS ROTATION LIMITS FOR ANTENNA BODY JOINTS
    for (final Component bodyJoint : Arrays.asList(antenna_body1, antenna_body2)) {
        bodyJoint.setXPositiveExtent(5);
        bodyJoint.setXNegativeExtent(-5);
        
        bodyJoint.setYPositiveExtent(95);
        bodyJoint.setYNegativeExtent(85);
        
        bodyJoint.setZPositiveExtent(0);
        bodyJoint.setZNegativeExtent(0);
      }
     
    //SETS ROTATION LIMITS FOR ANTENNA MID JOINTS
    for (final Component middleJoint : Arrays.asList(antenna_mid1, antenna_mid2)) {
        middleJoint.setXPositiveExtent(90);
        middleJoint.setXNegativeExtent(-30);
        
        middleJoint.setYPositiveExtent(100);
        middleJoint.setYNegativeExtent(-100);
        
        middleJoint.setZPositiveExtent(0);
        middleJoint.setZNegativeExtent(0);
      }
    
    //SETS ROTATION LIMITS FOR ANTENNA DISTAL JOINTS
    for (final Component distalJoint : Arrays.asList(antenna_distal1, antenna_distal2)) {
    	
      distalJoint.setXPositiveExtent(10);
      distalJoint.setXNegativeExtent(-10);
      
      distalJoint.setYPositiveExtent(20);
      distalJoint.setYNegativeExtent(-20);
      
      distalJoint.setZPositiveExtent(0);
      distalJoint.setZNegativeExtent(0);
    }
    
    
    //SETS ROATION LIMITS FOR BODY JOINTS
    for (final Component bodyJoint : Arrays.asList(body1, body2, body3, body4, body5,body6)) {
      bodyJoint.setXPositiveExtent(15);
      bodyJoint.setXNegativeExtent(-15);
      
      bodyJoint.setYPositiveExtent(10);
      bodyJoint.setYNegativeExtent(-10);
      
      bodyJoint.setZPositiveExtent(0);
      bodyJoint.setZNegativeExtent(0);
    }
    
    

    //SETS ROTATION LIMITS FOR MIDDLE JOINTS
    for (final Component middleJoint : Arrays.asList(middle1, middle2,
        middle3, middle4, middle5, middle6)) {
    	
      middleJoint.setXPositiveExtent(20);
      middleJoint.setXNegativeExtent(-20);

      middleJoint.setYPositiveExtent(50);
      middleJoint.setYNegativeExtent(10);
    }
    
    //SETS ROTATION LIMITS FOR FRONT LEGS ONLY
    middle1.setYNegativeExtent(-120);
    middle1.setYPositiveExtent(50);

    middle4.setYNegativeExtent(-50);
    middle4.setYPositiveExtent(120);
    
    //SETS ROTATION LIMITS FOR BACK LEGS LEGS ONLY
    middle3.setYPositiveExtent(90);
    middle6.setYNegativeExtent(-90);
    
    
    //SETS MIDDLE ROTATION LIMITS FOR OTHER LEGS
    for (final Component middleJoint : Arrays.asList(middle5)) {
          middleJoint.setYPositiveExtent(-10);
          middleJoint.setYNegativeExtent(-50);
        }

    //SETS ROTATION LIMITS FOR DISTAL JOINTS
    for (final Component distalJoint : Arrays.asList(distal1, distal2,
        distal3, distal4, distal5, distal6)) {
    	
      distalJoint.setXPositiveExtent(5);
      distalJoint.setXNegativeExtent(-5);
      
      distalJoint.setYPositiveExtent(30);
      distalJoint.setYNegativeExtent(10);
      
      distalJoint.setZPositiveExtent(0);
      distalJoint.setZNegativeExtent(0);
    }
    
    
    //SETS MIDDLE ROTATION LIMITS FOR OTHER LEGS
    for (final Component distalJoint : Arrays.asList(distal4, distal5, distal6)) {
        	
          distalJoint.setYPositiveExtent(-10);
          distalJoint.setYNegativeExtent(-30);
        }

    // create the list of all the components for debugging purposes
    this.components = Arrays.asList(
    	body1, middle1, distal1, 
    	body2, middle2, distal2, 
    	body3, middle3, distal3, 
    	body4, middle4, distal4, 
    	body5, middle5, distal5, 
    	body6, middle6, distal6, 
        antenna_body1, antenna_mid1, antenna_distal1,
        antenna_body2, antenna_mid2, antenna_distal2,
        this.body);
  }

  /**
   * Redisplays the scene containing the body model.
   * 
   * @param drawable
   *          The OpenGL drawable object with which to create OpenGL models.
   */
  public void display(final GLAutoDrawable drawable) {
    final GL2 gl = (GL2)drawable.getGL();

    // clear the display
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    // from here on affect the model view
    gl.glMatrixMode(GL2.GL_MODELVIEW);

    // start with the identity matrix initially
    gl.glLoadIdentity();

    // rotate the world by the appropriate rotation quaternion
    gl.glMultMatrixf(this.viewing_quaternion.toMatrix(), 0);

    // update the position of the components which need to be updated
    // TODO only need to update the selected and JUST deselected components
    if (this.stateChanged) {
      this.topLevelComponent.update(gl);
      this.stateChanged = false;
    }

    // redraw the components
    this.topLevelComponent.draw(gl);
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param drawable
   *          This parameter is ignored.
   * @param modeChanged
   *          This parameter is ignored.
   * @param deviceChanged
   *          This parameter is ignored.
   */
  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged) {
    // intentionally unimplemented
  }

  /**
   * Initializes the scene and model.
   * 
   * @param drawable
   *          {@inheritDoc}
   */
  public void init(final GLAutoDrawable drawable) {
    final GL2 gl = (GL2)drawable.getGL();

    // perform any initialization needed by the bug model
    this.topLevelComponent.initialize(gl);

    // initially draw the scene
    this.topLevelComponent.update(gl);
 
    // set up for shaded display of the bug
    final float light0_position[] = { 1, 1, 1, 0 };
    final float light0_ambient_color[] = { 0.25f, 0.25f, 0.25f, 1 };
    final float light0_diffuse_color[] = { 1, 1, 1, 1 };


    gl.glPolygonMode(GL.GL_FRONT, GL2.GL_FILL);
    gl.glEnable(GL2.GL_COLOR_MATERIAL);
    gl.glColorMaterial(GL.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glShadeModel(GL2.GL_SMOOTH);

    // set up the light source
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light0_position, 0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light0_ambient_color, 0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light0_diffuse_color, 0);

    // turn lighting and depth buffering on
    gl.glEnable(GL2.GL_LIGHTING);
    gl.glEnable(GL2.GL_LIGHT0);
    gl.glEnable(GL2.GL_DEPTH_TEST);
    gl.glEnable(GL2.GL_NORMALIZE);

  }

  /**
   * Interprets key presses according to the following scheme:
   * 
   * up-arrow, down-arrow: increase/decrease rotation angle
   * 
   * @param key
   *          The key press event object.
   */
  public void keyPressed(final KeyEvent key) {
    switch (key.getKeyCode()) {
    case KeyEvent.VK_KP_UP:
    case KeyEvent.VK_UP:
      for (final Component component : this.selectedComponents) {
        component.rotate(this.selectedAxis, ROTATION_ANGLE);
      }
      this.stateChanged = true;
      break;
    case KeyEvent.VK_KP_DOWN:
    case KeyEvent.VK_DOWN:
      for (final Component component : this.selectedComponents) {
        component.rotate(this.selectedAxis, -ROTATION_ANGLE);
      }
      this.stateChanged = true;
      break;
    default:
      break;
    }
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param key
   *          This parameter is ignored.
   */
  public void keyReleased(final KeyEvent key) {
    // intentionally unimplemented
  }

  private final TestCases testCases = new TestCases();

  private void setModelState(final Map<String, Angled> state) {
	//SET MODEL STATES IN TEST CASES
	  
    this.body.setAngles(state.get(BODY_NAME));
    this.legs[0].bodyJoint().setAngles(state.get(LEG1_BODY_NAME));
    this.legs[0].middleJoint().setAngles(state.get(LEG1_MIDDLE_NAME));
    this.legs[0].distalJoint().setAngles(state.get(LEG1_DISTAL_NAME));
    
    this.legs[1].bodyJoint().setAngles(state.get(LEG2_BODY_NAME));
    this.legs[1].middleJoint().setAngles(state.get(LEG2_MIDDLE_NAME));
    this.legs[1].distalJoint().setAngles(state.get(LEG2_DISTAL_NAME));
    
    this.legs[2].bodyJoint().setAngles(state.get(LEG3_BODY_NAME));
    this.legs[2].middleJoint().setAngles(state.get(LEG3_MIDDLE_NAME));
    this.legs[2].distalJoint().setAngles(state.get(LEG3_DISTAL_NAME));
    
    this.legs[3].bodyJoint().setAngles(state.get(LEG4_BODY_NAME));
    this.legs[3].middleJoint().setAngles(state.get(LEG4_MIDDLE_NAME));
    this.legs[3].distalJoint().setAngles(state.get(LEG4_DISTAL_NAME));
    
    this.legs[4].bodyJoint().setAngles(state.get(LEG5_BODY_NAME));
    this.legs[4].middleJoint().setAngles(state.get(LEG5_MIDDLE_NAME));
    this.legs[4].distalJoint().setAngles(state.get(LEG5_DISTAL_NAME));
    
    this.legs[5].bodyJoint().setAngles(state.get(LEG6_BODY_NAME));
    this.legs[5].middleJoint().setAngles(state.get(LEG6_MIDDLE_NAME));
    this.legs[5].distalJoint().setAngles(state.get(LEG6_DISTAL_NAME));
    
    this.legs[6].bodyJoint().setAngles(state.get(ANTENNA1_BODY_NAME));
    this.legs[6].middleJoint().setAngles(state.get(ANTENNA1_MID_NAME));
    this.legs[6].distalJoint().setAngles(state.get(ANTENNA1_DIST_NAME)); 
    
    this.legs[7].bodyJoint().setAngles(state.get(ANTENNA2_BODY_NAME));
    this.legs[7].middleJoint().setAngles(state.get(ANTENNA2_MID_NAME));
    this.legs[7].distalJoint().setAngles(state.get(ANTENNA2_DIST_NAME)); 
    
    this.stateChanged = true;
  }

  /**
   * Interprets typed keys according to the following scheme:
   * 
   * 1-6: toggle the (1st-6th) leg active in rotation
   * 
   * 7-8: toggle antenna(s) active in rotation
   * 
   * X : use the X axis rotation at the active joint(s)
   * 
   * Y : use the Y axis rotation at the active joint(s)
   * 
   * Z : use the Z axis rotation at the active joint(s)
   * 
   * C : resets the body to default position
   * 
   * B : select joint that connects leg/antenna to body
   * 
   * M : select middle joint
   * 
   * D : select last (distal) joint
   * 
   * R : resets the view to the initial rotation
   * 
   * K : prints the angles of the six legs/two antennas for debugging purposes
   * 
   * Q, Esc : exits the program
   * 
   */
  public void keyTyped(final KeyEvent key) {
    switch (key.getKeyChar()) {
    case 'Q':
    case 'q':
    case KeyEvent.VK_ESCAPE:
      new Thread() {
        @Override
        public void run() {
          PA2.this.animator.stop();
        }
      }.start();
      System.exit(0);
      break;

    // print the angles of the components
    case 'K':
    case 'k':
      printJoints();
      break;

    //RESETS TO DEFAULT 
    case 'C':
    case 'c':
      this.setModelState(this.testCases.def());
      break;

    // set the state of the body to the next test case
    case 'T':
    case 't':
      this.setModelState(this.testCases.next());
      break;

    // set the viewing quaternion to 0 rotation
    case 'R':
    case 'r':
      this.viewing_quaternion.reset();
      break;

    // Toggle which legs/antennas(s) are affected by the current rotation
    case '1':
      toggleSelection(this.legs[0]);
      break;
    case '2':
      toggleSelection(this.legs[1]);
      break;
    case '3':
      toggleSelection(this.legs[2]);
      break;
    case '4':
      toggleSelection(this.legs[3]);
      break;
    case '5':
      toggleSelection(this.legs[4]);
      break;
    case '6':
        toggleSelection(this.legs[5]);
        break;
    case '7':
        toggleSelection(this.legs[6]);
        break;
    case '8':
        toggleSelection(this.legs[7]);
        break;
        
    // toggle which joints are affected by the current rotation
    case 'D':
    case 'd':
      for (final Leg leg : this.selectedLegs) {
        toggleSelection(leg.distalJoint());
      }
      break;
    case 'M':
    case 'm':
      for (final Leg leg : this.selectedLegs) {
        toggleSelection(leg.middleJoint());
      }
      break;
    case 'B':
    case 'b':
      for (final Leg leg : this.selectedLegs) {
        toggleSelection(leg.bodyJoint());
      }
      break;



    // change the axis of rotation at current active joint
    case 'X':
    case 'x':
      this.selectedAxis = Axis.X;
      break;
    case 'Y':
    case 'y':
      this.selectedAxis = Axis.Y;
      break;
    case 'Z':
    case 'z':
      this.selectedAxis = Axis.Z;
      break;
    default:
      break;
    }
  }

  /**
   * Prints the joints on the System.out print stream.
   */
  private void printJoints() {
    this.printJoints(System.out);
  }

  /**
   * Prints the joints on the specified PrintStream.
   * 
   * @param printStream
   *          The stream on which to print each of the components.
   */
  private void printJoints(final PrintStream printStream) {
    for (final Component component : this.components) {
      printStream.println(component);
    }
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseClicked(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * Updates the rotation quaternion as the mouse is dragged.
   * 
   * @param mouse
   *          The mouse drag event object.
   */
  public void mouseDragged(final MouseEvent mouse) {
	if (this.rotate_world) {
		// get the current position of the mouse
		final int x = mouse.getX();
		final int y = mouse.getY();
	
		// get the change in position from the previous one
		final int dx = x - this.last_x;
		final int dy = y - this.last_y;
	
		// create a unit vector in the direction of the vector (dy, dx, 0)
		final double magnitude = Math.sqrt(dx * dx + dy * dy);
		final float[] axis = magnitude == 0 ? new float[]{1,0,0}: // avoid dividing by 0
			new float[] { (float) (dy / magnitude),(float) (dx / magnitude), 0 };
	
		// calculate appropriate quaternion
		final float viewing_delta = 3.1415927f / 180.0f;
		final float s = (float) Math.sin(0.5f * viewing_delta);
		final float c = (float) Math.cos(0.5f * viewing_delta);
		final Quaternion Q = new Quaternion(c, s * axis[0], s * axis[1], s
				* axis[2]);
		this.viewing_quaternion = Q.multiply(this.viewing_quaternion);
	
		// normalize to counteract accumulating round-off error
		this.viewing_quaternion.normalize();
	
		// save x, y as last x, y
		this.last_x = x;
		this.last_y = y;
	}
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseEntered(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseExited(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseMoved(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * Starts rotating the world if the left mouse button was released.
   * 
   * @param mouse
   *          The mouse press event object.
   */
  public void mousePressed(final MouseEvent mouse) {
    if (mouse.getButton() == MouseEvent.BUTTON1) {
      this.last_x = mouse.getX();
      this.last_y = mouse.getY();
      this.rotate_world = true;
    }
  }

  /**
   * Stops rotating the world if the left mouse button was released.
   * 
   * @param mouse
   *          The mouse release event object.
   */
  public void mouseReleased(final MouseEvent mouse) {
    if (mouse.getButton() == MouseEvent.BUTTON1) {
      this.rotate_world = false;
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @param drawable
   *          {@inheritDoc}
   * @param x
   *          {@inheritDoc}
   * @param y
   *          {@inheritDoc}
   * @param width
   *          {@inheritDoc}
   * @param height
   *          {@inheritDoc}
   */
  public void reshape(final GLAutoDrawable drawable, final int x, final int y,
      final int width, final int height) {
    final GL2 gl = (GL2)drawable.getGL();

    // prevent division by zero by ensuring window has height 1 at least
    final int newHeight = Math.max(1, height);

    // compute the aspect ratio
    final double ratio = (double) width / newHeight;

    // reset the projection coordinate system before modifying it
    gl.glMatrixMode(GL2.GL_PROJECTION);
    gl.glLoadIdentity();

    // set the viewport to be the entire window
    gl.glViewport(0, 0, width, newHeight);

    // set the clipping volume
    this.glu.gluPerspective(25, ratio, 0.1, 100);

    // camera positioned at (0,0,6), look at point (0,0,0), up vector (0,1,0)
    this.glu.gluLookAt(0, 0, 12, 0, 0, 0, 0, 1, 0);

    // switch back to model coordinate system
    gl.glMatrixMode(GL2.GL_MODELVIEW);
  }

  private void toggleSelection(final Component component) {
    if (this.selectedComponents.contains(component)) {
      this.selectedComponents.remove(component);
      component.setColor(INACTIVE_COLOR);
    } else {
      this.selectedComponents.add(component);
      component.setColor(ACTIVE_COLOR);
    }
    this.stateChanged = true;
  }

  private void toggleSelection(final Leg leg) {
    if (this.selectedLegs.contains(leg)) {
      this.selectedLegs.remove(leg);
      this.selectedComponents.removeAll(leg.joints());
      for (final Component joint : leg.joints()) {
        joint.setColor(INACTIVE_COLOR);
      }
    } else {
      this.selectedLegs.add(leg);
    }
    this.stateChanged = true;
  }

@Override
public void dispose(GLAutoDrawable drawable) {
	// TODO Auto-generated method stub
	
}
}
