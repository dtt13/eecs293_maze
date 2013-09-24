import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The Maze class represents a collection of MazeCells that make up
 * the maze map.
 * 
 * @author Derrick Tilsner dtt13
 *
 */
public class Maze {
	// private class variables
	private static int numMazeDeclarations = 0;
	private int mazeId; // used to differentiate Maze objects
	private boolean isValid;
	private Set<MazeCell> cells;
	
	/**
	 * Constructor of the Maze class which creates a new MazeRoute object
	 * and invalidates the route until a route has been added.
	 */
	public Maze() {
		//TODO thread safe
		this.mazeId = ++numMazeDeclarations;
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
		validityCheck();
		MazeRoute route = new MazeRoute();
		List<MazeCell> path = routePath(initialCell, null, false);
		route.addCells(path);
		return route;
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
		validityCheck();
		MazeRoute route = new MazeRoute();
		List<MazeCell> path = routePath(initialCell, null, true);
		route.addCells(path);
		return route;
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
	private void validityCheck() throws UninitializedObjectException {
		if(!isValid) {
			throw new UninitializedObjectException();
		}
	}
	
	/**
	 * Recursively generates a route until the mouse hits a dead end, leaves
	 * the maze, or visits the same cell twice. The next MazeCell along the path
	 * is chosen by using the first adjoining MazeCell found or randomly,
	 * depending on the whether the isRandom parameter is set. For the initial call
	 * to this method, the path should be null.
	 * 
	 * @param cell - the MazeCell that the mouse is currently in
	 * @param path - a List of MazeCells that mouse has previously visited
	 * @param isRandom - true if choosing a random passage; false if choosing the first path
	 * @return a List containing the new path
	 * @throws UninitializedObjectException only thrown if the Maze is invalid
	 */
	private List<MazeCell> routePath(MazeCell cell, List<MazeCell> path, boolean isRandom)
			throws UninitializedObjectException {
		path = initializePathIfNull(path);
		// base cases : if cell is a dead cell, has been seen before, or isn't in the Maze
		if(!cells.contains(cell)){
			path.clear();
		} else if(cell.isDeadEnd() || path.contains(cell)) {
			path.add(cell);	
		} else { // if never-before-seen cell
			path.add(cell);
			MazeCell nextCell = getNextMazeCell(cell, isRandom);
			path = routePath(nextCell, path, isRandom);
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
	 * Finds an adjoining MazeCell given the current cell. The current MazeCell must be a
	 * member of the Maze and must not be a dead end for this method to be used properly.
	 * If the getRandom boolean parameter is set, the method will return a random
	 * adjoining MazeCell. If the getRandom parameter is not set, the method will pick
	 * the first adjoining MazeCell found.
	 * 
	 * @param cell - the current MazeCell
	 * @param getRandom - true if picking a random passage;
	 * 		false if picking the first passage
	 * @return an adjoining MazeCell
	 * @throws UninitializedObjectException only thrown if the Maze is invalid
	 */
	private MazeCell getNextMazeCell(MazeCell cell, boolean isRandom)
			throws UninitializedObjectException {
		// use an Iterator to access the adjoining cells individually
		Set<MazeCell> connectedCells = cell.connectedCells();
		Iterator<MazeCell> nextCellIterate = connectedCells.iterator();
		// get the first cell
		MazeCell nextCell = nextCellIterate.next();
		if(isRandom) {
			// generate a random index
			int randomIndex = (int)(Math.random() * connectedCells.size());
			for(int i = 0; i < randomIndex && nextCellIterate.hasNext(); i++) {
				nextCell = nextCellIterate.next();
			}
		}
		return nextCell;
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
			validityCheck();
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
