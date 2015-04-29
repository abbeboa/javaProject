/**
 * EnemyWaves are created in the GamePanel-class (in the gameloop). It spawns enemies / creates enemy objects, and also
 * PowerUps.
 */
public class EnemyWave {
    private static final int DEFAULTNUMBEROFWAVES = 4;
    private static final int DEFAULTWAVEINTERVAL = 6000;
    private static final int DEFAULTSPAWNENEMIESINTERVAL = 1000;
    private static final int DEFAULTENEMIESPERWAVES = 4;
    private static final int ALMOSTINFINITENUMBEROFWAVES = 99999;

    private long waveStartTime = System.currentTimeMillis();
    private int waveInterval;
    private int spawnEnemiesInterval;
    private int numberOfWaves;
    private int waveNumber;
    private int enemiesPerWave;
    private int n = 1;
    private Direction direction = Direction.RIGHT;

    public EnemyWave(int waveNumber) {
        this.waveNumber = waveNumber;
        setVariables(waveNumber);
    }

    private void setVariables(int waveNumber) {
        // waveNumber 0 is progressive mode
        if (waveNumber == 0) {
            this.numberOfWaves = DEFAULTNUMBEROFWAVES;
            this.waveInterval = DEFAULTWAVEINTERVAL;
            this.spawnEnemiesInterval = DEFAULTSPAWNENEMIESINTERVAL;
            this.enemiesPerWave = DEFAULTENEMIESPERWAVES;
            this.direction = Direction.RIGHT;
        } else if (waveNumber == 1) {
            this.numberOfWaves = ALMOSTINFINITENUMBEROFWAVES;
            this.waveInterval = DEFAULTWAVEINTERVAL;
            this.spawnEnemiesInterval = DEFAULTSPAWNENEMIESINTERVAL;
            this.enemiesPerWave = DEFAULTENEMIESPERWAVES;
            this.direction = Direction.RIGHT;
        }
    }

    public void handleWave(long currentTime, GamePanel gamePanel) {
        if (currentTime > waveStartTime + waveInterval) {
            spawnPowerUp(gamePanel);
            n = 1;
            waveStartTime = currentTime;
            numberOfWaves--;
            changeDirection();
        }
        spawnEnemies(enemiesPerWave, gamePanel);
        if (numberOfWaves <= 0) {
            waveNumber++;
            setVariables(waveNumber);
        }
    }

    private void spawnEnemies(int enemyWavesLeft, GamePanel gamePanel) {
        if (enemyWavesLeft > 0 && System.currentTimeMillis() > waveStartTime + spawnEnemiesInterval * n) {
            Enemy newEnemy;
            int enemyMargin = GamePanel.getEnemymargin();
            if (direction == Direction.RIGHT) {
                newEnemy = new Enemy(0, -enemyMargin, Type.BASICENEMY, direction);
            } else {
                newEnemy = new Enemy(GamePanel.getJpwidth() - enemyMargin, -enemyMargin, Type.BASICENEMY, direction);
            }
            gamePanel.addToGameObjectsList(newEnemy);
            n++;
        }
    }

    private void changeDirection() {
        if (direction == Direction.RIGHT) {
            direction = Direction.LEFT;
        } else {
            direction = Direction.RIGHT;
        }
    }

    private void spawnPowerUp(GamePanel gamePanel) {
        // using -enemymargin as spawn-position, to spawn outside gamefield
        // Updates to random position in PowerUp update method
        PowerUp newPowerUp = new PowerUp(-GamePanel.getEnemymargin(), -GamePanel.getEnemymargin(), Type.POWERUP);
        gamePanel.addToGameObjectsList(newPowerUp);
    }
}
