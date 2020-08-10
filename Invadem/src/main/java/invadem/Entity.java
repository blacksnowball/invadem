package invadem;
import processing.core.*;
import processing.core.PImage;

public abstract class Entity {

    /** super class from which all entities (tank, invader, barrier, projectile) inherit
    * sets out basic characteristics including dimensions, current position, and velocty
    */
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int[] velocity;
    protected int hp;

    public Entity(int x, int y, int width, int height, int[] velocity, int hp) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocity = velocity;
        this.hp = hp;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int[] getVelocity() {
        return velocity;
    }

    public void hit() {
        hp--;
    }

    public int getHP() {
        return hp;
    }

    public boolean isDead() {
        return hp == 0;
    }

    // checks if there is collission with another entity
    // used to mark in program whether one or both entities are to be damaged or removed
    public boolean checkCollision(Entity other) {

        if ((this.getX() < (other.getX() + other.getWidth()))
            && ((this.getX() + this.getWidth()) > other.getX())
            && (this.getY() < (other.getY() + other.getHeight()))
            && ((this.getHeight() + this.getY()) > other.getY())) {
                return true;
        }

        return false;

    }


    // each inheriting entity will have its own implementation of draw depending on their role and  specifications
    public abstract void draw(PApplet app);

}
