package se.liu.ida.albpe868.tddd78.javaproject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

/**
 * This class creates a JPanel and also contains a run-loop that paints the screen and updates gameEvents. It also contains alot
 * of help-methods for drawing (font, importImages etc.) that are needed.
 */
public class GamePanel extends JPanel implements Runnable, KeyListener {
    private static final int JPWIDTH = 1280; // JPanel size
    private static final int JPHEIGHT = 720;
    private static final int PERIOD = 10; // time in ms for each game update
    private static final Rectangle GAMEFIELD = new Rectangle(0, 0, JPWIDTH, JPHEIGHT);
    private static final int ENEMYMARGIN = 50;
    private static final Rectangle ENEMIESGAMEFIELD =
            new Rectangle(-ENEMYMARGIN, -ENEMYMARGIN, JPWIDTH + (ENEMYMARGIN * 2), JPHEIGHT + (ENEMYMARGIN * 2));
    private static final double DIVIDEBYTWODOUBLE = 2.0;
    private static Color bgColor = Color.BLACK;
    private Thread graphicsThread = null;
    private boolean gameRunning = false;
    private boolean gameOver = false;
    private boolean newGame = true;
    private boolean resumeGame = false;
    private boolean soundEnabled = true;
    private int playerCount = 1;
    private int initialPlayerCount = 0;
    private List<Integer> pressedKeys = new ArrayList<>();
    private CollisionHandler collisionHandler = null;
    private KeyEventHandler keyEventHandler;
    private MouseActionHandler mouseActionHandler;
    private HighscoreHandler highscoreHandler = null;
    //score
    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    //double buffering variables
    private Graphics dbg = null;
    private Image dbImage = null;
    private GameObjectsList gameObjects;
    private List<VisualEffect> visualEffects = new ArrayList<>();
    private Collection<Integer> gameObjectIdsToRemove = new ArrayList<>();
    private Collection<Integer> visualEffectsIdsToRemove = new ArrayList<>();
    private EnemyWave currentWave = null;
    //fonts
    private Font digital7 = null;
    private Font headline = null;
    private Font menuText = null;

    //menu
    public enum STATE {
        /**
         * When you're in the menu
         */
        MENU,
        /**
         * Game has started
         */
        GAME
    }

    private STATE state = STATE.MENU;
    private Menu menu = null;
    private static final int HEALTHPLAYERSTRINGPLACING = 16;
    private static final int GREENHPCONSTANT = 60;
    private static final int YELLOWHPCONSTANT = 30;
    private static final float MEDIUMFONTSIZE = 32.0f;
    private static final float LARGEFONTSIZE = 50.0f;

    //image variables
    private static BufferedImage imgBackground = null;
    private static BufferedImage imgPlayer1 = null;
    private static BufferedImage imgPlayer2 = null;
    private static BufferedImage imgBullet = null;
    private static BufferedImage imgBasicEnemy = null;
    private static BufferedImage imgPlayerIndestructible = null;
    private static BufferedImage imgExplosion = null;
    private static BufferedImage imgPowerUp = null;
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

