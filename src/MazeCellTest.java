import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Test case class for the MazeCell class. This class tests all public methods
 * of the MazeCell class for proper and expected functionality.
 * 
 * @author Derrick Tilsner dtt13
 *
 */
public class MazeCellTest {
	// private class variables
	private Map<MazeCell, Integer> passages;
	private MazeCell destOne;
	private MazeCell destTwo;
	private MazeCell destThree;
	private MazeCell srcOne;
	private MazeCell srcTwo;
	private MazeCell srcThree;
	private MazeCell srcFour;
	private MazeCell srcFive;
	
	/**
	 * Initializes a variety of MazeCells to be used throughout the testing
	 * of the MazeCell class.
	 */
	@Before
	public void initializeMazeCells() {
		// create destination cells
		// note that all destination cells will be dead ends for simplicity
		destOne = new MazeCell();
		destOne.addPassages(new HashMap<MazeCell, Integer>(), new MazeCell.Status());
		destTwo = new MazeCell();
		destTwo.addPassages(new HashMap<MazeCell, Integer>(), new MazeCell.Status());
		destThree = new MazeCell();
		destThree.addPassages(new HashMap<MazeCell, Integer>(), new MazeCell.Status());
		// create source cells
		// srcOne has two different destinations
		passages = new HashMap<MazeCell, Integer>();
		passages.put(destOne, new Integer(1));
		passages.put(destTwo, new Integer(2));
		srcOne = new MazeCell();
		srcOne.addPassages(passages, new MazeCell.Status());
		// srcTwo has as a destination to srcThree and vice versa
		srcTwo = new MazeCell();
		srcThree = new MazeCell();
		Map<MazeCell, Integer> map = new HashMap<MazeCell, Integer>();
		map.put(srcThree, new Integer(3));
		srcTwo.addPassages(map, new MazeCell.Status());
		map = new HashMap<MazeCell, Integer>();
		map.put(srcTwo, new Integer(2));
		// srcFour will be different from srcOne and will contain an impassable connection
		map = new HashMap<MazeCell, Integer>();
		map.put(destOne, new Integer(MazeCell.IMPASSABLE));
		map.put(destThree, new Integer(3));
		srcFour = new MazeCell();
		srcFour.addPassages(map, new MazeCell.Status());
		// srcFive will only contain impassable connections
		map = new HashMap<MazeCell, Integer>();
		map.put(destOne, new Integer(MazeCell.IMPASSABLE));
		map.put(destTwo, new Integer(MazeCell.IMPASSABLE));
		map.put(destThree, new Integer(MazeCell.IMPASSABLE));
		srcFive = new MazeCell();
		srcFive.addPassages(map, new MazeCell.Status());
	}
	
	/**
	 * Tests the addPassages() method with valid and invalid inputs.
	 */
	@Test
	public void testAddPassages() {
		// test with null Map
		MazeCell test = new MazeCell();
		MazeCell.Status testStatus = new MazeCell.Status();
		test.addPassages(null, testStatus);
		assertEquals("Null Map should not be added as passages",
				MazeCell.Status.Code.INPUT_NULL, testStatus.get());
		// test adding a non-null Map
		test = new MazeCell();
		test.addPassages(new HashMap<MazeCell, Integer>(), testStatus);
		assertEquals("Did not properly add passages Map",
				MazeCell.Status.Code.OK, testStatus.get());
		// test adding a Map to an already valid cell
		test.addPassages(new HashMap<MazeCell, Integer>(), testStatus);
		assertEquals("MazeCell should not be able to add additional Maps to valid cells",
				MazeCell.Status.Code.ALREADY_VALID, testStatus.get());
		// test adding a Map with a non-positive travel time
		Map<MazeCell, Integer> map = new HashMap<MazeCell, Integer>();
		map.put(new MazeCell(), new Integer(-3));
		test = new MazeCell();
		test.addPassages(map, testStatus);
		assertEquals("MazeCell should not be able to add passages with non-positive travel time",
				MazeCell.Status.Code.INVALID_TIME, testStatus.get());
	}
	
	/**
	 * Tests the isValid() method with valid and invalid inputs.
	 * 
	 */
	@Test
	public void testIsValid() {
		// test initialization
		MazeCell test = new MazeCell();
		assertFalse("MazeCell should be initialized to be invalid", test.isValid());
		// test adding a non-null Map
		test.addPassages(new HashMap<MazeCell, Integer>(), new MazeCell.Status());
		assertTrue("MazeCell should be valid after adding passages", test.isValid());
		// test adding a Map to an already valid cell
		test.addPassages(new HashMap<MazeCell, Integer>(), new MazeCell.Status());
		assertTrue("MazeCell should be valid even if user attempts to add additional Maps to a valid cell",
				test.isValid());
		// test adding a Map with a non-positive travel time
		Map<MazeCell, Integer> map = new HashMap<MazeCell, Integer>();
		map.put(new MazeCell(), new Integer(-3));
		test = new MazeCell();
		test.addPassages(map, new MazeCell.Status());
		assertFalse("MazeCell should be invalid if travel time is non-positive", test.isValid());
	}
	
