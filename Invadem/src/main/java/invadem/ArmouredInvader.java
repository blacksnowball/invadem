package invadem;
import java.util.List;
import processing.core.PImage;


public class ArmouredInvader extends Invader {

    public ArmouredInvader(PImage[] img, int x, int y, int width, int height, int[] velocity, int hp, int points, PImage projectileImage) {
        super(img, x, y, width, height, velocity, hp, points, projectileImage);
    }

    // shoots a special projectile distinct from the regular invader's projectile
    public void shoot(List<Projectile> projectiles) {
        projectiles.add(new InvaderProjectile(projectileImage, x + 10, y, 1, 3, new int[] {1}, 1, false));
    }


}
