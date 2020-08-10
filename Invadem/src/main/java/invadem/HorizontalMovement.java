package invadem;

// for entities which require moving horizontally along x-coordinates
// if an entity can move in the x and y direction, they will implement this interface together with the VerticalMovementinterface
public interface HorizontalMovement {
    public void moveLeft();
    public void moveRight();
}
