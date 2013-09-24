import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test case class for the MazeCell.Status class. This class tests all public
 * methods of the MazeCell.Status class for proper and expected functionality.
 * 
 * @author Derrick Tilsner dtt13
 *
 */
public class MazeCellStatusTest {
	// private class variables
	MazeCell.Status ok;
	MazeCell.Status alreadyValid;
	MazeCell.Status invalidTime;
	MazeCell.Status inputNull;
	
	/**
	 * Initializes a variety of Status instances with each possible status code.
	 */
	@Before
	public void createStatuses() {
		ok = new MazeCell.Status();
		ok.set(MazeCell.Status.Code.OK);
		alreadyValid = new MazeCell.Status();
		alreadyValid.set(MazeCell.Status.Code.ALREADY_VALID);
		invalidTime = new MazeCell.Status();
		invalidTime.set(MazeCell.Status.Code.INVALID_TIME);
		inputNull = new MazeCell.Status();
		inputNull.set(MazeCell.Status.Code.INPUT_NULL);
	}
	
	/**
	 * Tests the get() and set() methods for each status code and after initialization.
	 */
	@Test
	public void testStatusGetAndSet() {
		MazeCell.Status test = new MazeCell.Status();
		assertEquals("Status code is not initialized to OK", MazeCell.Status.Code.OK, test.get());
		assertEquals("Status code is not set to OK", MazeCell.Status.Code.OK, ok.get());
		assertEquals("Status code is not set to ALREADY_VALID", MazeCell.Status.Code.ALREADY_VALID, alreadyValid.get());
		assertEquals("Status code is not set to INVALID_TIME", MazeCell.Status.Code.INVALID_TIME, invalidTime.get());
		assertEquals("Status code is not set to INPUT_NULL", MazeCell.Status.Code.INPUT_NULL, inputNull.get());
	}
	
	/**
	 * Tests the getMessage() method for expected status messages.
	 */
	@Test
	public void testGetMessage() {
		assertEquals("OK status message does not match expected output",
				"The MazeCell is operating normally", ok.get().getMessage());
		assertEquals("ALREADY_VALID status message does not match expected output",
				"The MazeCell is already valid and the passages cannot be updated", alreadyValid.get().getMessage());
		assertEquals("INVALID_TIME status message does not match expected output",
				"A non-positive travel time is invalid", invalidTime.get().getMessage());
		assertEquals("INPUT_NULL status message does not match expected output",
				"A null Map is an unacceptable parameter", inputNull.get().getMessage());
	}
}
