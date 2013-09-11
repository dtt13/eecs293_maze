import java.util.LinkedList;
import java.util.List;

/**
 * The MazeRoute class represents a single path in a maze composed of a list of MazeCells.
 * 
 * @author Derrick Tilsner dtt13
 *
 */
public class MazeRoute {
	// private class variables
	private static int numMazeRouteDeclarations = 0;
	private int mazeRouteId; // used to differentiate MazeRoute objects
	private boolean isValidRoute;
	private List<MazeCell> route;
	
	/**
	 * Constructor of the MazeRoute class which creates a new MazeRoute object
	 * and invalidates the route until a route has been added.
	 */
	public MazeRoute() {
		mazeRouteId = ++numMazeRouteDeclarations;
		this.isValidRoute = false;
	}
	
	/**
	 * Adds a route to the MazeRoute and checks that all cells along its path are valid.
	 * A route cannot be added to the MazeRoute more than once and cannot be removed once
	 * added. If the List is null, no route is added and the route remains invalid.
	 * 
	 * @param route - a List of MazeCells representing a series of passages in order of traversal
	 * @return true if the route was added, false if the route was not added
	 */
	public boolean addCells(List<MazeCell> route) {
		if(!isValidRoute && route != null){ // copy the route if the route is already invalid
			// copies the List to avoid inadvertent changes
			this.route = new LinkedList<MazeCell>();
			for(MazeCell cell : route) {
				if(cell.isValid()) {
					this.route.add(cell);
				} else { // if at any point a cell is invalid, route is invalid and not updated
					this.route = null;
					return false;
				}
			}
			isValidRoute = true;
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
		return isValidRoute;
	}
	
	/**
	 * Generates a list of MazeCells to traverse representing a single route. This route
	 * may not be passable.
	 * 
	 * @return a List of MazeCells representing a series of passages in order of traversal
	 * @throws UninitializedObjectException only thrown if the MazeRoute is invalid
	 */
	public List<MazeCell> getCells() throws UninitializedObjectException {
		// generate an exception if maze route is invalid
		if(!isValidRoute) {
			throw new UninitializedObjectException();
		}
		// copy the cells to a new List to avoid inadvertent changes to the route
		List<MazeCell> list = new LinkedList<MazeCell>();
		for(MazeCell cell : route) {
			list.add(cell);
		}
		return list;
	}
	
	/**
	 * Calculates the time required to traverse the MazeRoute. The travel time will
	 * be equal to Integer.MAX_VALUE if the route is impassable or there is no route
	 * between two consecutive MazeCells.
	 * 
	 * @return the time needed to travel the MazeRoute
	 * @throws UninitializedObjectException only thrown if the MazeRoute is invalid
	 */
	public Integer travelTime() throws UninitializedObjectException {
		// generate an exception if maze route is invalid
		if(!isValidRoute) {
			throw new UninitializedObjectException();
		}
		// add up the travel time from one MazeCell to the next
		int totalTime = 0;
		MazeCell prevCell = null;
		for(MazeCell cell : route) {
			if(prevCell == null) { // only taken the first iteration
				prevCell = cell;
				continue;
			}
			Integer time = prevCell.passageTimeTo(cell);
			if(time.intValue() < Integer.MAX_VALUE) { // only add times if the passage is passable
				totalTime += time.intValue();
			} else { // return impassable if one passage is impassable
				return new Integer(Integer.MAX_VALUE);
			}
			// increment the prevCell for the next iteration
			prevCell = cell;
		}
		return new Integer(totalTime);
	}
	
	/**
	 * Creates a unique String for each MazeRoute instance.
	 * 
	 * @return a String representation of the MazeRoute
	 * @throws UninitializedObjectException only thrown if the MazeRoute is invalid
	 */
	@Override
	public String toString() throws UninitializedObjectException {
		// generate an exception if maze route is invalid
		if(!isValidRoute) {
			throw new UninitializedObjectException();
		}
		// build a String to show the path
		StringBuilder sb = new StringBuilder();
		sb.append("MazeRoute ID " + mazeRouteId + ": ");
		if(route.isEmpty()) {
			sb.append("empty");
			return sb.toString();
		}
		// add each MazeCell to the StringBuilder
		for(MazeCell cell : route) {
			if(!route.get(0).equals(cell)) { // if its not the start of the route
				sb.append(" -> ");				
			} /* else {
				// don't append an arrow (->) at the start, for formating neatness
			}*/
			sb.append(cell);
		}
		return sb.toString();
	}
}
