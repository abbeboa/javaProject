import java.util.Collection;
import java.util.List;
import java.util.Random;

public class EnemyWave {
    private long waveStartTime = System.currentTimeMillis();
    private static int waveInterval;
    private static int spawnEnemiesInterval;
    private static int numberOfWaves;
    private static int waveNumber;
    private static int enemiesPerWave;
    private static int n = 1;
    private static boolean waveActive = false;
    private static boolean progressive = false;

    public EnemyWave(int waveNumber) {
        this.waveNumber = waveNumber;
        setVariables(waveNumber);
    }

    private void setVariables(int number) {
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

    public void handleWave(long currentTime, List<AbstractGameObject> gameObjects, List<Enemy> enemyList, int JPWIDTH) {
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
