import java.util.Iterator;
import java.util.Set;

/**
 * Implements a first-available next MazeCell algorithm.
 * 
 * @author Derrick Tilsner dtt13
 *
 */
public class FirstSelector implements PassageSelector {

	@Override
	public MazeCell nextCell(MazeCell currentCell)
			throws UninitializedObjectException {
		if(currentCell.isDeadEnd()) { // check that passages are available
			return null;
		}
		// use an Iterator to access the adjoining cells individually
		Set<MazeCell> connectedCells = currentCell.connectedCells();
		Iterator<MazeCell> nextCellIterate = connectedCells.iterator();
		// get the first cell
		return nextCellIterate.next();
	}

}
