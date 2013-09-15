import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Test case class for the Maze class. This class tests all public methods
 * of the Maze class for proper and expected functionality.
 * 
 * @author Derrick Tilsner dtt13
 *
 */
public class MazeTest {
	// private class variables
	private Maze mazeOne;
	private Maze mazeTwo;
	private MazeCell cellOne;
	private MazeCell cellTwo;
	private MazeCell cellThree;
	private MazeCell cellFour;
	private MazeCell cellFive;
	
	/**
	 * Creates Mazes to use for testing purposes.
	 */
	@Before
	public void generateMazes() {
		cellOne = new MazeCell();
		cellTwo = new MazeCell();
		cellThree = new MazeCell();
		cellFour = new MazeCell();
		cellFive = new MazeCell();
		// cellOne points to cellTwo
		Map<MazeCell, Integer> map = new HashMap<MazeCell, Integer>();
		map.put(cellTwo, new Integer(2));
		cellOne.addPassages(map);
		// cellTwo points to cellThree
		map = new HashMap<MazeCell, Integer>();
		map.put(cellThree, new Integer(3));
		cellTwo.addPassages(map);
		// cellThree is a dead end / exit to mazeOne
		cellThree.addPassages(new HashMap<MazeCell, Integer>());
		// cellFour points to cellFive
		map = new HashMap<MazeCell, Integer>();
		map.put(cellFive, new Integer(5));
		cellFour.addPassages(map);
		// cellFive point to cellFour
		map = new HashMap<MazeCell, Integer>();
		map.put(cellFour, new Integer(4));
		cellFive.addPassages(map);
		try {
			// create mazeOne to include cellOne and cellTwo
			Set<MazeCell> cells = new HashSet<MazeCell>();
			cells.add(cellOne);
			cells.add(cellTwo);
			mazeOne = new Maze();
			mazeOne.addCells(cells);
			cells = new HashSet<MazeCell>();
			cells.add(cellFour);
			cells.add(cellFive);
			mazeTwo = new Maze();
			mazeTwo.addCells(cells);
		} catch(UninitializedObjectException e) {
			fail("Initialization of Mazes failed");
		}
	}
	
	/**
	 * Tests the addCells() and isValid() methods with valid and invalid inputs.
	 */
	@Test
	public void testAddCellsAndIsValid() {
		try {
			// test with null Set
			Maze test = new Maze();
			assertFalse("Maze should be initialized to be invalid", test.isValid());
			assertFalse("Null Set should not be added as a maze", test.addCells(null));
			// test adding a non-null Set
			assertTrue("Did not properly add cells to the maze", test.addCells(new HashSet<MazeCell>()));
			assertTrue("Maze should be valid after adding cells", test.isValid());
			// test adding a List to an already valid route
			assertFalse("Maze should not be able to add additional cells to a valid maze", test.addCells(new HashSet<MazeCell>()));
			assertTrue("Maze should be valid even if user attempts to add additional cells to a valid maze", test.isValid());
		} catch(UninitializedObjectException e) {
			fail("addCells() method generated an UninitizedObjectException incorrectly");
		}
	}
	
	/**
	 * Tests the routeFirst() method assuming valid Maze.
	 */
	@Test
	public void testRouteFirst() {
		try {
			// test for a dead end and exit
			List<MazeCell> route = new LinkedList<MazeCell>();
			route.add(cellOne);
			route.add(cellTwo);
			route.add(cellThree);
			assertEquals("routeFirst() method does not return the correct route with a dead end", route, mazeOne.routeFirst(cellOne).getCells());
			// test for visit again
			route = new LinkedList<MazeCell>();
			route.add(cellFour);
			route.add(cellFive);
			route.add(cellFour);
			assertEquals("routeFirst() method does not return the correct route when revisiting", route, mazeTwo.routeFirst(cellFour).getCells());
		} catch(UninitializedObjectException e) {
			fail("routeFirst() method generated an UninitializedObjectException incorrectly");
		}
	}
	
	/**
	 * Tests the toString() method for unique identification Strings.
	 */
	@Test
	public void testToString() {
		// test String is not empty
		assertFalse("Maze does not create a String", mazeOne.toString().equals(""));
		// test Maze for unique identification String
		assertFalse("Maze does not create a unique identification String", mazeOne.toString().equals(mazeTwo.toString()));
		// test for invalid Maze String
		Maze test = new Maze();
		assertTrue("Maze does not generate the correct message when invalid", test.toString().equals("Uninitialized Maze"));
		// test for an empty maze
		try {
			test.addCells(new HashSet<MazeCell>());
			assertTrue("Maze String does not indicate that the maze is empty", test.toString().contains("empty"));
		} catch(UninitializedObjectException e) {
			fail("Maze generated an UninitializedObjectException incorrectly");
		}
	}
	
	/**
	 * Tests all methods that should throw an UninitializeObjectException when the Maze is invalid.
	 */
	@Test
	public void testExceptionThrowing() {
		Maze test = new Maze();
		// addCells() test when adding an invalid MazeCell
		try {
			Set<MazeCell> cells = new HashSet<MazeCell>();
			cells.add(cellOne);
			cells.add(new MazeCell());
			cells.add(cellTwo);
			test = new Maze();
			test.addCells(cells);
			fail("addCells() method should have thrown an UninitializedObjectException");
		} catch(UninitializedObjectException e) {
			// test passed because exception was thrown
		}
		// routeFirst() test
		try {
			test.routeFirst(cellOne);
			fail("routeFirst() method should have thrown an UninitializedObjectException");
		} catch(UninitializedObjectException e) {
			// test passed because exception was thrown
		}
	}
	
}