        gameObjects = new GameObjectsList();
        highscoreHandler = new HighscoreHandler();
        keyEventHandler = new KeyEventHandler(this);
        mouseActionHandler = new MouseActionHandler(this);
        this.addKeyListener(this);
        addMouseListenerFn();
    }

    private static void importImages() {
        String imageFolderAddress = "images/";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (
                InputStream input0 = classLoader.getResourceAsStream(imageFolderAddress + "background.jpg");
                InputStream input1 = classLoader.getResourceAsStream(imageFolderAddress + "menuBackground.jpg");
                InputStream input2 = classLoader.getResourceAsStream(imageFolderAddress + "player1.png");
                InputStream input3 = classLoader.getResourceAsStream(imageFolderAddress + "playertwo.png");
                InputStream input4 = classLoader.getResourceAsStream(imageFolderAddress + "basicEnemy.png");
                InputStream input5 = classLoader.getResourceAsStream(imageFolderAddress + "bullet.png");
                InputStream input6 = classLoader.getResourceAsStream(imageFolderAddress + "playerindestructible.png");
                InputStream input7 = classLoader.getResourceAsStream(imageFolderAddress + "explosion.png");
                InputStream input8 = classLoader.getResourceAsStream(imageFolderAddress + "powerup.png")
        ) {
            imgBackground = ImageIO.read(input0);
            imgMenuBackground = ImageIO.read(input1);
            imgPlayer1 = ImageIO.read(input2);
            imgPlayer2 = ImageIO.read(input3);
            imgBasicEnemy = ImageIO.read(input4);
            imgBullet = ImageIO.read(input5);
            imgPlayerIndestructible = ImageIO.read(input6);
            imgExplosion = ImageIO.read(input7);
            imgPowerUp = ImageIO.read(input8);
        } catch (IOException ex) {
            LogHandler.log(Level.SEVERE, "importImages error", ex);
        }
    }

    private void addMouseListenerFn() {
        MouseListener mouseAdapter = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                mouseActionHandler.mouseAction(e.getX(), e.getY());
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
            menu = new Menu(this);
            collisionHandler = new CollisionHandler(this);
            highscoreHandler.readFile();
            HighscoreList.printAllHighScores();
            graphicsThread.start();
            LogHandler.log(Level.INFO, "Game started", null);
        }
    }

    private void createPlayer(int playerNumber) {
        double playerStartPosX = JPWIDTH / DIVIDEBYTWODOUBLE - (imgPlayer1.getWidth() / DIVIDEBYTWODOUBLE);
        double playerStartPosY = JPHEIGHT - imgPlayer1.getHeight();
        if (playerNumber == 1) {
            gameObjects.addObject(new Player(playerStartPosX, playerStartPosY, Type.PLAYER1));
        } else {
            gameObjects.addObject(new Player(playerStartPosX - imgPlayer1.getWidth(), playerStartPosY, Type.PLAYER2));
        }
    }

    private void createWave() {
        currentWave = new EnemyWave();
    }

    public void run() {
        long startTime;

        startTime = System.currentTimeMillis();

        gameRunning = true;

        // *** Inspection error: "while loop spins on field" ***
        // A while loop is necessary to make the game run, after investigating this error we could not find
        // any alternative method.
        while (gameRunning) {

            gameRender(); // render image
            paintImage(); // paint image on screen

            if (state == STATE.GAME) {
                gameUpdate(); // update game events
            }

            long timeSinceStart = System.currentTimeMillis() - startTime;
            long sleepTime = PERIOD - timeSinceStart;

            if (sleepTime <= 0) {
                LogHandler.log(Level.WARNING, "gameLoop running slow. Millisec: " + sleepTime, null);
                sleepTime = 5; // a little bit of sleep is always needed
            }
            try {
                Thread.sleep(sleepTime);
                // frees the CPU to perform other tasks. Needed to make the game run at 100fps
                // This sleep makes each game-update the same time-length.
                // *** INSPECTION ERROR: probably busy-waiting ***
                // The method we used to make our game run at 100fps is not 100% accurate,
                // it may differ depending on how fast the computer's processor can execute a "do nothing" loop.
                // We chose not to focus on finding a better method because the game is running very good
                // and we could focus on learning OO-related things instead.
            } catch (InterruptedException ignored) {
            }

            startTime = System.currentTimeMillis();
        }
        exitGame();
    }

    private void exitGame() {
        highscoreHandler.writeToFile();
        HighscoreList.printAllHighScores();
        System.exit(0); // exits JFrame
    }

    public void stopGame() {
        gameRunning = false;
    }

    private void gameUpdate() {
        if (newGame) {
            initialPlayerCount = 0;
            scorePlayer1 = 0;
            createPlayer(1);
            initialPlayerCount++;
            if (playerCount > 1) {
                createPlayer(2);
                scorePlayer2 = 0;
                initialPlayerCount++;
            }
            createWave();
            newGame = false;
            resumeGame = true;
        }
        if (!gameOver) {
            // update game state ...
            keyEventHandler.handleKeyEvents(pressedKeys);
            moveBackgroundImages();
            gameObjects.updateObjects(this);
            for (int i = 0; i < visualEffects.size(); i++) {
                visualEffects.get(i).update(this);
            }
            if (currentWave != null) {
                currentWave.handleWave(System.currentTimeMillis(), this);
            }
            collisionHandler.checkForCollisions(gameObjects.getGameObjects());
            gameObjects.removeGameObjects(gameObjectIdsToRemove);
            removeVisualEffects(visualEffectsIdsToRemove);
        }
        if (gameOver) {
            Sound.playSoundYouLost(soundEnabled);
            JFrame frame = new JFrame("Save Highscore");
            String player1 = JOptionPane.showInputDialog(frame, "Please type in Player1 name");
            Highscore highscore = new Highscore(scorePlayer1, player1);
            HighscoreList.addHighscore(highscore);

            if (playerCount > 1) {
                System.out.println("\n");
                String player2 = JOptionPane.showInputDialog(frame, "Please type in Player2 name");
                Highscore hs2 = new Highscore(scorePlayer2, player2);
                HighscoreList.addHighscore(hs2);
            }

            int answer =
                    JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                resetGame();
                gameOver = false;
                resumeGame = false;
                state = STATE.MENU;

            } else {
                exitGame();
            }
        }
        // more methods
    }

    private void removeVisualEffects(Collection<Integer> visualEffectsIdsToRemove) {
        for (int i = 0; i < visualEffects.size(); i++) {
            if (visualEffectsIdsToRemove.contains(visualEffects.get(i).getId())) {
                visualEffects.remove(i);
            }
        }
    }

    public void resetGame() {
        scorePlayer1 = 0;
        scorePlayer2 = 0;
        gameObjects.clearList();
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
            gameObjects.drawGameObjects(dbg, this);
            for (int i = 0; i < visualEffects.size(); i++) {
                visualEffects.get(i).drawVisualEffect(dbg, this);
            }
            if (!gameObjects.getGameObjects().isEmpty()) {
                drawPlayerStats(dbg);
            }
        } else if (state == STATE.MENU) {
            drawMenu(dbg);
        }
    }

    private void paintImage() {
        try {
            Graphics g = this.getGraphics();
            if ((g != null) && (dbImage != null)) {
                g.drawImage(dbImage, 0, 0, null);
                g.dispose();
            }
        } catch (RuntimeException ex) {
            LogHandler.log(Level.SEVERE, "drawImage graphics error: ", ex);
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
        g.setColor(decideHealthColor(gameObjects.getObject(0).getHp()));
        String healthPlayer1 = "Health: " + gameObjects.getObject(0).getHp();
        g.drawString(healthPlayer1, (JPWIDTH - JPWIDTH / 9), JPHEIGHT / HEALTHPLAYERSTRINGPLACING);
        String scoreStringPlayer1 = "Score: " + scorePlayer1;
        g.setColor(Color.GREEN);
        g.drawString(scoreStringPlayer1, (JPWIDTH - JPWIDTH / 9), JPHEIGHT / 10);
        if (playerCount >= 2) {
            g.setColor(decideHealthColor(gameObjects.getObject(1).getHp()));
            String scoreStringPlayer2 = "Score: " + scorePlayer2;
            String healthStringPlayer2 = "Health: " + gameObjects.getObject(1).getHp();
            g.drawString(healthStringPlayer2, (JPWIDTH / 9), JPHEIGHT / HEALTHPLAYERSTRINGPLACING);
            g.setColor(Color.GREEN);
            g.drawString(scoreStringPlayer2, (JPWIDTH / 9), JPHEIGHT / 10);
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

    private void registerFontFiles() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            digital7 = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/digital-7.ttf")).deriveFont(MEDIUMFONTSIZE);
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/digital-7.ttf")));
            headline = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/headline.ttf")).deriveFont(LARGEFONTSIZE);
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/headline.ttf")));
            menuText = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/menutext.ttf")).deriveFont(MEDIUMFONTSIZE);
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/menutext.ttf")));
        } catch (IOException | FontFormatException ex) {
            LogHandler.log(Level.SEVERE, "registerFontFiles error: ", ex);
        }
    }


    public void addGameObjectIdToRemove(int id) {
        gameObjectIdsToRemove.add(id);
    }

    public void addVisualEffectIdToRemove(int id) {
        visualEffectsIdsToRemove.add(id);
    }

    public void addScorePlayer1(int points) {
        scorePlayer1 += points;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void addScorePlayer2(int points) {
        scorePlayer2 += points;
    }

    public void addToGameObjectsList(AbstractGameObject gameObject) {
        gameObjects.addObject(gameObject);
    }

    public void addToVisualEffects(VisualEffect visualEffect) {
        visualEffects.add(visualEffect);
    }

    public AbstractGameObject getGameObject(int i) {
        return gameObjects.getObject(i);
    }

    public boolean checkStateEqualsGame() {
        return state == STATE.GAME;
    }

    public boolean checkStateEqualsMenu() {
        return state == STATE.MENU;
    }

    public boolean isResumeGame() {
        return resumeGame;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
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

    public static BufferedImage getImgExplosion() {
        return imgExplosion;
    }

    public static BufferedImage getImgPowerUp() {
        return imgPowerUp;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public void setStateMenu() {
        state = STATE.MENU;
    }

    public void setStateGame() {
        state = STATE.GAME;
    }

    public static int getEnemymargin() {
        return ENEMYMARGIN;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getInitialPlayerCount() {
        return initialPlayerCount;
    }

    public Font getHeadline() {
        return headline;
    }

    public Font getMenuText() {
        return menuText;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setNewGame(boolean newGame) {
        this.newGame = newGame;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public static int getJpwidth() {
        return JPWIDTH;
    }

    public static int getJpheight() {
        return JPHEIGHT;
    }

    public static Rectangle getGamefield() {
        return GAMEFIELD;
    }

    public static Rectangle getEnemiesgamefield() {
        return ENEMIESGAMEFIELD;
    }
}