package UI;

import Core.*;
import Core.Object;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Timer;

// TODO: add gameover popup

public class GameBoardPanel extends JPanel {
    private Tile[][] board = new Tile[11][13];
    private Integer points = 20;
    private final ArrayList<Tile> objectTiles = new ArrayList<>();
    private final ArrayList<Object> objectTypes = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<JLabel> pointLabels = new ArrayList<>();
    private Player currentPlayer;
    private KeyListener keyListener = new KeyListener();

    public GameBoardPanel(Player playerCharacter) throws FileNotFoundException {
        initialize(playerCharacter);
    }


    private void initialize(Player playerCharacter) throws FileNotFoundException {
        this.setFocusable(true);
        this.addKeyListener(keyListener);
        populateBoard();
        addPointsLabel();
        instantiate(playerCharacter, parseMap());
        instantiateRandomObjects();
        showShortestPath();
    }


    private void instantiate(Player playerCharacter, ArrayList<String> enemyData) {

        // instantiate player
        instantiatePlayerCharacter(playerCharacter);

        // instantiate enemies
        for (int i = 0; i < enemyData.size() - 1; i++)

            instantiateEnemyAtDoor(enemyData.get(i), enemyData.get(i + 1));
    }

    private void instantiatePlayerCharacter(Player playerCharacter) {
        currentPlayer = playerCharacter;
        currentPlayer.setLocation(new Location(5, 6));
        board[5][6].setIcon(getIcon(currentPlayer.getName()));
        board[5][6].setEnabled(true);

    }


    private void instantiateEnemyAtDoor(String enemy, String door) {
        switch (door) {
            case "A":
                board[0][3].setText("");
                board[0][3].setEnabled(true);
                board[0][3].setBackground(Color.white);
                board[0][3].setIcon(getIcon(enemy));
                if (enemy.equals("Gargamel")) {
                    Gargamel gargamel = new Gargamel(board[0][3]);
                    gargamel.setLocation(new Location(0, 3));
                    enemies.add(gargamel);
                } else {
                    Azman azman = new Azman(board[0][3]);
                    azman.setLocation(new Location(0, 3));
                    enemies.add(azman);
                }

                break;
            case "B":
                board[0][10].setText("");
                board[0][10].setEnabled(true);
                board[0][10].setBackground(Color.white);
                board[0][10].setIcon(getIcon(enemy));
                if (enemy.equals("Gargamel")) {
                    Gargamel gargamel = new Gargamel(board[0][10]);
                    gargamel.setLocation(new Location(0, 10));
                    enemies.add(gargamel);
                } else {
                    Azman azman = new Azman(board[0][10]);
                    azman.setLocation(new Location(0, 10));
                    enemies.add(azman);
                }
                break;
            case "C":
                board[5][0].setText("");
                board[5][0].setEnabled(true);
                board[5][0].setBackground(Color.white);
                board[5][0].setIcon(getIcon(enemy));

                if (enemy.equals("Gargamel")) {
                    Gargamel gargamel = new Gargamel(board[5][0]);
                    gargamel.setLocation(new Location(5, 0));
                    enemies.add(gargamel);
                } else {
                    Azman azman = new Azman(board[5][0]);
                    azman.setLocation(new Location(5, 0));
                    enemies.add(azman);
                }
                break;
            case "D":
                board[10][3].setText("");
                board[10][3].setEnabled(true);
                board[10][3].setBackground(Color.white);
                board[10][3].setIcon(getIcon(enemy));

                if (enemy.equals("Gargamel")) {
                    Gargamel gargamel = new Gargamel(board[10][3]);
                    gargamel.setLocation(new Location(10, 3));
                    enemies.add(gargamel);
                } else {
                    Azman azman = new Azman(board[10][3]);
                    azman.setLocation(new Location(10, 3));
                    enemies.add(azman);
                }
                break;
        }


    }

