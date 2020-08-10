package invadem;
import processing.core.*;

public abstract class Projectile extends Entity implements VerticalMovement {

    protected PImage img;
    // will be used to mark if projectile needs to be removed from screen after colliding or reaching screen boundary
    protected boolean hostile;
    // hostile field used to determine whether the projectile will damage certain other entities


    public Projectile(PImage img, int x, int y, int width, int height, int[] velocity, int hp, boolean hostile) {
        super(x, y, width, height, velocity, hp);
        this.img = img;
        this.hostile = hostile;
    }
    // if true, projectile will harm anything it makes contact with
    // if false, projectile will not harm other entities - e.g. invader projectile will not damage other invaders
    // combined with other conditionals in the App program to determine this behaviour
    public boolean isHostile() {
        return hostile;
    }
    // this will be overriden in the inherting classes
    // we implement this here as projectiles in the App class will be saved as Projectile type rather than as child data-types that inherit from it
    // this will allow us to efficiently perform movement for all different types of projectiles
    public void moveVertically() {
        return;
    }



}
