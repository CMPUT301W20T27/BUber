package com.example.buber;


import org.junit.Test;
import org.junit.Assert;

/**
 * Not all tests can be run with solo(eg: scanning qrCode).
 * These tests are here to instead test individual methods that
 * could not otherwise be well tested with solo
 */
public class MethodUnitTests {

    /**
     * testing the rating algorithm
     * when called, this algorithm will always have at least one thumbs up or down
     * rating = numThumbsUp / (numThumbsUp + numThumbsDown) * 100
     */
    @Test
    public void testRating() {

        //no thumbs down should have rating of 100
        Assert.assertTrue(ratingAlgorithm(1.0,0.0) == 100.0);

        //more thumbs up should have a higher rating
        double smallerRating = ratingAlgorithm(2.0,2.0);
        double largerRating = ratingAlgorithm(3.0,2.0);
        Assert.assertTrue(smallerRating < largerRating);

        //more thumbs down should have a smaller rating
        smallerRating = ratingAlgorithm(1.0 , 3.0);
        largerRating = ratingAlgorithm(1.0,2.0);
        Assert.assertTrue(smallerRating < largerRating);
    }

    public double ratingAlgorithm(double numThumbsUp, double numThumbsDown){
        return numThumbsUp / (numThumbsDown + numThumbsUp) * 100.0;
    }
}
