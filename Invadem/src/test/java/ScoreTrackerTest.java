package invadem;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.List;
import java.util.ArrayList;

public class ScoreTrackerTest extends App {

    ScoreTracker scoreTracker;

    @Before
     public void load(){
         PApplet.runSketch(new String [] {"temp"}, this);
         delay(3000);
         noLoop();
         scoreTracker = this.getScoreTracker();
    }

    @Test
    public void testInitialScores() {
        // test that starting scores are 0 for current and 10,000 for high
        assertEquals(scoreTracker.getCurrentScore(), 0);
        assertEquals(scoreTracker.getHighScore(), 10000);
    }

    @Test
    public void testUpdateCurrentScore() {
        // tests that current score is updated but that the highscore is not overriden and remains the same
        // current should remain the same after attempting to update score
        Invader invader1 = new Invader(new PImage[] {new PImage(8, 8)}, 175, 50, 16, 16, new int[] {1, 1}, 1, 100, new PImage(16, 16));
        Invader invader2 = new ArmouredInvader(new PImage[] {new PImage(8, 8)}, 200, 60, 16, 16, new int[] {1, 1}, 1, 250, new PImage(16, 16));
        Invader invader3 = new PowerInvader(new PImage[] {new PImage(8, 8)}, 220, 80, 16, 16, new int[] {1, 1}, 1, 250, new PImage(16, 16));
        assertEquals(scoreTracker.getCurrentScore(), 0);

        scoreTracker.addScore(invader1);
        scoreTracker.addScore(invader2);
        scoreTracker.addScore(invader3);

        assertEquals(scoreTracker.getCurrentScore(), 600);
        assertEquals(scoreTracker.getHighScore(), 10000);

        scoreTracker.updateHighScore();
        assertEquals(scoreTracker.getHighScore(), 10000);
        assertEquals(scoreTracker.getCurrentScore(), 600);

    }

    @Test
    public void testOverrideHighScore() {
        // tests that current score updates high score when it exceeds 10,000 points
        // current score should be reset to 0 after successfully updating high score
        scoreTracker.resetCurrentPoints();

        Invader invader1 = new Invader(new PImage[] {new PImage(8, 8)}, 175, 50, 16, 16, new int[] {1, 1}, 1, 7000, new PImage(16, 16));
        scoreTracker.addScore(invader1);
        assertEquals(scoreTracker.getCurrentScore(), 7000);
        scoreTracker.updateHighScore();
        assertEquals(scoreTracker.getHighScore(), 10000);
        assertEquals(scoreTracker.getCurrentScore(), 7000);

        Invader invader2 = new Invader(new PImage[] {new PImage(8, 8)}, 175, 50, 16, 16, new int[] {1, 1}, 1, 7000, new PImage(16, 16));
        scoreTracker.addScore(invader2);
        assertEquals(scoreTracker.getCurrentScore() , 14000);
        scoreTracker.updateHighScore();
        assertEquals(scoreTracker.getHighScore(), 14000);
        assertEquals(scoreTracker.getCurrentScore(), 0);
    }

    // test when tanks

}
