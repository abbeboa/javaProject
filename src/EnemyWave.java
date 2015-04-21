import java.util.Collection;
import java.util.Random;

public class EnemyWave {
    private long waveStartTime = System.currentTimeMillis();
    private int waveInterval;
    private int spawnEnemiesInterval;
    private int numberOfWaves;
    private int waveNumber;
    private int enemiesPerWave;
    private int n = 1;
    private boolean progressive = false;

    public EnemyWave(int waveNumber) {
        this.waveNumber = waveNumber;
        setVariables(waveNumber);
    }

    private void setVariables(int waveNumber) {
        // waveNumber 0 is progressive mode
        if (waveNumber == 0) {
            this.progressive = true;
            this.numberOfWaves = 4;
            this.waveInterval = 6000;
            this.spawnEnemiesInterval = 1000;
            this.enemiesPerWave = 4;
        } else if (waveNumber == 1) {
            this.numberOfWaves = 1000;
            this.waveInterval = 6000;
            this.spawnEnemiesInterval = 1000;
            this.enemiesPerWave = 4;
        }
    }

    public void handleWave(long currentTime, Collection<AbstractGameObject> gameObjects, Collection<Enemy> enemyList, int JPWIDTH) {
        if (currentTime > waveStartTime + waveInterval) {
            n = 1;
            waveStartTime = currentTime;
            numberOfWaves--;
        }
        spawnEnemies(JPWIDTH, enemiesPerWave, gameObjects, enemyList);
        if (numberOfWaves <= 0) {
            waveNumber++;
            setVariables(waveNumber);
        }
    }

    private void spawnEnemies(int JPWIDTH, int enemyWavesLeft, Collection<AbstractGameObject> gameObjects, Collection<Enemy> enemyList) {
        if (enemyWavesLeft > 0 && System.currentTimeMillis() > waveStartTime + spawnEnemiesInterval * n) {
            Random rnd = new Random();
            int randomNum = rnd.nextInt((JPWIDTH) + 1);
            Enemy newEnemy = new Enemy(randomNum, 0, false, Type.BASICENEMY);
            gameObjects.add(newEnemy);
            enemyList.add(newEnemy);
            enemyWavesLeft--;
            n++;
        }
    }

    public long getWaveStartTime() {
        return waveStartTime;
    }

    public int getWaveInterval() {
        return waveInterval;
    }

    public int getSpawnEnemiesInterval() {
        return spawnEnemiesInterval;
    }

    public int getNumberOfWaves() {
        return numberOfWaves;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public int getEnemiesPerWave() {
        return enemiesPerWave;
    }

    public boolean isProgressive() {
        return progressive;
    }
}
