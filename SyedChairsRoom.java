package unit_2_Robots;

import becker.robots.*;
import java.util.Random;

/**
 * This program will show the chair moving robot moving chairs from cafeteria to storage area
 * @author Ali Syed
 * @version April 22 2019
 */
public class SyedChairsRoom {

	public static void main(String[] args) {
		
		//Variable declaration to set up the environment
		City oakville = new City (10,10);
		SyedChairMoverRobot karel = new SyedChairMoverRobot (oakville, 4, 3, Direction.NORTH);
		Random generator = new Random ();
		
		//These variables allow for the customization of the environment
		int streetSize = 6;
		int avenueSize = 8;
		int randomChairs = generator.nextInt(avenueSize*10)+1;
		int entryDiff = 3; //Determines where the entrance is going to be
		int entryAvenue = avenueSize - entryDiff; //This calculated value is one avenue lower than in city
		int posChange = 1; //Change the position of the cafeteria
		int storCafDistance = 1; //Distance between storage and cafeteria

		//Declaring and Initializing Array
		Wall[][] cafWalls = new Wall[avenueSize][streetSize];
		Wall[][] storageWalls = new Wall [avenueSize][2];
		int [] streetNums = new int [randomChairs];
		int [] avenueNums = new int [randomChairs];
		Thing[] chairs = new Thing[randomChairs];

		
		//Instantiating north cafeteria walls and south storage walls and holding them in array 
		for (int i=0;i<avenueSize;i++) {
			cafWalls[i][0] = new Wall (oakville, posChange, i+posChange, Direction.NORTH);
			storageWalls[i][0] = new Wall (oakville, (streetSize+posChange)+storCafDistance, i+posChange, Direction.SOUTH);
		}
		//Instantiating the south cafeteria walls and holding them in an array
		for (int i = 0; i<entryAvenue;i++){
			cafWalls[i][streetSize-1] = new Wall (oakville, streetSize+(posChange-1), i+posChange, Direction.SOUTH);
		}
		for (int i=entryAvenue+1;i<avenueSize;i++){
			cafWalls[i][streetSize-1] = new Wall (oakville, streetSize+(posChange-1), i+posChange, Direction.SOUTH);
		}
		
		//Instantiating east and west walls for the cafeteria and holding them in array 
		for (int i=0;i<streetSize;i++) {
			cafWalls[0][i] = new Wall (oakville, i+posChange, posChange, Direction.WEST);
			cafWalls[avenueSize-1][i] = new Wall (oakville, i+posChange, avenueSize+(posChange-1), Direction.EAST);
		}
		//Instantiating an east and west wall for the storage area and holding them in an array
		storageWalls[0][1] = new Wall (oakville, (streetSize+posChange)+storCafDistance, posChange, Direction.WEST);
		storageWalls[avenueSize-1][1] = new Wall (oakville, (streetSize+posChange)+storCafDistance, avenueSize+(posChange-1), Direction.EAST);

		
		//Instantiating chairs(things) in random locations within the cafeteria
		for (int i = 0; i < randomChairs; i++) {

			//Adding random street positions for garbages in an array
			int randomStreet = generator.nextInt(streetSize)+posChange;
			streetNums[i] += randomStreet;

			//Adding random avenue positions for garbages in an array
			int randomAvenue = generator.nextInt(avenueSize)+posChange;
			avenueNums[i] += randomAvenue;
		}

		//Instantiating things (garbages) and holds them in array 
		for (int i=0;i<randomChairs;i++) {
			chairs[i] = new Thing (oakville, streetNums[i], avenueNums[i]);
		}

		//Showing a count of the things
		oakville.showThingCounts(true);
		
		//The chair moving robot moves all the chairs from the cafeteria to the storage area
		karel.moveChairs();

	}

}
