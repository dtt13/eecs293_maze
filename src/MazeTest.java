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
	private Maze mazeThree;
	private Maze mazeFour;
	private MazeCell cellOne;
	private MazeCell cellTwo;
	private MazeCell cellThree;
	private MazeCell cellFour;
	private MazeCell cellFive;
	private MazeCell cellSix;
	private MazeCell cellSeven;
	private MazeCell cellEight;
	
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
		cellSix = new MazeCell();
		cellSeven = new MazeCell();
		cellEight = new MazeCell();
		// cellOne points to cellTwo
		Map<MazeCell, Integer> map = new HashMap<MazeCell, Integer>();
		map.put(cellTwo, new Integer(2));
		cellOne.addPassages(map, new MazeCell.Status());
		// cellTwo points to cellThree
		map = new HashMap<MazeCell, Integer>();
		map.put(cellThree, new Integer(3));
		cellTwo.addPassages(map, new MazeCell.Status());
		// cellThree is a dead end
		cellThree.addPassages(new HashMap<MazeCell, Integer>(), new MazeCell.Status());
		// cellFour points to cellFive
		map = new HashMap<MazeCell, Integer>();
		map.put(cellFive, new Integer(5));
		cellFour.addPassages(map, new MazeCell.Status());
		// cellFive points to cellFour
		map = new HashMap<MazeCell, Integer>();
		map.put(cellFour, new Integer(4));
		cellFive.addPassages(map, new MazeCell.Status());
		// cellSix points to cellSeven and cellEight
		map = new HashMap<MazeCell, Integer>();
		map.put(cellSeven, new Integer(14));
		map.put(cellEight, new Integer(8));
		cellSix.addPassages(map, new MazeCell.Status());
		// cellSeven is a dead end
		cellSeven.addPassages(new HashMap<MazeCell, Integer>(), new MazeCell.Status());
		// cellEight is a dead end
		cellEight.addPassages(new HashMap<MazeCell, Integer>(), new MazeCell.Status());
		try {
			// create mazeOne to include cellOne, cellTwo, and cellThree
			Set<MazeCell> cells = new HashSet<MazeCell>();
			cells.add(cellOne);
			cells.add(cellTwo);
			cells.add(cellThree);
			mazeOne = new Maze();
			mazeOne.addCells(cells);
			// create mazeTwo to include cellFour and cellFive
			cells = new HashSet<MazeCell>();
			cells.add(cellFour);
			cells.add(cellFive);
			mazeTwo = new Maze();
			mazeTwo.addCells(cells);
			// create mazeThree to include cellSix, cellSeven, and cellEight
			cells = new HashSet<MazeCell>();
			cells.add(cellSix);
			cells.add(cellSeven);
			cells.add(cellEight);
			mazeThree = new Maze();
			mazeThree.addCells(cells);
			// create mazeFour to include cellOne only
			cells = new HashSet<MazeCell>();
			cells.add(cellOne);
			mazeFour = new Maze();
			mazeFour.addCells(cells);
		} catch(UninitializedObjectException e) {
			fail("Initialization of Mazes failed");
		}
	}
	
	/**
	 * Tests the addCells() method with valid and invalid inputs.
	 */
	@Test
	public void testAddCells() { 
		try {
			// test with null Set
			Maze test = new Maze();
			assertFalse("Null Set should not be added as a maze", test.addCells(null));
			// test adding a non-null Set
			assertTrue("Did not properly add cells to the maze", test.addCells(new HashSet<MazeCell>()));
			// test adding a List to an already valid route
			assertFalse("Maze should not be able to add additional cells to a valid maze",
					test.addCells(new HashSet<MazeCell>()));
		} catch(UninitializedObjectException e) {
			fail("addCells() method generated an UninitizedObjectException incorrectly");
		}
	}
	
	/**
	 * Tests the isValid() method with valid and invalid inputs.
	 */
	@Test
	public void testIsValid() {
		try {
			// test initialization
			Maze test = new Maze();
			assertFalse("Maze should be initialized to be invalid", test.isValid());
			// test adding a non-null Set
			test.addCells(new HashSet<MazeCell>());
			assertTrue("Maze should be valid after adding cells", test.isValid());
			// test adding a Set to an already valid maze
			test.addCells(new HashSet<MazeCell>());
			assertTrue("Maze should be valid even if user attempts to add additional cells to a valid maze",
					test.isValid());
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
			// test for a dead end
			List<MazeCell> route = new LinkedList<MazeCell>();
			route.add(cellOne);
			route.add(cellTwo);
			route.add(cellThree);
			assertEquals("routeFirst() method does not return the correct route with a dead end",
					route, mazeOne.routeFirst(cellOne).getCells());
			// test for visit again
			route = new LinkedList<MazeCell>();
			route.add(cellFour);
			route.add(cellFive);
			route.add(cellFour);
			assertEquals("routeFirst() method does not return the correct route when revisiting",
					route, mazeTwo.routeFirst(cellFour).getCells());
			// test for a branching path
			route = new LinkedList<MazeCell>();
			route.add(cellSix);
			route.add(cellSeven);
			assertEquals("routeFirst() method does not return the correct route when branching",
					route, mazeThree.routeFirst(cellSix).getCells());
			// test leaving the Maze
			assertEquals("routeFirst() method does not return the correct route when leaving the Maze",
					new LinkedList<MazeCell>(), mazeFour.routeFirst(cellOne).getCells());
		} catch(UninitializedObjectException e) {
			fail("routeFirst() method generated an UninitializedObjectException incorrectly");
		}
	}
	
	/**
	 * Tests the routeRandom() method assuming valid Maze.
	 */
	@Test
	public void testRouteRandom() {
		try {
			// test for a dead end
			List<MazeCell> routeOne = new LinkedList<MazeCell>();
			routeOne.add(cellOne);
			routeOne.add(cellTwo);
			routeOne.add(cellThree);
			assertEquals("routeRandom() method does not return the correct route with a dead end",
					routeOne, mazeOne.routeRandom(cellOne).getCells());
			// test for visit again
			routeOne = new LinkedList<MazeCell>();
			routeOne.add(cellFour);
			routeOne.add(cellFive);
			routeOne.add(cellFour);
			assertEquals("routeRandom() method does not return the correct route when revisiting",
					routeOne, mazeTwo.routeRandom(cellFour).getCells());
			// test for a branching path
			routeOne = new LinkedList<MazeCell>();
			routeOne.add(cellSix);
			routeOne.add(cellSeven);
			List<MazeCell> routeTwo = new LinkedList<MazeCell>();
			routeTwo.add(cellSix);
			routeTwo.add(cellEight);
			List<MazeCell> randomPath = mazeThree.routeRandom(cellSix).getCells(); 
			assertTrue("routeRandom() method does not return the correct route when branching",
					routeOne.equals(randomPath) || routeTwo.equals(randomPath));
			// test leaving the Maze
			assertEquals("routeRandom() method does not return the correct route when leaving the Maze",
					new LinkedList<MazeCell>(), mazeFour.routeRandom(cellOne).getCells());
		} catch(UninitializedObjectException e) {
			fail("routeRandom() method generated an UninitializedObjectException incorrectly");
		}
	}
	
	@Test
	public void testRouteGreedy() {
		try {
			// test for a dead end
			List<MazeCell> route = new LinkedList<MazeCell>();
			route.add(cellOne);
			route.add(cellTwo);
			route.add(cellThree);
			assertEquals("routeGreedy() method does not return the correct route with a dead end",
					route, mazeOne.routeGreedy(cellOne).getCells());
			// test for visit again
			route = new LinkedList<MazeCell>();
			route.add(cellFour);
			route.add(cellFive);
			route.add(cellFour);
			assertEquals("routeGreedy() method does not return the correct route when revisiting",
					route, mazeTwo.routeGreedy(cellFour).getCells());
			// test for a branching path
			route = new LinkedList<MazeCell>();
			route.add(cellSix);
			route.add(cellEight);
			assertEquals("routeGreedy() method does not return the correct route when branching",
					route, mazeThree.routeGreedy(cellSix).getCells());
			// test leaving the Maze
			assertEquals("routeGreedy() method does not return the correct route when leaving the Maze",
					new LinkedList<MazeCell>(), mazeFour.routeGreedy(cellOne).getCells());
		} catch(UninitializedObjectException e) {
			fail("routeGreedy() method generated an UninitializedObjectException incorrectly");
		}
	}
	
	@Test
	public void testRoute() {
		try {
			// test null passageSelector
			assertEquals("route() method does not return an empty route when passageSelector is null",
					new LinkedList<MazeCell>(), mazeOne.route(cellOne, null).getCells());
		} catch(UninitializedObjectException e) {
			fail("route() method generated an UninitializedObjectException incorrectly");
		}
	}
	
	@Test
	public void testAverageExitTime() {
		//TODO write test cases
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
		// routeRandom() test
		try {
			test.routeRandom(cellOne);
			fail("routeRandom() method should have thrown an UninitializedObjectException");
		} catch(UninitializedObjectException e) {
			// test passed because exception was thrown
		}
		// routeGreedy() test
		try {
			test.routeGreedy(cellOne);
			fail("routeGreedy() method should have thrown an UninitializedObjectException");
		} catch(UninitializedObjectException e) {
			// test passed because exception was thrown
		}
		// route() test
		try {
			test.route(cellOne, new FirstSelector());
			fail("route() method should have thrown an UninitializedObjectException");
		} catch(UninitializedObjectException e) {
			// test passed because exception was thrown
		}
	}
	
}
