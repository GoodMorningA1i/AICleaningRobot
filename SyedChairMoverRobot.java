package unit_2_Robots;

import becker.robots.*;

/**
 * This program will create a chair moving robot
 * @author Ali Syed
 * @version April 21 2019
 */
public class SyedChairMoverRobot extends RobotSE {

	//These variables can only be accessed by this class
	private int avenueLength, streetLength;
	private int initialStreet, initialAvenue, finalStreet, finalAvenue;
	private int entryAve;
	private int avenueMovement,streetMovement;;
	private int moveStorage = 0;
	private int cafeteriaMoved = 0;
	private int cafeteriaArea;

	private boolean entryFound;

	/**
	 * Constructor method
	 * @param c - Object that creates the city the robot will be in
	 * @param s - Initial street that the robot will be on
	 * @param a - Initial avenue that the robot will be on
	 * @param d - Initial direction of the robot
	 */
	public SyedChairMoverRobot(City c, int s, int a, Direction d){
		super (c,s,a,d);
	}

	/**
	 * This method is a collection of what the robot needs in order to stack all chairs from cafeteria
	 */
	public void moveChairs(){

		this.roomMeasurements();

		//This keeps the loop running until the robot has checked every intersection of the cafeteria
		while (this.cafeteriaMoved < this.cafeteriaArea){
			//Determines whether the robot has to pick up chair or keep moving across cafeteria
			if (this.chairCheck()==true){
				this.moveToStorage();
				this.placeInStorage();	
				this.moveToCaf();
			}
			else {
				this.movingAroundCaf();
			}
		}
	}

	/**
	 * This method determines entry location and the initial and final streets and avenues of cafeteria
	 */
	private void roomMeasurements() {
		this.entryFound = false;

		this.moveToTopLeft();
		this.initialStreet = this.getStreet();
		this.initialAvenue = this.getAvenue();

		//The robot will keep going through the avenues until the entrance is found
		while (this.entryFound == false) {
			this.moveTillSouthWall();
			this.getFinalStreetAndEntryAvenue();		
			this.moveTillNorthWall();
			
			//Breaking loop because the rest is unnecessary is entryFound is true
			if (this.entryFound == true)
				break;
			
			//Checking if there is at least two avenues in the cafeteria
			if (this.checkRightAvenue() == true){
				this.move();
			}
			else {
				this.lookBothWays();
				this.finalStreet = this.getStreet();
				this.moveTillNorthWall();
				break;
			}
		}
		
		this.moveTillEastWall();
		this.finalAvenue = this.getAvenue();
		
		//Setting up variables using information gained from everything above
		this.avenueMovement = this.finalAvenue;
		this.streetMovement = this.initialStreet;
		this.avenueLength = (this.finalAvenue-this.initialAvenue)+1;
		this.streetLength = (this.finalStreet-this.initialStreet)+1;
		this.cafeteriaArea = this.avenueLength*this.streetLength;
	}

	/**
	 * This method will tell the robot to keep moving east until a wall is reached
	 */
	private void moveTillEastWall() {
		this.directionEast();
		this.moveTillWall();
	}

	/**
	 * This method is important to check for a cafeteria with an avenue size of 1
	 * @return - determines whether there is a wall on the exact right of the robot
	 */
	private boolean checkRightAvenue() {
		boolean existingAve;
		
		this.directionEast();
		//This exception is only when the cafeteria has an avenue with size 1
		if (this.frontIsClear()) 
			existingAve = true;
		else 
			existingAve = false;
		return existingAve;
	}

	/**
	 * This method tells the robot to keep moving north until a wall is reached
	 */
	private void moveTillNorthWall() {
		this.directionNorth();
		this.moveTillWall();
	}

