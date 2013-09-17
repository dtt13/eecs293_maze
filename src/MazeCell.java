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
	// class constants
	/**
	 * Represents an impassable passage.
	 */
	public final static int IMPASSABLE = Integer.MAX_VALUE;
	
	/**
	 * Represents the status codes that Status can express.
	 */
	public static enum Code { //TODO where does this go?!
		OK, ALREADY_VALID, INVALID_TIME
	}
	
	// private class variables
	private static int numMazeCellDeclarations = 0;
	private int mazeCellId; // used to differentiate MazeCell objects
	private boolean isValid;
	private Map<MazeCell, Integer> passages;
	
	/**
	 * Constructor of the MazeCell class which creates a new MazeCell object
	 * and invalidates the cell until passages have been added.
	 */
	public MazeCell() {
		this.mazeCellId = ++numMazeCellDeclarations;
		this.isValid = false;
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
	public void addPassages(Map<MazeCell, Integer> passages, Status status) { //TODO McCabe!!!!
		if (!isValid && passages != null) { // copy the passages if the cell is invalid
			// copies the Map to avoid inadvertent changes
			this.passages = new HashMap<MazeCell, Integer>();
			for(MazeCell cell : passages.keySet()) {
				if(passages.get(cell) > 0) { // passage time is valid
					this.passages.put(cell, passages.get(cell));
				} else {
					this.passages = null;
					status.set(Code.INVALID_TIME);
					return;
				}
			}
			isValid = true;
			status.set(Code.OK);
		} else { // don't copy the passages if the cell is already valid or the input was null
			status.set(Code.ALREADY_VALID);
		}
	}

	/**
	 * Checks if the MazeCell has been validated. A MazeCell is considered
	 * valid if it has added passages.
	 * 
	 * @return true if the MazeCell has been validated, false otherwise
	 */
	public boolean isValid() {
		return isValid;
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
		validityCheck();
		// copy passages that are passable into a new Map to avoid inadvertent changes
		Map<MazeCell, Integer> map = new HashMap<MazeCell, Integer>();
		for(MazeCell cell : passages.keySet()) {
			Integer value = passages.get(cell);
			if(value.intValue() != IMPASSABLE) {
				map.put(cell, passages.get(cell));
			} else {
				// don't add the passage to the HashMap if it is impassable
			}
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
		validityCheck();
		// use the Map of passages to find the time
		Integer time = passages.get(cell);
		if(time == null) { // if this key has no value, assume the passage is impassable
			time = IMPASSABLE;
		} else {
			// don't alter the time
		}
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
		validityCheck();
		// copy passages that are passable into a new Set to avoid inadvertent changes to the Map
		Set<MazeCell> set = new HashSet<MazeCell>();
		for(MazeCell cell : passages.keySet()) {
			Integer time = passages.get(cell);
			if(time != IMPASSABLE) {
				set.add(cell);
			} else {
				// don't add the passage to the Set if it is impassable
			}
		}
		return set;
	}

	/**
	 * Determines if this MazeCell is a dead end meaning that there are no
	 * passable passages out of this cell.
	 * 
	 * @return true if MazeCell is a dead end, false otherwise
	 * @throws UninitializedObjectException only thrown if the MazeCell is invalid
	 */
	public boolean isDeadEnd() throws UninitializedObjectException {
		return connectedCells().isEmpty();
	}
	
	/**
	 * Generates an exception if the MazeCell is invalid.
	 * 
	 * @throws UninitializedObjectException only thrown if MazeCell is invalid
	 */
	private void validityCheck() throws UninitializedObjectException {
		if(!isValid) {
			throw new UninitializedObjectException();
		}
	}
	
	/**
	 * Generates a unique hash code integer for each instance of the MazeCell class.
	 * 
	 * @return a hash code for the MazeCell object
	 */
	@Override
	public int hashCode() {
		// use the MazeCell's Id since each instance will have a unique number
		return mazeCellId;
	}

	/**
	 * Creates a unique String for each MazeCell instance.
	 * 
	 * @return a String representation of the MazeCell
	 */
	@Override
	public String toString() { //TODO use <>?
		if(!isValid) {
			return "Uninitialized MazeCell";
		}
		return "MazeCell ID " + mazeCellId;
	}
	
	
	//TODO javadoc
	public class Status {
		// private class variables
		private Code code;
		
		public Status() {
			this.code = Code.OK;
		}
		
		public String getMessage() {
			switch(code) {
			case OK:
				return "";
			case ALREADY_VALID:
				return "The MazeCell is already valid and the passages cannot be updated";
			case INVALID_TIME:
				return "A non-positive travel time is invalid";
			default:
				//do something
				return "Error";
			}
		}
		
		public void set(Code code) {
			this.code = code;
		}
		
		public Code get() {
			return code;
		}
	}
}
