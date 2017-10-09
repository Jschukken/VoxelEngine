package KNearest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Implementation for the K-Nearest algorithm. This class can be used to
 * classify new points and new points can be added as well.
 * 
 * @author s135755
 *
 */
public class KNearest {

	/* The lists of good and bad points, the current data */
	List<Point> points;

	/* The k of this KNearest implementation */
	int k;

	/**
	 * Constructor given a value for k
	 */
	public KNearest(int K) {
		this.k = K;
		this.points = new ArrayList<Point>();
	}
	
	/**
	 * Classifies a new point, so that it returns the class, good or bad.
	 * 
	 * @param p
	 *            the point to classify
	 * @return {@code true} if {@code p} is "good", {@code false} if {@code p} is
	 *         "bad"
	 */
	public boolean classify(Point p) {

		System.out.println("Classifying point: " + p.toString());
		
		/* Retrieve the list of k nearest points */
		List<Point> kNearestPoints = getKNearestPoints(p);

		/* Create two counters to count each class */
		int goodCount = 0;
		int badCount = 0;

		/**
		 * Get the classification from each point, and increment the correct counter
		 */
		System.out.println("Checking neighbors:");
		for (Point q : kNearestPoints) {
			System.out.println(q.toString());
			if (q.getClassification()) {
				goodCount++;
			} else {
				badCount++;
			}
		}

		/**
		 * If more good points are near, we classify the point as good, otherwise we
		 * classify as bad. In case of a tie, we classify the point as good. To avoid
		 * ties an uneven value for k is ideal.
		 */
		if (goodCount >= badCount) {
			p.setClassification(true);
			points.add(p);
			return true;
		} else {
			p.setClassification(false);
			points.add(p);
			return false;
		}

	}

	/**
	 * Classifies a new point, but now the coordinates are given instead of the
	 * point. This method calls classify(Point p) by creating a new point with the
	 * given coordinates.
	 * 
	 * @param coordsP
	 *            the coordinates to classify
	 * @return see classify(Point p)
	 */
	public boolean classify(List<Double> coordsP) {

		Point p = new Point(coordsP);
		return classify(p);

	}

	/**
	 * Adds a new data point to either the list of good or bad points. Given as well
	 * is the class of this point.
	 * 
	 * @param p
	 *            the point to add
	 * @param isGood
	 *            the class of the point
	 */
	public void addDataPoint(Point p) {

		points.add(p);

	}

	/**
	 * Removes a point from either the good or the bad list.
	 * 
	 * @param p
	 *            the point to remove
	 * @param isGood
	 *            whether the point is good or bad
	 */
	public void removePoint(Point p) {

		/* Get the iterator */
		Iterator it = points.iterator();

		/**
		 * Find the point in the list and remove if it is found
		 */
		while (it.hasNext()) {
			Point a = (Point) it.next();
			if (Point.equals(a, p)) {
				it.remove();
			}
		}

	}
	
	/**
	 * Getter for the list of all points
	 * @return  this.points
	 */
	public List<Point> getAllPoints() {
		return this.points;
	}

	/**
	 * Get the K nearest points to the given point p.
	 * 
	 * @param p
	 *            the point to find the neighbors of
	 */
	private List<Point> getKNearestPoints(Point p) {

		/**
		 * Store all the distances to the other points in an array.
		 */
		Map<Integer, Double> distances = new HashMap<Integer, Double>();
		for (int i = 0; i < points.size(); i++) {
			distances.put(i, Point.getDistance(p, points.get(i)));
		}

		/* Store the result */
		List<Point> result = new ArrayList<Point>();

		/**
		 * Loop k times to find the point with the smallest distance and add it to the
		 * result
		 */
		for (int i = 0; i < k; i++) {

			double minDist = Double.MAX_VALUE;

			for (int j : distances.keySet()) {
				if (distances.get(j) < minDist) {
					minDist = distances.get(j);
					result.add(points.get(j));
				}
			}

		}

		return result;

	}

}
