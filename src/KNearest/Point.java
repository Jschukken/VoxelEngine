package KNearest;

import java.util.List;
import java.util.ArrayList;

/**
 * Class representing a (data)point. This point can be arbitrarily dimensional.
 * Has basic functionality for retrieving the distance between two given points.
 * 
 * @author s135755
 */
public class Point {
    
    /* A list containing all the coordinates of this data point */
    private List<Double> coordinates;
    
    /* This point's class */
    private boolean classification;
    
    /** 
     * Empty constructor, constructs and empty point .
     */
    public Point() {
        this.coordinates = new ArrayList<Double>();
        this.classification = false;
    }
    
    /**
     * Constructor where only a list of coordinates is given.
     * 
     * @param coords  the list of coordinates
     */
    public Point(List<Double> coords) {
    	this.coordinates = coords;
    	this.classification = false;
    }
    
    /**
     * Constructor where a list of coordinates and the class is passed.
     * 
     * @param coords  the list of coordinates
     * @param isGood  the class of the point
     */
    public Point(List<Double> coords, boolean isGood) {
        this.coordinates = coords;
        this.classification = isGood;
    }
    
    /**
     * Adds a given value to the end of the vector.
     * 
     * @param p  the value to add
     */
    public void addPoint(double v) {
        coordinates.add(v);
    }
    
    /* Getter for the list of coordinates */
    public List<Double> getCoordinates() {
        return coordinates;
    }
    
    /* Setter for the list of coordinates */
    public void setCoordinates(List<Double> coords) {
        this.coordinates = coords;
    }
    
    /* Getter for the class of this point */
    public boolean getClassification() {
    	return classification;
    }
    
    /* Setter for the class of this point */
    public void setClassification(boolean isGood) {
    	classification = isGood;
    }
    
    /**
     * Computes the distance between two given points.
     * 
     * @param a  the first point
     * @param b  the second point
     * @pre  {@code a.size == b.size}
     * @return  the distance between points {@code a} and {@code b}
     * @throws IllegalArgumentException if {@code a.size != b.size}
     */
    public static double getDistance(Point a, Point b) throws IllegalArgumentException {
        
        List<Double> coorA = a.getCoordinates();
        List<Double> coorB = b.getCoordinates();
        
        if (coorA.size() != coorB.size()) {
            throw new IllegalArgumentException("Point.getDistance: Point a and b"
                    + " are not of the same size! a: " + coorA.size() + ", b: " + coorB.size());
        }
        
        /**
         * Sum all terms (a_0 - b_0)^2 ... (a_
         */
        double underSum = 0;
        for (int i = 0; i < coorA.size(); i++) {
            underSum += Math.pow((coorB.get(i) - coorA.get(i)), 2);
        }
        
        /**
         * The square root of underSum then is the distance between the two 
         * points.
         */
        return Math.sqrt(underSum);
        
    }
    
    /**
     * Adds two coordinate sets and returns the resulting set.
     * 
     * @param a  the first coordinate set
     * @param b  the second coordinate set to add to the first
     * @pre  {@code a.size == b.size}
     * @return  {@code a + b}
     * @throws IllegalArgumentException if {@code a.size != b.size}
     */
    public static List<Double> add(List<Double> a, List<Double> b) throws IllegalArgumentException {
        
        if (a.size() != b.size()) {
            throw new IllegalArgumentException("Point.add: Point a and b are "
                    + "not of the same size! a: " + a.size() + ", b: " + b.size());
        }
        
        List<Double> result = new ArrayList<Double>();
        
        /**
         * Sum each coordinate with the coordinate of the other point, put
         * result in result coordinate list
         */
        for (int i = 0; i < a.size(); i++) {
            result.add(a.get(i) + b.get(i));
        }
        
        return result;
        
    }
    
    /**
     * Multiplies a given vector with a given scalar.
     * 
     * @param p  the vector to multiply
     * @param s  the scalar
     * @return  {@code s * p}
     */
    public static List<Double> multiply(List<Double> p, double s) {
        
        List<Double> result = new ArrayList<Double>();
        
        for (double d : p) {
            result.add(d * s);
        }
        
        return result;
        
    }
    
    /**
     * Computes and returns whether two points are equal, by checking if all of their coordinates
     * are equal.
     * 
     * @param a  the first point
     * @param b  the second point
     * @return {@code a == b}
     */
    public static boolean equals(Point a, Point b) throws IllegalArgumentException {
    	
    	/**
    	 * First retreive the lists of coordinates
    	 */
    	List<Double> coordsA = a.getCoordinates();
    	List<Double> coordsB = b.getCoordinates();
    	
    	/**
    	 * Check precondition
    	 */
    	if (coordsA.size() != coordsB.size()) {
    		throw new IllegalArgumentException("Point.equals: Point a and b are "
                    + "not of the same size! a: " + coordsA.size() + ", b: " + coordsB.size());
    	}
    	
    	/**
    	 * Loop through the coordinates to find differences, if we find a difference then we break
    	 * the loop and return false, otherwise we return true
    	 */
    	boolean foundDifference = false;
    	for (int i = 0; i < coordsA.size(); i++) {
    		if (coordsA.get(i) != coordsB.get(i)) {
    			foundDifference = true;
    			break;
    		}
    	}
    	
    	return !foundDifference;
    	
    }
    
    /**
     * Converts this point into a string.
     */
    public String toString() {
        
        String result = "(";
        for (double d : coordinates) {
            result += "" + d + ", ";
        }
        if (result.length() > 3) {
            result = result.substring(0, result.length() - 2);
        }
        result += ")";
        return result;
        
    }
    
}
