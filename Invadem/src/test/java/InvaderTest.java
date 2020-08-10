package invadem;

import org.junit.Test;
import static org.junit.Assert.*;
import processing.core.PImage;
import java.util.List;
import java.util.ArrayList;
import org.junit.Before;
import java.util.Random;

public class InvaderTest {

    private PImage[] invaderImages;
    private PImage[] invaderArmouredImages;
    private PImage[] invaderPowerImages;
    private PImage[] projectileImages;
    private PImage invaderAcrossImage;
    private PImage invaderDownImage;
    private PImage invaderArmouredAcrossImage;
    private PImage invaderArmouredDownImage;
    private PImage invaderPowerAcrossImage;
    private PImage invaderPowerDownImage;
    private PImage projectileImage;
    private PImage armouredInvaderProjectileImage;
    private Random random;

    @Before
    public void setup() {
        // loading images to construct invader obejcts
        invaderAcrossImage = new PImage(16,16);
        invaderDownImage = new PImage(16,16);
        invaderArmouredAcrossImage = new PImage(16,16);
        invaderArmouredDownImage = new PImage(16,16);
        invaderPowerAcrossImage = new PImage(16,16);
        invaderPowerDownImage = new PImage(16,16);
        projectileImage = new PImage(16,16);
        armouredInvaderProjectileImage = new PImage(16,16);
        invaderImages = new PImage[] {invaderAcrossImage, invaderDownImage};
        invaderArmouredImages = new PImage[] {invaderArmouredAcrossImage, invaderArmouredDownImage};
        invaderPowerImages = new PImage[] {invaderPowerAcrossImage, invaderPowerDownImage};
        projectileImages = new PImage[] {projectileImage, armouredInvaderProjectileImage};
        random = new Random(1);
        // PImage image1 = new PImage(16,16);
        // PImage image2 = new PImage(16,16);
        // PImage image3 = new PImage(16,16);
        // PImage[] images = new PImage[] {image1, image2};
    }

    @Test
    public void testInvaderConstruction() {
        // test one invader can be constructed
        Invader invader = new Invader(invaderImages, 175, 50, 16, 16, new int[] {1, 1}, 1, 100, projectileImages[0]);
        assertNotNull(invader);

        // test that 40 invaders are able to be constructed, as per the start of the game
        List<Invader> invaders = Invader.loadInvaders(invaderImages, invaderArmouredImages, invaderPowerImages, projectileImages);
        assertEquals(invaders.size(), 40);
        assertNotNull(invaders);

        // assert all invaders are of the same proportions (16x16) and characteristics
        for (Invader i : invaders) {
            assertEquals(i.getWidth(), 16);
            assertEquals(i.getHeight(), 16);
            assertArrayEquals(i.getVelocity(), new int[] {1, 1});

        }
    }

    @Test
    public void testInvaderCycleMovementSample() {
        // replicates one cycle of movement (move right, move down, move left) for one pixel
        Invader invader = new Invader(invaderImages, 175, 50, 16, 16, new int[] {1, 1}, 1, 100, projectileImages[0]);
        assertNotNull(invader);
        assertEquals(invader.getX(), 175);
        invader.moveRight();
        assertEquals(invader.getX(), 176);
        assertEquals(invader.getY(), 50);
        invader.moveVertically();
        assertEquals(invader.getY(), 51);
        invader.moveLeft();
        assertEquals(invader.getX(), 175);
        assertEquals(invader.getY(), 51);
    }


    @Test
    public void testInvaderCycleMovementProper() {
        // replicates cycle of movement as per the real game (move right, move down, move left) for one pixel
        Invader invader = new Invader(invaderImages, 175, 50, 16, 16, new int[] {1, 1}, 1, 100, projectileImages[0]);
        assertEquals(invader.getX(), 175);
        assertEquals(invader.getY(), 50);

        // check after first series of ticks the invader moves right by 30 pixels (and nothing past that)

        for (int i = 0; i < 31; i++) {
            invader.moveRight();
        }
        // check invader moves down

        for (int i = 0; i < 8; i++) {
            invader.moveVertically();
        }
        //  invader moves left and returns back
        for (int i = 0; i < 31; i++) {
            invader.moveLeft();
        }

        assertEquals(invader.getX(), 175);
        assertEquals(invader.getY(), 58);


        for (int i = 0; i < 60; i++) {
            invader.tick();
        }
        // check invader moves down

        for (int i = 0; i < 60; i++) {
            invader.tick();
        }
        //  invader moves left and returns back
        for (int i = 0; i < 60; i++) {
            invader.tick();
        }

    }

    @Test
    public void testRandomInvaderFireProjectile() {
         // tests a random invader from a list of invaders can fire a projectile
         List<Projectile> projectiles = new ArrayList<>();
         List<Invader> invaders = new ArrayList<>();
         Invader invader = new Invader(invaderImages, 100, 50, 16, 16, new int[] {1, 1}, 1, 100, projectileImages[0]);
         Invader invader2 = new ArmouredInvader(invaderImages, 200, 50, 16, 16, new int[] {1, 1}, 3, 100, projectileImages[0]);
         invaders.add(invader);
         invaders.add(invader2);
         // seed guarantees that the random object, from a list of two, will guarantee that the second item is selected via nextInt()
         // so we check the x-coordinate and y-coordinate matches
         Invader.invaderAttack(invaders, projectiles, random);
         // we offset by 10 pixels to account for the way images are drawn using PImage
         assertEquals(projectiles.get(0).getX(), 210);
         assertEquals(projectiles.get(0).getY(), 50);
    }

    @Test
    public void testInvaderFireProjectile() {
        // tests that an invader can fire a projectile and that it is not a null object
        // further checks that the projectile is added properly to a list of projectiles
        List<Projectile> projectiles = new ArrayList<>();
        assertEquals(projectiles.size(), 0);
        Invader invader = new Invader(invaderImages, 175, 50, 16, 16, new int[] {1, 1}, 1, 100, projectileImages[0]);
        invader.shoot(projectiles);
        assertEquals(projectiles.size(), 1);
        assertNotNull(projectiles.get(0));
   }

   @Test
    public void testInvaderIsNotDead() {
        // tests different type of invaders with different hp levels are not killed
        Invader invader = new Invader(invaderImages, 175, 50, 16, 16, new int[] {1, 1}, 1, 100, projectileImages[0]);
        assertFalse(invader.isDead());

        // tests still has 1 hp after 2 hits
        Invader invader2 = new ArmouredInvader(invaderImages, 175, 50, 16, 16, new int[] {1, 1}, 3, 100, projectileImages[0]);
        assertEquals(invader2.getHP(), 3);
        assertFalse(invader2.isDead());
        invader2.hit();
        invader2.hit();
        assertFalse(invader2.isDead());
        assertEquals(invader2.getHP(), 1);
    }

   @Test
   public void testInvaderIsDead() {
       // tests different type of invaders with different hp levels are not killed
       Invader invader = new Invader(invaderImages, 175, 50, 16, 16, new int[] {1, 1}, 1, 100, projectileImages[0]);
       invader.hit();
       assertTrue(invader.isDead());

       Invader invader2 = new ArmouredInvader(invaderImages, 175, 50, 16, 16, new int[] {1, 1}, 3, 100, projectileImages[0]);
       invader2.hit();
       invader2.hit();
       invader2.hit();
       assertTrue(invader2.isDead());
   }


}
