package invadem;
import processing.core.PImage;
import java.util.List;

public class PowerInvader extends Invader {

    public PowerInvader(PImage[] img, int x, int y, int width, int height, int[] velocity, int hp, int points, PImage projectileImage) {
        super(img, x, y, width, height, velocity, hp, points, projectileImage);
    }

    public void shoot(List<Projectile> projectiles) {
        projectiles.add(new PowerInvaderProjectile(projectileImage, x + 10, y, 2, 5, new int[] {1}, 1, false));
    }

}
