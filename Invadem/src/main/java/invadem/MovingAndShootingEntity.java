package invadem;
import java.util.*;
import processing.core.PImage;

public abstract class MovingAndShootingEntity extends Entity {

    // used to determine help determine the kind of projectile launched by inherting classes
    protected PImage projectileImage;

    public MovingAndShootingEntity(int x, int y, int width, int height, int[] velocity, int hp, PImage projectileImage) {
        super(x, y, width, height, velocity, hp);
        this.projectileImage = projectileImage;
    }
    // adds a new projectile to a list in the App which keeps track of all projectiles
    // each inherting class will have a unique implementation depending on their role in their game
    // and the type of projectile they want to add
    public abstract void shoot(List<Projectile> projectiles);


}
