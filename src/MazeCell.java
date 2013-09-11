import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The MazeCell class represents a single cell in a maze and its passages to adjoining cells.
 * 
 * @author Derrick Tilsner dtt13
 * 
 */
public class MazeCell {
	// private class variables
	private static int numMazeCellDeclarations = 0;
	private int mazeCellId; // used to differentiate MazeCell objects
	private boolean isValidCell;
	private Map<MazeCell, Integer> passages;
	
	/**
	 * Constructor of the MazeCell class which creates a new MazeCell object
	 * and invalidates the cell until passages have been added.
	 */
	public MazeCell() {
		mazeCellId = ++numMazeCellDeclarations;
		this.isValidCell = false;
	}
	
	/**
	 * Adds passages to the MazeCell and validates the cell. Passages cannot be added to the
	 * MazeCell more than once and cannot be removed once added. If the Map is null, no passages
	 * are added and the cell remains invalid.
	 * 
	 * @param passages - a Map of adjoining MazeCells to their corresponding Integer time
	 * required to reach that cell
	 * @return true if the passages were added, false if the passages were not added
	 */
	public boolean addPassages(Map<MazeCell, Integer> passages) {
		if (!isValidCell && passages != null) { // copy the passages if the cell is invalid
			// copies the Map to avoid inadvertent changes
			this.passages = new HashMap<MazeCell, Integer>();
			for(MazeCell cell : passages.keySet()) {
				this.passages.put(cell, passages.get(cell));
			}
			isValidCell = true;
			return true;
		} else { // don't copy the passages if the cell is already valid or the input was null
			return false;
		}
	}

	/**
	 * Checks if the MazeCell has been validated. A MazeCell is considered
	 * valid if it has added passages.
	 * 
	 * @return true if the MazeCell has been validated, false otherwise
	 */
	public boolean isValid() {
		return isValidCell;
	}
	
	/**
	 * Generates a unique hash code integer for each MazeCell. The hash code for MazeCells
	 * with identical passages is the same.
	 * 
	 * @return a hash code for the MazeCell object
	 */
	@Override
	public int hashCode() {
		// use the Map's hashCode since identical Maps imply identical cells
		return passages.hashCode();
	}

	/**
	 * Generates a mapping of MazeCells to Integers that gives the travel time
	 * associated with taking a passage from 
	 * 
	 * @return a Map containing all the passable passages that maps from an adjoining
	 * MazeCell to its corresponding Integer time of travel from the current MazeCell
	 * object
	 * @throws UninitializedObjectException only thrown if the MazeCell is invalid
	 */
	public Map<MazeCell, Integer> passages() throws UninitializedObjectException {
		// generate an exception if this MazeCell is invalid
		if(!isValidCell) {
			throw new UninitializedObjectException();
		}
		// copy passages that are passable into a new Map to avoid inadvertent changes
		Map<MazeCell, Integer> map = new HashMap<MazeCell, Integer>();
		for(MazeCell cell : passages.keySet()) {
			Integer value = passages.get(cell);
			if(value.intValue() < Integer.MAX_VALUE) {
				map.put(cell, passages.get(cell));
			} /*else {
				// don't add the passage to the HashMap if it is impassable
			}*/
		}
		return map;
	}
	
	/**
	 * Determines the time required to travel from the current MazeCell to
	 * an adjoining MazeCell. If the MazeCells are not connected, then the
	 * passage is interpreted as impassable.
	 * 
	 * @param cell - the MazeCell to which the passage time is requested 
	 * @return the time to travel from this MazeCell to an adjoining one
	 * @throws UninitializedObjectException only thrown if the MazeCell is invalid
	 */
	public Integer passageTimeTo(MazeCell cell) throws UninitializedObjectException {
		// generate an exception if maze cell is invalid
		if(!isValidCell) {
			throw new UninitializedObjectException();
		}
		// use the Map of passages to find the time
		Integer time = passages.get(cell);
		if(time == null) { // if this key has no value, assume the passage is impassable
			time = Integer.MAX_VALUE;
		} /* else {
			// don't alter the time
		}*/
		return time;
		
	}

	/**
	 * Generates a Set of all directly-connected MazeCells. This Set does not include
	 * MazeCells for which the passages is impassable.
	 * 
	 * @return a Set of all directly-connected MazeCells
	 * @throws UninitializedObjectException only thrown if the MazeCell is invalid
	 */
	public Set<MazeCell> connectedCells() throws UninitializedObjectException {
		// generate an exception if maze cell is invalid
		if(!isValidCell) {
			throw new UninitializedObjectException();
		}
		// copy passages that are passable into a new Set to avoid inadvertent changes to the Map
		Set<MazeCell> set = new HashSet<MazeCell>();
		for(MazeCell cell : passages.keySet()) {
			Integer time = passages.get(cell);
			if(time.intValue() < Integer.MAX_VALUE) {
				set.add(cell);
			} /*else {
				// don't add the passage to the Set if it is impassable
			}*/
		}
		return set;
	}

	/**
	 * Determines if this MazeCell is a dead end meaning that there are no passable passages
	 * out of this cell.
	 * 
	 * @return true if MazeCell is a dead end, false otherwise
	 * @throws UninitializedObjectException only thrown if the MazeCell is invalid
	 */
	public boolean isDeadEnd() throws UninitializedObjectException {
		// generate an exception if maze cell is invalid
		if(!isValidCell) {
			throw new UninitializedObjectException();
		}
		// iterate through passages of this MazeCell
		for(MazeCell cell : passages.keySet()) {
			Integer time = passages.get(cell);
			if(time.intValue() < Integer.MAX_VALUE) { // return false as soon as a passable passage is found
				return false;
			}
		}
		// no passable passages were found, so only impassable ones are left meaning this is a deadend
		return true;
	}
	
	/**
	 * Creates a unique String for each MazeCell instance.
	 * 
	 * @return a String representation of the MazeCell
	 */
	@Override
	public String toString() {
		return "MazeCell ID " + mazeCellId;
	}
}
