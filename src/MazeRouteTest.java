import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Test case class for the MazeRoute class. This class tests all public methods
 * of the MazeRoute class for proper and expected functionality.
 * 
 * @author Derrick Tilsner dtt13
 *
 */
public class MazeRouteTest {
	// private class variables
	private List<MazeCell> route;
	private MazeRoute routeOne;
	private MazeRoute routeTwo;
	private MazeRoute routeThree;
	private MazeCell startOne;
	private MazeCell startTwo;
	private MazeCell middleOne;
	private MazeCell endOne;
	private MazeCell endTwo;
	
	/**
	 * Creates a world map of MazeCells and single MazeRoute through this world
	 * to be used throughout the testing of the MazeRoute class.
	 */
	@Before
	public void initializeCellsAndRoutes() {
		// create a "world map" of passages
		endOne = new MazeCell();
		endOne.addPassages(new HashMap<MazeCell, Integer>(), new MazeCell.Status());
		endTwo = new MazeCell();
		endTwo.addPassages(new HashMap<MazeCell, Integer>(), new MazeCell.Status());
		Map<MazeCell, Integer> map = new HashMap<MazeCell, Integer>();
		map.put(endOne, new Integer(2));
		map.put(endTwo, new Integer(3));
		middleOne = new MazeCell();
		middleOne.addPassages(map, new MazeCell.Status());
		map = new HashMap<MazeCell, Integer>();
		map.put(middleOne, new Integer(1));
		startOne= new MazeCell();
		startOne.addPassages(map, new MazeCell.Status());
		map = new HashMap<MazeCell, Integer>();
		map.put(middleOne, new Integer(MazeCell.IMPASSABLE));
		startTwo = new MazeCell();
		startTwo.addPassages(map, new MazeCell.Status());
		/*
		 * startOne -1-> middleOne
		 * startTwo -//-> middleOne
		 * middleOne -2-> endOne
		 * middleOne -3-> endTwo
		 */
		// create a route through this "world map"
		try {
			// routeOne is a passable route
			route = new LinkedList<MazeCell>();
			route.add(startOne);
			route.add(middleOne);
			route.add(endOne);
			routeOne = new MazeRoute();
			routeOne.addCells(route);
			// routeTwo is non-existent route
			List<MazeCell> list = new LinkedList<MazeCell>();
			list.add(startOne);
			list.add(startTwo);
			routeTwo = new MazeRoute();
			routeTwo.addCells(list);
			// routeThree is an impassable route
			list = new LinkedList<MazeCell>();
			list.add(startTwo);
			list.add(middleOne);
			list.add(endTwo);
			routeThree = new MazeRoute();
			routeThree.addCells(list);
		} catch(UninitializedObjectException e) {
			fail("Initialization of MazeRoutes failed");
		}
	}
	
	/**
	 * Tests the addCells() and isValid() methods with valid and invalid inputs.
	 */
	@Test
	public void testAddCellsAndIsValid() {
		try {
			// test with null List
			MazeRoute test = new MazeRoute();
			assertFalse("MazeRoute should be initialized to be invalid", test.isValid());
			assertFalse("Null List should not be added as a route", test.addCells(null));
			// test adding a non-null List
			assertTrue("Did not properly add a route List", test.addCells(new LinkedList<MazeCell>()));
			assertTrue("MazeRoute should be valid after adding a route", test.isValid());
			// test adding a List to an already valid route
			assertFalse("MazeRoute should not be able to add additional cells to a valid route",
					test.addCells(new LinkedList<MazeCell>()));
			assertTrue("MazeRoute should be valid even if user attempts to add additional cells to a valid route",
					test.isValid());
		} catch(UninitializedObjectException e) {
			fail("addCells() method generated an UninitizedObjectException incorrectly");
		}
	}
	
	/**
	 * Tests the getCells() method assuming valid MazeRoute.
	 */
	@Test
	public void testGetCells() {
		try {
			// test with an empty route
			MazeRoute test = new MazeRoute();
			test.addCells(new LinkedList<MazeCell>());
			assertEquals("getCells() method does not return the correct route List",
					new LinkedList<MazeCell>(), test.getCells());
			// test with a non-empty route
			assertEquals("getCells() method does not return the correct route List",
					route, routeOne.getCells());
		} catch(UninitializedObjectException e) {
			fail("getCells() method generated an UninitializedObjectException incorrectly");
		}
	}
	
