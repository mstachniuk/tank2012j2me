package com.blogspot.mstachniuk.j2me.tank2012;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;

/**
 * Implementacja klasy odpowiedzialnej za logike gry, obsluge klawiszy i
 * malowania po ekranie.
 *
 * @author Marcin Stachniuk http://mstachniuk.blogspot.com
 */
public class TankGameCanvas extends AbstractTankCanvas {

    private boolean endGame = false;    // flaga czy gra sie skonczyla czy nie
    private int width;                  // szerokosc ekranu
    private int height;                 // wysokosc ekranu
    private Sprite tank;		// Sprite reprezentujący czołg
    private final static int STEP = 10;
    private TiledLayer grass;
    private int[][] grassTab = new int[][]{
        {0, 0, 0, 0, 5, 0, 0, 5},
        {0, 0, 0, 0, 0, 0, 0, 5},
        {5, 0, 0, 0, 0, 0, 0, 0},
        {5, 0, 0, 5, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 5},
        {5, 0, 0, 0, 0, 0, 0, 0},
        {0, 5, 5, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {5, 0, 0, 0, 0, 0, 0, 5},
        {5, 0, 0, 0, 0, 0, 0, 5},
        {5, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 5, 5, 0, 0, 0}
    };
    private int[][] collisionTab = new int[][]{
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 4},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {4, 0, 0, 4, 4, 4, 0, 0},
        {0, 0, -1, -1, 0, 0, 0, 0},
        {-1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, -1, -1, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, -1, -1, 0, 0, 0, 0, 0},
        {0, 4, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0}
    };
    private int[][] wallTab = new int[][]{
        {0, 0, 1, 0, 0, 0, 1, 0},
        {0, 0, 1, 1, 0, 1, 1, 0},
        {0, 0, 1, 0, 1, 0, 1, 0},
        {0, 0, 1, 0, 0, 0, 1, 0},
        {0, 0, 1, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 1, 1, 1, 0},
        {0, 0, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 1, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 0},
        {0, 0, 1, 1, 1, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0}
    };
    private TiledLayer colisionTiles;
    private int animatedTile;
    private int[] waterSequence = new int[]{2, 3};
    private int actualWaterIndex = 0;
    private Sprite[] walls;
    private Sprite missile;
    private static final int TANK_UP = 0;
    private static final int TANK_RIGHT = 1;
    private static final int TANK_DOWN = 2;
    private static final int TANK_LEFT = 3;
    private int missileDirection = 0;
    private static final int MISSILE_STEP = 12;

    /**
     * Konstruktor wykonywany podczas tworzenia obiektu TankGameCanvas
     */
    public TankGameCanvas() {
        // pusty konstruktor
    }

    /**
     * Przygotowanie aplikacji
     */
    public void init() {
        setFullScreenMode(true);        // pelen ekran MIDP 2.0
        width = getWidth();             // pobranie szerokosci ekranu
        height = getHeight();           // pobranie wysokosci ekranu

        Image tankImage = createImage("tank.png");  // wczytanie obrazka czolgu

        tank = new Sprite(tankImage, 30, 30);       // utorzenie Sprite z obrazka czolgu
        tank.setPosition(30, 30);                   // ustawienie pozycji poczatkowej Sprite czolgu

        Image backgroundImage = createImage("tiles.png");
        grass = new TiledLayer(8, 12, backgroundImage, 30, 30);
        fillTilesLayerFromTab(grass, grassTab);

        colisionTiles = new TiledLayer(8, 12, backgroundImage, 30, 30);
        animatedTile = colisionTiles.createAnimatedTile(0);
        colisionTiles.setAnimatedTile(animatedTile, 2);
        fillTilesLayerFromTab(colisionTiles, collisionTab);

        int numberOfWals = calculateNumberOfInTab(1, wallTab);
        walls = new Sprite[numberOfWals];
        for (int i = 0; i < walls.length; i++) {
            walls[i] = new Sprite(backgroundImage, 30, 30);
        }
        for (int i = 0, k = 0; i < wallTab.length; i++) {
            for (int j = 0; j < wallTab[i].length; j++) {
                if (wallTab[i][j] == 1) {
                    walls[k].setPosition(j * 30, i * 30);
                    k++;
                }
            }
        }

        Image missileImage = createImage("missile.png");
        missile = new Sprite(missileImage);
        missile.setVisible(false);
        missile.setRefPixelPosition(missile.getWidth() / 2, missile.getHeight() / 2);
    }

