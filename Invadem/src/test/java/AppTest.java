package invadem;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.List;
import java.util.ArrayList;

public class AppTest extends App {

    /* Tests aspects of the GUI alongside the extension task
    / Also tests the ScoreTracker and CollisionHandler class here as it requires an active running App to work (although can still process their data independently - they just require data from App)
    / Whereas other Entity classes can act more independently so they are tested in their own classes
    /
    */

    ScoreTracker scoreTracker;
    CollisionHandler collisionHandler;

    @Before
     public void load(){
         PApplet.runSketch(new String [] {"temp"}, this);
         delay(3000);
         noLoop();
         scoreTracker = this.getScoreTracker();
    }

    @Test
    public void checkWinLossStart() {
        // check win/loss condition at start of game
        assertFalse(this.checkWin());
        assertFalse(this.checkLoss());
    }


    @Test
    public void renderTanksAfterWin() {
        // check game resets after winning and that all entities are properly reset
        // check transition to next level
        this.loadEntities();
        assertEquals(this.getInvaders().size(), 40);
        assertEquals(this.getBarriers().size(), 3);

        this.clearInvaders();
        assertEquals(this.getInvaders().size(), 0);
        this.checkWinCondition();
        assertTrue(this.checkWin());

        for (int i = 0; i < 121; i++) {
            this.displayWinLoseMessage();
        }

        this.runGame();
        assertFalse(this.checkWin());
        assertEquals(this.getInvaders().size(), 40);
        assertEquals(this.getBarriers().size(), 3);

    }

    // test cooperative mode render
    @Test
    public void renderTanks() {
        // first one player
        assertEquals(this.getPlayers().size(), 1);
        keyCode = 80;
        keyPressed();
        // then check for two players
        assertEquals(this.getPlayers().size(), 2);
    }

    @Test
    public void renderTanksAfterLoss() {
        // edge case where both tanks are destroyed at the same time
        // check that game can restart properly after loss in coop multiplayer
        // further checks transition from lost game to new game

        keyCode = 80;
        keyPressed();
        assertEquals(this.getPlayers().get(0).getHP(), 3);
        assertEquals(this.getPlayers().get(1).getHP(), 3);

        this.getPlayers().get(0).destroyTank();
        this.getPlayers().get(1).destroyTank();

        assertEquals(this.getPlayers().get(0).getHP(), 0);
        assertEquals(this.getPlayers().get(1).getHP(), 0);

        this.checkLoseConditions();
        this.runGame();

        assertTrue(this.checkLoss());

        for (int i = 0; i < 121; i++) {
            this.displayWinLoseMessage();
        }

        this.runGame();
        assertFalse(this.checkLoss());


        assertEquals(this.getPlayers().get(0).getHP(), 3);
        assertEquals(this.getPlayers().get(1).getHP(), 3);
    }

    @Test
    public void renderTankMovementShooting() {
        // test both tanks can move and shoot simulatenously
        // check several projectiles have been shot

        keyCode = 80;
        keyPressed();

        keyCode = 37;
        keyPressed();
        this.getPlayers().get(0).moveRight();
        keyPressed();
        this.getPlayers().get(0).moveRight();
        keyReleased();
        keyCode = 32;
        keyPressed();
        this.getPlayers().get(0).shoot(this.getProjectiles());
        this.getPlayers().get(0).shootReset();
        keyReleased();
        keyCode = 39;
        keyPressed();
        this.getPlayers().get(0).moveLeft();
        keyPressed();
        this.getPlayers().get(0).moveLeft();
        keyReleased();
        keyCode = 32;
        this.getPlayers().get(0).shoot(this.getProjectiles());
        keyReleased();

        this.tankActivity();

        keyCode = 65;
        keyPressed();
        this.getPlayers().get(1).moveLeft();
        keyPressed();
        this.getPlayers().get(1).moveLeft();
        keyReleased();
        keyCode = 87;
        keyPressed();
        this.getPlayers().get(1).shoot(this.getProjectiles());
        this.getPlayers().get(1).shootReset();
        keyReleased();
        keyCode = 68;
        keyReleased();
        keyPressed();
        this.getPlayers().get(1).moveRight();
        this.getPlayers().get(1).moveRight();
        keyCode = 65;
        keyPressed();
        this.getPlayers().get(1).shoot(this.getProjectiles());
        this.getPlayers().get(1).shootReset();
        keyReleased();

        this.tankActivity();

        assertNotEquals(this.getProjectiles().size(), 0);

    }


    @Test
    public void testInitialScores() {
        // test that starting scores are 0 for current and 10,000 for high
        assertEquals(scoreTracker.getCurrentScore(), 0);
        assertEquals(scoreTracker.getHighScore(), 10000);
    }

