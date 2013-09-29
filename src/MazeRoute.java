import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The MazeRoute class represents a single path in a maze composed of a list of MazeCells.
 * 
 * @author Derrick Tilsner dtt13
 *
 */
public class MazeRoute {
	// private class variables
	private static AtomicInteger mazeRouteInstances = new AtomicInteger();
	private int mazeRouteId; // used to differentiate MazeRoute objects
	private boolean isValid;
	private List<MazeCell> route;
	
	/**
	 * Constructor of the MazeRoute class which creates a new MazeRoute object
	 * and invalidates the route until a route has been added.
	 */
	public MazeRoute() {
		this.mazeRouteId = mazeRouteInstances.getAndIncrement();
		this.isValid = false;
	}
	
	/**
	 * Adds a route to the MazeRoute and checks that all cells along its path are valid.
	 * A route cannot be added to the MazeRoute more than once and cannot be removed once
	 * added. If the List is null, no route is added and the route remains invalid.
	 * 
	 * @param route - a List of MazeCells representing a series of passages in order of traversal
	 * @return true if the route was added, false if the route was not added
	 * @throws UninitializedObjectException if any MazeCell being added to the MazeRoute is invalid
	 */
	public boolean addCells(List<MazeCell> route) throws UninitializedObjectException {
		if(!isValid && route != null){ // copy the route if the route is already invalid
			copyCells(route);
			isValid = true;
			return true;
		} else { // don't copy the route if the route was already valid or the input was null
			return false;
		}
	}
	
	/**
	 * Checks if the MazeRoute has been validated. A MazeRoute is considered
	 * valid if it has added a route and all MazeCells along this route are valid.
	 * 
	 * @return true if the MazeRoute is valid, false otherwise
	 */
	public boolean isValid() {
		return isValid;
	}
	
	/**
	 * Generates a list of MazeCells to traverse representing a single route. This route
	 * may not be passable.
	 * 
	 * @return a List of MazeCells representing a series of passages in order of traversal
	 * @throws UninitializedObjectException only thrown if the MazeRoute is invalid
	 */
	public List<MazeCell> getCells() throws UninitializedObjectException {
		checkValidity();
		// copy the cells to a new List to avoid inadvertent changes to the route
		return new LinkedList<MazeCell>(route);
	}

	
	/**
	 * Calculates the time required to traverse the MazeRoute. The travel time will
	 * be equal to MazeCell.IMPASSABLE if the route is impassable or there is no route
	 * between two consecutive MazeCells.
	 * 
	 * @return the time needed to travel the MazeRoute
	 * @throws UninitializedObjectException only thrown if the MazeRoute is invalid
	 */
	public Integer travelTime() throws UninitializedObjectException {
		checkValidity();
		return calculateTravelTime(false);
	}
	
	/**
	 * Calculates the time required to traverse the MazeRoute where each passage
	 * may take between 1 and pre-specified travel time inclusively. The travel
	 * time will be equal to MazeCell.IMPASSABLE if the route is impassable or
	 * there is no route between two consecutive MazeCells.
	 * 
	 * @return the time needed to travel the MazeRoute
	 * @throws UninitializedObjectException only thrown if the MazeRoute is invalid
	 */
	public Integer travelTimeRandom() throws UninitializedObjectException {
		checkValidity();
		return calculateTravelTime(true);
	}
	
	/**
	 * Adds the input route to the MazeRoute and checks that all MazeCells being added
	 * are valid.
	 * 
	 * @param route - a List of MazeCells representing a series of passages in order of traversal
	 * @throws UninitializedObjectException if any MazeCell being added to the MazeRoute is invalid
	 */
	private void copyCells(List<MazeCell> route) throws UninitializedObjectException {
		// copies the List to avoid inadvertent changes
		this.route = new LinkedList<MazeCell>();
		for(MazeCell cell : route) {
			if(cell.isValid()) {
				this.route.add(cell);
			} else { // if at any point a cell is invalid, route is invalid and not updated
				this.route = null;
				throw new UninitializedObjectException();
			}
		}
	}

	/**
	 * Generates an exception if MazeRoute is invalid.
	 * 
	 * @throws UninitializedObjectException only thrown if the MazeRoute is invalid
	 */
	private void checkValidity() throws UninitializedObjectException {
		// generate an exception if maze route is invalid
		if(!isValid) {
			throw new UninitializedObjectException();
		}
	}
	
	/**
	 * Sums the travel time of taking a path through the maze. The travel time can be
	 * randomized by setting the isRandom parameter which adds a passage time between
	 * 1 and the actual travel time for that passage.
	 * 
	 * @param isRandom true to randomize travel times; false otherwise
	 * @return a travel time
	 * @throws UninitializedObjectException only thrown if a MazeCell is invalid
	 */
	private int calculateTravelTime(boolean isRandom) throws UninitializedObjectException { //TODO make this cleaner still
		int totalTime = 0;
		if(!route.isEmpty()) {
			// add up the travel time from one MazeCell to the next
			Iterator<MazeCell> routeIterate = route.iterator();
			MazeCell prevCell = routeIterate.next();
			MazeCell currentCell;
			while(routeIterate.hasNext()) {
				currentCell = routeIterate.next();
				int time = prevCell.passageTimeTo(currentCell);
				if(time != MazeCell.IMPASSABLE) { // only add times if the passage is passable
					if(isRandom) {
						time = (int)(Math.random() * time + 1);
					}
					totalTime += time;
				} else { // return impassable if one passage is impassable
					return MazeCell.IMPASSABLE;
				}
				// increment the prevCell for the next iteration
				prevCell = currentCell;
			}
		}
		return totalTime;
	}

	/**
	 * Generates a String only containing the route of MazeCells. The
	 * route must contain at least one MazeCell to be used properly.
	 * 
	 * @return a String representing the path of MazeCells in the route
	 */
	private String buildRouteString() {
		StringBuilder builder = new StringBuilder();
		ListIterator<MazeCell> routeIterate = route.listIterator();
		// add the first MazeCell in the path
		MazeCell cell = routeIterate.next();
		builder.append(cell);
		// add the remaining MazeCells
		while(routeIterate.hasNext()) {
			cell = routeIterate.next();
			builder.append(" -> ");
			builder.append(cell);
		}
		return builder.toString();
	}

	/**
	 * Creates a unique String for each MazeRoute instance.
	 * 
	 * @return a String representation of the MazeRoute
	 */
	@Override
	public String toString() {
		// build a String to show the path
		StringBuilder builder = new StringBuilder();
		builder.append("<MazeRoute ID " + mazeRouteId + ">: ");
		try {
			if(travelTime() == MazeCell.IMPASSABLE) {
				builder.append("no passage");
			} else if(route.isEmpty()) {
				builder.append("empty");
			} else { // add each MazeCell to the StringBuilder
				builder.append(buildRouteString());
			}
			return builder.toString();
		} catch(UninitializedObjectException e) {
			return "Uninitialized MazeRoute";
		}
	}

}
