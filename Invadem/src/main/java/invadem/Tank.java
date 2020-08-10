package invadem;
import processing.core.*;
import processing.core.PImage;
import java.util.*;

public class Tank extends MovingAndShootingEntity implements HorizontalMovement {

    private PImage img;
    private boolean shootCooldown;
    // used to determine whether the shooting key (e.g. spacebar) has been pressed but not let go
    // if so, a 'cooldown' is invoked and the tank may not shoot a projectile again until the key is released and user presses key again
    private boolean movingLeft;
    private boolean movingRight;
    private boolean shooting;
    // boolean flags indicating whether a given key assigned to a tank's activities have been pressed, if true perform the relevant action

    public Tank(PImage img, int x, int y, int width, int height, int[] velocity, int hp, PImage projectileImage) {
        super(x, y, width, height, velocity, hp, projectileImage);
        this.img = img;
        shootCooldown = false;
        movingLeft = false;
        movingRight = false;
        shooting = false;
    }

    public void destroyTank() {
        // use to instantly destroy tank based on interaction with armoured invader's projectile
        hp = 0;
    }

    public void checkActivityStatus(boolean shootKey, boolean leftKey, boolean rightKey, List<Projectile> projectiles) {

        // internalises the logic of user keyboard input to help determine the tank's behaviour
        if (shootKey) {
            shooting = true;
            shoot(projectiles);
        } if (leftKey) {
            movingLeft = true;
        } if (rightKey) {
            movingRight = true;
        }

    }

    public void tick() {

        // after checking the its activity status (keyboard user input), the tank will act accordingly
        // this is to allow it to update its position on the screen when invoked by draw() constantly

        if (shooting) {
            shooting = false;
        }

        if (movingLeft) {
            moveLeft();
            movingLeft = false;
        }

        if (movingRight) {
            moveRight();
            movingRight = false;
        }

    }

    public void shoot(List<Projectile> projectiles) {

        // only shoots if the designated key for shooting isn't being pressed down
        if (!shootCooldown) {
            shootCooldown = true;
            projectiles.add(new TankProjectile(projectileImage, x + 10, y, 1, 3, new int[] {1}, 1, true));
        }

    }

    public void shootReset() {
        // if key assigned to shooting has been let go after being pressed, turns off cooldown
        // then tank will be able to shoot again
        shootCooldown = false;
    }

    public void moveLeft() {
        // only move if within boundary
        if (x > 180) {
            x -= velocity[0];
        }
    }

    public void moveRight() {
        if (x < 460) {
            x += velocity[0];
        }
    }

    public void draw(PApplet app) {
        app.image(img, x, y, width, height);
        tick();
    }

}
