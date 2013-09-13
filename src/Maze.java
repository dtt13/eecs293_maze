import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The MazeRoute class represents a collection of MazeCells that make up
 * the maze map.
 * 
 * @author Derrick Tilsner dtt13
 *
 */
public class Maze {
	// private class variables
	private boolean isValid;
	private Set<MazeCell> cells;
	
	/**
	 * Constructor of the Maze class which creates a new MazeRoute object
	 * and invalidates the route until a route has been added.
	 */
	public Maze() {
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
	public boolean addCells(Set<MazeCell> cells) throws UninitializedObjectException { //TODO McCabe?
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
	 *TODO
	 * 
	 * @param initialCell - the starting MazeCell of the route to be created
	 * @return a MazeRoute of one possible path through the Maze
	 * @throws UninitializedObjectException - only thrown if the Maze is invalid
	 */
	public MazeRoute routeFirst(MazeCell initialCell) throws UninitializedObjectException {
		validityCheck();
		List<MazeCell> path = routePath(initialCell, new LinkedList<MazeCell>());
		MazeRoute route = new MazeRoute();
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
	 * Recursively generates a route until a the mouse hits a dead end or
	 * visits the same sell twice.
	 * 
	 * @param cell - the MazeCell that the mouse is currently in
	 * @param path - a List of MazeCells that mouse has previously visited
	 * @return a List contain the new path
	 * @throws UninitializedObjectException - only
	 */
	private List<MazeCell> routePath(MazeCell cell, List<MazeCell> path)
			throws UninitializedObjectException {
		if(cell.isDeadEnd() || path.contains(cell)) { // if dead end or seen before
			path.add(cell);
		} else { // if never-before-seen cell
			path.add(cell);
			MazeCell passages[] = (MazeCell[])cell.connectedCells().toArray();
			int arbitraryPathIndex = (int)(passages.length * Math.random());
			MazeCell nextCell = passages[arbitraryPathIndex];
			path = routePath(nextCell, path);
		}
		return path;
	}
	
	@Override
	public String toString() {
		//TODO
		return "";
	}
}