    @Test
    public void testUpdateCurrentScore() {
        // tests that current score is updated but that the highscore is not overriden and remains the same
        // current should remain the same after attempting to update score
        Invader invader1 = new Invader(new PImage[] {new PImage(8, 8)}, 175, 50, 16, 16, new int[] {1, 1}, 1, 100, new PImage(16, 16));
        Invader invader2 = new ArmouredInvader(new PImage[] {new PImage(8, 8)}, 200, 60, 16, 16, new int[] {1, 1}, 1, 250, new PImage(16, 16));
        Invader invader3 = new PowerInvader(new PImage[] {new PImage(8, 8)}, 220, 80, 16, 16, new int[] {1, 1}, 1, 250, new PImage(16, 16));
        assertEquals(scoreTracker.getCurrentScore(), 0);

        scoreTracker.addScore(invader1);
        scoreTracker.addScore(invader2);
        scoreTracker.addScore(invader3);

        assertEquals(scoreTracker.getCurrentScore(), 600);
        assertEquals(scoreTracker.getHighScore(), 10000);

        scoreTracker.updateHighScore();
        assertEquals(scoreTracker.getHighScore(), 10000);
        assertEquals(scoreTracker.getCurrentScore(), 600);

    }

    @Test
    public void testOverrideHighScore() {
        // tests that current score updates high score when it exceeds 10,000 points
        // current score should be reset to 0 after successfully updating high score
        scoreTracker.resetCurrentPoints();

        Invader invader1 = new Invader(new PImage[] {new PImage(8, 8)}, 175, 50, 16, 16, new int[] {1, 1}, 1, 7000, new PImage(16, 16));
        scoreTracker.addScore(invader1);
        assertEquals(scoreTracker.getCurrentScore(), 7000);
        scoreTracker.updateHighScore();
        assertEquals(scoreTracker.getHighScore(), 10000);
        assertEquals(scoreTracker.getCurrentScore(), 7000);

        Invader invader2 = new Invader(new PImage[] {new PImage(8, 8)}, 175, 50, 16, 16, new int[] {1, 1}, 1, 7000, new PImage(16, 16));
        scoreTracker.addScore(invader2);
        assertEquals(scoreTracker.getCurrentScore() , 14000);
        scoreTracker.updateHighScore();
        assertEquals(scoreTracker.getHighScore(), 14000);
        assertEquals(scoreTracker.getCurrentScore(), 0);
    }

    @Test
    public void testBarriersDamagedAndDelete() {
        // tests case where barriers are hit by tank projectile, armoured invader's projectiles and regular invaders
        // show the blocks and projectiles are damaged and eventually removed
        collisionHandler = this.getCollisionHandler();

        // hitting left barrier block with armoured invader projectile
        Projectile proj1 = new PowerInvaderProjectile(new PImage(16, 16), 200, 378, 2, 5, new int[] {1}, 1, false);
        this.projectilesAdd(proj1);
        assertEquals(this.getBarriers().size(), 3);

        collisionHandler.detectBarrierCollision();
        collisionHandler.removeEntities();
        assertEquals(this.getBarriers().size(), 2);


        // hitting centre barrer block with regular projectile
        Projectile proj2 = new InvaderProjectile(new PImage(16, 16), 309, 379, 1, 3, new int[] {1}, 1, false);
        Projectile proj3 = new InvaderProjectile(new PImage(16, 16), 310, 379, 1, 3, new int[] {1}, 1, false);
        Projectile proj4 = new InvaderProjectile(new PImage(16, 16), 310, 379, 1, 3, new int[] {1}, 1, false);
        this.projectilesAdd(proj2);
        this.projectilesAdd(proj3);
        this.projectilesAdd(proj4);


        collisionHandler.detectBarrierCollision();
        collisionHandler.removeEntities();
        assertEquals(proj2.getHP(), 0);
        assertEquals(proj3.getHP(), 0);
        assertEquals(proj4.getHP(), 0);


        // hittin right block with regular projectile
        Projectile proj5= new TankProjectile(new PImage(16, 16), 414, 379, 1, 3, new int[] {1}, 1, false);
        Projectile proj6 = new TankProjectile(new PImage(16, 16), 415, 379, 1, 3, new int[] {1}, 1, false);
        Projectile proj7 = new TankProjectile(new PImage(16, 16), 416, 379, 1, 3, new int[] {1}, 1, false);
        this.projectilesAdd(proj5);
        this.projectilesAdd(proj6);
        this.projectilesAdd(proj7);

        collisionHandler.detectBarrierCollision();
        collisionHandler.removeEntities();
        assertEquals(proj5.getHP(), 0);
        assertEquals(proj6.getHP(), 0);
        assertEquals(proj7.getHP(), 0);
    }

