import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
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
	private static List<MazeCell> route;
	private static MazeRoute routeOne;
	private static MazeRoute routeTwo;
	private static MazeRoute routeThree;
	private static MazeCell startOne;
	private static MazeCell startTwo;
	private static MazeCell middleOne;
	private static MazeCell endOne;
	private static MazeCell endTwo;
	
	/**
	 * Creates a world map of MazeCells and single MazeRoute through this world
	 * to be used throughout the testing of the MazeRoute class.
	 */
	@BeforeClass
	public static void initializeCellsAndRoutes() {
		// create a "world map" of passages
		endOne = new MazeCell();
		endOne.addPassages(new HashMap<MazeCell, Integer>());
		endTwo = new MazeCell();
		endTwo.addPassages(new HashMap<MazeCell, Integer>());
		Map<MazeCell, Integer> map = new HashMap<MazeCell, Integer>();
		map.put(endOne, new Integer(1));
		map.put(endTwo, new Integer(2));
		middleOne = new MazeCell();
		middleOne.addPassages(map);
		map = new HashMap<MazeCell, Integer>();
		map.put(middleOne, new Integer(1));
		startOne= new MazeCell();
		startOne.addPassages(map);
		map = new HashMap<MazeCell, Integer>();
		map.put(middleOne, new Integer(Integer.MAX_VALUE));
		startTwo = new MazeCell();
		startTwo.addPassages(map);
		// create a route through this "world map"
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
	}
	
	/**
	 * Tests the addCells() and isValid() methods with valid and invalid inputs.
	 */
	@Test
	public void testAddCellsAndIsValid() {
		// test with null List
		MazeRoute test = new MazeRoute();
		assertFalse("MazeRoute should be initialized to be invalid", test.isValid());
		assertFalse("Null List should not be added as a route", test.addCells(null));
		// test adding a non-null List
		assertTrue("Did not properly add a route List", test.addCells(new LinkedList<MazeCell>()));
		assertTrue("MazeRoute should be valid after adding a route", test.isValid());
		// test adding a List to an already valid route
		assertFalse("MazeRoute should not be able to add additional cells to a valid route", test.addCells(new LinkedList<MazeCell>()));
		assertTrue("MazeRoute should be valid even if user attempts to add additional cells to a valid route", test.isValid());
		// test adding invalid MazeCell
		List<MazeCell> list = new LinkedList<MazeCell>();
		list.add(startOne);
		list.add(new MazeCell());
		list.add(endOne);
		test = new MazeRoute();
		assertFalse("MazeRoute added a route with invalid MazeCell", test.addCells(list));
		assertFalse("MazeRoute should not be valid if invalid MazeCell was added", test.isValid());
	}
	
	/**
	 * Tests the getCells() method assuming valid MazeRoute.
	 */
	@Test
	public void testGetCells() {
		// test with an empty route
		MazeRoute test = new MazeRoute();
		test.addCells(new LinkedList<MazeCell>());
		assertEquals("getCells() method does not return the correct route List", new LinkedList<MazeCell>(), test.getCells());
		// test with a non-empty route
		assertEquals("getCells() method does not return the correct route List", route, routeOne.getCells());
	}
	
	/**
	 * Tests the travelTime() method assuming valid MazeRoute.
	 */
	@Test
	public void testTravelTime() {
		// test route with no passage
		assertEquals("travelTime() method does not return the correct travel time when there is no passage", new Integer(Integer.MAX_VALUE), routeTwo.travelTime());
		// test route with impassable passage
		assertEquals("travelTime() method does not return the correct travel time when there is an impassable passage", new Integer(Integer.MAX_VALUE), routeThree.travelTime());
		// test route with all passable passages
		assertEquals("travelTime() method does not return the correct travel time when all passages are passable", new Integer(2), routeOne.travelTime());
	}
	
	/**
	 * Tests the toString() method for unique identification Strings assuming valid MazeRoute.
	 */
	@Test
	public void testToString() {
		// test String is not empty
		assertFalse("MazeRoute does not create a String", routeOne.toString().equals(""));
		// test MazeRoute for unique identification String
		assertFalse("MazeRoute does not create a unique identification String", routeOne.toString().equals(routeTwo.toString()));
	}
	
	/**
	 * Tests all methods the should throw an UninitializedObjectException when the MazeRoute is invalid.
	 */
	@Test
	public void testExceptionThrowing() {
		MazeRoute test = new MazeRoute();
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
		// toString() test
		try {
			test.toString();
			fail("toString() method should have thrown an UninitializedObjectException");
		} catch(UninitializedObjectException e) {
			// test passed because exception was thrown
		}
	}
}
