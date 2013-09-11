import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test case class for the MazeCell class. This class tests all public methods
 * of the MazeCell class for proper and expected functionality.
 * 
 * @author Derrick Tilsner dtt13
 *
 */
public class MazeCellTest {
	//private class variables
	private static Map<MazeCell, Integer> passages;
	private static MazeCell destOne;
	private static MazeCell destTwo;
	private static MazeCell destThree;
	private static MazeCell srcOne;
	private static MazeCell srcTwo;
	private static MazeCell srcThree;
	private static MazeCell srcFour;
	
	/**
	 * Initializes a variety of MazeCells to be used throughout the testing
	 * of the MazeCell class.
	 */
	@BeforeClass
	public static void initializeMazeCells() {
		// create destination cells
		// note that all destination cells will be dead ends for simplicity
		destOne = new MazeCell();
		destOne.addPassages(new HashMap<MazeCell, Integer>());
		destTwo = new MazeCell();
		destTwo.addPassages(new HashMap<MazeCell, Integer>());
		destThree = new MazeCell();
		destThree.addPassages(new HashMap<MazeCell, Integer>());
		// create source cells
		// srcOne and srcTwo will have the same destinations but will be declared separately
		passages = new HashMap<MazeCell, Integer>();
		passages.put(destOne, new Integer(1));
		passages.put(destTwo, new Integer(2));
		srcOne = new MazeCell();
		srcOne.addPassages(passages);
		passages = new HashMap<MazeCell, Integer>();
		passages.put(destOne, new Integer(1));
		passages.put(destTwo, new Integer(2));
		srcTwo = new MazeCell();
		srcTwo.addPassages(passages);
		// srcThree will be different from srcOne and srcTwo and will contain an impassable connection
		Map<MazeCell, Integer> map = new HashMap<MazeCell, Integer>();
		map.put(destOne, new Integer(Integer.MAX_VALUE));
		map.put(destThree, new Integer(3));
		srcThree = new MazeCell();
		srcThree.addPassages(map);
		// srcFour will only contain impassiable connections
		map = new HashMap<MazeCell, Integer>();
		map.put(destOne, new Integer(Integer.MAX_VALUE));
		map.put(destTwo, new Integer(Integer.MAX_VALUE));
		map.put(destThree, new Integer(Integer.MAX_VALUE));
		srcFour = new MazeCell();
		srcFour.addPassages(map);
	}
	
	/**
	 * Tests the addPassages() and isValid() methods with valid and invalid inputs.
	 */
	@Test
	public void testAddPassagesAndIsValid() {
		// test with null Map
		MazeCell test = new MazeCell();
		assertFalse("MazeCell should be initialized to be invalid", test.isValid());
		assertFalse("Null Map should not be added as passages", test.addPassages(null));
		// test adding a non-null Map
		test = new MazeCell();
		assertTrue("Did not properly add passages Map", test.addPassages(new HashMap<MazeCell, Integer>()));
		assertTrue("MazeCell should be valid after adding passages", test.isValid());
		// test adding a Map to an already valid cell
		assertFalse("MazeCell should not be able to add additional Maps to valid cells", test.addPassages(new HashMap<MazeCell, Integer>()));
		assertTrue("MazeCell should be valid even if user attempts to add additional Maps to a valid cell", test.isValid());
	}
	
	/**
	 * Tests the hashCode() method for multiple MazeCell objects.
	 */
	@Test
	public void testHashCode() {
		// test return value is the same as the hashcode of the passages map
		assertEquals("hashCode() method is not returning the proper value", passages.hashCode(), srcOne.hashCode());
		// test if two MazeCells are the same
		assertTrue("hashCode() of similar MazeCells are not the same", srcOne.hashCode() == srcTwo.hashCode());
		// test it two MazeCells are different
		assertFalse("hashCode() of different MazeCells should not be the same", srcOne.hashCode() == srcThree.hashCode());
	}
	
