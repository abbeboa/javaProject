public class EnemyWave {
    private long waveStartTime = System.currentTimeMillis();
    private int spawnEnemiesInterval = 6000;
    private int numberOfWaves;
    private int waveNumber;

    public EnemyWave(int waveNumber) {
        this.waveNumber = waveNumber;
        setVariables(waveNumber);
    }
    private void setVariables(int waveNumber) {

    }
}
