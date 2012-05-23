package com.blogspot.mstachniuk.j2me.tank2012;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

/**
 * @author Marcin Stachniuk http://mstachniuk.blogspot.com
 */
public class Tank2012 extends MIDlet implements Runnable {

    private AbstractTankCanvas canvas;

    public void startApp() {
        canvas = new TankGameCanvas();                  // Utworzenie naszego Canvasu
        canvas.init();                                  // wywolanie metody inicjujacej gre
        Display display = Display.getDisplay(this);     // pobranie referencji do wyswietlacza
        display.setCurrent(canvas);                     // ustawienie, aby wyswietlalo nasz Canvas
        Thread thread = new Thread(this);               // stworzenie watku gry
        thread.start();                                 // uruchomienie watku
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    /**
     * Glowna petla programu, wykonywana do poki canvas.isEndGame() nie bedzie
     * true
     */
    public void run() {
        while (!canvas.isEndGame()) {       // dopoki gra sie nie skonczyła
            try {
                canvas.inputKeys();         // obsluga wcisnietych klawiszy
                canvas.think();             // pozostala logika gry
                canvas.paint();             // odmalowanie ekranu
                Thread.sleep(100);          // uspienie watku na 100 ms
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        destroyApp(false);                  // sprzatanie przed zamknieciem aplikacji
        notifyDestroyed();                  // zamkniecie aplikacji 
    }
}