    @Test
    public void testProjectilesExceedBoundary() {
        collisionHandler = this.getCollisionHandler();
        // corner/edge case testing that projectiles that go beyond the screen size are no longer tracked
        Projectile proj1 = new TankProjectile(new PImage(16, 16), 1000, 1000, 1, 3, new int[] {1}, 1, false);
        Projectile proj2 = new TankProjectile(new PImage(16, 16), 0, 0, 1, 3, new int[] {1}, 1, false);
        Projectile proj3 = new TankProjectile(new PImage(16, 16), 100, 0, 1, 3, new int[] {1}, 1, false);
        Projectile proj4 = new InvaderProjectile(new PImage(16, 16), 40, 481, 1, 1, new int[] {1}, 1, false);

        this.projectilesAdd(proj1);
        this.projectilesAdd(proj2);
        this.projectilesAdd(proj3);
        this.projectilesAdd(proj4);

        collisionHandler.detectProjectileCollision();
        collisionHandler.removeEntities();
    }

    @Test
    public void testTankDamageAndDestroy() {
        // test that tank takes damage by regular invader projectile and is instantly destroyed by power invader's projectile
        // further checks basic lose condition for single player
        collisionHandler = this.getCollisionHandler();
        Projectile proj1 = new InvaderProjectile(new PImage(16, 16), 305, 429, 1, 3, new int[] {1}, 1, false);
        Projectile proj2 = new PowerInvaderProjectile(new PImage(16, 16), 306, 427, 2, 5, new int[] {1}, 1, false);
        this.projectilesAdd(proj1);
        collisionHandler.detectTankCollision();
        this.projectilesAdd(proj2);
        collisionHandler.detectTankCollision();
        assertEquals(this.getPlayers().get(0).getHP(), 0);
        collisionHandler.removeEntities();
        this.checkLoseConditions();
        assertTrue(this.checkLoss());
    }

    @Test
    public void testProjectileInvaderInteraction() {

        // test tank projectiles hit and destroy
        // meanwhile, test that invader projectiles don't destroy other invaders
        // to confirm, we check that the invader size should be adjusted accordingly]


        collisionHandler = this.getCollisionHandler();

        // first check that invader and invader projectiles do no harm to each other
        Invader invader1 = new Invader(new PImage[] {new PImage(16,16)}, 300, 300, 16, 16, new int[] {1, 1}, 1, 100, new PImage(16,16));
        this.invaderAdd(invader1);
        assertEquals(this.getInvaders().size(), 41);
        // add another invaders, totalling 41

        Projectile proj1 = new InvaderProjectile(new PImage(16, 16), 300, 300, 1, 3, new int[] {1}, 1, false);
        this.projectilesAdd(proj1);

        // invader projectile should not be damaged and invader should still be full hp
        collisionHandler.detectInvaderCollision();
        //assertEquals(this.getInvaders().size(), 41);
        assertEquals(proj1.getHP(), 1);
        assertEquals(invader1.getHP(), 1);


        // invader should progressively take damage and drop from 3 hp to 0 hp
        Invader invader2 = new ArmouredInvader(new PImage[] {new PImage(16,16)}, 300, 330, 16, 16, new int[] {1, 1}, 3, 100, new PImage(16,16));
        this.invaderAdd(invader2);
        Projectile proj2 = new TankProjectile(new PImage(16, 16), 300, 330, 1, 3, new int[] {1}, 1, true);
        Projectile proj3 = new TankProjectile(new PImage(16, 16), 300, 330, 1, 3, new int[] {1}, 1, true);
        Projectile proj4 = new TankProjectile(new PImage(16, 16), 300, 330, 1, 3, new int[] {1}, 1, true);
        this.projectilesAdd(proj2);
        this.projectilesAdd(proj3);
        this.projectilesAdd(proj4);
        collisionHandler.checkEntityCollision();
        assertEquals(invader2.getHP(), 3);
    }

    @Test
    public void testRemoveDeadEntities() {
        // check that program recognises when entities have 0 hp and can remove from being tracked
        // test on projectiles and invaders
        collisionHandler = this.getCollisionHandler();

        Projectile proj1 = new InvaderProjectile(new PImage(16, 16), 310, 300, 1, 3, new int[] {1}, 1, false);
        Projectile proj2 = new PowerInvaderProjectile(new PImage(16, 16), 300, 300, 2, 5, new int[] {1}, 1, false);
        this.projectilesAdd(proj1);
        this.projectilesAdd(proj2);


        Invader invader1 = new Invader(new PImage[] {new PImage(16,16)}, 310, 300, 16, 16, new int[] {1, 1}, 0, 100, new PImage(16,16));
        Invader invader2 = new ArmouredInvader(new PImage[] {new PImage(16,16)}, 300, 300, 16, 16, new int[] {1, 1}, 0, 100, new PImage(16,16));
        this.invaderAdd(invader1);
        this.invaderAdd(invader2);


        assertEquals(this.getInvaders().size(), 42);
        // new invader size should be 42
        collisionHandler.checkEntityCollision();

        // size should be 40 now

    }

}
