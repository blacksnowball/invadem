package invadem;

// for entities which require moving vertically along x-coordinates
// if an entity can move in the x and y direction, they will implement this interface together with the HorizontalMovement interface
public interface VerticalMovement {
    public void moveVertically();
}