	/**
	 * This method finds the final street and the avenue that the entrance is on in the cafeteria
	 */
	private void getFinalStreetAndEntryAvenue() {

		//Assigning values to finalStreet and entryAvenue for the first avenue of the cafeteria
		if (this.getAvenue() == this.initialAvenue){
			this.finalStreet = this.getStreet();
			this.entryAve = this.getAvenue();
		}

		//If the robot is able to reach a greater street than before, it is the entrance
		else if (this.getStreet() > this.finalStreet){
			this.entryAve = this.getAvenue();
			this.entryFound = true;
		}

		//If the robot is able to reach a shorter street than before, avenue before was entrance
		else if (this.getStreet() < this.finalStreet){
			this.finalStreet = this.getStreet();
			this.entryFound = true;
		}
	}

	/**
	 * This method tells the robot to keep moving south until a wall is reached
	 */
	private void moveTillSouthWall() {
		this.directionSouth();
		this.moveTillWall();
	}

	/**
	 * This method tells the robot to keep moving west until a wall is reached
	 */
	private void moveTillWestWall(){
		this.directionWest();
		this.moveTillWall();
	}

	/**
	 * This method tells robot to move to the top left corner of the cafeteria
	 */
	private void moveToTopLeft() {
		this.moveTillNorthWall();
		this.moveTillWestWall();
	}
	
	/**
	 * This method tells the robot to check for a chair for the intersection it is on
	 * @return - determines whether the robot has picked up a chair 
	 */
	private boolean chairCheck() {
		boolean validChair = false;

		//Checks if a chair is present at the intersection
		if (this.canPickThing()){
			this.pickThing();
			validChair = true;
		}
		return validChair;
	}

	/**
	 * This method tells the robot to go to the storage area
	 */
	private void moveToStorage() {
		this.moveToAvenueEntrance();
		this.moveTillSouthWall();
		this.moveTillWestWall();
	}

	/**
	 * This method tells the robots to put down the chair
	 */
	private void placeInStorage() {
		this.spotToPutChair();
		this.checkMaxChairs();
		this.putThing();
	}

	/**
	 * This method tells the robot to check for the number of chairs and move to new spot accordingly
	 */
	private void checkMaxChairs() {
		//If there is 10 chairs already stacked, robot moves to the intersection on the right
		if (this.countThingsOnGround() >= 10){
			this.move();
			this.moveStorage++;
		}
	}

	/**
	 * This method tells the robot to get to the spot where it could possibly put the chair
	 */
	private void spotToPutChair() {
		this.turnAround();
		this.move(this.moveStorage);
	}

	/**
	 * This method tells the robot to come back to the cafeteria to pick up more chairs
	 */
	private void moveToCaf() {
		this.moveToAvenueEntrance();
		this.directionNorth();
		this.moveToProperStreet();
		this.moveToProperAvenue();
	}

	/**
	 * This method tells the robot to move and turn onto the avenue it was previously on
	 */
	private void moveToProperAvenue() {
		//Moves to the avenue that the robot was previously on
		while (this.getAvenue() != this.avenueMovement){
			this.move();
		}
		this.turnRelativeToAvenuesLeft();
	}

	/**
	 * This method tells the robot to move and turn onto the street it was previously on
	 */	
	private void moveToProperStreet() {
		//Moves up to the street that the robot was previously on
		while (this.getStreet() != this.streetMovement){
			this.move();
		}
		this.turnRelativeToAvenuesLeft();
	}
	
	/**
	 * This method tells the robot which way to turn on a street/avenue in the cafeteria
	 */
	private void turnRelativeToAvenuesLeft() {
		//Tells the robot which direction to turn once it comes to avenue/street 
		if (this.getAvenue() <= this.avenueMovement){
			this.turnRight();
		}
		else if (this.getAvenue() > this.avenueMovement){
			this.turnLeft();
		}
	}

	/**
	 * This method tells the robot how to move across the cafeteria when there is no chair
	 */
	private void movingAroundCaf() {

		//If robot reaches the end of an avenue and there is no chair
		if (this.streetMovement == this.finalStreet){
			//If robot has not reached the end of the cafeteria (last intersection)
			if (this.avenueMovement != this.initialAvenue){
				this.moveAvenues();
			}
			else {
				this.getToFinalPos();
			}
		}
		else {
			this.moveStreets();
		}
		this.cafeteriaMoved++;
	}

