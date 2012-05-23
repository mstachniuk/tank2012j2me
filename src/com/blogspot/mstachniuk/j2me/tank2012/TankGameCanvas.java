package com.blogspot.mstachniuk.j2me.tank2012;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

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
    }

    /**
     * Metoda odpowiedzialna za malowanie po ekranie
     */
    public void paint() {
        Graphics g = getGraphics();			// pobranie buffora graficznego
        // -------- od tego miejsca umieszczamy malowanie grafiki --------------

        g.setColor(0, 0, 0);                            // ustawienie aktualnego koloru na czarny
        g.fillRect(0, 0, width, height);                // wypełnianie tla na aktualny kolor

        tank.paint(g);					// malowanie czolgu

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

        // pobranie stanu wcisnietych klawiszy
        int keyStates = getKeyStates();

        // naciśnięto w lewo
        if ((keyStates & LEFT_PRESSED) != 0) {
        }

        // naciśnięto w prawo
        if ((keyStates & RIGHT_PRESSED) != 0) {
        }

        // naciśniętow w gorę
        if ((keyStates & UP_PRESSED) != 0) {
        }

        // naciśnięto w doł
        if ((keyStates & DOWN_PRESSED) != 0) {
        }

    }

    /**
     * Miejsce na logike aplikacji.
     */
    public void think() {
    }

    /**
     * Metoda informujaca, czy gra sie juz zakonczyla czy nie.
     *
     * @return true jesli gra jest zakonczona, false w przeciwnym wypadku.
     */
    public boolean isEndGame() {
        return endGame;
    }
}
