package invadem;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;
import java.util.Random;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class App extends PApplet {

    private ScoreTracker scoreTracker;
    private CollisionHandler collisionHandler;
    private List<Tank> players;
    private Map<Integer, Boolean> commands;
    private Tank tank1;
    private Tank tank2;
    private List<Projectile> projectiles;
    private List<Invader> invaders;
    private List<Barrier> barrierLeft;
    private List<Barrier> barrierCentre;
    private List<Barrier> barrierRight;
    private List<List<Barrier>> barrierComponents;

    private PImage tankImage1;
    private PImage tankImage2;
    private PImage gameOverImage;
    private PImage nextLevelImage;
    private PImage invaderArmouredAcrossImage;
    private PImage invaderArmouredDownImage;
    private PImage invaderPowerAcrossImage;
    private PImage invaderPowerDownImage;
    private PImage barrierTopImage1;
    private PImage barrierLeftImage1;
    private PImage barrierRightImage1;
    private PImage barrierSolidImage1;
    private PImage barrierTopImage2;
    private PImage barrierLeftImage2;
    private PImage barrierRightImage2;
    private PImage barrierSolidImage2;
    private PImage barrierTopImage3;
    private PImage barrierLeftImage3;
    private PImage barrierRightImage3;
    private PImage barrierSolidImage3;
    private PImage barrierEmpty;
    private PImage[] barrierTopImages;
    private PImage[] barrierLeftImages;
    private PImage[] barrierRightImages;
    private PImage[] barrierSolidImages;
    private PImage backgroundImage;
    private PImage powerInvaderProjectileImage;

    private PImage[] invaderImages;
    private PImage[] invaderArmouredImages;
    private PImage[] invaderPowerImages;
    private PImage[] projectileImages;
    private PImage invaderAcrossImage;
    private PImage invaderDownImage;
    private PImage projectileImage;

    private boolean playerWonGame = false;
    private boolean playerLostGame = false;
    private int messageDuration = 0;
    private Random random;

    public App() {
        players = new ArrayList<>();
        commands = new HashMap<Integer, Boolean>();
        invaders = new ArrayList<>();
        projectiles = new ArrayList<>();
        barrierComponents = new ArrayList<>();
    }

    public void resetGameConditions() {
        // reset game conditions after win/loses
        // mainly reloading entities to start conditions
        playerWonGame = false;
        playerLostGame = false;
        messageDuration = 0;
        loadEntities();
    }

    public void checkWinCondition() {
        // win game when no more invaders
        if (invaders.size() == 0) {
            playerWonGame = true;
        }

    }

    public void checkLoseConditions() {

        // check if invaders have reached barriers
        if (Barrier.barriersReached(invaders, 380)) {
            playerLostGame = true;
            return;
        }

        if (players.size() == 1) {
            // for single player, game is over when HP is 0
            if (tank1.getHP() == 0) {
                playerLostGame = true;
                return;
            }
        } else {
            // for coop, game is over when either player loses all health
            if (tank1.getHP() == 0 || tank2.getHP() == 0) {
                playerLostGame = true;
                return;
            }
        }
    }

    public void displayWinLoseMessage() {
        if (messageDuration >= 120) {
            // reduces invader shooting period by one seocond
            Invader.reduceShootingFrameThreshold();
            // if lost game, reset current points and update high score
            if (playerLostGame) {
                scoreTracker.updateHighScore();
                scoreTracker.resetCurrentPoints();
                Invader.resetShootingFrameThreshold();
            }
            // reload entities and reset game
            resetGameConditions();

        } else {

            background(0);
            if (playerWonGame) {
                image(nextLevelImage, 320 - 61, 240 - 8);
            } else if (playerLostGame) {
                image(gameOverImage, 320 - 61, 240 - 8);
            }
            messageDuration++;
        }
    }

    public void keyPressed() {

        // save value and update accordigngly

        // space bar
        if (keyCode == 32) {
            commands.replace(32, true);
        // left arrow; move left
        } else if (keyCode == 37) {
            commands.replace(37, true);
        // right arrow; move right
        } else if (keyCode == 39) {
            commands.replace(39, true);
        }

        if (keyCode == 87) {
        // w key for shooting
            commands.replace(87, true);
        } else if (keyCode == 65) {
        // a key for moving left
            commands.replace(65, true);
        // s key for moving right
        } else if (keyCode == 68) {
            commands.replace(68, true);
        }

        // activiate coop made by pressing the key 'P'
        if (keyCode == 80) {
            if (players.size() != 2) {
                players.add(tank2);
            }
            scoreTracker.resetCurrentPoints();
            resetGameConditions();
        }

    }

    public void keyReleased() {
        if (keyCode == 32) {
            commands.put(32, false);
            tank1.shootReset();
        } else if (keyCode  == 37) {
            commands.put(37, false);
        } else if (keyCode == 39) {
            commands.put(39, false);
        }

        if (keyCode == 87) {
            tank2.shootReset();
            commands.put(87, false);
        } else if (keyCode  == 65) {
            commands.put(65, false);
        } else if (keyCode == 68) {
            commands.put(68, false);
        }
    }


    public void tankActivity() {
        // tanks check the types of keys pressed by user and acts accordingly
        tank1.checkActivityStatus(commands.get(32), commands.get(37), commands.get(39), projectiles);

        if (players.size() == 2) {
            tank2.checkActivityStatus(commands.get(87), commands.get(65), commands.get(68), projectiles);
        }
    }

    public void loadEntities() {

        // reloading tank in new state
        tank1 = new Tank(tankImage1, 304, 430, 22, 16, new int[] {1}, 3, projectileImage);
        players.set(0, tank1);

        // same for player 2
        if (players.size() == 2) {
            tank2 = new Tank(tankImage2, 304, 430, 22, 16, new int[] {1}, 3, projectileImage);
            players.set(1, tank2);
        }

        // generating other entities
        projectiles.clear();
        invaders = Invader.loadInvaders(invaderImages, invaderArmouredImages, invaderPowerImages, projectileImages);
        barrierComponents.clear();
        // adding left, centre, and right barriers respectively
        // distinguish based on value of x coordinates
        List<Barrier> barrierLeft = Barrier.createBarrierBlock(barrierTopImages, barrierLeftImages, barrierRightImages, barrierSolidImages, 200, 380, 8, 8, 3);
        List<Barrier> barrierRight = Barrier.createBarrierBlock(barrierTopImages, barrierLeftImages, barrierRightImages, barrierSolidImages, 310, 380, 8, 8, 3);
        List<Barrier> barrierCentre =  Barrier.createBarrierBlock(barrierTopImages, barrierLeftImages, barrierRightImages, barrierSolidImages, 415, 380, 8, 8, 3);

        barrierComponents.add(barrierLeft);
        barrierComponents.add(barrierRight);
        barrierComponents.add(barrierCentre);

        // pass in objects for collission handler to perform collision checking
        collisionHandler = new CollisionHandler(projectiles, players, invaders, barrierComponents, scoreTracker);

    }


    public void setup() {
        frameRate(60);

        // load and cache assets

        tankImage1 = loadImage("src/main/resources/tank1.png");
        tankImage2 = loadImage("src/main/resources/tank2.png");
        invaderAcrossImage = loadImage("src/main/resources/invader1.png");
        invaderDownImage = loadImage("src/main/resources/invader2.png");
        invaderArmouredAcrossImage = loadImage("src/main/resources/invader1_armoured.png");
        invaderArmouredDownImage = loadImage("src/main/resources/invader2_armoured.png");
        invaderPowerAcrossImage = loadImage("src/main/resources/invader1_power.png");
        invaderPowerDownImage = loadImage("src/main/resources/invader2_power.png");
        projectileImage = loadImage("src/main/resources/projectile.png");
        powerInvaderProjectileImage = loadImage("src/main/resources/projectile_lg.png");
        barrierTopImage1 = loadImage("src/main/resources/barrier_top1.png");
        barrierLeftImage1 = loadImage("src/main/resources/barrier_left1.png");
        barrierRightImage1 = loadImage("src/main/resources/barrier_right1.png");
        barrierSolidImage1 = loadImage("src/main/resources/barrier_solid1.png");
        barrierTopImage2 = loadImage("src/main/resources/barrier_top2.png");
        barrierLeftImage2 = loadImage("src/main/resources/barrier_left2.png");
        barrierRightImage2 = loadImage("src/main/resources/barrier_right2.png");
        barrierSolidImage2 = loadImage("src/main/resources/barrier_solid2.png");
        barrierTopImage3 = loadImage("src/main/resources/barrier_top3.png");
        barrierLeftImage3 = loadImage("src/main/resources/barrier_left3.png");
        barrierRightImage3 = loadImage("src/main/resources/barrier_right3.png");
        barrierSolidImage3 = loadImage("src/main/resources/barrier_solid3.png");
        barrierEmpty = loadImage("src/main/resources/empty.png");
        gameOverImage = loadImage("src/main/resources/gameover.png");
        nextLevelImage = loadImage("src/main/resources/nextlevel.png");
        backgroundImage = loadImage("src/main/resources/cosmic.jpg");

        // place some assets into lists for easier accessibility (also because they need to be bundled together)

        invaderImages = new PImage[] {invaderAcrossImage, invaderDownImage};
        invaderArmouredImages = new PImage[] {invaderArmouredAcrossImage, invaderArmouredDownImage};
        invaderPowerImages = new PImage[] {invaderPowerAcrossImage, invaderPowerDownImage};
        projectileImages = new PImage[] {projectileImage, powerInvaderProjectileImage};

        barrierTopImages = new PImage[] {
            barrierTopImage1,
            barrierTopImage2,
            barrierTopImage3,
            barrierEmpty
        };

        barrierLeftImages = new PImage[] {
            barrierLeftImage1,
            barrierLeftImage2,
            barrierLeftImage3,
            barrierEmpty
        };

        barrierRightImages = new PImage[] {
            barrierRightImage1,
            barrierRightImage2,
            barrierRightImage3,
            barrierEmpty
        };

        barrierSolidImages = new PImage[] {
            barrierSolidImage1,
            barrierSolidImage2,
            barrierSolidImage3,
            barrierEmpty
        };

        random = new Random();
        scoreTracker = new ScoreTracker(this);

        // render tanks

        tank1 = new Tank(tankImage1, 304, 430, 22, 16, new int[] {1}, 3, projectileImage);
        tank2 = new Tank(tankImage2, 304, 430, 22, 16, new int[] {1}, 3, projectileImage);

        // tank2 is only added when coop mode is activitated
        players.add(tank1);

        // adding values to the hashmap
        // keys correspond to spacebar, left-arrow, right-arrow for player 1 and W, A, D for player 2 in terms of ascii keycode values (added in that order)
        // these match the actions of shooting, moving left, and moviing right, respectively
        commands.put(32, false);
        commands.put(37, false);
        commands.put(39, false);
        commands.put(87, false);
        commands.put(65, false);
        commands.put(68, false);

        // loading entities for first level
        loadEntities();

    }

    public void settings() {
        size(640, 480);
    }

    public void drawEntities() {
        // use lambdas to draw the entities that are being tracked
        players.forEach(tank -> tank.draw(this));
        projectiles.forEach(projectile -> projectile.draw(this));
        barrierComponents.forEach(barrierBlock -> barrierBlock.forEach(b -> b.draw(this)));
        invaders.forEach(invader -> invader.draw(this));
    }

    public void runGame() {

        // first check if player has lost or won game and display relevant message
        if (playerWonGame || playerLostGame) {
            displayWinLoseMessage();
        }

        // if not, then perform core functions to run game (constantly happening and looping)
        // first check user keyboard input
        // then draw entities
        // then check if invaders are to shoot
        // finally, check for collision and stop tracking and accounting for objects which are collided and therefore 'destroyed' (update score internally also)

        if (!playerWonGame && !playerLostGame) {
            background(0);
            tankActivity();
            drawEntities();
            Invader.shootingFrameCheck(invaders, projectiles, random);
            collisionHandler.checkEntityCollision();
        }

    }

    public void draw() {
        runGame();
        // update score on screen
        scoreTracker.drawScore();
        // check if game is won or lost
        checkWinCondition();
        checkLoseConditions();
    }

    public static void main(String[] args) {
        PApplet.main("invadem.App");
    }

    // methods to help with testing (mainly getters and setters)

    public ScoreTracker getScoreTracker() {
        return scoreTracker;
    }

    public boolean checkWin() {
        return playerWonGame;
    }

    public boolean checkLoss() {
        return playerLostGame;
    }

    public List<Tank> getPlayers() {
        return players;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public List<Invader> getInvaders() {
        return invaders;
    }

    public List<List<Barrier>> getBarriers() {
        return barrierComponents;
    }

    public CollisionHandler getCollisionHandler() {
        return collisionHandler;
    }

    public void clearInvaders() {
        invaders.clear();
    }

    public void projectilesAdd(Projectile projectile) {
        projectiles.add(projectile);
    }

    public void invaderAdd(Invader invader) {
        invaders.add(invader);
    }

}
