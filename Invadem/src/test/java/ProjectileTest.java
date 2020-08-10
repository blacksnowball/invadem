package invadem;

import org.junit.Test;
import static org.junit.Assert.*;
import processing.core.PImage;
import java.util.List;
import java.util.ArrayList;
import org.junit.Before;
import java.util.Random;

public class ProjectileTest {

    // tests collision handling at individual entity level
    private PImage projectileImage;

    @Before
    public void setup() {
        projectileImage = new PImage(16, 16);
    }

   @Test
   public void testProjectileConstruction() {
       Projectile proj1 = new InvaderProjectile(projectileImage, 100, 100, 1, 3, new int[] {1}, 1, false);
       Projectile proj2 = new InvaderProjectile(projectileImage, 100, 100, 1, 3, new int[] {1}, 1, false);
       Projectile proj3 = new InvaderProjectile(projectileImage, 100, 100, 1, 3, new int[] {1}, 1, false);
       Projectile proj4 = new TankProjectile(projectileImage, 100, 100, 1, 3, new int[] {1}, 1, true);
       assertNotNull(proj1);
       assertNotNull(proj2);
       assertNotNull(proj3);
       assertNotNull(proj4);
   }

   @Test
   public void testProjectileIsHostile() {
       Projectile proj1 = new InvaderProjectile(projectileImage, 100, 100, 1, 3, new int[] {1}, 1, false);
       Projectile proj2 = new PowerInvaderProjectile(projectileImage, 100, 100, 1, 3, new int[] {1}, 1, false);
       Projectile proj3 = new InvaderProjectile(projectileImage, 100, 100, 1, 3, new int[] {1}, 1, false);
       Projectile proj4 = new TankProjectile(projectileImage, 100, 100, 1, 3, new int[] {1}, 1, true);
       assertFalse(proj1.isHostile());
       assertFalse(proj2.isHostile());
       assertFalse(proj3.isHostile());
       assertTrue(proj4.isHostile());

   }


   @Test
   public void testProjectileMove() {
       // test tank projectiles moves upwards while invader projectiles move downwards
       // x-coordinates should remain the same

       Projectile proj1 = new InvaderProjectile(projectileImage, 100, 200, 1, 3, new int[] {1}, 1, false);
       assertEquals(proj1.getX(), 100);
       assertEquals(proj1.getY(), 200);
       proj1.moveVertically();
       assertEquals(proj1.getX(), 100);
       assertEquals(proj1.getY(), 201);
       proj1.moveVertically();
       assertEquals(proj1.getY(), 202);


       Projectile proj2 = new TankProjectile(projectileImage, 50, 150, 1, 3, new int[] {1}, 1, true);
       assertEquals(proj2.getX(), 50);
       assertEquals(proj2.getY(), 150);
       proj2.moveVertically();
       assertEquals(proj2.getX(), 50);
       assertEquals(proj2.getY(), 149);
       proj2.moveVertically();
       assertEquals(proj2.getY(), 148);
   }

   @Test
   public void testCollide() {
       // basic test for collision where entities are immediately adjacent
       Projectile proj1 = new InvaderProjectile(projectileImage, 300, 430, 1, 3, new int[] {1}, 1, false);
       Tank tank1 = new Tank(projectileImage, 300, 430, 22, 16, new int[] {1}, 3, projectileImage);
       assertTrue(proj1.checkCollision(tank1));
   }

   @Test
   public void testProjectileMoveToCollide() {
       // test a case where the projectile is out of reach of the tank
       // before updating its movement and then colliding
       Projectile proj1 = new InvaderProjectile(projectileImage, 300, 427, 1, 3, new int[] {1}, 1, false);
       Tank tank1 = new Tank(projectileImage, 300, 430, 22, 16, new int[] {1}, 3, projectileImage);
       assertFalse(proj1.checkCollision(tank1));
       proj1.moveVertically();
       assertTrue(proj1.checkCollision(tank1));
   }


   @Test
   public void testProjectileDoesNotCollide() {
       // test a corner/edge case where the projectile passes by the tank and does not collide after going through
       Projectile proj1 = new InvaderProjectile(projectileImage, 300, 447, 1, 3, new int[] {1}, 1, false);
       Tank tank1 = new Tank(projectileImage, 300, 430, 22, 16, new int[] {1}, 3, projectileImage);
       assertFalse(proj1.checkCollision(tank1));

       //test trivial case where the projectile is far away (above not behind) from tank
       Projectile proj2 = new InvaderProjectile(projectileImage, 400, 300, 1, 3, new int[] {1}, 1, false);
       Tank tank2 = new Tank(projectileImage, 400, 430, 22, 16, new int[] {1}, 3, projectileImage);
       assertFalse(proj2.checkCollision(tank2));
   }

}
