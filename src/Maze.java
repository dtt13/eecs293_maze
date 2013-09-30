import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Maze class represents a collection of MazeCells that make up
 * the maze map.
 * 
 * @author Derrick Tilsner dtt13
 *
 */
public class Maze {
	// private class variables
	private static AtomicInteger mazeInstances = new AtomicInteger();
	private int mazeId; // used to differentiate Maze objects
	private boolean isValid;
	private Set<MazeCell> cells;
	
	/**
	 * Constructor of the Maze class which creates a new MazeRoute object
	 * and invalidates the route until a route has been added.
	 */
	public Maze() {
		this.mazeId = mazeInstances.getAndIncrement();
		this.isValid = false;
	}
	
	/**
	 * Adds cells to the Maze and checks that all cells in the maze are valid.
	 * A set of cells cannot be added to the Maze more than once and cannot be removed once
	 * added. If the Set is null, no cells are added and the maze remains invalid.
	 * 
	 * @param cells - a Set of MazeCells representing cells of the maze
	 * @return true if the cells were added, false if the cells were not added
	 * @throws UninitializedObjectException if any MazeCell being added to the Maze is invalid
	 */
	public boolean addCells(Set<MazeCell> cells) throws UninitializedObjectException {
		if(!isValid && cells != null){ // copy the cells if the maze is already invalid
			copyCells(cells);
			isValid = true;
			return true;
		} else { // don't copy the cells if the maze was already valid or the input was null
			return false;
		}
	}

	/**
	 * Checks if the Maze has been validated. A Maze is considered valid if it
	 * has added cells to the maze and all MazeCells are valid.
	 * 
	 * @return true if the Maze is valid, false otherwise
	 */
	public boolean isValid() {
		return isValid;
	}
	
	/**
	 * Generates a MazeRoute through the Maze starting at the specified MazeCell 
	 * and moving to adjoining MazeCells. The next cell along the path is chosen by
	 * taking the first passage found to an adjoining cell. If a dead end is reached or
	 * a MazeCell has already been visited, the routine exits and the MazeRoute is
	 * returned.
	 * 
	 * @param initialCell - the starting MazeCell of the route to be created
	 * @return a MazeRoute of one possible path through the Maze
	 * @throws UninitializedObjectException only thrown if the Maze is invalid
	 */
	public MazeRoute routeFirst(MazeCell initialCell) throws UninitializedObjectException {
		return route(initialCell, new FirstSelector());
	}
	
	/**
	 * Generates a MazeRoute through the Maze starting at the specified MazeCell 
	 * and moving to adjoining MazeCells. The next cell along the path is chosen 
	 * randomly among adjoining cell. If a dead end is reached or a MazeCell has
	 * already been visited, the routine exits and the MazeRoute is returned.
	 * 
	 * @param initialCell - the starting MazeCell of the route to be created
	 * @return a MazeRoute of one possible path through the Maze
	 * @throws UninitializedObjectException only thrown if the Maze is invalid
	 */
	public MazeRoute routeRandom(MazeCell initialCell) throws UninitializedObjectException {
		return route(initialCell, new RandomSelector());
	}
	
	/**
	 * Generates a MazeRoute through the Maze starting at the specified MazeCell 
	 * and moving to adjoining MazeCells. The next cell along the path is chosen 
	 * greedily meaning the passage with the shortest travel time. If a dead end
	 * is reached or a MazeCell has already been visited, the routine exits and
	 * the MazeRoute is returned.
	 * 
	 * @param initialCell - the starting MazeCell of the route to be created
	 * @return a MazeRoute of one possible path through the Maze
	 * @throws UninitializedObjectException only thrown if the Maze is invalid
	 */
	public MazeRoute routeGreedy(MazeCell initialCell) throws UninitializedObjectException {
		return route(initialCell, new GreedySelector());
	}
	
	/**
	 * Generates a MazeRoute through the Maze starting at the specified MazeCell 
	 * and moving to adjoining MazeCells. The next cell along the path is chosen 
	 * based on the PassageSelector's implementation of the nextCell() method. If
	 * a dead end is reached or a MazeCell has already been visited, the routine
	 * exits and the MazeRoute is returned. An empty MazeRoute is returned if
	 * the PassageSelector is null or a cell is not in the Maze.
	 * 
	 * @param initialCell - the starting MazeCell of the route to be created
	 * @param passageSelector - implementation of the next cell algorithm
	 * @return a MazeRoute of one possible path through the Maze
	 * @throws UninitializedObjectException only thrown if the Maze is invalid
	 */
	public MazeRoute route(MazeCell initialCell, PassageSelector passageSelector)
			throws UninitializedObjectException {
		checkValidity();
		MazeRoute route = new MazeRoute();
		List<MazeCell> path;
		path = routePath(initialCell, null, null, passageSelector);
		route.addCells(path);
		return route;
	}
	
