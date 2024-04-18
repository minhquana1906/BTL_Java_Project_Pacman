package Model;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class Game extends JPanel implements ActionListener {
    //main model
    private Board board;
    private Pacman pacman;
    private Ghost ghosts;

    //game components
    private int lives;
    private int score;
    private int levelSpeed;
    private final int MAX_SPEED = 6;
    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};

    //game control inputs
    private int req_dx, req_dy;

    //game status
    private boolean isDeath;
    private boolean inGame = false;

    private final int MAX_GHOSTS = 12;
    private int N_GHOSTS;

    private int[] dx,dy;

    //Define components's images
    private Image pacmanLeft, pacmanRight, pacmanDown, pacmanUp;
    private Image heart;
    private Image ghost;

    private short[] screenData;
    private Timer timer;
    private Dimension d;

    //Define board's map
    private final short levelData[] = {
            19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            25, 24, 24, 24, 28, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
            0,  0,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
            19, 18, 18, 18, 18, 18, 16, 16, 16, 16, 24, 24, 24, 24, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
            17, 16, 16, 16, 24, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 18, 18, 18, 18, 20,
            17, 24, 24, 28, 0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 20,
            21, 0,  0,  0,  0,  0,  0,   0, 17, 16, 16, 16, 16, 16, 20,
            17, 18, 18, 22, 0, 19, 18, 18, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            25, 24, 24, 24, 26, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };
    /*
    225 numbers, 15rows x 15columns
    0 - blue
    1 - left border
    2 - top border
    4 - right border
    8 - bottom border
    16 - white dots
     */

    public int getReq_dx() {
        return req_dx;
    }

    public int getReq_dy() {
        return req_dy;
    }

    public void setReq_dx(int req_dx) {
        this.req_dx = req_dx;
    }

    public void setReq_dy(int req_dy) {
        this.req_dy = req_dy;
    }

    public Timer getTimer() {
        return timer;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public Game() {
        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();
        requestFocusInWindow();
    }

    void loadImages(){
        pacmanLeft = new ImageIcon(getClass().getResource("/resource_test/pacman/left.gif")).getImage();
        pacmanRight = new ImageIcon(getClass().getResource("/resource_test/pacman/right.gif")).getImage();
        pacmanDown = new ImageIcon(getClass().getResource("/resource_test/pacman/down.gif")).getImage();
        pacmanUp = new ImageIcon(getClass().getResource("/resource_test/pacman/up.gif")).getImage();

        ghost = new ImageIcon(getClass().getResource("/resource_test/pacman/ghost.gif")).getImage();
        heart = new ImageIcon(getClass().getResource("/resource_test/pacman/heart.png")).getImage();
    }

    //TODO Init screen, board, ghosts, pacman
    private void initVariables(){
        board = new Board();
        pacman = new Pacman(0,0,0,0,6);
        ghosts = new Ghost(0,0,0,0,6);
        
        score = 0;
        levelSpeed = 3;
        isDeath = false;
        inGame = false;
        d = new Dimension(400,400);

        screenData = new short[board.getN_BLOCKS() * board.getN_BLOCKS()];
//        board.setDimension(new Dimension(board.getSCREEN_SIZE(), board.getSCREEN_SIZE()));


        dx = new int[4];
        dy = new int[4];
        // Set speed by timer:
        timer = new Timer(40, this);
        timer.restart();
    }

    //TODO Init game
    public void initGame(){
        lives = 3;
        score = 0;
        N_GHOSTS = 2;
        levelSpeed = 3;
        initLevel();
    }

    //Init level
    private void initLevel(){
        for(int i=0;i<board.getN_BLOCKS() * board.getN_BLOCKS();i++){
            screenData[i] = levelData[i];
        }
        continueLevel();
    }

    //TODO playgame & render
    private void playGame(Graphics2D g2d){
        if(isDeath){
            death();
        }
        else{
            movePacman();
            drawPacman(g2d);
            moveGhosts(g2d);
            checkMaze();
        }
    }

    //TODO show intro screen
    private void showIntroScreen(Graphics2D g2d){
        g2d.setFont(new Font("Crackman", Font.BOLD, 14));
        String start = "Press SPACE button to start";
        g2d.setColor(new Color(254,192,1));
        g2d.drawString(start, (board.getSCREEN_SIZE() - g2d.getFontMetrics().stringWidth(start)) / 2, board.getSCREEN_SIZE() / 2);
    }

    //TODO Draw score
    private void drawScore(Graphics2D g2d){
        g2d.setColor(Color.white);
        g2d.setFont(new Font("Crackman", Font.BOLD, 14));
        String s = "Score: " + score;
        g2d.drawString(s, board.getSCREEN_SIZE() / 2 + 96, board.getSCREEN_SIZE() + 16);

        // check lives cua pac man
        for(int i=0;i<lives;i++){
            g2d.drawImage(heart, i * 28 + 8, board.getSCREEN_SIZE() + 1, this);
        }
    }

    //Death
    private void death(){
        lives--;
        if(lives == 0){
            inGame = false;
        }
        continueLevel();
    }

    private void continueLevel(){
        int dx = 1;
        int random;
        for(int i=0;i<N_GHOSTS;i++){
            //init ghost's position
            ghosts.setX(4 * board.getBLOCK_SIZE());
            ghosts.setY(4 * board.getBLOCK_SIZE());
            ghosts.setDx(dx);
            ghosts.setDy(0);
            dx = - dx;

            random = (int) (Math.random() * (levelSpeed + 1));
            if(random > levelSpeed){
                random = levelSpeed;
            }
            ghosts.setSpeed(validSpeeds[random]);

        }
        //init pacman position
        pacman.setX(7 * board.getBLOCK_SIZE());
        pacman.setY(11 * board.getBLOCK_SIZE());

        //pacman direction
        pacman.setX(0);
        pacman.setY(0);
        req_dx = req_dy = 0;
        isDeath = false;
    }

    //TODO Move pacman
    private void movePacman(){
        int pos;
        short ch;

        //TODO xem lai cong thuc tinh
        if(pacman.getX() % board.getBLOCK_SIZE() == 0 && pacman.getY() % board.getBLOCK_SIZE() == 0){
            pos = pacman.getX() / board.getBLOCK_SIZE() + board.getN_BLOCKS() * (short) (pacman.getY() / board.getBLOCK_SIZE());
            ch = screenData[pos];

            /*
                0 - blue
                1 - left border
                2 - top border
                4 - right border
                8 - bottom border
                16 - white dots
             */
            //TODO Check if pacman can move to the next position
            //eat white dots
            if((ch & 16) != 0){
                screenData[pos] = (short) (ch & 15);
                score++;
            }

            // check if pacman hit the wall (if not stuck then do)
            if(req_dx != 0 && req_dy != 0){
                if(!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))){
                    pacman.setX(req_dx);
                    pacman.setY(req_dy);
                }
            }
            // Stuck (hit the wall)
//            if((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
//                    || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
//                    || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
//                    || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0)){
//                pacman.setX(0);
//                pacman.setY(0);
//            }
            if((pacman.getX() == -1 && pacman.getY() == 0 && (ch & 1) != 0)
                    || (pacman.getX() == 1 && pacman.getY() == 0 && (ch & 4) != 0)
                    || (pacman.getX() == 0 && pacman.getY() == -1 && (ch & 2) != 0)
                    || (pacman.getX() == 0 && pacman.getY() == 1 && (ch & 8) != 0)) {
                pacman.setX(0);
                pacman.setY(0);
            }
        }
        pacman.setX(pacman.getX() + pacman.getX() * pacman.getSpeed());
        pacman.setY(pacman.getY() + pacman.getY() * pacman.getSpeed());
    }

    //TODO Draw pacman
    private void drawPacman(Graphics2D g2d){
        if(req_dx == -1){
            g2d.drawImage(pacmanLeft, pacman.getX() + 1, pacman.getY() + 1, this);
        } else if(req_dx == 1){
            g2d.drawImage(pacmanRight, pacman.getX() + 1, pacman.getY() + 1, this);
        } else if (req_dy == -1) {
            g2d.drawImage(pacmanUp, pacman.getX() + 1, pacman.getY() + 1, this);
        } else{
            g2d.drawImage(pacmanDown, pacman.getX() + 1, pacman.getY() + 1, this);
        }
    }

    //TODO Move ghosts
    private void moveGhosts(Graphics2D g2d){
        int pos;
        int count;
        for(int i=0;i<N_GHOSTS;i++){
            //TODO xem lai cong thuc tinh
            if(ghosts.getX() % board.getBLOCK_SIZE() == 0 && ghosts.getY() % board.getBLOCK_SIZE() == 0){
                pos = ghosts.getX() / board.getBLOCK_SIZE() + board.getN_BLOCKS() * (int) (ghosts.getY() / board.getBLOCK_SIZE());
                //kiem tra lieu ghost co dang o center cua 1 block hay khong
                count = 0;

                //neu goc la 1(goc trai) & huong di cua ghosts khong phai la sang phai
                if((screenData[pos] & 1) == 0 && ghosts.getDx() != 1){
                    dx[count] = -1; //di chuyen sang trai
                    dy[count] = 0;
                    count++;    //tang count: so huong di co the di chuyen
                }
                //neu goc la 2(goc tren) & huong di cua ghosts khong phai la di xuong
                if((screenData[pos] & 2) == 0 && ghosts.getDy() != 1){
                    dx[count] = 0;
                    dy[count] = -1;     //di len
                    count++;
                }
                // neu goc la 4(goc phai) & huong di cua ghosts khong phai la sang trai
                if((screenData[pos] & 4) == 0 && ghosts.getDx() != -1){
                    dx[count] = 1;  //di chuyen sang phai
                    dy[count] = 0;
                    count++;
                }
                // neu goc la 8(goc duoi) & huong di cua ghosts khong phai la di len
                if((screenData[pos] & 8) == 0 && ghosts.getDy() != -1){
                    dx[count] = 0;
                    dy[count] = 1;   //di xuong
                    count++;
                }

                if(count == 0){
                    // ko co huong di nao (bi ket 4 goc)
                    if((screenData[pos] & 15) == 15){   //(1+2+4+8)
                        ghosts.setDx(0);   //dung lai
                        ghosts.setDy(0);
                    }
                    //cac TH ket 1,2,3 goc => di chuyen nguoc lai
                    else{
                        ghosts.setDx(-ghosts.getDx());    //di nguoc lai
                        ghosts.setDy(-ghosts.getDy());
                    }
                } else{
                    count = (int) (Math.random() * count);  //chon 1 trong cac huong di co the di chuyen

                    //chi co 4 huong di trong dx dy, neu count vuot qua 3 thi chon huong di cuoi cung
                    if(count > 3){
                        count = 3;  //chon huong di cuoi cung
                    }

                    ghosts.setDx(dx[count]);   //set huong di (dx, dy chua cac huong di co the di chuyen)
                    ghosts.setDy(dy[count]);
                }
            }
            ghosts.setX(ghosts.getX() + (ghosts.getDx() * ghosts.getSpeed()));  //
            ghosts.setY(ghosts.getY() + (ghosts.getDy() * ghosts.getSpeed()));  //
            drawGhost(g2d, ghosts.getX() + 1, ghosts.getY() + 1);     //ve ghost o vi tri + 1 theo huong dc chon


            // check pacman cham vao ghost(24x24 pixels) 12 pixels cho 1 nua
            if(pacman.getX() > (ghosts.getX() - 12) && pacman.getX() < (ghosts.getX() + 12)
            && pacman.getY() > (ghosts.getY() - 12) && pacman.getY() < (ghosts.getY() + 12)
            && inGame){
                isDeath = true;
            }
        }
    }

    //TODO Draw ghost
    private void drawGhost(Graphics2D g2d, int x, int y){
        g2d.drawImage(ghost, x, y, this);
    }

    //TODO Check maze
    private void checkMaze(){
        short i=0;
        boolean finished = true;

        // duyet vong lap while t o dau tien den o cuoi cung neu so bit voi 48 != 0 thi la chua an het white dots
        while(i < board.getN_BLOCKS() * board.getN_BLOCKS() && finished){
            if((screenData[i]) != 0){  //48 = 16 + 32 (white dots + blue)
                finished = false;
            }
            i++;    //check tiep den o tiep theo
        }

        //chuyen sang next level
        if(finished){
            score += 50;
            if(N_GHOSTS < MAX_GHOSTS){
                N_GHOSTS++;
            }
            if(levelSpeed < MAX_SPEED){
                levelSpeed++;
            }
            initLevel();
        }
    }

    // TODO Draw maze
    private void drawMaze(Graphics2D g2d){
        int i =0;
        int x, y;

        for(y =0; y < board.getSCREEN_SIZE(); y+= board.getBLOCK_SIZE()){
            for(x=0; x < board.getSCREEN_SIZE(); x+=board.getBLOCK_SIZE()){

                g2d.setColor(new Color(16,39,184));
                g2d.setStroke(new BasicStroke(5));      //set do day cua duong vien

                //paint blue block
                if(levelData[i] == 0){
                    g2d.fillRect(x,y,board.getBLOCK_SIZE(), board.getBLOCK_SIZE());
                }

                //paint left border
                if((screenData[i] & 1) != 0){
                    g2d.drawLine(x, y, x, y + board.getBLOCK_SIZE() - 1);
                }

                //paint top border
                if((screenData[i] & 2) != 0){
                    g2d.drawLine(x, y, x + board.getBLOCK_SIZE() - 1, y);
                }

                //paint right border
                if((screenData[i] & 4) != 0){
                    g2d.drawLine(x + board.getBLOCK_SIZE() - 1, y, x + board.getBLOCK_SIZE() - 1, y + board.getBLOCK_SIZE() - 1);
                }

                //paint bottom border
                if((screenData[i] & 8) != 0){
                    g2d.drawLine(x,y + board.getBLOCK_SIZE() - 1,x + board.getBLOCK_SIZE() - 1, y + board.getBLOCK_SIZE() - 1);
                }

                //paint white dots
                if((screenData[i] & 16) != 0){
                    g2d.setColor(Color.white);
                    g2d.fillOval(x + 10, y + 10, 6, 6);
                }
                i++;
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        g2d.fillRect(0,0,d.width,d.height);

        drawMaze(g2d);
        drawScore(g2d);

        if(inGame){
            playGame(g2d);
        }
        else{
            showIntroScreen(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                }
            } else {
                if (key == KeyEvent.VK_SPACE) {
                    inGame = true;
                    initGame();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