	/**
	 * Tests the travelTime() method assuming valid MazeRoute.
	 */
	@Test
	public void testTravelTime() {
		try {
			// test route with no passage
			assertEquals("travelTime() method does not return the correct travel time when there is no passage",
					new Integer(MazeCell.IMPASSABLE), routeTwo.travelTime());
			// test route with impassable passage
			assertEquals("travelTime() method does not return the correct travel time when there is an impassable passage",
					new Integer(MazeCell.IMPASSABLE), routeThree.travelTime());
			// test route with all passable passages
			assertEquals("travelTime() method does not return the correct travel time when all passages are passable",
					new Integer(3), routeOne.travelTime());
		} catch(UninitializedObjectException e) {
			fail("travelTime() method generated an UninitializedObjectException incorrectly");
		}
	}
	
	/**
	 * Tests the travelTimeRandom() method assuming valid MazeRoute.
	 */
	@Test
	public void testTravelTimeRandom() {
		try {
			// test route with no passage
			assertEquals("travelTimeRandom() method does not return the correct travel time when there is no passage",
					new Integer(MazeCell.IMPASSABLE), routeTwo.travelTimeRandom());
			// test route with impassable passage
			assertEquals("travelTimeRandom() method does not return the correct travel time when there is an impassable passage",
					new Integer(MazeCell.IMPASSABLE), routeThree.travelTimeRandom());
			// test route with all passable passages
			int randomTime = routeOne.travelTimeRandom();
			assertTrue("travelTimeRandom() method does not return a valid travel time when all passages are passable",
					(randomTime == 2) || ( randomTime == 3));
		} catch(UninitializedObjectException e) {
			fail("travelTimeRandom() method generated an UninitializedObjectException incorrectly");
		}
	}
	
	/**
	 * Tests the toString() method for unique identification Strings.
	 */
	@Test
	public void testToString() {
		// test String is not empty
		assertFalse("MazeRoute does not create a String", routeOne.toString().equals(""));
		// test MazeRoute for unique identification String
		assertFalse("MazeRoute does not create a unique identification String",
				routeOne.toString().equals(routeTwo.toString()));
		// test for no passage
		assertTrue("MazeRoute String does not indicate that there is no passage",
				routeThree.toString().contains("no passage"));
		// test for invalid MazeRoute String
		MazeRoute test = new MazeRoute();
		assertTrue("MazeRoute does not generate the correct message when invalid",
				test.toString().equals("Uninitialized MazeRoute"));
		// test for an empty route
		try {
			test.addCells(new LinkedList<MazeCell>());
			assertTrue("MazeRoute String does not indicate that the route is empty",
					test.toString().contains("empty"));
		} catch(UninitializedObjectException e) {
			fail("MazeRoute generated an UninitializedObjectException incorrectly");
		}
	}
	
	/**
	 * Tests all methods that should throw an UninitializedObjectException when the MazeRoute is invalid.
	 */
	@Test
	public void testExceptionThrowing() {
		MazeRoute test = new MazeRoute();
		// addCells() test when adding an invalid MazeCell
		try {
			List<MazeCell> list = new LinkedList<MazeCell>();
			list.add(startOne);
			list.add(new MazeCell());
			list.add(endOne);
			test.addCells(list);
			fail("addCells() method should have thrown an UninitializedObjectException");
		} catch(UninitializedObjectException e) {
			// test passed because exception was thrown
		}
		// getCells() test
		try {
			test.getCells();
			fail("getCells() method should have thrown an UninitializedObjectException");
		} catch(UninitializedObjectException e) {
			// test passed because exception was thrown
		}
		// travelTime() test
		try {
			test.travelTime();
			fail("travelTime() method should have thrown an UninitializedObjectException");
		} catch(UninitializedObjectException e) {
			// test passed because exception was thrown
		}
		// travelTimeRandom() test
		try {
			test.travelTimeRandom();
			fail("travelTimeRandom() method should have thrown an UninitializedObjectException");
		} catch(UninitializedObjectException e) {
			// test passed because exception was thrown
		}
	}
}
