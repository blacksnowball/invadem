package invadem;
import processing.core.PImage;
import java.util.List;

public class PowerInvaderProjectile extends InvaderProjectile {

    public PowerInvaderProjectile(PImage img, int x, int y, int width, int height, int[] velocity, int hp, boolean hostile) {
        super(img, x, y, width, height, velocity, hp, hostile);
    }

    // although not too different from regular invader projectile, this is here to allow for future expansion of functionality unique to the armoured invader's projectile

}