	/**
	 * Tests the hashCode() method for multiple MazeCell objects.
	 */
	@Test
	public void testHashCode() {
		// test it two MazeCells are different
		assertFalse("hashCode() of different MazeCells should not be the same",
				srcOne.hashCode() == srcFour.hashCode());
		// test when MazeCells reference each other
		assertFalse("hashCode() of MazeCells that reference each other have an endless loop",
				srcTwo.hashCode() == srcThree.hashCode());
	}
	
	/**
	 * Tests the passages() method assuming valid MazeCells.
	 */
	@Test
	public void testPassages() {
		try {
			// test with no impassable passages
			assertEquals("passages() method does not return the correct passages Map",
					passages, srcOne.passages());
			// test with all impassable passages
			assertEquals("passages() method does not return the correct passages Map",
					new HashMap<MazeCell, Integer>(), srcFive.passages());
			// test with some impassable passages
			Map<MazeCell, Integer> map = new HashMap<MazeCell, Integer>();
			map.put(destThree, new Integer(3));
			assertEquals("passages() method does not return the correct passages Map",
					map, srcFour.passages());
		} catch(UninitializedObjectException e) {
			fail("passages() method generated an UninitializedObjectException incorrectly");
		}
	}
	
	/**
	 * Tests the passageTimeTo() method assuming valid MazeCells.
	 */
	@Test
	public void testPassageTimeTo() {
		try {
			// test a MazeCell that is passable
			assertEquals("passageTimeTo() method does not return the correct time",
					new Integer(3), srcFour.passageTimeTo(destThree));
			// test a MazeCell this is impassable
			assertEquals("passageTimeTo() method does not return the correct time",
					new Integer(MazeCell.IMPASSABLE), srcFour.passageTimeTo(destOne));
			// test a MazeCell that has no passage
			assertEquals("passageTimeTo() method does not handle non-adjoining MazeCell inputs",
					new Integer(MazeCell.IMPASSABLE), srcFour.passageTimeTo(destTwo));
		} catch(UninitializedObjectException e) {
			fail("passageTimeTo() method generated an UninitializedObjectException incorrectly");
		}
	}
	
	/**
	 * Tests the connectedCells() method assuming valid MazeCells.
	 */
	@Test
	public void testConnectedCells() {
		try {
			// test with no impassable passages
			assertEquals("connectedCells() method does not return the correct MazeCells",
					passages.keySet(), srcOne.connectedCells());
			// test with all impassable passages
			assertEquals("connectedCells() method returns MazeCells that shouldn't be passable",
					new HashSet<MazeCell>(), srcFive.connectedCells());
			// test with some impassable passages
			Set<MazeCell> set = new HashSet<MazeCell>();
			set.add(destThree);
			assertEquals("connectedCells() method does not return the correct MazeCells",
					set, srcFour.connectedCells());
		} catch(UninitializedObjectException e) {
			fail("connectedCells() method generated an UninitializedObjectException incorrectly");
		}
	}
	
	/**
	 * Tests the isDeadEnd() method assuming valid MazeCells.
	 */
	@Test
	public void testIsDeadEnd() {
		try {
			// test with cell that is not a dead end
			assertFalse("isDeadEnd() is incorrect when not a dead end", srcOne.isDeadEnd());
			// test with no passages
			assertTrue("isDeadEnd() is incorrect when there is a dead end", destOne.isDeadEnd());
			// test with all impassable passages
			assertTrue("isDeadEnd() is incorrect when there is a dead end", srcFive.isDeadEnd());
			// test with some impassable passages
			assertFalse("isDeadEnd() is incorrect when not a dead end", srcFour.isDeadEnd());
		} catch(UninitializedObjectException e) {
			fail("isDeadEnd() method generated an UninitializedObjectException incorrectly");
		}
	}
	
	/**
	 * Tests the toString() method for unique identification Strings assuming valid MazeCells.
	 */
	@Test
	public void testToString() {
		// test String is not empty
		assertFalse("MazeCell does not create a String", destOne.toString().equals(""));
		// test MazeCells for unique identification String
		assertFalse("MazeCell does not create a unique identification String",
				destOne.toString().equals(destTwo.toString()));
		// test for invalid MazeCell String
		MazeCell test = new MazeCell();
		assertTrue("MazeCell does not generate the correct message when invalid",
				test.toString().equals("Uninitialized MazeCell"));
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
