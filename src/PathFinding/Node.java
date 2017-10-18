package PathFinding;

/**
 * nodes in the A* algorithm
 * 
 * @author Chiel Ton
 *
 */
public class Node {
	Node p;
	int x;
	int y;
	double g;
	double h;
	double f;

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
		g = parent.g + 1;
		h = AStar.calculateH(x, y);
		f = g + h;
		p = parent;
	}
}