    private void populateBoard() {
        this.setLayout(new GridLayout(12, 13, 0, 0));
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 13; j++) {
                board[i][j] = new Tile();
                board[i][j].setEnabled(false);
                board[i][j].setTileLocation(new Location(i, j));

                this.add(board[i][j]);
            }
        }
    }

    private void addPointsLabel() {
        for (int i = 0; i < 13; i++) {
            JLabel label = new JLabel();
            pointLabels.add(label);
            this.add(label);
        }
        pointLabels.get(0).setText("Points: ");
        pointLabels.get(1).setText(this.points.toString()); // initial points value
    }


    private ArrayList<String> parseMap() throws FileNotFoundException {

        ArrayList<String> enemyData = new ArrayList<>();
        File map = new File("src/Core/harita.txt");
        Scanner mapScanner = new Scanner(map);
        int i = 0;
        int j = 0;
        while (mapScanner.hasNext()) {
            //Karakter:Gargamel,Kapi:A
            String currentWord = mapScanner.next();
            if (currentWord.contains("Karakter") || currentWord.contains("karakter")) {
                // get the enemy's character :
                if (currentWord.contains("Gargamel") || currentWord.contains("gargamel")) {
                    enemyData.add("Gargamel");
                } else {
                    enemyData.add("Azman");
                }

                // get the enemy's door :using "api" to ignore Kapi capitalaization
                String[] doorToken = currentWord.split("api:");
                String door = doorToken[1];
                enemyData.add(door);
            }

            // if the current line doesn't contain "Karakter", then it should be "1" or "0" , 0 indicates for wall.

            if (currentWord.equals("0")) {
                board[i][j].setBackground(Color.gray);
                board[i][j].setIsWall(true);
                if (j < 13) {
                    j++;
                }

                if (j == 13) {
                    j = 0;
                    i++;
                }
            }
            if (currentWord.equals("1")) {
                board[i][j].setBackground(Color.white);
                board[i][j].setIsWall(false);
                if (j < 13) {
                    j++;
                }

                if (j == 13) {
                    j = 0;
                    i++;
                }
            }

        }

        board[0][3].setText("A");
        board[0][3].setBackground(Color.DARK_GRAY);
        board[0][3].setIsWall(true);
        board[0][10].setText("B");
        board[0][10].setBackground(Color.DARK_GRAY);
        board[0][10].setIsWall(true);
        board[5][0].setText("C");
        board[5][0].setBackground(Color.DARK_GRAY);
        board[5][0].setIsWall(true);
        board[10][3].setText("D");
        board[10][3].setBackground(Color.DARK_GRAY);
        board[10][3].setIsWall(true);

        return enemyData;
    }

    private ImageIcon getIcon(String iconName) {

        return new ImageIcon(new ImageIcon("src/Images/" + iconName + ".png").getImage());
    }


    private void instantiateRandomObjects() {
        showRandomGold();
        showRandomMushroom();
        StartInitialGoldTimer();
        StartInitialMushroomTimer();
    }

    private void showRandomGold() {
        Random random = new Random();
        int x;
        int y;
        int goldCount = 5;
        int drawnGold = 0;
        Tile playerTile = board[5][6];
        while (drawnGold < goldCount) {
            x = random.nextInt(11);
            y = random.nextInt(13);

            if (board[x][y].isNotWall() && !objectTiles.contains(board[x][y]) && board[x][y] != playerTile) {
                board[x][y].setHasGold(true);
                board[x][y].setIcon(getIcon("gold"));
                board[x][y].setEnabled(true);
                objectTiles.add(board[x][y]);
                objectTypes.add(new Gold());
                drawnGold++;
            }
        }
        rePaint();
    }

    private void showRandomMushroom() {
        Random random = new Random();
        int x;
        int y;
        int MushroomCount = random.nextInt(20);
        int drawnMushroom = 0;
        Tile playerTile = board[5][6];
        while (MushroomCount == 0) {  // assuring that mushroom count will never == 0
            MushroomCount = random.nextInt(20);
        }
        while (drawnMushroom < MushroomCount) {
            x = random.nextInt(11);
            y = random.nextInt(13);

            if (board[x][y].isNotWall() && !objectTiles.contains(board[x][y]) && board[x][y] != playerTile) {
                board[x][y].setHasMushroom(true);
                board[x][y].setIcon(getIcon("mushroom"));
                board[x][y].setEnabled(true);
                objectTiles.add(board[x][y]);
                objectTypes.add(new Mushroom());
                drawnMushroom++;
            }
        }
        rePaint();
    }

    private void repeatRandomObjects() {
        Random random = new Random();
        int randomInt = random.nextInt(2); // helper random number to show gold or mushroom.
        switch (randomInt) {
            case 1: // will show random gold
                int goldDelay = random.nextInt(10); // after x seconds (0-10)
                while (goldDelay == 0) {
                    goldDelay = random.nextInt(10);
                }
                TimerTask showGold = new TimerTask() {

                    @Override
                    public void run() {
                        showRandomGold();
                        StartRepeatedGoldTimer(); // when gold timer finish executing repeatRandomObjects() will be recalled  withing gold timer task for infinite loop
                    }
                };

                Timer goldTimer = new Timer();
                goldTimer.schedule(showGold, goldDelay * 1000);
                break;
            case 0: // shows random mushroom
                int mushroomDelay = random.nextInt(20); // after x seconds (1-20)
                while (mushroomDelay == 0) {
                    mushroomDelay = random.nextInt(20);
                }

                TimerTask showMushroom = new TimerTask() {

                    @Override
                    public void run() {
                        showRandomMushroom();
                        StartRepeatedMushroomTimer(); // when mushroom timer finish executing repeatRandomObjects() will be recalled  withing gold timer task for infinite loop
                    }
                };

                Timer mushroomTimer = new Timer();
                mushroomTimer.schedule(showMushroom, mushroomDelay * 1000);
        }
    }


    private void StartInitialGoldTimer() { // this method will only be called once (when the game starts)
        TimerTask hideGold = new TimerTask() {

            @Override
            public void run() {
                hideGold();
            }
        };

        Timer timer = new Timer();
        timer.schedule(hideGold, 5000);
    }

    private void StartInitialMushroomTimer() { // this method will only be called once (when the game starts)
        TimerTask hideGold = new TimerTask() {

            @Override
            public void run() {
                hideMushroom();
                repeatRandomObjects(); // but it will trigger repeatRandomObjects()
            }
        };

        Timer timer = new Timer();
        timer.schedule(hideGold, 7000);
    }

    private void StartRepeatedGoldTimer() { //  this method is called whenever repeatRandomObjects() is called and when the timer task finish
        TimerTask hideGold = new TimerTask() { //  repeatRandomObjects() will be called again to insure the infinite loop

            @Override
            public void run() {
                hideGold();
                repeatRandomObjects(); // the infinite loop happens here
            }
        };

        Timer timer = new Timer();
        timer.schedule(hideGold, 5000);
    }

    private void StartRepeatedMushroomTimer() { //  this method is called whenever repeatRandomObjects() is called and when the timer task finish
        TimerTask hideMushroom = new TimerTask() {//  repeatRandomObjects() will be called again to insure the infinite loop

            @Override
            public void run() {
                hideMushroom();
                repeatRandomObjects(); // the infinite loop happens here
            }
        };

        Timer timer = new Timer();
        timer.schedule(hideMushroom, 7000);
    }

    private void hideGold() {
        for (Tile objectTile : objectTiles) {
            if (objectTile.HasGold()) {
                objectTile.setHasGold(false);
                objectTile.setEnabled(false);
                objectTile.setIcon(null);
            }
        }
        rePaint();
    }

    private void hideMushroom() {
        for (Tile objectTile : objectTiles) {
            if (objectTile.HasMushroom()) {
                objectTile.setHasMushroom(false);
                objectTile.setEnabled(false);
                objectTile.setIcon(null);
            }
        }
        rePaint();
    }

    // when hiding objects, player or enemy icon will be hidden if the player or the enemy is standing at an object tile , this method will be called in such
    // cases to repaint player and enemy's  icon.
    private void rePaint() {
        int x = currentPlayer.getLocation().getX();
        int y = currentPlayer.getLocation().getY();
        board[x][y].setIcon(getIcon(currentPlayer.getName()));
        board[x][y].setEnabled(true);
        for (Enemy enemy : enemies) {
            int enemyX = enemy.getLocation().getX();
            int enemyY = enemy.getLocation().getY();
            Tile enemyTile = board[enemyX][enemyY];
            enemyTile.setIcon(getIcon(enemy.getName()));
            enemyTile.setEnabled(true);
        }
    }
    private void takeGoldOrMushroom(Tile nextTile){
        if(nextTile.HasGold())
            nextTile.setHasGold(false);
        if(nextTile.HasMushroom())
            nextTile.setHasMushroom(false);
    }

    private void movePlayerUp() {
        int steps = currentPlayer.getSteps();
        int movedSteps = 0;
        Tile nextTile, currentTile;
//        int x = currentPlayer.getLocation().getX();
//        int y = currentPlayer.getLocation().getY();
        //        while (movedSteps < steps && !board[x - steps][y].isWall()) {
        while (movedSteps < steps) {
            int x = currentPlayer.getLocation().getX();
            int y = currentPlayer.getLocation().getY();
            currentTile = board[x][y];
            nextTile = board[x - 1][y];
            if (nextTile.isNotWall()) {
                currentTile.setIcon(null);
                currentTile.setEnabled(false);
                nextTile.setIcon(getIcon(currentPlayer.getName()));
                nextTile.setEnabled(true);
                currentPlayer.getLocation().setX(x - 1);
                takeGoldOrMushroom(nextTile); // gold or mushroom will not exist after the player takes them
                movedSteps++;


            } else
                break;
        }
    }

    private void movePlayerDown() {
        int steps = currentPlayer.getSteps();
        int movedSteps = 0;
        Tile nextTile, currentTile;
//        int x = currentPlayer.getLocation().getX();
//        int y = currentPlayer.getLocation().getY();
        //        while (movedSteps < steps && !board[x + steps][y].isWall()) {
        while (movedSteps < steps) {
            int x = currentPlayer.getLocation().getX();
            int y = currentPlayer.getLocation().getY();
            currentTile = board[x][y];
            nextTile = board[x + 1][y];
            if (nextTile.isNotWall()) {
                currentTile.setIcon(null);
                currentTile.setEnabled(false);
                nextTile.setIcon(getIcon(currentPlayer.getName()));
                nextTile.setEnabled(true);
                currentPlayer.getLocation().setX(x + 1);
                takeGoldOrMushroom(nextTile); // gold or mushroom will not exist after the player takes them
                movedSteps++;
            } else
                break;

        }
    }

    private void movePlayerRight() {
        int steps = currentPlayer.getSteps();
        int movedSteps = 0;
        Tile nextTile, currentTile;
//        int x = currentPlayer.getLocation().getX();
//        int y = currentPlayer.getLocation().getY();
        //        while (movedSteps < steps && !board[x][y + steps].isWall()) {
        while (movedSteps < steps) {
            int x = currentPlayer.getLocation().getX();
            int y = currentPlayer.getLocation().getY();
            currentTile = board[x][y];
            nextTile = board[x][y + 1];
            if (nextTile.isNotWall()) {
                currentTile.setIcon(null);
                currentTile.setEnabled(false);
                nextTile.setIcon(getIcon(currentPlayer.getName()));
                nextTile.setEnabled(true);
                currentPlayer.getLocation().setY(y + 1);
                takeGoldOrMushroom(nextTile); // gold or mushroom will not exist after the player takes them
                movedSteps++;

            } else
                break;
        }
    }

    private void movePlayerLeft() {
        int steps = currentPlayer.getSteps();
        int movedSteps = 0;
        Tile nextTile, currentTile;
//        int x = currentPlayer.getLocation().getX();
//        int y = currentPlayer.getLocation().getY();
        //         while (movedSteps < steps && !board[x][y - steps].isWall()) {
        while (movedSteps < steps) {
            int x = currentPlayer.getLocation().getX();
            int y = currentPlayer.getLocation().getY();
            currentTile = board[x][y];
            nextTile = board[x][y - 1];
            if (nextTile.isNotWall()) {
                currentTile.setIcon(null);
                currentTile.setEnabled(false);
                nextTile.setIcon(getIcon(currentPlayer.getName()));
                nextTile.setEnabled(true);
                currentPlayer.getLocation().setY(y - 1);
                takeGoldOrMushroom(nextTile); // gold or mushroom will not exist after the player takes them
                movedSteps++;

            } else
                break;
        }
    }

    private void showShortestPath() {
        for (Enemy enemy : enemies) {
            enemy.setShortestPath(this, currentPlayer.getLocation());
        }
    }

    private void moveEnemy(Enemy enemy) {
        int steps = enemy.getSteps();

        int movedSteps = 0;
        if (checkForEnemyCatch(enemy)) {
            return;
        }

        enemy.setShortestPath(this, currentPlayer.getLocation());

        while (movedSteps < steps) {
            // check all the possible movements :
            Tile currentTile = this.getTile(enemy.getLocation());

            int last = enemy.getShortestPath().size() - 1;

            Tile destination = enemy.getShortestPath().get(last);
            enemy.getShortestPath().remove(last);
            enemy.setLocation(destination.getTileLocation());

            movedSteps++;

            currentTile.setEnabled(false);
            currentTile.setIcon(null);
            currentTile.setBackground(Color.white);
            destination.setIcon(getIcon(enemy.getName()));
            destination.setEnabled(true);
            if (checkForEnemyCatch(enemy)) {
                return;
            }
        }
    }

    private void movePlayer(String direction) {
        switch (direction) {
            case "up":
                movePlayerUp();
                break;
            case "down":
                movePlayerDown();
                break;
            case "left":
                movePlayerLeft();
                break;
            case "right":
                movePlayerRight();
        }
    }

    private void cleanupTiles() {
        for (Tile[] row : board) {
            for (Tile tile : row) {
                if (tile.getPaths() > 0) {
                    tile.setBackground(Color.white);
                    tile.setPaths(0);
                }
            }
        }
    }

    private void gameLoop(String direction) {
        cleanupTiles();
        movePlayer(direction);
        for (Enemy enemy : enemies) {
            moveEnemy(enemy);
        }
        updatePoints();
        checkPoints();
    }

    private void checkPoints() {
        if (points <= 0) {
            // TODO: end game with game over popup
            //  And remove keylisteners
        }
    }

    private void updatePoints() {
        didCatchObject(); // checks if the player is standing at an object's tile.
    }

    private void didCatchObject() {
        int x = currentPlayer.getLocation().getX();
        int y = currentPlayer.getLocation().getY();
        Tile currentTile = board[x][y];

        if (currentTile.HasGold() || currentTile.HasMushroom()) {
            // when new object is added the object's location is stored at objectTiles array , and it's type is stored at ObjectTypes array synchronously.
            // so both arrayLists  will have the same index for each object.
            int tileIndex = objectTiles.indexOf(currentTile);
            int addedPoints = objectTypes.get(tileIndex).getPoints();
            points = points + addedPoints;
            pointLabels.get(1).setText(points.toString()); // updating points on the GUI
        }
    }

    private boolean checkForEnemyCatch(Enemy enemy) {
        int x = currentPlayer.getLocation().getX();
        int y = currentPlayer.getLocation().getY();
        Tile playerTile = board[x][y];
        int enemyX = enemy.getLocation().getX();
        int enemyY = enemy.getLocation().getY();
        Tile enemyTile = board[enemyX][enemyY];
        if (playerTile == enemyTile) {
            int hitPoints = enemy.getHitPoints();
            points = points - hitPoints;
            pointLabels.get(1).setText(Integer.toString(points)); // updating points on the GUI
            reInstantiate(enemy); // re instantiate enemy and player's location if enemy catches a player
            return true;
        }
        return false;
    }

    private void reInstantiate(Enemy enemy) {
        reInstantiateEnemyAtDoor(enemy);


        int x = currentPlayer.getLocation().getX();
        int y = currentPlayer.getLocation().getY();
        Tile currentPlayerTile = board[x][y];
        currentPlayerTile.setBackground(Color.white);
        currentPlayerTile.setIcon(getIcon(currentPlayer.getName()));
        currentPlayerTile.setEnabled(true);
        enemy.setShortestPath(this, currentPlayer.getLocation());

    }

    private void reInstantiateEnemyAtDoor(Enemy enemy) {

        Tile tile = enemy.getDoor();
        tile.setEnabled(true);
        tile.setBackground(Color.white);
        tile.setIcon(getIcon(enemy.getName()));
        enemy.setLocation(tile.getTileLocation());
    }

    private void checkGameLoop(String direction) {
        boolean run = false;
        switch (direction) {
            case "up":
                Tile upperTile = board[currentPlayer.getLocation().getX() - 1][currentPlayer.getLocation().getY()];
                run = upperTile.isNotWall();
                break;

            case "down":
                Tile lowerTile = board[currentPlayer.getLocation().getX() + 1][currentPlayer.getLocation().getY()];
                run = lowerTile.isNotWall();
                break;
            case "left":
                Tile leftTile = board[currentPlayer.getLocation().getX()][currentPlayer.getLocation().getY() - 1];
                run = leftTile.isNotWall();
                break;
            case "right":
                Tile rightTile = board[currentPlayer.getLocation().getX()][currentPlayer.getLocation().getY() + 1];
                run = rightTile.isNotWall();
        }
        if (run) {
            gameLoop(direction);
        }
    }

    public Tile[][] getBoard() {
        return board;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    public Tile getTile(Location location) {
        return board[location.getX()][location.getY()];
    }

    public ArrayList<Tile> getNeighbours(Tile tile) {
        Location[] neighborsLocations = {
                new Location(0, 1),
                new Location(0, -1),
                new Location(-1, 0),
                new Location(1, 0)
        };
        ArrayList<Tile> neighborTiles = new ArrayList<>();
        for (Location location : neighborsLocations) {
            try {
                Tile neighborTile = getTile(tile.getTileLocation().plus(location));
                if (neighborTile.isNotWall()) {
                    neighborTiles.add(neighborTile);
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
        return neighborTiles;
    }


    public class KeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    checkGameLoop("left");
                    break;
                case KeyEvent.VK_RIGHT:
                    checkGameLoop("right");
                    break;

                case KeyEvent.VK_UP:
                    checkGameLoop("up");
                    break;
                case KeyEvent.VK_DOWN:
                    checkGameLoop("down");
                    break;

            }
        }

    }

}
