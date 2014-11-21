/**
 * 
 */


import java.util.HashMap;
import java.util.Map;

/**
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since Spring 2011
 */
public class TestCases extends CyclicIterator<Map<String, Angled>> {

  
  Map<String, Angled> def(){
	  return this.def;
  }
  
  
  private final Map<String, Angled> def;

  @SuppressWarnings("unchecked")
  TestCases() {
	  
    this.def = new HashMap<String,Angled>();
    final Map<String, Angled> eating = new HashMap<String, Angled>();
    final Map<String, Angled> running = new HashMap<String, Angled>();
    final Map<String, Angled> dead = new HashMap<String, Angled>();
    final Map<String, Angled> spider = new HashMap<String, Angled>();
    final Map<String, Angled> spread = new HashMap<String, Angled>();

    super.add(def, eating, running, dead, spider, spread);

    /***TEST CASES***/
    
    //BODY ANGLE DOES NOT CHANGE THROUGH ANY OF THE TEST CASES 
    def.put(PA2.BODY_NAME, new BaseAngled(0, 0, 0));
    eating.put(PA2.BODY_NAME, new BaseAngled(0, 0, 0));
    running.put(PA2.BODY_NAME, new BaseAngled(0, 0, 0));
    dead.put(PA2.BODY_NAME, new BaseAngled(0, 0, 0));
    spider.put(PA2.BODY_NAME, new BaseAngled(0, 0, 0));
    spread.put(PA2.BODY_NAME, new BaseAngled(0, 0, 0));
    
    //DEFAULT SET ANGLES FOR COCKROACH LEGS AND ANTENNAS
    def.put(PA2.LEG1_DISTAL_NAME, new BaseAngled(0, 40, 0));
    def.put(PA2.LEG1_MIDDLE_NAME, new BaseAngled(0, 20, 0));
    def.put(PA2.LEG1_BODY_NAME, new BaseAngled(0, 0, 0));
    
    def.put(PA2.LEG2_DISTAL_NAME, new BaseAngled(0, 40, 0));
    def.put(PA2.LEG2_MIDDLE_NAME, new BaseAngled(0, 20, 0));
    def.put(PA2.LEG2_BODY_NAME, new BaseAngled(0, 0, 0));
    
    def.put(PA2.LEG3_DISTAL_NAME, new BaseAngled(0, 40, 0));
    def.put(PA2.LEG3_MIDDLE_NAME, new BaseAngled(0, 20, 0));
    def.put(PA2.LEG3_BODY_NAME, new BaseAngled(0, 0, 0));
    
    def.put(PA2.LEG4_DISTAL_NAME, new BaseAngled(0, -40, 0));
    def.put(PA2.LEG4_MIDDLE_NAME, new BaseAngled(0, -20, 0));
    def.put(PA2.LEG4_BODY_NAME, new BaseAngled(0, 0, 0));
    
    def.put(PA2.LEG5_DISTAL_NAME, new BaseAngled(0, -40, 0));
    def.put(PA2.LEG5_MIDDLE_NAME, new BaseAngled(0, -20, 0));
    def.put(PA2.LEG5_BODY_NAME, new BaseAngled(0, 0, 0));
    
    def.put(PA2.LEG6_DISTAL_NAME, new BaseAngled(0, -40, 0));
    def.put(PA2.LEG6_MIDDLE_NAME, new BaseAngled(0, -20, 0));
    def.put(PA2.LEG6_BODY_NAME, new BaseAngled(0, 0, 0));
    
    def.put(PA2.ANTENNA1_DIST_NAME, new BaseAngled(0, 0, 0));
    def.put(PA2.ANTENNA1_MID_NAME, new BaseAngled(0, 60, 0));
    def.put(PA2.ANTENNA1_BODY_NAME, new BaseAngled(0, 90, 0));
    
    def.put(PA2.ANTENNA2_DIST_NAME, new BaseAngled(0, 0, 0));
    def.put(PA2.ANTENNA2_MID_NAME, new BaseAngled(0, -60, 0));
    def.put(PA2.ANTENNA2_BODY_NAME, new BaseAngled(0, 90, 0));
   

    //"EATING" TEST CASE - FIRST POSE
    eating.put(PA2.LEG1_DISTAL_NAME, new BaseAngled(-20, 40, 0));
    eating.put(PA2.LEG1_MIDDLE_NAME, new BaseAngled(0, -120, 0));
    eating.put(PA2.LEG1_BODY_NAME, new BaseAngled(0, 0, 0));
    
    eating.put(PA2.LEG2_DISTAL_NAME, new BaseAngled(0, 40, 0));
    eating.put(PA2.LEG2_MIDDLE_NAME, new BaseAngled(0, 20, 0));
    eating.put(PA2.LEG2_BODY_NAME, new BaseAngled(0, 0, 0));
    
    eating.put(PA2.LEG3_DISTAL_NAME, new BaseAngled(0, 40, 0));
    eating.put(PA2.LEG3_MIDDLE_NAME, new BaseAngled(0, 20, 0));
    eating.put(PA2.LEG3_BODY_NAME, new BaseAngled(0, 0, 0));
    
    eating.put(PA2.LEG4_DISTAL_NAME, new BaseAngled(20, -40, 0));
    eating.put(PA2.LEG4_MIDDLE_NAME, new BaseAngled(0, 120, 0));
    eating.put(PA2.LEG4_BODY_NAME, new BaseAngled(0, 0, 0));
    
    eating.put(PA2.LEG5_DISTAL_NAME, new BaseAngled(0, -40, 0));
    eating.put(PA2.LEG5_MIDDLE_NAME, new BaseAngled(0, -20, 0));
    eating.put(PA2.LEG5_BODY_NAME, new BaseAngled(0, 0, 0));
    
    eating.put(PA2.LEG6_DISTAL_NAME, new BaseAngled(0, -40, 0));
    eating.put(PA2.LEG6_MIDDLE_NAME, new BaseAngled(0, -20, 0));
    eating.put(PA2.LEG6_BODY_NAME, new BaseAngled(0, 0, 0));
    
    
    eating.put(PA2.ANTENNA1_DIST_NAME, new BaseAngled(0, 0, 0));
    eating.put(PA2.ANTENNA1_MID_NAME, new BaseAngled(0, 60, 0));
    eating.put(PA2.ANTENNA1_BODY_NAME, new BaseAngled(0, 90, 0));

    eating.put(PA2.ANTENNA2_DIST_NAME, new BaseAngled(0, 0, 0));
    eating.put(PA2.ANTENNA2_MID_NAME, new BaseAngled(0, -60, 0));
    eating.put(PA2.ANTENNA2_BODY_NAME, new BaseAngled(0, 90, 0));

    //"RUNNING" TEST CASE - SECOND POSE
    running.put(PA2.LEG1_DISTAL_NAME, new BaseAngled(0, 40, 0));
    running.put(PA2.LEG1_MIDDLE_NAME, new BaseAngled(0, 20, 0));
    running.put(PA2.LEG1_BODY_NAME, new BaseAngled(0, 0, 0));
    
    running.put(PA2.LEG2_DISTAL_NAME, new BaseAngled(0, 40, 0));
    running.put(PA2.LEG2_MIDDLE_NAME, new BaseAngled(0, 20, 0));
    running.put(PA2.LEG2_BODY_NAME, new BaseAngled(0, 0, 0));
   
    running.put(PA2.LEG3_DISTAL_NAME, new BaseAngled(0, 40, 0));
    running.put(PA2.LEG3_MIDDLE_NAME, new BaseAngled(0, 90, 0));
    running.put(PA2.LEG3_BODY_NAME, new BaseAngled(0, 0, 0));
    
    running.put(PA2.LEG4_DISTAL_NAME, new BaseAngled(0, -40, 0));
    running.put(PA2.LEG4_MIDDLE_NAME, new BaseAngled(0, -20, 0));
    running.put(PA2.LEG4_BODY_NAME, new BaseAngled(0, 0, 0));
    
    running.put(PA2.LEG5_DISTAL_NAME, new BaseAngled(0, -40, 0));
    running.put(PA2.LEG5_MIDDLE_NAME, new BaseAngled(0, -20, 0));
    running.put(PA2.LEG5_BODY_NAME, new BaseAngled(0, 0, 0));
    
    running.put(PA2.LEG6_DISTAL_NAME, new BaseAngled(0, -40, 0));
    running.put(PA2.LEG6_MIDDLE_NAME, new BaseAngled(0, -90, 0));
    running.put(PA2.LEG6_BODY_NAME, new BaseAngled(0, 0, 0));
    
    running.put(PA2.ANTENNA1_DIST_NAME, new BaseAngled(0, 0, 0));
    running.put(PA2.ANTENNA1_MID_NAME, new BaseAngled(0, 60, 0));
    running.put(PA2.ANTENNA1_BODY_NAME, new BaseAngled(0, 90, 0));
    
    running.put(PA2.ANTENNA2_DIST_NAME, new BaseAngled(0, 0, 0));
    running.put(PA2.ANTENNA2_MID_NAME, new BaseAngled(0, -60, 0));
    running.put(PA2.ANTENNA2_BODY_NAME, new BaseAngled(0, 90, 0));

    //"DEAD" TEST CASE - THIRD POSE
    dead.put(PA2.LEG1_DISTAL_NAME, new BaseAngled(60, 40, 0));
    dead.put(PA2.LEG1_MIDDLE_NAME, new BaseAngled(130, 20, 0));
    dead.put(PA2.LEG1_BODY_NAME, new BaseAngled(0, 0, 0));
    
    dead.put(PA2.LEG2_DISTAL_NAME, new BaseAngled(60, 40, 0));
    dead.put(PA2.LEG2_MIDDLE_NAME, new BaseAngled(130, 20, 0));
    dead.put(PA2.LEG2_BODY_NAME, new BaseAngled(0, 0, 0));
    
    dead.put(PA2.LEG3_DISTAL_NAME, new BaseAngled(60, 40, 0));
    dead.put(PA2.LEG3_MIDDLE_NAME, new BaseAngled(130, 20, 0));
    dead.put(PA2.LEG3_BODY_NAME, new BaseAngled(0, 0, 0));
    
    dead.put(PA2.LEG4_DISTAL_NAME, new BaseAngled(-60, -40, 0));
    dead.put(PA2.LEG4_MIDDLE_NAME, new BaseAngled(-130, -20, 0));
    dead.put(PA2.LEG4_BODY_NAME, new BaseAngled(0, 0, 0));
    
    dead.put(PA2.LEG5_DISTAL_NAME, new BaseAngled(-60, -40, 0));
    dead.put(PA2.LEG5_MIDDLE_NAME, new BaseAngled(-130, -20, 0));
    dead.put(PA2.LEG5_BODY_NAME, new BaseAngled(0, 0, 0));

    dead.put(PA2.LEG6_DISTAL_NAME, new BaseAngled(-60, -40, 0));
    dead.put(PA2.LEG6_MIDDLE_NAME, new BaseAngled(-130, -20, 0));
    dead.put(PA2.LEG6_BODY_NAME, new BaseAngled(0, 0, 0));
    
    
    dead.put(PA2.ANTENNA1_DIST_NAME, new BaseAngled(0, 0, 0));
    dead.put(PA2.ANTENNA1_MID_NAME, new BaseAngled(0, 60, 0));
    dead.put(PA2.ANTENNA1_BODY_NAME, new BaseAngled(0, 90, 0));
    
    dead.put(PA2.ANTENNA2_DIST_NAME, new BaseAngled(0, 0, 0));
    dead.put(PA2.ANTENNA2_MID_NAME, new BaseAngled(0, -60, 0));
    dead.put(PA2.ANTENNA2_BODY_NAME, new BaseAngled(0, 90, 0));
    
    
    //"SPIDER" - COMBINES THE "EATING" AND "RUNNING" TEST CASES TO GIVE THE COCKROACH A 'SPIDER' LIKE POSE
    spider.put(PA2.LEG1_DISTAL_NAME, new BaseAngled(-20, 20, 0));
    spider.put(PA2.LEG1_MIDDLE_NAME, new BaseAngled(0, -120, 0));
    spider.put(PA2.LEG1_BODY_NAME, new BaseAngled(0, 0, 0));
    
    spider.put(PA2.LEG2_DISTAL_NAME, new BaseAngled(0,20, 0));
    spider.put(PA2.LEG2_MIDDLE_NAME, new BaseAngled(0, 40, 0));
    spider.put(PA2.LEG2_BODY_NAME, new BaseAngled(0, 0, 0));
    
    spider.put(PA2.LEG3_DISTAL_NAME, new BaseAngled(0, 40, 0));
    spider.put(PA2.LEG3_MIDDLE_NAME, new BaseAngled(0, 90, 0));
    spider.put(PA2.LEG3_BODY_NAME, new BaseAngled(0, 0, 0));
    
    spider.put(PA2.LEG4_DISTAL_NAME, new BaseAngled(20, -20, 0));
    spider.put(PA2.LEG4_MIDDLE_NAME, new BaseAngled(0, 120, 0));
    spider.put(PA2.LEG4_BODY_NAME, new BaseAngled(0, 0, 0));
    
    spider.put(PA2.LEG5_DISTAL_NAME, new BaseAngled(0, -20, 0));
    spider.put(PA2.LEG5_MIDDLE_NAME, new BaseAngled(0, -40, 0));
    spider.put(PA2.LEG5_BODY_NAME, new BaseAngled(0, 0, 0));

    spider.put(PA2.LEG6_DISTAL_NAME, new BaseAngled(0, -40, 0));
    spider.put(PA2.LEG6_MIDDLE_NAME, new BaseAngled(0, -90, 0));
    spider.put(PA2.LEG6_BODY_NAME, new BaseAngled(0, 0, 0));
    
    
    spider.put(PA2.ANTENNA1_DIST_NAME, new BaseAngled(0, 0, 0));
    spider.put(PA2.ANTENNA1_MID_NAME, new BaseAngled(0, 60, 0));
    spider.put(PA2.ANTENNA1_BODY_NAME, new BaseAngled(0, 90, 0));
    
    spider.put(PA2.ANTENNA2_DIST_NAME, new BaseAngled(0, 0, 0));
    spider.put(PA2.ANTENNA2_MID_NAME, new BaseAngled(0, -60, 0));
    spider.put(PA2.ANTENNA2_BODY_NAME, new BaseAngled(0, 90, 0));
    

    //"SPREAD" - BOTH ANNTENAS SPREAD OUT 
    spread.put(PA2.LEG1_DISTAL_NAME, new BaseAngled(0, 40, 0));
    spread.put(PA2.LEG1_MIDDLE_NAME, new BaseAngled(0, 20, 0));
    spread.put(PA2.LEG1_BODY_NAME, new BaseAngled(0, 0, 0));
    
    spread.put(PA2.LEG2_DISTAL_NAME, new BaseAngled(0, 40, 0));
    spread.put(PA2.LEG2_MIDDLE_NAME, new BaseAngled(0, 20, 0));
    spread.put(PA2.LEG2_BODY_NAME, new BaseAngled(0, 0, 0));
    
    spread.put(PA2.LEG3_DISTAL_NAME, new BaseAngled(0, 40, 0));
    spread.put(PA2.LEG3_MIDDLE_NAME, new BaseAngled(0, 20, 0));
    spread.put(PA2.LEG3_BODY_NAME, new BaseAngled(0, 0, 0));
    
    spread.put(PA2.LEG4_DISTAL_NAME, new BaseAngled(0, -40, 0));
    spread.put(PA2.LEG4_MIDDLE_NAME, new BaseAngled(0, -20, 0));
    spread.put(PA2.LEG4_BODY_NAME, new BaseAngled(0, 0, 0));
    
    spread.put(PA2.LEG5_DISTAL_NAME, new BaseAngled(0, -40, 0));
    spread.put(PA2.LEG5_MIDDLE_NAME, new BaseAngled(0, -20, 0));
    spread.put(PA2.LEG5_BODY_NAME, new BaseAngled(0, 0, 0));
    
    spread.put(PA2.LEG6_DISTAL_NAME, new BaseAngled(0, -40, 0));
    spread.put(PA2.LEG6_MIDDLE_NAME, new BaseAngled(0, -20, 0));
    spread.put(PA2.LEG6_BODY_NAME, new BaseAngled(0, 0, 0));
    
    spread.put(PA2.ANTENNA1_DIST_NAME, new BaseAngled(0, -30, 0));
    spread.put(PA2.ANTENNA1_MID_NAME, new BaseAngled(90, 100, 0));
    spread.put(PA2.ANTENNA1_BODY_NAME, new BaseAngled(0, 90, 0));
    
    spread.put(PA2.ANTENNA2_DIST_NAME, new BaseAngled(0, 30, 0));
    spread.put(PA2.ANTENNA2_MID_NAME, new BaseAngled(90, -100, 0));
    spread.put(PA2.ANTENNA2_BODY_NAME, new BaseAngled(0, 90, 0));
    
  }
}
