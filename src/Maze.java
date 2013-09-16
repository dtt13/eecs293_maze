import java.util.HashSet;
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
	private int mazeId;
	private boolean isValid;
	private Set<MazeCell> cells;
	
	/**
	 * Constructor of the Maze class which creates a new MazeRoute object
	 * and invalidates the route until a route has been added.
	 */
	public Maze() {
		this.mazeId = ++numMazeDeclarations;
		this.isValid = false;
	}
	
	/**
	 * Adds cells to the Maze and checks that all cells in the maze are valid.
	 * A set of cells cannot be added to the MazeRoute more than once and cannot be removed once
	 * added. If the Set is null, no cells are added and the maze remains invalid.
	 * 
	 * @param route - a Set of MazeCells representing cells of the maze
	 * @return true if the cells were added, false if the cells were not added
	 * @throws UninitializedObjectException if any MazeCell being added to the Maze is invalid
	 */
	public boolean addCells(Set<MazeCell> cells) throws UninitializedObjectException {
		if(!isValid && cells != null){ // copy the cells if the maze is already invalid
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
	 * Generates an arbitrary MazeRoute through the Maze starting at the specified
	 * MazeCell and moving to adjoining MazeCells. If the MazeCell is a dead end or
	 * the MazeCell has already been visited, the routine exits and the MazeRoute is
	 * returned.
	 * 
	 * @param initialCell - the starting MazeCell of the route to be created
	 * @return a MazeRoute of one possible path through the Maze
	 * @throws UninitializedObjectException - only thrown if the Maze is invalid
	 */
	public MazeRoute routeFirst(MazeCell initialCell) throws UninitializedObjectException {
		validityCheck();
		MazeRoute route = new MazeRoute();
		List<MazeCell> path = routePath(initialCell, new LinkedList<MazeCell>());
		route.addCells(path);
		return route;
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
	 * Recursively generates a route until the mouse hits a dead end or
	 * visits the same sell twice.
	 * 
	 * @param cell - the MazeCell that the mouse is currently in
	 * @param path - a List of MazeCells that mouse has previously visited
	 * @return a List contain the new path
	 * @throws UninitializedObjectException - only
	 */
	private List<MazeCell> routePath(MazeCell cell, List<MazeCell> path)
			throws UninitializedObjectException {
		// base case : if cell is a dead cell, has been seen before, or isn't in the Maze
		if(cell.isDeadEnd() || path.contains(cell) || !cells.contains(cell)) {
			path.add(cell);	
		} else { // if never-before-seen cell
			path.add(cell);
			Object passages[] = cell.connectedCells().toArray();
//			int arbitraryPathIndex = (int)(passages.length * Math.random());
			int arbitraryPathIndex = 0;
			MazeCell nextCell = (MazeCell)passages[arbitraryPathIndex];
			path = routePath(nextCell, path);
		}
		return path;
	}
	
	/**
	 * Generates a unique String for a specified MazeCell containing information
	 * on its adjoining cells and the time to reach each of them. 
	 * 
	 * @param cell - the MazeCell to create a String for
	 * @return a String contain information of the MazeCell and its adjoining cells
	 * @throws UninitializedObjectException if the MazeCell is invalid
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
		builder.append("Maze ID " + mazeId + ":\n");
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
