package com.blogspot.mstachniuk.j2me.tank2012;

import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/**
 * Abstrakcyjna klasa bazowa z pomocniczymi metodami dla TankGameCanvas.
 *
 * @author Marcin Stachniuk http://mstachniuk.blogspot.com
 */
public abstract class AbstractTankCanvas extends GameCanvas {

    public AbstractTankCanvas() {
        super(true);
    }

    /**
     * Metoda pomocnicza do wczytywania grafiki. Przykład urzycia:
     * <code>
     *      Image tank = createImage("tank.png")
     * </code>
     *
     * @param filename Nazwa pliku do wczytania. Plik musi byc umieszczony w
     * glownym katalogu wskazywanym przez Resources (patrz po lewej zakladka
     * Projects).
     * @throws RuntimeException Rzuca wyjatkiem, gdy nie odnaleziono pliku
     * @return Obiekt wczytanego obrazka.
     */
    protected Image createImage(String filename) {
        try {
            return Image.createImage("/" + filename);       // ladowanie pliku graficznego
        } catch (IOException ex) {
            throw new RuntimeException("Nie udalo sie wczytac pliku: /" + filename);
        }
    }

    public abstract boolean isEndGame();

    public abstract void inputKeys();

    public abstract void think();

    public abstract void init();

    public abstract void paint();
}
