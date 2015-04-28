import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    public static final int JPWIDTH = 1280; // JPanel size
    public static final int JPHEIGHT = 720;
    private static final int PERIOD = 10; // time in ms for each game update
    public static final Rectangle GAMEFIELD = new Rectangle(0, 0, JPWIDTH, JPHEIGHT);
    private static final int ENEMYMARGIN = 50;
    public static final Rectangle ENEMIESGAMEFIELD =
            new Rectangle(-ENEMYMARGIN, -ENEMYMARGIN, JPWIDTH + (ENEMYMARGIN * 2), JPHEIGHT + (ENEMYMARGIN * 2));
    private static Color bgColor = Color.BLACK;
    private Thread graphicsThread = null;
    private static boolean gameRunning = false;
    private static boolean gameOver = false;
    private static boolean newGame = true;
    private static boolean resumeGame = false;
    private static boolean soundEnabled = true;
    private static int playerCount = 1;
    private static List<Integer> pressedKeys = new ArrayList<>();
    private CollisionHandler collisionHandler = null;
    private KeyEventHandler keyEventHandler;
    //score
    private static int scorePlayer1 = 0;
    private static int scorePlayer2 = 0;
    //double buffering variables
    private Graphics dbg = null;
    private Image dbImage = null;
    private static List<AbstractGameObject> gameObjects = new ArrayList<>();
    private static List<Projectile> projectileList = new ArrayList<>();
    private static List<Enemy> enemyList = new ArrayList<>();
    private static List<PowerUp> powerUps = new ArrayList<>();
    private static Collection<Integer> gameObjectIdsToRemove = new ArrayList<>();
    private EnemyWave currentWave = null;
    //fonts
    private static Font digital7 = null;
    private static Font headline = null;
    private static Font menuText = null;

    //menu
    public enum STATE {
        MENU, GAME
    }

    private static STATE state = STATE.MENU;
    private Menu menu = null;
    private static final int RESUMEBUTTONRIGHT 	= 1024;
    private static final int RESUMEBUTTONBOTTOM = 120;
    private static final int NEWBUTTONRIGHT 	= 1024;
    private static final int NEWBUTTONBOTTOM 	= 220;
    private static final int PLAYERBUTTONRIGHT 	= 1024;
    private static final int PLAYERBUTTONBOTTOM = 320;
    private static final int QUITBUTTONRIGHT 	= 1024;
    private static final int QUITBUTTONBOTTOM 	= 420;
    private static final int HEALTHPLAYERSTRINGPLACING = 16;
    private static final int GREENHPCONSTANT = 60;
    private static final int YELLOWHPCONSTANT = 30;
    private static final float MEDIUMFONTSIZE = 32.0f;
    private static final float LARGEFONTSIZE = 50.0f;

    //sound
    private static final String SOUNDFOLDER = "src/sounds/";
    private static final String TAKENHIT = SOUNDFOLDER + "takenhit1.wav";
    private static final String EXPLOSION = SOUNDFOLDER + "explosion.wav";
    private static final String BLASTER = SOUNDFOLDER + "blaster.wav";
    private static final String ENEMYBLASTER = SOUNDFOLDER + "enemyBlaster.wav"; // ligger just nu i enemy-klassen istället
    private static final String YOULOST = SOUNDFOLDER + "youLost.wav";
    private static final String INDESTRUCTIBLE = SOUNDFOLDER + "indestructible_new.wav";
    private static final String DOUBLEFIRERATE = SOUNDFOLDER + "double_firerate.wav";
    private static final String DOUBLESPEED = SOUNDFOLDER + "double_speed.wav";
    private static final String EXTRAHEALTH = SOUNDFOLDER + "extra_health.wav";

    //image variables
    private static BufferedImage imgBackground = null;
    private static BufferedImage imgPlayer1 = null;
    private static BufferedImage imgPlayer2 = null;
    private static BufferedImage imgBullet = null;
    private static BufferedImage imgBasicEnemy = null;
    private static BufferedImage imgPlayerIndestructible = null;
    private static Image imgMenuBackground = null;

    private int backgroundImageY1 = -JPHEIGHT;
    private int backgroundImageY2 = 0;

    public GamePanel() {
        registerFontFiles();
        importImages();
        setBackground(bgColor);
        setPreferredSize(new Dimension(JPWIDTH, JPHEIGHT));

        setFocusable(true);
        requestFocus(); // enables key events for JPanel

        // create / add game components

        keyEventHandler = new KeyEventHandler();
        this.addKeyListener(this);
        addMouseListenerFn();
    }

    private static void importImages() {
        try {
            String imageFolderAddress = "src/images/";
            imgBackground = ImageIO.read(new File(imageFolderAddress + "background.jpg"));
            imgMenuBackground = ImageIO.read(new File(imageFolderAddress + "menuBackground.jpg"));
            imgPlayer1 = ImageIO.read(new File(imageFolderAddress + "player3.png"));
            imgPlayer2 = ImageIO.read(new File(imageFolderAddress + "player2.png"));
            imgBasicEnemy = ImageIO.read(new File(imageFolderAddress + "basicEnemy.png"));
            imgBullet = ImageIO.read(new File(imageFolderAddress + "bullet.png"));
            imgPlayerIndestructible = ImageIO.read(new File(imageFolderAddress + "playerindestructible.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mouseAction(int x, int y) {
        // x and y used to check where user has clicked
        if (!gameOver) {
            // actions only performed when game is not over
            if (state == STATE.MENU) {
                if (x > Menu.getResumebuttonleft() && x < RESUMEBUTTONRIGHT && y > RESUMEBUTTONBOTTOM && y < Menu.getResumebuttontop()) { // resume button
                    if (resumeGame) {
                        state = STATE.GAME;
                    }
                } else if (x > Menu.getNewbuttonleft() && x < NEWBUTTONRIGHT && y > NEWBUTTONBOTTOM && y < Menu.getNewbuttotop()) { // new game button
                    resetGame();
                    newGame = true;
                    state = STATE.GAME;
                } else if (x > Menu.getPlayerbuttonleft() && x < PLAYERBUTTONRIGHT && y > PLAYERBUTTONBOTTOM && y < Menu.getPlayerbuttontop()) { // Player count button
                    if (playerCount == 1) {
                        playerCount = 2;
                    } else {
                        playerCount = 1;
                    }
                } else if (x > Menu.getQuitbuttonleft() && x < QUITBUTTONRIGHT && y > QUITBUTTONBOTTOM && y < Menu.getQuitbuttontop()) { // quit game button
                    stopGame();
                } else if ((x > Menu.getSoundbuttonleft()) && (x < Menu.getSoundbuttonright()) && (y > Menu.getSoundbuttontop()) && (y < Menu.getSoundbuttonbottom())) {
                    if (soundEnabled) {
                        soundEnabled = false;
                    } else {
                        soundEnabled = true;
                    }
                }
            }
        }
    }

    private void addMouseListenerFn() {
        MouseListener mouseAdapter = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseAction(e.getX(), e.getY());
            }
        };
        addMouseListener(mouseAdapter);
    }

    public void keyPressed(KeyEvent e) {
        handleKey("keyPressed", e.getKeyCode());
    }

    public void keyReleased(KeyEvent e) {
        handleKey("keyReleased", e.getKeyCode());
    }

    public void keyTyped(KeyEvent e) {
    }

    private void handleKey(String eventType, int keyCode) {
        if (Objects.equals(eventType, "keyPressed")) {
            if (!pressedKeys.contains(keyCode)) {
                pressedKeys.add(keyCode);
            }
        } else {
            while (pressedKeys.contains(Integer.valueOf(keyCode))) pressedKeys.remove(Integer.valueOf(keyCode));
        }
    }

    public void addNotify() {
     /* Makes sure JPanel and JFrame is ready
     before starting the game. */
        super.addNotify(); // Built in java-function
        startGame();
    }

    private void startGame() {
        // starts a new thread
        if (graphicsThread == null || !gameRunning) {
            graphicsThread = new Thread(this);
            menu = new Menu();
            collisionHandler = new CollisionHandler();
            graphicsThread.start();
        }
    }

    private void createPlayer1() {
        double playerStartPosX = JPWIDTH / 2.0 - (imgPlayer1.getWidth() / 2.0);
        double playerStartPosY = JPHEIGHT - imgPlayer1.getHeight();
        gameObjects.add(new Player(playerStartPosX, playerStartPosY, Type.PLAYER1));
    }

    private void createWave() {
        currentWave = new EnemyWave(1);
    }

    public void run() {
        long startTime;

        startTime = System.currentTimeMillis();

        gameRunning = true;
        while (gameRunning) {

            gameRender(); // render image
            paintImage(); // paint image on screen

            if (state == STATE.GAME) {
                gameUpdate(); // update game events
            }

            long timeSinceStart = System.currentTimeMillis() - startTime;
            long sleepTime = PERIOD - timeSinceStart;

            if (sleepTime <= 0) {
                sleepTime = 5; // a little bit of sleep is always needed
            }
            try {
                Thread.sleep(sleepTime); // frees the CPU to perform other tasks
            } catch (InterruptedException ignored) {
            }

            startTime = System.currentTimeMillis();
        }
        System.exit(0); // exits JFrame
    }

    public static void stopGame() {
        gameRunning = false;
    }

    private void gameUpdate() {
        if (newGame) {
            scorePlayer1 = 0;
            scorePlayer2 = 0;
            createPlayer1();
            createWave();
            newGame = false;
            resumeGame = true;
        }
        if (!gameOver) {
            // update game state ...
            keyEventHandler.handleKeyEvents(pressedKeys);
            moveBackgroundImages();
            for (int i = 0; i < projectileList.size(); i++) {
                projectileList.get(i).updateProjectile();
            }
            for (int i = 0; i < enemyList.size(); i++) {
                enemyList.get(i).update();
            }
            for (int i = 0; i < powerUps.size(); i++) {
                powerUps.get(i).update();
            }
            if (currentWave != null) {
                currentWave.handleWave(System.currentTimeMillis());
            }
            collisionHandler.checkForCollisions(gameObjects);
            removeGameObjects(gameObjectIdsToRemove);
        }
        if (gameOver) {
            Sound.play(YOULOST);
            JFrame frame = new JFrame("Save Highscore");
            String player1 = JOptionPane.showInputDialog(frame, "Please type in your name");
            Highscore hs = new Highscore(scorePlayer1, player1);
            HighscoreList.addHighscore(hs);
            int answer =
                    JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                resetGame();
                gameOver = false;
                resumeGame = false;
                state = STATE.MENU;

            } else {
                System.exit(0);
            }
        }

        // more methods
    }

    private void removeGameObjects(Collection<Integer> gameObjectsIdsToRemove) {
        for (int i = 0; i < gameObjects.size(); i++) {
            if (gameObjectsIdsToRemove.contains(gameObjects.get(i).getId())) {
                gameObjects.remove(i);
            }
        }
        for (int i = 0; i < projectileList.size(); i++) {
            if (gameObjectsIdsToRemove.contains(projectileList.get(i).getId())) {
                projectileList.remove(i);
            }
        }
        for (int i = 0; i < enemyList.size(); i++) {
            if (gameObjectsIdsToRemove.contains(enemyList.get(i).getId())) {
                enemyList.remove(i);
            }
        }
        for (int i = 0; i < powerUps.size(); i++) {
            if (gameObjectsIdsToRemove.contains(powerUps.get(i).getId())) {
                powerUps.remove(i);
            }
        }
    }

    private static void resetGame() {
        scorePlayer1 = 0;
        scorePlayer2 = 0;
        gameObjects.clear();
        projectileList.clear();
        enemyList.clear();
        gameObjectIdsToRemove.clear();
        pressedKeys.clear();
        AbstractGameObject.setCounter(0);
    }

    private void gameRender() {
        //for double buffering (draw frame to image buffer)
        if (dbImage == null) {
            dbImage = createImage(JPWIDTH, JPHEIGHT);
            if (dbImage == null) {
                System.out.println("dbImage is null");
                return;
            } else {
                dbg = dbImage.getGraphics();
            }
        }

        // clear JFrame
        dbg.setColor(Color.BLACK);
        dbg.fillRect(0, 0, JPWIDTH, JPHEIGHT);

        // draw all game objects ...
        dbg.setColor(bgColor);
        dbg.fillRect(0, 0, JPWIDTH, JPHEIGHT);

        dbg.drawImage(imgBackground, 0, backgroundImageY1, this);
        dbg.drawImage(imgBackground, 0, backgroundImageY2, this);

        if (state == STATE.GAME) {
            for (int i = 0; i < gameObjects.size(); i++) {
                gameObjects.get(i).drawGameObject(dbg, this);
                drawPlayerStats(dbg);
            }
        } else if (state == STATE.MENU) {
            drawMenu(dbg);
        }
        if (gameOver) {
            drawGameOver(dbg);
        }
    }

    private void paintImage() {
        try {
            Graphics g = this.getGraphics();
            if ((g != null) && (dbImage != null)) {
                g.drawImage(dbImage, 0, 0, null);
                g.dispose();
            }
        } catch (RuntimeException e) {
            System.out.println("drawImage graphics error:" + e);
        }
    }

    private void drawMenu(Graphics dbg) {
        Graphics2D g = (Graphics2D) dbg;
        if (state == STATE.MENU) {
            menu.render(g, scorePlayer1, scorePlayer2);
        }
    }

    private void moveBackgroundImages() {
        backgroundImageY1 += 2;
        backgroundImageY2 += 2;
        if (backgroundImageY1 > JPHEIGHT) {
            backgroundImageY1 = -JPHEIGHT;
        }
        if (backgroundImageY2 > JPHEIGHT) {
            backgroundImageY2 = -JPHEIGHT;
        }
    }

    private void drawPlayerStats(Graphics g) {
        g.setFont(digital7);
        g.setColor(decideHealthColor(gameObjects.get(0).getHp()));
        String healthPlayer1 = "Health: " + gameObjects.get(0).getHp();
	g.drawString(healthPlayer1, (JPWIDTH - JPWIDTH / 9), JPHEIGHT / HEALTHPLAYERSTRINGPLACING);
        String scoreStringPlayer1 = "Score: " + scorePlayer1;
	g.setColor(Color.GREEN);
	g.drawString(scoreStringPlayer1, (JPWIDTH - JPWIDTH / 9), JPHEIGHT / 10);
        if (playerCount >= 2) {
            g.setColor(decideHealthColor(gameObjects.get(1).getHp()));
            String healthStringPlayer2 = "Health: " + gameObjects.get(1).getHp();
	    g.drawString(healthStringPlayer2, (JPWIDTH / 9), JPHEIGHT / HEALTHPLAYERSTRINGPLACING);
        }
    }

    private Color decideHealthColor(int hp) {
        if (hp > GREENHPCONSTANT) {
            return Color.GREEN;
        } else if (hp > YELLOWHPCONSTANT) {
            return Color.YELLOW;
        } else {
            return Color.RED;
        }
    }

    private void drawGameOver(Graphics g) {
        gameRunning = false;
        // should be replaced by drawImage, using drawString for now
        System.out.println("You lost");
        String msg = "You lost";
        g.drawString(msg, JPWIDTH / 2, JPHEIGHT / 2);
    }

    private void registerFontFiles() {
        try {
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            digital7 = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/digital-7.ttf")).deriveFont(MEDIUMFONTSIZE);
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/digital-7.ttf")));
	    headline = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/headline.ttf")).deriveFont(LARGEFONTSIZE);
	    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/headline.ttf")));
	    menuText = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/menutext.ttf")).deriveFont(MEDIUMFONTSIZE);
	    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/menutext.ttf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public static void addGameObjectIdToRemove(int id) {
        gameObjectIdsToRemove.add(id);
    }

    public static void addScore(int points) {
        GamePanel.scorePlayer1 += points;
    }

    public static void setGameOver(boolean gameOver) {
        GamePanel.gameOver = gameOver;
    }

    public static void playSoundTakenHit() {
        Sound.play(TAKENHIT);
    }

    public static void playSoundExplosion() {
        Sound.play(EXPLOSION);
    }

    public static void playSoundBlaster() {
        Sound.play(BLASTER);
    }

    public static void playSoundEnemyBlaster() {
        Sound.play(ENEMYBLASTER);
    }

    public static void playSoundIndestructible() { Sound.play(INDESTRUCTIBLE); }

    public static void playSoundDoubleFirerate() { Sound.play(DOUBLEFIRERATE); }

    public static void playSoundDoubleSpeed() { Sound.play(DOUBLESPEED); }

    public static void playSoundExtraHealth() { Sound.play(EXTRAHEALTH); }

    public static void addToGameObjectsList(AbstractGameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public static void addToProjectileList(Projectile projectile) {
        projectileList.add(projectile);
    }

    public static void addToEnemyList(Enemy enemy) {
        enemyList.add(enemy);
    }

    public static void addToPowerUps(PowerUp powerUp) {
        powerUps.add(powerUp);
    }

    public static AbstractGameObject getGameObject(int i) {
        return gameObjects.get(i);
    }

    public static boolean checkStateEqualsGame() {
        return state == STATE.GAME;
    }

    public static boolean checkStateEqualsMenu() {
        return state == STATE.MENU;
    }

    public static boolean isResumeGame() {
        return resumeGame;
    }

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    public static Image getImgMenuBackground() {
        return imgMenuBackground;
    }

    public static BufferedImage getImgPlayer1() {
        return imgPlayer1;
    }

    public static BufferedImage getImgPlayer2() {
        return imgPlayer2;
    }

    public static BufferedImage getImgBullet() {
        return imgBullet;
    }

    public static BufferedImage getImgBasicEnemy() {
        return imgBasicEnemy;
    }

    public static BufferedImage getImgPlayerIndestructible() {
        return imgPlayerIndestructible;
    }

    public static void setGameRunning(boolean gameRunning) {
        GamePanel.gameRunning = gameRunning;
    }

    public static void setStateMenu() {
        GamePanel.state = STATE.MENU;
    }

    public static void setStateGame() {
        GamePanel.state = STATE.GAME;
    }

    public static int getEnemymargin() {
        return ENEMYMARGIN;
    }

    public static int getPlayerCount() {
        return playerCount;
    }

    public static Font getHeadline() {return headline; }

    public static Font getMenuText() {return menuText; }
}