	/**
	 * Tests the passages() method assuming valid MazeCells.
	 */
	@Test
	public void testPassages() {
		// test with no impassable passages
		assertEquals("passages() method does not return the correct passages Map", passages, srcOne.passages());
		// test with all impassable passages
		assertEquals("passages() method does not return the correct passages Map", new HashMap<MazeCell, Integer>(), srcFour.passages());
		// test with some impassable passages
		Map<MazeCell, Integer> map = new HashMap<MazeCell, Integer>();
		map.put(destThree, new Integer(3));
		assertEquals("passages() method does not return the correct passages Map", map, srcThree.passages());
	}
	
	/**
	 * Tests the passageTimeTo() method assuming valid MazeCells.
	 */
	@Test
	public void testPassageTimeTo() {
		// test a MazeCell that is passable
		assertEquals("passageTimeTo() method does not return the correct time", new Integer(3), srcThree.passageTimeTo(destThree));
		// test a MazeCell this is impassable
		assertEquals("passageTimeTo() method does not return the correct time", new Integer(Integer.MAX_VALUE), srcThree.passageTimeTo(destOne));
		// test a MazeCell that has no passage
		assertEquals("passageTimeTo() method does not handle non-adjoining MazeCell inputs", new Integer(Integer.MAX_VALUE), srcThree.passageTimeTo(destTwo));
	}
	
	/**
	 * Tests the connectedCells() method assuming valid MazeCells.
	 */
	@Test
	public void testConnectedCells() {
		// test with no impassable passages
		assertEquals("connectedCells() method does not return the correct MazeCells", passages.keySet(), srcOne.connectedCells());
		// test with all impassable passages
		assertEquals("connectedCells() method returns MazeCells that shouldn't be passable", new HashSet<MazeCell>(), srcFour.connectedCells());
		// test with some impassable passages
		Set<MazeCell> set = new HashSet<MazeCell>();
		set.add(destThree);
		assertEquals("connectedCells() method does not return the correct MazeCells", set, srcThree.connectedCells());
	}
	
	/**
	 * Tests the isDeadEnd() method assuming valid MazeCells.
	 */
	@Test
	public void testIsDeadEnd() {
		// test with cell that is not a dead end
		assertFalse("isDeadEnd() is incorrect when not a dead end", srcOne.isDeadEnd());
		// test with no passages
		assertTrue("isDeadEnd() is incorrect when there is a dead end", destOne.isDeadEnd());
		// test with all impassable passages
		assertTrue("isDeadEnd() is incorrect when there is a dead end", srcFour.isDeadEnd());
		// test with some impassable passages
		assertFalse("isDeadEnd() is incorrect when not a dead end", srcThree.isDeadEnd());
	}
	
	/**
	 * Tests the toString() method for unique identification Strings assuming valid MazeCells.
	 */
	@Test
	public void testToString() {
		// test String is not empty
		assertFalse("MazeCell does not create a String", destOne.toString().equals(""));
		// test MazeCells for unique identification String
		assertFalse("MazeCell does not create a unique identification String", destOne.toString().equals(destTwo.toString()));
	}
	
	/**
	 * Tests all methods that should throw an UninitializedObjectException when the MazeCell is invalid.
	 */
	@Test
	public void testExceptionThrowing() {
		MazeCell test = new MazeCell();
		// passages() test
		try {
			test.passages();
			fail("passages() method should have thrown an UninitializedObjectException");
		} catch(UninitializedObjectException e) {
			// test passed because exception was thrown
		}
		// passageTimeTo() test
		try {
			test.passageTimeTo(destOne);
			fail("passageTimeTo() method should have thrown an UninitializedObjectException");
		} catch(UninitializedObjectException e) {
			// test passed because exception was thrown
		}
		// connectedCells() test
		try {
			test.connectedCells();
			fail("connectedCells() method should have thrown an UninitializedObjectException");
		} catch(UninitializedObjectException e) {
			// test passed because exception was thrown
		}
		// isDeadEnd() test
		try {
			test.isDeadEnd();
			fail("isDeadEnd() method should have thrown an UninitializedObjectException");
		} catch(UninitializedObjectException e) {
			// test passed because exception was thrown
		}
	}
	
}
