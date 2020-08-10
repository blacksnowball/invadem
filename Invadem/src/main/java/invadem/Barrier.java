package invadem;
import processing.core.*;
import processing.core.PImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Barrier extends Entity {

    private PImage[] barrierComponentImages;
    // list of barrier component images that change based on hp

    public Barrier(PImage[] barrierComponentImages, int x, int y, int width, int height, int[] velocity, int hp) {
        super(x, y, width, height, velocity, hp);
        this.barrierComponentImages = barrierComponentImages;
    }


    public static boolean barriersReached(List<Invader> invaders, int y) {
        // checks if an invader has reached barriers, signalling loss of game (requires y  coordinates to be given)
        for (int i = invaders.size() - 1; i >= 0; i--) {
            if (y - invaders.get(i).getY() <= 10) {
                return true;
            }
        }
        return false;
    }

    public static List<Barrier> createBarrierBlock(PImage[] barrierTopImages, PImage[] barrierLeftImages, PImage[] barrierRightImages,
                                        PImage[] barrierSolidImages, int x, int y, int width, int height, int hp) {

        // generates an entire barrier block consisting of several barrier components
        // these will be converted in App to a list of barriers
        Barrier barrierTopComponent = new Barrier(barrierTopImages, x, y, width, height, new int[] {0}, hp);
        Barrier barrierLeftComponent = new Barrier(barrierLeftImages, x-width, y, width, height, new int[] {0}, hp);
        Barrier barrierRightComponent = new Barrier(barrierRightImages, x+width, y, width, height, new int[] {0}, hp);
        Barrier barrierSolidLeftComponent1 = new Barrier(barrierSolidImages, x-width, y+height, width, height, new int[] {0}, hp);
        Barrier barrierSolidLeftComponent2 = new Barrier(barrierSolidImages, x-width, y+(2*height), width, height, new int[] {0}, hp);
        Barrier barrierSolidRightComponent1 = new Barrier(barrierSolidImages, x+width, y+height, width, height, new int[] {0}, hp);
        Barrier barrierSolidRightComponent2 = new Barrier(barrierSolidImages, x+width, y+(2*height), width, height, new int[] {0}, hp);

        Barrier[] temp = new Barrier[] {
            barrierTopComponent,
            barrierLeftComponent,
            barrierRightComponent,
            barrierSolidLeftComponent1,
            barrierSolidLeftComponent2,
            barrierSolidRightComponent1,
            barrierSolidRightComponent2
        };

        List<Barrier> barrierTemp = Arrays.asList(temp);

        List<Barrier> barrierBlock = new ArrayList<>(barrierTemp);

        return barrierBlock;

    }

    public static List<Barrier> findBarrierBlock(List<List<Barrier>> barrierComponents, Barrier barrier) {

        // find the list (barrier block) to which a given barrier component is given
        List<Barrier> temp = null;

        for (List<Barrier> barrierBlock : barrierComponents) {
            if (barrierBlock.contains(barrier)) {
                temp = barrierBlock;
            }
        }

        return temp;

    }

    public static void destroyBarrierBlock(List<List<Barrier>> barrierComponents, Barrier barrier) {

        // destroys an entire barrier block by clearing the list of its contents
        // will interact with armoured invader projectile
        List<Barrier> barrierToDestroy = findBarrierBlock(barrierComponents, barrier);

        if (barrierToDestroy != null) {
            barrierToDestroy.clear();
            barrierComponents.remove(barrierToDestroy);

        }

    }


    public void hit() {
        // reduce tank hp after collision
        // will decrementing hp once hp is zero as can't have negative health; barrier component will then be marked to be destroyed and no longer drawn
        // overrides the super method declared in the immediate super class
        if (hp > 0 && hp <= 3) {
            hp--;
        }
    }

    public void draw(PApplet app) {
        // we cycle through the sprites based on the hp of the tank
        app.image(barrierComponentImages[3-hp], x, y, width, height);
    }
}