	/**
	 * Calculates the average time to exit the Maze from every other cell
	 * in the maze, given a specific PassageSelector. In the case that one
	 * time to reach the exit MazeCell is IMPASSABLE or the PassageSelector
	 * is null, the average time returned is IMPASSABLE_DOUBLE. One should
	 * note that even if a passable path exists, the PassageSelector may not
	 * find it and may return IMPASSABLE_DOUBLE.
	 * 
	 * @param outside - the exit to the maze
	 * @param passageSelector - implementation of the next cell algorithm
	 * @return the average time to reach the exit from all other cells in the maze
	 * @throws UninitializedObjectException only
	 */
	public Double averageExitTime(MazeCell outside, PassageSelector passageSelector)
			throws UninitializedObjectException {
		checkValidity();
		int totalTime = 0;
		int numPaths = 0;
		for(MazeCell cell : cells) {
			if(cell != outside) {
				MazeRoute route = new MazeRoute();
				List<MazeCell> path = routePath(cell, outside, null, passageSelector);
				route.addCells(path);
				int time = route.travelTime();
				if(time != MazeCell.IMPASSABLE && path.contains(outside)) {
					totalTime += time;
					numPaths++;
				} else {
					return MazeCell.IMPASSABLE_DOUBLE;
				}
			}
		}
		return (double)(totalTime / numPaths);
	}
	
	/**
	 * Adds the input cells to the Maze and checks that all MazeCells being added are
	 * valid.
	 * 
	 * @param cells - a Set of MazeCells representing cells of the maze
	 * @throws UninitializedObjectException
	 */
	private void copyCells(Set<MazeCell> cells) throws UninitializedObjectException {
		// copies the List to avoid inadvertent changes
		this.cells = new HashSet<MazeCell>();
		for(MazeCell cell : cells) {
			if(cell.isValid()) {
				this.cells.add(cell);
			} else { // if at any point a cell is invalid, maze is invalid and not updated
				this.cells = null;
				throw new UninitializedObjectException();
			}
		}
	}
	
	/**
	 * Generates an exception if Maze is invalid.
	 * 
	 * @throws UninitializedObjectException only thrown if the Maze is invalid
	 */
	private void checkValidity() throws UninitializedObjectException {
		if(!isValid) {
			throw new UninitializedObjectException();
		}
	}
	
	/**
	 * Recursively generates a route until the mouse hits a dead end, leaves
	 * the maze, or visits the same cell twice. The next MazeCell along the path
	 * is chosen according to the PassageSelector specified. For the initial call
	 * to this method, the path should be null.
	 * 
	 * @param cell - the MazeCell that the mouse is currently in
	 * @param outside - the MazeCell indicating the end of the maze
	 * @param path - a List of MazeCells that mouse has previously visited
	 * @param passageSelector - a PassageSelector that specifies the 
	 * @return a List containing the new path
	 * @throws UninitializedObjectException only thrown if the Maze is invalid
	 */
	private List<MazeCell> routePath(MazeCell cell, MazeCell outside, List<MazeCell> path, PassageSelector passageSelector)
			throws UninitializedObjectException {
		path = initializePathIfNull(path);
		if(!isRouteBaseCase(cell, outside, path) && passageSelector != null) {
			// if never-before-seen cell
			path.add(cell);
			MazeCell nextCell = passageSelector.nextCell(cell);
			if(nextCell != null) {
				path = routePath(nextCell, outside, path, passageSelector);
			} else {
				// don't recurse since there is nowhere else to go (dead end)
			}
		}
		return path;
	}

	/**
	 * Creates an empty path if the input is null. Otherwise, the path is
	 * unaltered and returned.
	 * 
	 * @param path - a potentially-null List
	 * @return a non-null List that is identical to the input if the input is not null
	 */
	private List<MazeCell> initializePathIfNull(List<MazeCell> path) {
		if(path == null) {
			path = new LinkedList<MazeCell>();
		}
		return path;
	}
	
	/**
	 * Checks for a base case and changes the path accordingly.
	 * 
	 * @param cell - the MazeCell that the mouse is currently in
	 * @param outside - the MazeCell indicating the end of the maze
	 * @param path - a List of MazeCells that the mouse has previously visited
	 * @return true if base case for routePath(); false otherwise
	 */
	private boolean isRouteBaseCase(MazeCell cell, MazeCell outside, List<MazeCell> path) {
		if(!cells.contains(cell)) {
			// base case: cell isn't in the Maze
			path.clear();
			return true;
		}
		if(path.contains(cell) || cell == outside) {
			// base case: cell has been visited before or is exit to the maze
			path.add(cell);
			return true;
		}
		return false;
	}
	
	/**
	 * Generates a unique String for a specified MazeCell containing information
	 * on its adjoining cells and the time to reach each of them. 
	 * 
	 * @param cell - the MazeCell to create a String for
	 * @return a String contain information of the MazeCell and its adjoining cells
	 * @throws UninitializedObjectException only thrown if the MazeCell is invalid
	 */
	private String buildCellString(MazeCell cell) throws UninitializedObjectException {
		StringBuilder builder = new StringBuilder();
		if(cell.isDeadEnd()) {
			builder.append(cell + " -> dead end\n");
		} else {
			for(MazeCell neighbor : cell.connectedCells()) {
				Integer time = cell.passageTimeTo(neighbor);
				builder.append(cell + " -" + time + "-> " + neighbor + "\n");
			}
		}
		return builder.toString();
	}
	
	/**
	 * Creates a unique String for each Maze instance.
	 * 
	 * @return a String representation of the Maze
	 */
	@Override
	public String toString() {
		// build a String to show the entire Maze
		StringBuilder builder = new StringBuilder();
		builder.append("<Maze ID " + mazeId + ">:\n");
		try {
			checkValidity();
			if(cells.isEmpty()) {
				builder.append("empty");
			} else {
				// add each MazeCell in the Maze to the StringBuilder
				for(MazeCell cell : cells) {
					builder.append(buildCellString(cell));
				}
			}
			return builder.toString();
		} catch (UninitializedObjectException e) {
			return "Uninitialized Maze";
		}
	}
}
