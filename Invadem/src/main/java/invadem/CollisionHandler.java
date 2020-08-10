package invadem;
import java.util.List;
import java.util.ArrayList;

public class CollisionHandler {

    private List<Projectile> projectiles;
    private List<Tank> players;
    private List<Invader> invaders;
    private List<List<Barrier>> barrierComponents;
    private List<Projectile> projectilesToRemove;
    private List<Invader> invadersToRemove;
    private List<Barrier> barrierComponentsToRemove;
    private ScoreTracker scoreTracker;

    // class handles collision mechanisms to determine if two entities have collided
    // if so, they lose health and if they reached the 0 HP threshold are marked for 'deletion'
    // entities marked for 'deletion' will no longer be tracked by App, saving memory

    public CollisionHandler(List<Projectile> projectiles, List<Tank> players, List<Invader> invaders, List<List<Barrier>> barrierComponents, ScoreTracker scoreTracker) {
        this.scoreTracker = scoreTracker;
        this.projectiles = projectiles;
        this.players = players;
        this.invaders = invaders;
        this.barrierComponents = barrierComponents;
        projectilesToRemove = new ArrayList<>();
        invadersToRemove = new ArrayList<>();
        barrierComponentsToRemove = new ArrayList<>();
    }

    public void checkEntityCollision() {
        // checks collision of projectiles against different types of entities
        // mark for deletion and remove
        detectProjectileCollision();
        detectTankCollision();
        detectInvaderCollision();
        detectBarrierCollision();
        removeEntities();
    }

    public void removeEntities() {

        // removes the entities marked for deletion so App no longer has to track them

        projectilesToRemove.forEach(projectile -> projectiles.remove(projectile));
        invadersToRemove.forEach(invader -> invaders.remove(invader));

        for (Barrier b : barrierComponentsToRemove) {

            if (Barrier.findBarrierBlock(barrierComponents, b) != null) {
                Barrier.findBarrierBlock(barrierComponents, b).remove(b);
            }

        }

    }

    public void detectProjectileCollision() {

        // checks if projectile has been hit and now has 0 hp
        for (Projectile pt : projectiles) {

            if (pt.isDead()) {
                projectilesToRemove.add(pt);
                continue;
            }

            // checks if projectile has went beyond dimensions of screen
            // stops tracking if so

            if (pt.getY() < 0 || pt.getY() > 480) {
                pt.hit();
                projectilesToRemove.add(pt);
                continue;
            }



        }
    }

    public void detectBarrierCollision() {
        for (Projectile pt : projectiles) {
            for (List<Barrier> barrierBlock : barrierComponents) {

                for (Barrier barrierComponent : barrierBlock) {
                    if (barrierComponent.checkCollision(pt)) {

                        pt.hit();

                        // if projectile comes from power invader
                        // mark the entire barrier block (consisting of 7 components) rather than the individual component
                        if (pt instanceof PowerInvaderProjectile) {
                            Barrier.destroyBarrierBlock(barrierComponents, barrierComponent);
                            return;
                        } else if (!pt.isDead()) {
                            barrierComponent.hit();
                        }

                    }
                    // check if barrier component has 0 hp before marking for 'deletion'
                    if (barrierComponent.isDead()) {
                        barrierComponentsToRemove.add(barrierComponent);
                    }

                }

            }
        }
    }

    public void detectInvaderCollision() {
        for (Projectile pt :  projectiles) {
            for (Invader invader : invaders) {
                // invaders only take damage from 'hostile' projectiles which is a property attached to tank projectiles
                if (invader.checkCollision(pt) && pt.isHostile()) {
                    pt.hit();

                    if (!pt.isDead()) {
                        invader.hit();
                    }

                    if (invader.isDead()) {
                        // mark invader for deletion
                        // add invader's score to the score tracker to update current score
                        invadersToRemove.add(invader);
                        scoreTracker.addScore(invader);
                    }

                }
            }
        }
    }

    public void detectTankCollision() {
        // check tanks and projectiles interaction
        for (Projectile pt : projectiles) {
            for (Tank tank : players) {
                if (pt.checkCollision(tank) && !pt.isHostile()) {
                    pt.hit();
                    // if projectile originated from power invader, instantly destroy tank
                    if (pt instanceof PowerInvaderProjectile) {
                        tank.destroyTank();
                    // otherwise only lose 1 hp
                    } else if (!pt.isDead()) {
                        tank.hit();
                    }

                }
            }
        }
    }


}
