package invadem;

import org.junit.Test;
import static org.junit.Assert.*;
import processing.core.PImage;
import java.util.List;
import java.util.ArrayList;
import org.junit.Before;
import java.util.Random;

public class TankTest {


    private PImage tankImage;
    private PImage projectileImage;


    @Before
    public void setup() {
        // loading images to construct invader obejcts
        tankImage = new PImage(16,16);
        projectileImage = new PImage(16,16);
    }

   @Test
   public void testTankConstruction() {
       // construct tanks for single and multiplayer
       List<Tank> players = new ArrayList<>();
       assertEquals(players.size(), 0);
       Tank tank1 = new Tank(tankImage, 304, 430, 22, 16, new int[] {1}, 3, projectileImage);
       assertNotNull(tank1);
       players.add(tank1);
       assertEquals(players.size(), 1);
       Tank tank2 = new Tank(tankImage, 304, 430, 22, 16, new int[] {1}, 3, projectileImage);
       assertNotNull(tank2);
       players.add(tank2);
       assertEquals(players.size(), 2);
   }

   @Test
   public void testTankProjectile() {
       // test shooting mechanism works properly
       List<Projectile> projectiles = new ArrayList<>();
       Tank  tank = new Tank(tankImage, 304, 430, 22, 16, new int[] {1}, 3, projectileImage);
       tank.shoot(projectiles);
       assertEquals(projectiles.size(), 1);
       assertNotNull(projectiles.get(0));
       tank.shootReset();
       tank.checkActivityStatus(true, false, false, projectiles);
       assertEquals(projectiles.size(), 2);

   }

   @Test
   public void testTankNoSpam() {
       // simulate user input with booleans to prevent tank shooting multiple times by holding space bar

       List<Projectile> projectiles = new ArrayList<>();
       Tank tank = new Tank(tankImage, 304, 430, 22, 16, new int[] {1}, 3, projectileImage);
       assertEquals(projectiles.size(), 0);
       tank.checkActivityStatus(true, false, false, projectiles);
       assertEquals(projectiles.size(), 1);
       tank.shoot(projectiles);
       assertEquals(projectiles.size(), 1);
       tank.shoot(projectiles);
       assertEquals(projectiles.size(), 1);
       tank.shoot(projectiles);
       assertEquals(projectiles.size(), 1);

       // test that tank can shoot again once boolean is reset to simulate user lifting the key and pressing down again
       tank.shootReset();
       tank.checkActivityStatus(true, false, false, projectiles);
       assertEquals(projectiles.size(), 2);
   }

   @Test
   public void testShootMove() {
       List<Projectile> projectiles = new ArrayList<>();
       // move left two pixels and shoot two projectiles
       assertEquals(projectiles.size(), 0);
       Tank tank = new Tank(tankImage, 300, 430, 22, 16, new int[] {1}, 3, projectileImage);
       tank.checkActivityStatus(true, true, false, projectiles);
       tank.tick();
       tank.shootReset();
       tank.checkActivityStatus(true, true, false, projectiles);
       tank.tick();
       tank.shootReset();
       assertEquals(tank.getX(), 298);
       assertEquals(projectiles.size(), 2);
       tank.checkActivityStatus(true, false, true, projectiles);
       tank.tick();
       tank.shootReset();
       tank.checkActivityStatus(true, false, true, projectiles);
       tank.tick();
       assertEquals(tank.getX(), 300);
       assertEquals(projectiles.size(), 4);

   }

   @Test
   public void testTankSimultaneousActivity() {
       // corner/edge case where left and arrow keys are pressed simulatenously
       // tank should remain still, x-coordinate should not change
       // tank should be able to shoot as well while 'immobile' like this (although depends on type of keyboard, some keyboards have rollover issues so this may not be possible due to hardware limitations)
       List<Projectile> projectiles = new ArrayList<>();
       Tank tank = new Tank(tankImage, 300, 430, 22, 16, new int[] {1}, 3, projectileImage);
       assertEquals(tank.getX(), 300);

       tank.checkActivityStatus(false, true, true, projectiles);
       tank.tick();
       assertEquals(projectiles.size(), 0);
       assertEquals(tank.getX(), 300);
       tank.checkActivityStatus(true, true, true, projectiles);
       tank.tick();
       assertEquals(projectiles.size(), 1);
       assertEquals(tank.getX(), 300);
   }

   @Test
   public void testTankMovementNormalAndMovementBoundary() {
       // check trivial movement and
       // corner/edge case where we ensure tank cannot move beyond certain boundaries as per specifications
       Tank tank = new Tank(tankImage, 300, 430, 22, 16, new int[] {1}, 3, projectileImage);

       // check y-coordinate once at beginning and end of test to ensure it has not moved vertically
       assertEquals(tank.getY(), 430);

       // first check tank's spawned x-coordinate is righ
       assertEquals(tank.getX(), 300);

       // move once left
       tank.moveLeft();
       assertEquals(tank.getX(), 299);
       // move one right
       tank.moveRight();
       assertEquals(tank.getX(), 300);

       // move 120 pixels to the left
       for (int i = 0; i < 120; i++) {
           tank.moveLeft();
       }

       assertEquals(tank.getX(), 180);

       // attempt to move left, should still be at x-coordinate 180
       tank.moveLeft();
       tank.moveLeft();
       tank.moveLeft();
       assertEquals(tank.getX(), 180);

       // repeat for reaching boundary on right
       for (int i = 0; i < 280; i++) {
           tank.moveRight();
       }

       // attempt to move left, should still be at x-coordinate 460;

       assertEquals(tank.getX(), 460);
       tank.moveRight();
       tank.moveRight();
       tank.moveRight();
       assertEquals(tank.getX(), 460);

       assertEquals(tank.getY(), 430);


   }

   @Test
   public void testTankIsNotDead() {
       // tests tank is alive after taking no damage then some damage
       // ensure hp is actually lost during this time
       Tank tank = new Tank(tankImage, 304, 430, 22, 16, new int[] {1}, 3, projectileImage);
       assertFalse(tank.isDead());
       assertEquals(tank.getHP(), 3);
       tank.hit();
       assertFalse(tank.isDead());
       assertEquals(tank.getHP(), 2);
       tank.hit();
       assertFalse(tank.isDead());
       assertEquals(tank.getHP(), 1);

   }

   @Test
   public void testTankIsDead() {
       // test tank is dead after three hits but still alive until then
       Tank tank = new Tank(tankImage, 304, 430, 22, 16, new int[] {1}, 3, projectileImage);
       assertFalse(tank.isDead());
       tank.hit();
       assertFalse(tank.isDead());
       tank.hit();
       assertFalse(tank.isDead());
       tank.hit();
       assertEquals(tank.getHP(), 0);
       assertTrue(tank.isDead());
   }


}
