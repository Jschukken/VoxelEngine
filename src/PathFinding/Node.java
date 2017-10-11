package PathFinding;

public class Node {
	private Node parent;
	private int x;
	private int y;
	private int z;
	private int g;
	private int h;
	int f;
	
	public Node(int startX, int startY, int startZ, int startG, int startH) {
		x = startX;
		y = startY;
		z = startZ;
		g = startG;
		h = startH;
		f = g+h;
	}
}
