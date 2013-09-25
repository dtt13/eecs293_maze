import java.util.Iterator;
import java.util.Set;

/**
 * Implements a random next MazeCell selection algorithm.
 * 
 * @author Derrick Tilsner dtt13
 *
 */
public class RandomSelector implements PassageSelector {

	@Override
	public MazeCell nextCell(MazeCell currentCell)
			throws UninitializedObjectException {
		if(currentCell.isDeadEnd()) { // check that passages are available
			return null;
		}
		// use an Iterator to access the adjoining cells individually
		Set<MazeCell> connectedCells = currentCell.connectedCells();
		Iterator<MazeCell> nextCellIterate = connectedCells.iterator();
		// generate a random index
		int randomIndex = (int)(Math.random() * connectedCells.size()) + 1;
		// get the next cell
		MazeCell nextCell = null;
		for(int i = 0; i < randomIndex && nextCellIterate.hasNext(); i++) {
			nextCell = nextCellIterate.next();
		}
		return nextCell;
	}
	
}
