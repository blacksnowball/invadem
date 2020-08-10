package invadem;
import processing.core.*;

public class TankProjectile extends Projectile implements VerticalMovement {

    public TankProjectile(PImage img, int x, int y, int width, int height, int[] velocity, int hp, boolean hostile) {
        super(img, x, y, width, height, velocity, hp, hostile);
    }

    // moves upwards
    public void moveVertically() {
        y -= velocity[0];
    }

    public void tick() {
        moveVertically();
    }

    public void draw(PApplet app) {
        app.image(img, x, y, width, height);
        tick();
    }


}
