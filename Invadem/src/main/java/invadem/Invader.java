package invadem;
import processing.core.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Invader extends MovingAndShootingEntity implements HorizontalMovement, VerticalMovement {

    protected PImage[] img;
    protected int points;
    // boolean flags to keep track of whether an invader is moving left, right, or down
    // used to control movement within class; we don't want to handle such logic in the main class as it will be too cluttered
    protected boolean movingRight;
    protected boolean movingLeft;
    protected boolean movingDown;
    // keeps track of the number of steps the invader takes horizontally or vertically; invader steps moving once it reaches a given threshold
    protected int sideMovements;
    protected int downMovements;
    protected int currentMovementFrame = 0;
    private static int currentShootingFrame = 0;
    private static int shootingFrameThreshold = 300;
    // keeps track of the current frame from the main program, used to determine when the invader's position is updated

    public Invader(PImage[] img, int x, int y, int width, int height, int[] velocity, int hp, int points, PImage projectileImage) {
        super(x, y, width, height, velocity, hp, projectileImage);
        this.img = img;
        this.points = points;
    // initially at start of the game, the invader will be moving to the right
        movingRight = true;
        movingDown = false;
        movingLeft = false;
        // initially, the invader is
        sideMovements = 0;
        downMovements = 0;
    }

    public static List<Invader> loadInvaders(PImage[] invaderImages, PImage[] invaderArmouredImages, PImage[] invaderPowerImages, PImage[] projectileImages) {
        List<Invader> invaders = new ArrayList<>();
        int x = 170;
        int y = 50;
        int rows = 4;
        int elementsPerRow = 10;

        for (int row = 0; row < rows; row++) {

            for (int element = 0; element < elementsPerRow; element++) {

                if (row == 0) {
                    // load armoured invaders
                    invaders.add(new ArmouredInvader(invaderArmouredImages, x, y, 16, 16, new int[] {1, 1}, 3, 250, projectileImages[0]));
                } else if (row == 1) {
                    // load power invaders
                    invaders.add(new PowerInvader(invaderPowerImages, x, y, 16, 16, new int[] {1, 1}, 1, 250, projectileImages[1]));
                } else {
                    // load regular invaders
                    invaders.add(new Invader(invaderImages, x, y, 16, 16, new int[] {1, 1}, 1, 100, projectileImages[0]));
                }
                x += 30;
            }
            y += 30;
            // reset x coordinates
            x = 170;
        }

    return invaders;

    }


    // chooses a random invader from a list of invaders to attack
    public static void invaderAttack(List<Invader> invaders, List<Projectile> projectiles, Random random) {
        int index = random.nextInt(invaders.size());
        invaders.get(index).shoot(projectiles);
    }

    // determines the timing window for an invader to attack every n seconds
    public static void shootingFrameCheck(List<Invader> invaders, List<Projectile> projectile, Random random) {
        if (currentShootingFrame >= shootingFrameThreshold && invaders.size() > 0) {
            currentShootingFrame = 0;
            invaderAttack(invaders, projectile, random);
        } else {
            currentShootingFrame++;
        }
    }

    public static void reduceShootingFrameThreshold() {
        if (shootingFrameThreshold != 60) {
            shootingFrameThreshold -= 60;
        }
    }

    public static void resetShootingFrameThreshold() {
        shootingFrameThreshold = 300;
    }

    public int getPoints() {
        return points;
    }

    public void shoot(List<Projectile> projectiles) {
        projectiles.add(new InvaderProjectile(projectileImage, x + 10, y, 1, 3, new int[] {1}, 1, false));
    }

    public void moveRight() {

        if (sideMovements < 30) {
            // if less than 30 steps have been made, continue moving right
            // update the step counter based on size of step taken which is based on velocity
            x += velocity[0];
            sideMovements += velocity[0];
        } else {
            // once 30 steps have been made, reset side movement counter
            // on the next few ticks, the invader will first move down before moving left
            sideMovements = 0;
            movingRight = false;
            movingDown = true;
            movingLeft = true;
        }

    }

    public void moveLeft() {
        if (sideMovements < 30) {
            // if less than 30 steps have been made, continue moving left
            x -= velocity[0];
            sideMovements += velocity[0];
        } else {
            // on the next few ticks, the invader will first move down before moving right
            sideMovements = 0;
            movingLeft = false;
            movingDown = true;
            movingRight = true;
        }
    }

    public void moveVertically() {
        if (downMovements < 8) {
            // if less than 30 steps have been made, continue moving down
            // increment movements made
            y += velocity[1];
            downMovements++;
        } else {
            // once 8 downward steps are taken, reset the downMovements counter to 0
            // set the movingDown status to false; on the next tick, the invader will be able to move either left or right depending on their boolean status
            downMovements = 0;
            movingDown = false;
        }
    }

    public void tick()  {
        // updates the invader's position after each draw rendition
        // after the second frame is reached, will invoke a some form of movement
        if (currentMovementFrame == 2)  {

            // in every second frame, the invader will either be moving downw, right, or left (but only one of these three at a time)
            // use the boolean flags that track the movement status to determine, in a given precedence order, where the invader will move

            if (movingDown) {
                moveVertically();
            } else if (movingRight) {
                moveRight();
            } else if (movingLeft) {
                moveLeft();
            }

        // resets the frame count
        currentMovementFrame = 0;

        }

        // we increment the current frame after regardless of whether the invader's position is updated
        // note that once the invader's position is updated, the currentmovementFrame count is reset
        currentMovementFrame++;

    }

    public void draw(PApplet app) {
            // if invader is moving across, stick with the sprite where they are static (invader1.png)
            if (!movingDown) {
                app.image(img[0], x, y, width, height);
            // otherwises if moving downwards, stick with the sprite where they are shifting down (invader1.png)
            } else {
                app.image(img[1], x, y, width, height);
            }
            tick();
        }


}
