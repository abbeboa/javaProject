public class EnemyWave {
    private long waveStartTime = System.currentTimeMillis();
    private int waveInterval;
    private int spawnEnemiesInterval;
    private int numberOfWaves;
    private int waveNumber;
    private int enemiesPerWave;
    private boolean progressive = false;

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
            this.numberOfWaves = 4;
            this.waveInterval = 6000;
            this.spawnEnemiesInterval = 1000;
            this.enemiesPerWave = 4;
        }
    }

    public void handleWave(long currentTime) {
        if (currentTime > waveStartTime + waveInterval) {
            System.out.println("WAVE ACTIVATE!!!");
            waveStartTime = currentTime;
            numberOfWaves--;
        }
        if (numberOfWaves <= 0) {
            waveNumber++;
            setVariables(waveNumber);
        }
    }
}
