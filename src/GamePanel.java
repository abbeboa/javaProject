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
    private static final int ENEMYMARGIN = 100;
    private static final Rectangle ENEMIESGAMEFIELD =
            new Rectangle(-ENEMYMARGIN, -ENEMYMARGIN, JPWIDTH + ENEMYMARGIN, JPHEIGHT + ENEMYMARGIN);
    private static Color bgColor = Color.BLACK;
    private Thread graphicsThread = null;
    private boolean gameRunning = false;
    private boolean gameOver = false;
    private boolean newGame = true;
    private static boolean resumeGame = false;
    private static List<Integer> pressedKeys = new ArrayList<>();
    //score
    private int score1 = 0;
    private int score2 = 0;
    //double buffering variables
    private Graphics dbg = null;
    private Image dbImage = null;
    private static List<AbstractGameObject> gameObjects = new ArrayList<>();
    private static List<Projectile> projectileList = new ArrayList<>();
    private static List<Enemy> enemyList = new ArrayList<>();
    private static List<Integer> gameObjectIdsToRemove = new ArrayList<>();
    private int shootingDelayCounter = 0;
    private EnemyWave currentWave = null;

    //menu
    private enum STATE {
        MENU, GAME
    }

    private STATE state = STATE.MENU;
    private Menu menu = null;
    private int clickableLeft = Menu.getButtonLeft();
    private int clickableRight = Menu.getButtonRight();
    private int clickableTop = Menu.getButtonTop();
    private int clickableBottom = Menu.getButtonBottom();
    //sound
    private String s = "src/sounds/";
    private String soundBackground = s + "backgroundSound.wav"; // jobbig.
    private String takenHit = s + "takenHit1.wav"; // lite jobbig
    private String reloading = s + "reloading1.wav"; // jobbig.
    private String weaponChange = s + "weaponChange1.wav"; // denna bör vi har när vi byter mellan vapen
    private String explosion = s + "explosion.wav";
    private String blaster = s + "blaster.wav";
    //private String enemyBlaster = s+"enemyBlaster.wav"; // ligger just nu i enemy-klassen istället
    private String youLost = s + "youLost.wav";

    //image variables
    private static BufferedImage imgBackground = null;
    private static BufferedImage imgPlayer1 = null;
    private static BufferedImage imgBullet = null;
    private static BufferedImage imgBasicEnemy = null;
    private static Image imgMenuBackground = null;

    private int backgroundImageY1 = -JPHEIGHT;
    private int backgroundImageY2 = 0;

    public GamePanel() {
        importImages();
        setBackground(bgColor);
        setPreferredSize(new Dimension(JPWIDTH, JPHEIGHT));

        setFocusable(true);
        requestFocus(); // enables key events for JPanel

        // create / add game components

        this.addKeyListener(this);
        addMouseListenerFn();
    }

    private static void importImages() {
        try {
            String imageFolderAddress = "src/images/";
            imgBackground = ImageIO.read(new File(imageFolderAddress + "background.jpg"));
            imgMenuBackground = ImageIO.read(new File(imageFolderAddress + "menuBackground.jpg"));
            imgPlayer1 = ImageIO.read(new File(imageFolderAddress + "player3.png"));
            imgBasicEnemy = ImageIO.read(new File(imageFolderAddress + "basicEnemy.png"));
            imgBullet = ImageIO.read(new File(imageFolderAddress + "bullet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mouseAction(int x, int y) {
        // x and y used to check where user has clicked
        if (!gameOver) {
            // actions only performed when game is not over
            if (state == STATE.MENU) {
                if (x > clickableLeft && x < clickableRight && y > clickableBottom && y < clickableTop) { // resume button
                    if (resumeGame) {
                        state = STATE.GAME;
                    }
                } else if (x > clickableLeft && x < clickableRight && y > clickableBottom + 100 &&
                        y < clickableTop + 100) { // new game button
                    resetGame();
                    newGame = true;
                    state = STATE.GAME;
                } else if (x > clickableLeft && x < clickableRight && y > clickableBottom + 200 &&
                        y < clickableTop + 200) { // quit game button
                    stopGame();
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

    private void handleKeyEvents() {
        AbstractGameObject player1 = gameObjects.get(0);
        Collection<Integer> keysToHandle = new ArrayList<>();
        for (int i = 0; i < pressedKeys.size(); i++) {
            keysToHandle.add(pressedKeys.get(i));
        }
        for (int keyCode : keysToHandle) {
            if (keyCode == KeyEvent.VK_ESCAPE) {
                gameRunning = false;
            }
            if (state == STATE.GAME) {
                if (keyCode == KeyEvent.VK_LEFT) {
                    if (GAMEFIELD.contains(player1.getRectangle())) {
                        player1.move(Direction.LEFT, player1.getSpeed());
                    }
                }
                if (keyCode == KeyEvent.VK_RIGHT) {
                    if (GAMEFIELD.contains(player1.getRectangle())) {
                        player1.move(Direction.RIGHT, player1.getSpeed());
                    }
                }
                if (keyCode == KeyEvent.VK_UP) {
                    if (GAMEFIELD.contains(player1.getRectangle())) {
                        player1.move(Direction.UP, player1.getSpeed());
                    }
                }
                if (keyCode == KeyEvent.VK_DOWN) {
                    if (GAMEFIELD.contains(player1.getRectangle())) {
                        player1.move(Direction.DOWN, player1.getSpeed());
                    }
                }
                if (keyCode == KeyEvent.VK_SPACE) {
                    shootingDelayCounter -= 1;
                    if (shootingDelayCounter <= 0) {
                        Sound.play(blaster);
                        player1.shoot(Type.BULLET, Direction.UP, gameObjects, projectileList);
                        int currentShootingDelay = 30;
                        shootingDelayCounter += currentShootingDelay;
                    }
                }
                if (keyCode == KeyEvent.VK_P) {
                    state = STATE.MENU;
                }
            } else if (state == STATE.MENU) {
                if (keyCode == KeyEvent.VK_P) {
                    state = STATE.GAME;
                }
            }
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
            graphicsThread.start();
        }
    }

    private void createPlayer1() {
        double playerStartPosX = JPWIDTH / 2.0 - (imgPlayer1.getWidth() / 2.0);
        double playerStartPosY = JPHEIGHT - imgPlayer1.getHeight();
        gameObjects.add(new Player(playerStartPosX, playerStartPosY, false, Type.PLAYER1));
    }

    private void createWave() {
        currentWave = new EnemyWave(1);
    }

    /**
     * private void createBasicEnemy() { Random rnd = new Random(); int randomNum = rnd.nextInt((JPWIDTH) + 1);
     * <p/>
     * gameObjects.add(new Enemy(randomNum, 0, false, Type.BASICENEMY)); }
     */

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

    public void stopGame() {
        gameRunning = false;
    }

    private void gameUpdate() {
        if (newGame) {
            score1 = 0;
            score2 = 0;
            createPlayer1();
            createWave();
            newGame = false;
            resumeGame = true;
        }
        if (!gameOver) {
            // update game state ...
            handleKeyEvents();
            moveBackgroundImages();
            for (int i = 0; i < projectileList.size(); i++) {
                gameObjectIdsToRemove = projectileList.get(i).updateProjectile(gameObjectIdsToRemove);
            }
            for (int i = 0; i < enemyList.size(); i++) {
                enemyList.get(i).update();
            }
            if (currentWave != null) {
                currentWave.handleWave(System.currentTimeMillis(), gameObjects, enemyList, JPWIDTH);
            }
            checkForCollisions(gameObjectIdsToRemove);
            removeGameObjects(gameObjectIdsToRemove);
        }
        if (gameOver) {
            Sound.play(youLost);
            JFrame frame = new JFrame("Save Highscore");
            String player1 = JOptionPane.showInputDialog(frame, "Please type in your name");
            Highscore hs = new Highscore(score1, player1);
            HighscoreList.addHighscore(hs);
            int answer =
                    JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                resetGame();
                gameOver = false;
                newGame = true;
                resumeGame = false;
                state = STATE.MENU;

            } else {
                System.exit(0);
            }
        }

        // more methods
    }

    private void checkForCollisions(Collection<Integer> gameObjectIdsToRemove) {
        for (int i = 0; i < gameObjects.size(); i++) {
            for (int j = 0; j < gameObjects.size(); j++) {
                //do not check collision with oneself
                if (i != j) {
                    if (gameObjects.get(i).rectangle.intersects(gameObjects.get(j).rectangle)) {
                        handleCollision(gameObjects.get(i), gameObjects.get(j), gameObjectIdsToRemove);
                        //System.out.println("THIS IS A COLLISION!");
                    }
                }
            }
        }
    }

    private void handleCollision(AbstractGameObject objectA, AbstractGameObject objectB, Collection<Integer> gameObjectIdsToRemove) {
        switch (objectA.getGameObjectType()) {
            case PLAYER:
                break;
            case ENEMY:
                enemyCollisionActions(objectA, objectB);
                break;
            case PROJECTILE:
                projectileCollisionActions(objectA, objectB);
                break;
            default:
                System.out.println("handleCollision fault!");
        }
    }

    private void enemyCollisionActions(AbstractGameObject objectA, AbstractGameObject objectB) {
        switch (objectB.getGameObjectType()) {
            case PLAYER:
                gameObjectIdsToRemove.add(objectA.getId());
                Sound.play(takenHit);
                changeStats(objectA, objectB);
                break;
            case ENEMY:
                break;
            case PROJECTILE:
                break;
            default:
        }
    }

    private void projectileCollisionActions(AbstractGameObject objectA, AbstractGameObject objectB) {
        switch (objectB.getGameObjectType()) {
            case PLAYER:
                gameObjectIdsToRemove.add(objectA.getId());
                changeStats(objectA, objectB);
                if (objectA.getOwnerID() != objectB.getId()) { // You can not hurt yourself
                    Sound.play(takenHit);
                }
                break;
            case ENEMY:
                if (objectA.getOwnerID() == 0) { // only players need score
                    changeStats(objectA, objectB);
                }
                gameObjectIdsToRemove.add(objectA.getId());
                break;
            case PROJECTILE:
                gameObjectIdsToRemove.add(objectA.getId());
                break;
            default:
                System.out.println("handleCollision fault!");
        }
    }

    private void removeGameObjects(List<Integer> gameObjectsIdsToRemove) {
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
    }

    private void changeStats(AbstractGameObject objectA, AbstractGameObject objectB) {
        if (objectA.getOwnerID() != objectB.getId()) { // You can not hurt yourself
            objectA.hp--;
            objectB.hp--;
        }
        checkIfDead(objectA);
        checkIfDead(objectB);
    }

    private void checkIfDead(AbstractGameObject objectX) {
        if (objectX.hp <= 0) {
            switch (objectX.getGameObjectType()) {
                case ENEMY:
                    score1 += 100;
                    Sound.play(explosion);
                    gameObjectIdsToRemove.add(objectX.getId());
                    break;
                case PROJECTILE:
                    gameObjectIdsToRemove.add(objectX.getId());
                    break;
                case PLAYER:
                    Sound.play(explosion);
                    System.out.println("Game over");
                    gameOver = true;
            }
        }
    }

    private void resetGame() {
        score1 = 0;
        score2 = 0;
        while (!gameObjects.isEmpty()) {
            gameObjects.remove(0);
        }
        while (!projectileList.isEmpty()) {
            projectileList.remove(0);
        }
        while (!enemyList.isEmpty()) {
            enemyList.remove(0);
        }
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
            }
        } else if (state == STATE.MENU) {
            drawMenu(dbg);
            //Sound.play(soundBackground); // jobbigt..

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
            menu.render(g, score1, score2);
        }
    }

    private void moveBackgroundImages() {
        backgroundImageY1 += 5;
        backgroundImageY2 += 5;
        if (backgroundImageY1 > JPHEIGHT) {
            backgroundImageY1 = -JPHEIGHT;
        }
        if (backgroundImageY2 > JPHEIGHT) {
            backgroundImageY2 = -JPHEIGHT;
        }
    }

    private void drawGameOver(Graphics g) {
        gameRunning = false;
        // should be replaced by drawImage, using drawString for now
        System.out.println("You lost");
        String msg = "You lost";
        g.drawString(msg, JPWIDTH / 2, JPHEIGHT / 2);
    }

    public static int getJpwidth() {
        return JPWIDTH;
    }

    public static int getJpheight() {
        return JPHEIGHT;
    }

    public Image getDbImage() {
        return dbImage;
    }

    public static boolean isResumeGame() {
        return resumeGame;
    }

    public static Collection<AbstractGameObject> getGameObjects() {
        return gameObjects;
    }

    public static Collection<Projectile> getProjectileList() {
        return projectileList;
    }

    public static List<Enemy> getEnemyList() {
        return enemyList;
    }

    public int getClickableLeft() {
        return clickableLeft;
    }

    public int getClickableRight() {
        return clickableRight;
    }

    public int getClickableTop() {
        return clickableTop;
    }

    public int getClickableBottom() {
        return clickableBottom;
    }

    public static Image getImgMenuBackground() {
        return imgMenuBackground;
    }

    public static BufferedImage getImgBackground() {
        return imgBackground;
    }

    public static BufferedImage getImgPlayer1() {
        return imgPlayer1;
    }

    public static BufferedImage getImgBullet() {
        return imgBullet;
    }

    public static BufferedImage getImgBasicEnemy() {
        return imgBasicEnemy;
    }
}