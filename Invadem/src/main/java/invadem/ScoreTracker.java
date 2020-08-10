package invadem;
import processing.core.PFont;
import processing.core.PApplet;
import processing.core.*;

public class ScoreTracker {

    private PApplet app;
    private PFont myFont;
    private int currentScore;
    private int highScore;

    public ScoreTracker(PApplet app) {
        this.app = app;
        highScore = 10000;
        currentScore = 0;
        myFont = app.createFont("PressStart2P-Regular.ttf", 10);
        app.textFont(myFont);
    }

    public void drawScore() {
        // draw to screen current and high score
        app.text("Current score: " + currentScore, 40, 30);
        app.text("Highscore: " + highScore, 450, 30);
    }

    public void addScore(Invader invader) {
        // adds to current score the point value of the invader
        currentScore += invader.getPoints();
    }

    public void updateHighScore() {
        // only update high score if current score exceeds
        // reset points as this update occurs when user loses game
        if (currentScore > highScore) {
            highScore = currentScore;
            resetCurrentPoints();

        }
    }

    public void resetCurrentPoints() {
        currentScore = 0;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighScore() {
        return highScore;
    }


}