	/**
	 * This method is the robot's final position once it checked all intersections of the cafeteria
	 */
	private void getToFinalPos() {
		this.moveToStorage();
		this.spotToPutChair();
		this.moveOneUp();
	}

	/**
	 * This method tells the robot to move down the next avenue when there is no chair
	 */
	private void moveAvenues() {
		this.directionWest();
		this.move();
		this.moveTillNorthWall();
		this.turnAround();
		this.avenueMovement--;
		this.streetMovement = this.initialStreet;
	}

	/**
	 * This method tells the robot to move down the next street when there is no chair
	 */
	private void moveStreets() {
		this.streetMovement++;
		this.moveOneDown();
	}
	
	/**
	 * This method tells the robot to move one intersection down
	 */
	private void moveOneDown(){
		this.directionSouth();
		this.move();
	}
	
	/**
	 * This method tells the robot to move one intersection up
	 */
	private void moveOneUp(){
		this.directionNorth();
		this.move();
	}

	/**
	 * This method tells the robot to keep on moving until it reaches the avenue with the entrance
	 */
	private void moveToAvenueEntrance(){

		//If the robot is west to the entrance avenue, turn east
		if (this.getAvenue() < this.entryAve){
			this.directionEast();
		}
		//If the robot is east to the entrance avenue, turn west
		else if (this.getAvenue() > this.entryAve){
			this.directionWest();
		}
		//Until the robot doesn't reach the entrance avenue, keep moving
		while (this.getAvenue() != this.entryAve) {
			this.move();
		}
	}

	/**
	 * This method helps to determine how many streets there are in a one way avenue
	 */
	private void lookBothWays() {
		//Robot looks both ways to find out when one way avenue ends
		while (true) {
			this.moveOneDown();

			this.directionEast();
			//No more cafeteria streets so moves back to previous street
			if (this.moveToPreviousStreet()==true)
				break;

			this.directionWest();
			//No more cafeteria streets so moves back to previous street
			if (this.moveToPreviousStreet()==true)
				break;
		}
	}

	/**
	 * This method makes the robot go up one street if there was no wall in front of the robot
	 * @return - determines whether a robot should move up one intersection
	 */
	private boolean moveToPreviousStreet() {
		boolean frontClear = false;

		//Moves upwards if no wall in front
		if (this.frontIsClear()){
			this.moveOneUp();
			frontClear = true;
		}
		return frontClear;
	}

	/**
	 * This method tells the robot to keep turning right until its direction is east
	 */
	private void directionEast() {
		//while the robot isn't facing east, turn right
		while (this.isFacingEast() == false){
			this.turnRight();
		}
	}

	/**
	 * This method tells the robot to keep turning right until its direction is west
	 */
	private void directionWest(){
		//while the robot isn't facing west, turn right
		while (this.isFacingWest() == false){
			this.turnRight();
		}
	}

	/**
	 * This method tells the robot to keep turning right until its direction is south
	 */
	private void directionSouth(){
		//while the robot isn't facing south, turn right
		while (this.isFacingSouth()==false){
			this.turnRight();
		}
	}
	
	/**
	 * This method tells the robot to keep turning right until its direction is north
	 */
	private void directionNorth(){
		//while the robot isn't facing north, turn right
		while (this.isFacingNorth() == false){
			this.turnRight();
		}
	}

	/**
	 * This method tells the robot to keep on moving until it reaches the wall
	 */
	private void moveTillWall(){
		//Keep on moving until there is a wall in front
		while (this.frontIsClear()){
			this.move();
		}
	}

	/**
	 * This method tells the robot to counts the number of chairs on the ground
	 * @return i - represents the number of chairs on the ground
	 */
	private int countThingsOnGround() {
		int i = 0;
		//Keeps on picking up things at the current intersection and counts how many picking up
		while (this.canPickThing() == true) {
			this.pickThing();
			i++;
		}

		//Puts the things it picked up back on the ground after counting
		for (int put=0;put<i;put++) {
			this.putThing(); 
		}		
		return i;
	}
}