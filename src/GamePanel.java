import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.awt.Rectangle;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    public static final int JPWIDTH = 1280; // JPanel size
    public static final int JPHEIGHT = 720;
    private static final int PERIOD = 10; // time in ms for each game update
    public static final Rectangle gameField = new Rectangle(0, 0, JPWIDTH, JPHEIGHT);

    public static int getJpwidth() {
        return JPWIDTH;
    }

    public static int getJpheight() {
        return JPHEIGHT;
    }

    private static Color bgColor = Color.BLACK;

    private Thread graphicsThread;
    private boolean gameRunning = false;
    private boolean gameOver = false;
    private boolean newGame = true;
    private static boolean resumeGame = false;

    private static List<Integer> pressedKeys = new ArrayList<Integer>();

    //double buffering variables
    private Graphics dbg;
    private Image dbImage;

    private Player player1;
    private int currentShootingDelay = 30;
    private int shootingDelayCounter = 0;
    private EnemyWave currentWave;

    //menu
    private enum STATE {
        MENU, GAME
    }

    private STATE state = STATE.MENU;
    private Menu menu;
    private int clickableLeft = (JPWIDTH / 2 + JPWIDTH / 10);
    private int clickableRight = (JPWIDTH / 2 + JPWIDTH / 10 + JPWIDTH / 5);
    private int clickableTop = (Menu.height + JPWIDTH / 20);
    private int clickableBottom = Menu.height;

    //test variables
    private int testX;
    private int testY;

    public static List<Projectile> projectileList = new ArrayList<Projectile>();

    //image variables
    public static BufferedImage imgBackground;
    public static BufferedImage imgPlayer1;
    public static BufferedImage imgBullet;
    public static BufferedImage imgBasicEnemy;
    public static Image imgMenuBackground;


    private int backgroundImageY1 = -JPHEIGHT;
    private int backgroundImageY2 = 0;

    public GamePanel() {
        importImages();
        setBackground(bgColor);
        setPreferredSize(new Dimension(JPWIDTH, JPHEIGHT));

        setFocusable(true);
        requestFocus(); // enables key events for JPanel

        // create / add game components

        //addKeyListener_();
        this.addKeyListener(this);
        addMouseListener_();
    }

    private void importImages() {
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
                    newGame = true;
                    state = STATE.GAME;
                } else if (x > clickableLeft && x < clickableRight && y > clickableBottom + 200 &&
                        y < clickableTop + 200) { // quit game button
                    stopGame();
                }
            }
        }
    }

    private void addMouseListener_() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseAction(e.getX(), e.getY());
            }
        };
        addMouseListener(mouseAdapter);
    }

    /*private void addKeyListener_() {
    KeyAdapter keyAdapter = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                handleKey("keyPressed", e);
                System.out.println("KeyEvent is: " + e);
                int keyCode = e.getKeyCode();
                if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_C && e.isControlDown())) {
                    gameRunning = false;
                }
                if (keyCode == KeyEvent.VK_A) {
                    bgColor = Color.PINK;
                }
                if (keyCode == KeyEvent.VK_S) {
                    bgColor = Color.YELLOW;
                }
                if (keyCode == KeyEvent.VK_D) {
                    bgColor = Color.WHITE;
                }
                if (keyCode == KeyEvent.VK_F) {
                    bgColor = Color.BLACK;
                }
                if (keyCode == KeyEvent.VK_LEFT) {
                    if (gameField.contains(player1.getRectangle())) {
                        player1.move(Direction.LEFT, player1.getSpeed());
                    }

                }
                if (keyCode == KeyEvent.VK_RIGHT) {
                    if (gameField.contains(player1.getRectangle())) {
                        player1.move(Direction.RIGHT, player1.getSpeed());
                    }
                }
                if (keyCode == KeyEvent.VK_UP) {
                    if (gameField.contains(player1.getRectangle())) {
                        player1.move(Direction.UP, player1.getSpeed());
                    }
                }
                if (keyCode == KeyEvent.VK_DOWN) {
                    if (gameField.contains(player1.getRectangle())) {
                        player1.move(Direction.DOWN, player1.getSpeed());
                    }
                }
                if (keyCode == KeyEvent.VK_SPACE) {
                        player1.shoot(Type.BULLET, Direction.UP);
                }
            }
            public void keyReleased(KeyEvent e) {
                handleKey("keyReleased", e);
                System.out.println("KEY was released!!");
            }

        };
        addKeyListener(keyAdapter);
    }*/

    public void keyPressed(KeyEvent e) {
        handleKey("keyPressed", e.getKeyCode());
    }

    public void keyReleased(KeyEvent e) {
        handleKey("keyReleased", e.getKeyCode());
    }

    public void keyTyped(KeyEvent e) {
    }

    private void handleKey(String eventType, int keyCode) {
        if (eventType == "keyPressed") {
            if (!pressedKeys.contains(keyCode)) {
                pressedKeys.add(keyCode);
            }
        } else {
            while (pressedKeys.contains(new Integer(keyCode))) pressedKeys.remove(new Integer(keyCode));
        }
    }

    private void handleKeyEvents() {

        List<Integer> keysToHandle = new ArrayList<Integer>();
        for (int i = 0; i < pressedKeys.size(); i++) {
            keysToHandle.add(pressedKeys.get(i));
        }

        try {
            if (state == STATE.GAME) {
                for (int keyCode : keysToHandle) {

                    if (keyCode == KeyEvent.VK_LEFT) {
                        if (gameField.contains(player1.getRectangle())) {
                            player1.move(Direction.LEFT, player1.getSpeed());
                        }

                    }
                    if (keyCode == KeyEvent.VK_RIGHT) {
                        if (gameField.contains(player1.getRectangle())) {
                            player1.move(Direction.RIGHT, player1.getSpeed());
                        }
                    }
                    if (keyCode == KeyEvent.VK_UP) {
                        if (gameField.contains(player1.getRectangle())) {
                            player1.move(Direction.UP, player1.getSpeed());
                        }
                    }
                    if (keyCode == KeyEvent.VK_DOWN) {
                        if (gameField.contains(player1.getRectangle())) {
                            player1.move(Direction.DOWN, player1.getSpeed());
                        }
                    }
                    if (keyCode == KeyEvent.VK_SPACE) {
                        shootingDelayCounter -= 1;
                        if (shootingDelayCounter <= 0) {
                            player1.shoot(Type.BULLET, Direction.UP);
                            shootingDelayCounter += currentShootingDelay;
                        }
                    }
                    if (keyCode == KeyEvent.VK_P) {
                        state = STATE.MENU;
                    }
                }
            } else if (state == STATE.MENU) {
                for (int keyCode : pressedKeys) {
                    if (keyCode == KeyEvent.VK_P) {
                        System.out.println("start game with P");
                        state = STATE.GAME;
                    }
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("handleKeyEvents error: " + e);
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
        double playerStartPosX = JPWIDTH / 2 - (imgPlayer1.getWidth() / 2);
        double playerStartPosY = JPHEIGHT - imgPlayer1.getHeight();
        player1 = new Player(playerStartPosX, playerStartPosY, false, Type.PLAYER1);
    }

    private void createWave() {
        currentWave = new EnemyWave(1);
    }

    public void run() {
        long startTime, timeSinceStart, sleepTime;

        startTime = System.currentTimeMillis();

        gameRunning = true;
        while (gameRunning) {
            gameRender(); // render image
            paintImage(); // paint image on screen

            if (state == STATE.GAME) {
                gameUpdate(); // update game events
                //gameRender(); // render image
                //paintImage(); // paint image on screen
                // repaint(); replaced by paintImage

                timeSinceStart = System.currentTimeMillis() - startTime;
                sleepTime = PERIOD - timeSinceStart;

                if (sleepTime <= 0) {
                    sleepTime = 5; // a little bit of sleep is always needed
                }
                try {
                    Thread.sleep(sleepTime); // frees the CPU to perform other tasks
                } catch (InterruptedException ex) {
                }
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
            createPlayer1();
            createWave();
            newGame = false;
            resumeGame = true;
        }
        if (!gameOver) {
            // update game state ...
            handleKeyEvents();
            moveBackgroundImages();
            moveProjectiles();
            if (currentWave != null) {
                currentWave.handleWave(System.currentTimeMillis());
            }
        }

        // more methods
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
            drawPlayer();
            drawProjectiles();
        } else if (state == STATE.MENU) {
            drawMenu(dbg);
        }

        if (gameOver) {
            drawGameOver(dbg);
        }
    }

    private void paintImage() {
        Graphics g;
        try {
            g = this.getGraphics();
            if ((g != null) && (dbImage != null)) {
                g.drawImage(dbImage, 0, 0, null);
                g.dispose();
            }
        } catch (Exception e) {
            System.out.println("drawImage graphics error:" + e);
        }
    }

    private void drawPlayer() {
        if (player1 != null) {
            dbg.drawImage(player1.getImage(), (int) player1.getX(), (int) player1.getY(), this);
        }
    }

    public Image getDbImage() {
        return dbImage;
    }

    public static boolean isResumeGame() {
        return resumeGame;
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

    private void drawMenu(Graphics dbg) {
        Graphics2D g = (Graphics2D) dbg;
        if (state == STATE.MENU) {
            menu.render(g);
        }
    }

    private void drawProjectiles() {
        List<Projectile> projectileListCopy = new ArrayList<Projectile>(projectileList);

        for (Projectile p : projectileListCopy) {
            dbg.drawImage(p.getImage(), (int) p.getX(), (int) p.getY(), this);
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

    private void moveProjectiles() {
        List<Integer> outsideProjectiles = new ArrayList<Integer>();
        for (Projectile p : projectileList) {
            p.move(p.getDirection(), p.getSpeed());
            if (!gameField.contains(p.getRectangle())) {
                outsideProjectiles.add(projectileList.indexOf(p));
            }
        }
        removeProjectiles(outsideProjectiles);
    }

    private void removeProjectiles(List<Integer> outsideProjectiles) {
        for (int i = 0; i < outsideProjectiles.size(); i++) {
            try {
                projectileList.remove(i);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("removeProjectiles: " + e);
            }

        }
    }

    private void drawGameOver(Graphics g) {
        // should be replaced by drawImage, using drawString for now
        String msg = "You lost";
        g.drawString(msg, JPWIDTH / 2, JPHEIGHT / 2);
    }

    /* replaced by paintImage method
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (dbImage != null) {
            g.drawImage(dbImage, 0, 0, null);
        }
    } */
}
