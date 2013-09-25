import java.util.Iterator;
import java.util.Set;

/**
 * Implements a greedy next MazeCell selection algorithm.
 * 
 * @author Derrick Tilsner dtt13
 *
 */
public class GreedySelector implements PassageSelector {

	@Override
	public MazeCell nextCell(MazeCell currentCell) 
			throws UninitializedObjectException {
		if(currentCell.isDeadEnd()) { // check that passages are available
			return null;
		}
		// use an Iterator to access the adjoining cells individually
		Set<MazeCell> connectedCells = currentCell.connectedCells();
		Iterator<MazeCell> nextCellIterate = connectedCells.iterator();
		// find the next cell with the shortest passage time
		MazeCell nextCell = null;
		MazeCell tmpCell = null;
		int minimumPassageTime = MazeCell.IMPASSABLE;
		while(nextCellIterate.hasNext()) {
			tmpCell = nextCellIterate.next();
			int tmpPassageTime = currentCell.passageTimeTo(tmpCell);
			if(tmpPassageTime < minimumPassageTime) {
				nextCell = tmpCell;
				minimumPassageTime = tmpPassageTime;
			}
		}
		return nextCell;
	}

}
