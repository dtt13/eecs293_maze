/**
 * Provides an interface for a passage selection algorithm.
 * 
 * @author Derrick Tilsner dtt13
 *
 */
public interface PassageSelector {
	
	public MazeCell nextCell(MazeCell currentCell) throws UninitializedObjectException;
	
}