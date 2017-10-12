package KNearest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

	/* The name of the file this object can save its training data in */
	String fileName = "resources\\res\\knTrainingData.txt";

	/**
	 * Constructor given a value for k
	 */
	public KNearest(int K) {
		this.k = K;
		this.points = new ArrayList<Point>();
		try {
			readTrainingDataFromFile();
		} catch (IOException e){
			System.out.println("Could not read training data!");
		};
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

		/* Retrieve the list of k nearest points */
		List<Point> kNearestPoints = getKNearestPoints(p);

		/* Create two counters to count each class */
		int goodCount = 0;
		int badCount = 0;

		/**
		 * Get the classification from each point, and increment the correct counter
		 */
		for (Point q : kNearestPoints) {
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
	 * Removes all points from the points list
	 */
	public void clearPoints() {
		points = new ArrayList<Point>();
	}

	/**
	 * Getter for the list of all points
	 * 
	 * @return this.points
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
		for (int i = 0; i < Math.min(k, distances.size()); i++) {

			double minDist = Double.MAX_VALUE;
			int indexOfSmallest = 0;

			for (int j : distances.keySet()) {
				if (distances.get(j) < minDist) {
					minDist = distances.get(j);
					indexOfSmallest = j;
				}
			}

			distances.remove(indexOfSmallest);
			result.add(points.get(indexOfSmallest));

		}

		return result;

	}

	/**
	 * Writes the current set of points in the file
	 * 
	 * @throws IOException  if some IO error occurs, prints stacktrace
	 */
	void writeTrainingDataToFile() throws IOException {
		
		/* Use standard file name */
	    Path path = Paths.get(fileName);
	    
	    try (BufferedWriter writer = Files.newBufferedWriter(path)){
	      
	    	/**
	    	 * For each point, write down all its coordinates in
	    	 * one line, separated by a space
	    	 */
		    for (Point p : points){
		    	writer.write("" + p.getClassification() + " ");
		    	for (double s : p.getCoordinates()) {
			        writer.write("" + s + " ");
		    	}
		    	writer.newLine();
		    }
		    
		    writer.close();
		    
		    /* Log succesful */
		    System.out.println("Writing training data was succesful!");
	    	
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    
	}
	
	/**
	 * Reads the training data from a file, putting all points
	 * in the file into the points list
	 * 
	 * @throws IOException  if an IO error occurs, prints stracktrace
	 */
	void readTrainingDataFromFile() throws IOException {
		
		/* Use standard file name */
		Path path = Paths.get(fileName);
		
		try (Scanner scanner = new Scanner(path)) {
			
			/**
			 * For each line, separate the numbers by spaces and
			 * read them into the points list
			 */
			while (scanner.hasNextLine()) {
				
				/* Read line and split */
				String line = scanner.nextLine();
				String[] readCoords = line.split(" ");
				List<Double> pointCoords = new ArrayList<Double>();
				
				/* Read first item which is the class */
				boolean c = Boolean.parseBoolean(readCoords[0]);
				
				/* Add each double to the list */
				for (int i = 1; i < readCoords.length; i++) {
					pointCoords.add(Double.parseDouble(readCoords[i]));
				}
				
				/* Add point to points list */
				points.add(new Point(pointCoords, c));
				
			}
			
			scanner.close();
			
			/* Log succesful */
		    System.out.println("Reading training data was succesful!");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
