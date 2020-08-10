package invadem;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import processing.core.PImage;
import org.junit.Before;


public class BarrierTest {

    private PImage[] barrierComponentImages;
    private PImage[] barrierTopImages;
    private PImage[] barrierLeftImages;
    private PImage[] barrierRightImages;
    private PImage[] barrierSolidImages;

    @Before
    public void setup() {
        barrierComponentImages = new PImage[] {new PImage(8, 8)};
        barrierTopImages = new PImage[] {new PImage(8, 8)};
        barrierLeftImages = new PImage[] {new PImage(8, 8)};
        barrierRightImages = new PImage[] {new PImage(8, 8)};
        barrierSolidImages = new PImage[] {new PImage(8, 8)};
    }

    @Test
    public void barrierComponentConstruction() {
        // tests creating a barrier component
        // seven of these will later be used to form a barrier block
        Barrier b = new Barrier(barrierComponentImages, 100, 100, 8, 8, new int[] {0}, 3);
        assertNotNull(b);
        assertFalse(b.isDead());
    }

    @Test
    public void barrierBlockConstruction() {
        // tests creating an entire barrer block consisting of seven smaller barrier components
        List<Barrier> barrier = new ArrayList<>();
        assertEquals(barrier.size(), 0);
        barrier = Barrier.createBarrierBlock(barrierTopImages, barrierLeftImages, barrierRightImages, barrierSolidImages, 200, 380, 8, 8, 3);
        assertEquals(barrier.size(), 7);
        for (Barrier b : barrier) {
            assertNotNull(b);
            assertFalse(b.isDead());
        }
    }


   @Test
   public void testBarrierHPLoss() {
       // tests that a barrier starts with 3 hp and is gradually damages and eventually destroyed by a regular invader's projectile
       // hp should go from 3 to 0
       Barrier b = new Barrier(barrierComponentImages, 100, 100, 8, 8, new int[] {0}, 3);
       assertFalse(b.isDead());
       assertEquals(b.getHP(), 3);
       b.hit();
       assertFalse(b.isDead());
       assertEquals(b.getHP(), 2);
       b.hit();
       assertFalse(b.isDead());
       assertEquals(b.getHP(), 1);
       b.hit();
       assertTrue(b.isDead());
       assertEquals(b.getHP(), 0);
       b.hit();
       assertEquals(b.getHP(), 0);
       b.hit();
       assertEquals(b.getHP(), 0);

   }

   @Test
   public void testHPLossNegativeHealth() {
       // if barrier somehow has negative hp,
       Barrier b = new Barrier(barrierComponentImages, 100, 100, 8, 8, new int[] {0}, -2);

   }

   @Test
   public void testBarriersReached() {
       // check that a barrier block can determine that an invader is within reached (<= 10 pixels), signalling end of game
       // testing for case of 10 pixels difference
       Barrier b = new Barrier(barrierComponentImages, 100, 100, 8, 8, new int[] {0}, 3);
       List<Invader> invaders = new ArrayList<>();
       Invader invader = new Invader(barrierComponentImages, 175, 90, 16, 16, new int[] {1, 1}, 1, 100, new PImage(16, 16));
       invaders.add(invader);
       assertTrue(Barrier.barriersReached(invaders, b.getY()));
   }

   @Test
   public void testBarriersNotReached() {
       // check that a barrier block recognises when an inader is not within reach (>10 pixels)
       // testing for extreme case of 11 pixels difference
       Barrier b = new Barrier(barrierComponentImages, 100, 100, 8, 8, new int[] {0}, 3);
       List<Invader> invaders = new ArrayList<>();
       Invader invader = new Invader(barrierComponentImages, 175, 89, 16, 16, new int[] {1, 1}, 1, 100, new PImage(16, 16));
       invaders.add(invader);
       assertFalse(Barrier.barriersReached(invaders, b.getY()));
   }

   @Test
   public void testLocateBarrierBlock() {
       // tests the program can validate that a single barrier component belongs to a particular barrier block on the screen
       List<List<Barrier>> barrierComponents = new ArrayList<>();
       List<Barrier> barrierLeft = Barrier.createBarrierBlock(barrierTopImages, barrierLeftImages, barrierRightImages, barrierSolidImages, 200, 380, 8, 8, 3);
       List<Barrier> barrierRight = Barrier.createBarrierBlock(barrierTopImages, barrierLeftImages, barrierRightImages, barrierSolidImages, 310, 380, 8, 8, 3);
       List<Barrier> barrierCentre =  Barrier.createBarrierBlock(barrierTopImages, barrierLeftImages, barrierRightImages, barrierSolidImages, 415, 380, 8, 8, 3);
       barrierComponents.add(barrierLeft);
       barrierComponents.add(barrierRight);
       barrierComponents.add(barrierCentre);

       // checking a barrier component from the left barrier block is found and returns that block
       assertNotNull(Barrier.findBarrierBlock(barrierComponents, barrierLeft.get(0)));
       assertEquals(Barrier.findBarrierBlock(barrierComponents, barrierLeft.get(0)), barrierLeft);

       // checking a barrier component from the centre barrier block is found and returns that block
       assertNotNull(Barrier.findBarrierBlock(barrierComponents, barrierCentre.get(0)));
       assertEquals(Barrier.findBarrierBlock(barrierComponents, barrierCentre.get(0)), barrierCentre);


       // checking a barrier component from the right barrier block is found and eturns that block
       assertNotNull(Barrier.findBarrierBlock(barrierComponents, barrierRight.get(0)));
       assertEquals(Barrier.findBarrierBlock(barrierComponents, barrierRight.get(0)), barrierRight);

       // checking that searching for a barrier component from the wrong block
       assertNotEquals(Barrier.findBarrierBlock(barrierComponents, barrierLeft.get(0)), barrierRight);
       assertNotEquals(Barrier.findBarrierBlock(barrierComponents, barrierCentre.get(0)), barrierLeft);

      }

      @Test
      public void testDestroyBarrierBlock() {
          // tests destruction of an entire barrier component instantly, simulating the interaction with an armorued invader projectiles

          List<List<Barrier>> barrierComponents = new ArrayList<>();
          List<Barrier> barrierBlock = Barrier.createBarrierBlock(barrierTopImages, barrierLeftImages, barrierRightImages, barrierSolidImages, 310, 380, 8, 8, 3);
          barrierComponents.add(barrierBlock);
          assertNotNull(barrierBlock);
          assertEquals(barrierBlock.size(), 7);

          // nothing should happen
          Barrier.destroyBarrierBlock(barrierComponents, null);

          // barrier block should be identified and removed from a list of barrier blocks
          // the contents of that block should be cleared
          Barrier.destroyBarrierBlock(barrierComponents, barrierBlock.get(0));
          assertEquals(barrierBlock.size(), 0);
          assertEquals(barrierComponents.size(), 0);


      }


}
