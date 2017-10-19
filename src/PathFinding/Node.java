package PathFinding;

/**
 * nodes in the A* algorithm
 * 
 * @author Chiel Ton
 */
public class Node {
	Node p; // parent node
	int x; // x coordinate
	int y; // y coordinate
	double g; // accumulated till this point
	double h; // estimated cost till end
	double f; // g+h

	/**
	 * constructor for a starting node (no parent)
	 * 
	 * @param startX
	 *            x coordinate
	 * @param startY
	 *            y coordinate
	 * @param startG
	 *            current distance to the node
	 */
	public Node(int startX, int startY) {
		x = startX;
		y = startY;
		g = 0;
		h = AStar.calculateH(x, y);
		f = g + h;
		p = null;
	}

	/**
	 * constructor for a normal node
	 * 
	 * @param startX
	 *            x coordinate
	 * @param startY
	 *            y coordinate
	 * @param startG
	 *            current distance to the node
	 * @param parent
	 *            the parent node from this node
	 */
	public Node(int startX, int startY, Node parent) {
		x = startX;
		y = startY;
		g = parent.g + 1 + AStar.getDeaths(x,y);
		h = AStar.calculateH(x, y);
		f = g + h;
		p = parent;
	}
}