    private void fillTilesLayerFromTab(TiledLayer tiles, int[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[i].length; j++) {
                tiles.setCell(j, i, tab[i][j]);
            }
        }
    }

    /**
     * Metoda odpowiedzialna za malowanie po ekranie
     */
    public void paint() {
        Graphics g = getGraphics();			// pobranie buffora graficznego
        // -------- od tego miejsca umieszczamy malowanie grafiki --------------

        g.setColor(0, 0, 0);                            // ustawienie aktualnego koloru na czarny
        g.fillRect(0, 0, width, height);                // wypełnianie tla na aktualny kolor

        colisionTiles.paint(g);

        for (int i = 0; i < walls.length; i++) {
            walls[i].paint(g);
        }

        tank.paint(g);					// malowanie czolgu

        missile.paint(g);
        
        grass.paint(g);

        // -------- do tego miejsca umieszczamy malowanie grafiki --------------
        flushGraphics();				// podmienienie bufforow
    }

    /**
     * Obsluga naciskanych klawiszy.
     */
    public void inputKeys() {
        // zapamietanie starej pozycji czolgu
        int oldX = tank.getX();
        int oldY = tank.getY();
        boolean backToOldPosition = false;

        // pobranie stanu wcisnietych klawiszy
        int keyStates = getKeyStates();

        // naciśnięto w lewo
        if ((keyStates & LEFT_PRESSED) != 0) {
            tank.move(-STEP, 0);
            tank.setFrame(TANK_LEFT);
        }

        // naciśnięto w prawo
        if ((keyStates & RIGHT_PRESSED) != 0) {
            tank.move(STEP, 0);
            tank.setFrame(TANK_RIGHT);
        }

        // naciśniętow w gorę
        if ((keyStates & UP_PRESSED) != 0) {
            tank.move(0, -STEP);
            tank.setFrame(TANK_UP);
        }

        // naciśnięto w doł
        if ((keyStates & DOWN_PRESSED) != 0) {
            tank.move(0, STEP);
            tank.setFrame(TANK_DOWN);
        }

        if ((keyStates & FIRE_PRESSED) != 0 && !missile.isVisible()) {
            missile.setVisible(true);
            missileDirection = tank.getFrame();
            switch (missileDirection) {
                case TANK_UP:
                    missile.setRefPixelPosition(tank.getX() + tank.getWidth() / 2, tank.getY());
                    break;
                case TANK_RIGHT:
                    missile.setRefPixelPosition(tank.getX() + tank.getWidth(), 
                            tank.getY() + tank.getHeight() / 2);
                    break;
                case TANK_DOWN:
                    missile.setRefPixelPosition(tank.getX() + tank.getWidth() / 2, 
                            tank.getY() + tank.getHeight());
                    break;
                case TANK_LEFT:
                    missile.setRefPixelPosition(tank.getX(), tank.getY() + tank.getHeight() / 2);
                    break;
            }
        }

        if (tank.getX() < 0 || tank.getX() + tank.getWidth() > width
                || tank.getY() < 0 || tank.getY() + tank.getHeight() > height) {
            backToOldPosition = true;
        }

        if (tank.collidesWith(colisionTiles, false)) {
            backToOldPosition = true;
        }

        for (int i = 0; i < walls.length; i++) {
            if (tank.collidesWith(walls[i], false)) {
                backToOldPosition = true;
            }
        }

        if (backToOldPosition) {
            tank.setPosition(oldX, oldY);
        }
    }

    /**
     * Miejsce na logike aplikacji.
     */
    public void think() {
        actualWaterIndex = (actualWaterIndex + 1) % waterSequence.length;
        colisionTiles.setAnimatedTile(animatedTile, waterSequence[actualWaterIndex]);
        
        if(missile.isVisible()) {
            switch(missileDirection) {
                case TANK_DOWN:
                    missile.move(0, MISSILE_STEP);
                    break;
                case TANK_LEFT:
                    missile.move(-MISSILE_STEP, 0);
                    break;
                case TANK_RIGHT:
                    missile.move(MISSILE_STEP, 0);
                    break;
                case TANK_UP:
                    missile.move(0, -MISSILE_STEP);
                    break;
            }
            for (int i = 0; i < walls.length; i++) {
                if(missile.collidesWith(walls[i], false)) {
                    walls[i].setVisible(false);
                    missile.setVisible(false);
                }
            }
            if(missile.getX() < 0 || missile.getX() > width
                    || missile.getY() < 0 || missile.getY() > height) {
                missile.setVisible(false);
            }
            if(missile.collidesWith(colisionTiles, false)) {
                missile.setVisible(false);
            }
        }
    }

    /**
     * Metoda informujaca, czy gra sie juz zakonczyla czy nie.
     *
     * @return true jesli gra jest zakonczona, false w przeciwnym wypadku.
     */
    public boolean isEndGame() {
        return endGame;
    }

    private int calculateNumberOfInTab(int i, int[][] wallTab) {
        int result = 0;
        for (int j = 0; j < wallTab.length; j++) {
            for (int k = 0; k < wallTab[j].length; k++) {
                if (wallTab[j][k] == i) {
                    result++;
                }
            }
        }
        return result;
    }
}
