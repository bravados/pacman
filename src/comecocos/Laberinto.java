/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comecocos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Santi
 */
public class Laberinto extends JPanel{
    private String rejilla[] = {
        "1AAAAAAAAAAAAfeAAAAAAAAAAAA2",
        "I............di............D",
        "I.5BB6.5BBB6.di.5BBB6.5BB6.D",
        "IoD  I.D   I.di.D   I.D  IoD",
        "I.7AA8.7AAA8.78.7AAA8.7AA8.D",
        "I..........................D",
        "I.5bb6.56.5bbbbbb6.56.5bb6.D",
        "I.7aa8.di.7aa21aa8.di.7aa8.D",
        "I......di....di....di......D",
        "3BBBB6.d3bb6 di 5bb4i.5BBBB4",
        "     I.d1aa8 78 7aa2i.D     ",
        "     I.di          di.D     ",
        "     I.di 5BPPPPB6 di.D     ",
        "AAAAA8.78 D      I 78.7AAAAA",
        "      .   D      I   .      ",
        "BBBBB6.56 D      I 56.5BBBBB",
        "     I.di 7AAAAAA8 di.D     ",
        "     I.di          di.D     ",
        "     I.di 5bbbbbb6 di.D     ",
        "1AAAA8.78 7aa21aa8 78.7AAAA2",
        "I............di............D",
        "I.5bb6.5bbb6.di.5bbb6.5bb6.D",
        "I.7a2i.7aaa8.78.7aaa8.d1a8.D",
        "Io..di................di..oD",
        "gb6.di.56.5bbbbbb6.56.di.5bm",
        "ha8.78.di.7aa21aa8.di.78.7an",
        "I......di....di....di......D",
        "I.5bbbb43bb6.di.5bb43bbbb6.D",
        "I.7aaaaaaaa8.78.7aaaaaaaa8.D",
        "I..........................D",
        "3BBBBBBBBBBBBBBBBBBBBBBBBBB4"
    };

    private class pair{
        public int dir;
        public double dist;
        pair(int Dir, double Dist){
            dir=Dir;
            dist=Dist;
        }
    }
    public static final int VACIO = 0;
    public static final int GHOST = 1;
    public static final int LITTLEPOINT = 2;
    public static final int BIGPOINT = 3;
    public static int cocos_fuera = 0;
    boolean pacman_muerto;

    private char laberinto[][] = new char[31][28];
    private PacMan pacman = new PacMan(14,23);
    private Fantasma blinky = new Fantasma(14,11,"blinky");
    private Fantasma inky = new Fantasma(12,14,"inky");
    private Fantasma pinky = new Fantasma(14,14,"pinky");
    private Fantasma clyde = new Fantasma(16,14,"clyde");
    private int direccion = PacMan.IZQUIERDA;
    
    private Graphics2D g2d;
    private Color azul,blanco,amarillo;
    private int tamFuente = 36;
    private Font mifuente = new Font("Comic Sans MS", Font.BOLD+Font.ITALIC, tamFuente);
    private RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private Stroke stroke = new BasicStroke(1.0F, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_ROUND, 10.0F);

    private String mensaje = "";
    private Color textColor;
    private int x,y,ancho_casilla, alto_casilla;
    private int fantasmasMuertos=0;
    boolean nuevoFantasmaComido=false;

    public Laberinto(){
        super();
        for(int i=0; i<rejilla.length; i++)
            for(int j=0; j<rejilla[i].length(); j++)
                laberinto[i][j] = rejilla[i].charAt(j);

        pacman_muerto=false;
        cocos_fuera = 1;
        azul = Color.blue;
        blanco = Color.white;
        amarillo = Color.yellow;
        setBackground(Color.black);
        addMouseListener(new ManejadorRaton());
        addKeyListener(new ManejadorTeclado());
        addComponentListener(new ManejadorComponente());
    }

    public void reiniciar(){
        for(int i=0; i<rejilla.length; i++)
            for(int j=0; j<rejilla[i].length(); j++)
                laberinto[i][j] = rejilla[i].charAt(j);

        pacman_muerto=false;
        cocos_fuera = 1;
        pacman = new PacMan(14,23);
        direccion = PacMan.IZQUIERDA;

        blinky = new Fantasma(13,17,"blinky");
        inky = new Fantasma(12,14,"inky");
        pinky = new Fantasma(14,14,"pinky");
        clyde = new Fantasma(16,14,"clyde");

        repaint();
    }


    public boolean isNuevoFantasmaComido(){
        return nuevoFantasmaComido;
    }
    
    
    public void setNuevoFantasmaComido(boolean f){
        nuevoFantasmaComido=f;
    }



    public int getFantasmasMuertos(){
        return fantasmasMuertos;
    }


    public void setFantasmasMuertos(){
        fantasmasMuertos=0;
    }
    
    public boolean areComidos(){
        if(clyde.comido  ||  blinky.comido  ||  inky.comido ||  pinky.comido)
            return true;
        else
            return false;
    }

    public boolean areweakCocos(){
        if(clyde.comestible  ||  blinky.comestible  ||  inky.comestible  ||  pinky.comestible)
            return true;
        else
            return false;
    }

    public void setweakCocos(boolean comer){
        blinky.setComestible(comer);
        inky.setComestible(comer);
        pinky.setComestible(comer);
        clyde.setComestible(comer);

    }

    public void releaseCoco(){
        if(cocos_fuera<4)
            {

                boolean fuera=false;
                laberinto[12][12]=' ';
                laberinto[12][13]=' ';
                laberinto[12][14]=' ';
                laberinto[12][15]=' ';
               
                if(!blinky.fuera){
                    blinky.coco_quiere_salir=true;
                    try {
                        while(blinky.y>=12)
                            Thread.sleep(2);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Laberinto.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    blinky.coco_quiere_salir=false;
                    blinky.fuera=true;
                    }
                else
                    if(!clyde.fuera){
                        clyde.coco_quiere_salir=true;
                        try {
                            while(clyde.y>=12)
                                Thread.sleep(2);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Laberinto.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        clyde.coco_quiere_salir=false;
                        clyde.fuera=true;
                        }
                    else
                        if(!inky.fuera){
                            inky.coco_quiere_salir=true;
                        try {
                            while(inky.y>=12)
                                Thread.sleep(2);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Laberinto.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        inky.coco_quiere_salir=false;
                        inky.fuera=true;
                    }
                    else
                        if(!pinky.fuera){
                            pinky.coco_quiere_salir=true;
                        try {
                            while(pinky.y>=12)
                                Thread.sleep(2);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Laberinto.class.getName()).log(Level.SEVERE, null, ex);
                        }
                            pinky.coco_quiere_salir=false;
                            pinky.fuera=true;
                            }
                laberinto[12][12]='P';
                laberinto[12][13]='P';
                laberinto[12][14]='P';
                laberinto[12][15]='P';
                Laberinto.this.repaint();
                cocos_fuera++;
        }
    }

    public char At(int x, int y){
        return laberinto[x][y];
    }

    public int getColumnas(){
        return rejilla[0].length();
    }

    public int getFilas(){
        return rejilla.length;
    }

    public int getPacManX(){
        return pacman.x;
    }

    public int getPacManY(){
        return pacman.y;
    }

    public Point getPacManLocation(){
        return pacman;
    }

    public boolean isLittlePointAt(int x, int y){
        if(x < 0 || x >= rejilla[0].length() || y < 0 || y >= rejilla.length)
            return false;
        else
            if(laberinto[y][x] == '.')
                return true;
            else
                return false;
    }

    public boolean isBigPointAt(int x, int y){
        if(x < 0 || x >= rejilla[0].length() || y < 0 || y >= rejilla.length)
            return false;
        else
            if(laberinto[y][x] == 'o')
                return true;
            else
                return false;
    }

    synchronized public boolean isBlockAt(int x, int y){
        if(x < 0 || x >= rejilla[0].length() || y < 0 || y >= rejilla.length)
            return false;
        else{
            char valor = laberinto[y][x];
            if(valor=='1' || valor=='2' || valor=='3' || valor=='4' || valor=='5' || valor=='6' || valor=='7' || valor=='8'
                || valor=='e' || valor=='f' || valor=='g' || valor=='h' || valor=='n' || valor=='m' || valor=='P'
                    || valor=='A' || valor=='a' || valor=='B' || valor=='b' || valor=='D' || valor=='d' || valor=='I' || valor=='i')
                        return true;
            else
                return false;
        }

    }

    public boolean isPacManAt(int x, int y){
        if(x < 0 || x >= rejilla[0].length() || y < 0 || y >= rejilla.length)
            return false;
        else
            if(pacman.getX() == x && pacman.getY() == y)
                return true;
            else
                return false;
    }

    public boolean isGhostAt(int x, int y){
        if( (clyde.x==pacman.getX()  &&  clyde.y==pacman.getY())  ||  (pinky.x==pacman.getX()  &&  pinky.y==pacman.getY())  ||  (inky.x==pacman.getX()  &&  inky.y==pacman.getY())  ||  (blinky.x==pacman.getX()  &&  blinky.y==pacman.getY()))
            return true;
        else
            return false;
    }



    public boolean isAnyComido(){
        if( (clyde.comido)  ||  (pinky.comido)  ||  (inky.comido)  ||  (blinky.comido))
            return true;
        else
            return false;
    }


    public void pacmanEatsGhost(){
        if( (clyde.x==pacman.getX()  &&  clyde.y==pacman.getY()))
            clyde.comido();
        else
            if( (blinky.x==pacman.getX()  &&  blinky.y==pacman.getY()))
                blinky.comido();
            else
                if( (pinky.x==pacman.getX()  &&  pinky.y==pacman.getY()))
                    pinky.comido();
                else
                    if( (inky.x==pacman.getX()  &&  inky.y==pacman.getY()))
                        inky.comido();
}


    public void brighter(){
        azul = azul.brighter();
        blanco = blanco.brighter();
        amarillo = amarillo.brighter();
    }

    public void darker(){
        azul = azul.darker();
        blanco = blanco.darker();
        amarillo = amarillo.darker();
    }

    public int moverPacMan(){
        switch(pacman.getDireccion()){
            case PacMan.IZQUIERDA:
                if(pacman.x <= 0)
                    pacman.x = rejilla[0].length()-1;
                else{
                    if(!isBlockAt(pacman.x-1,pacman.y)){
                        if(pacman.getFase() == 0)
                            direccion = PacMan.IZQUIERDA;

                        pacman.mover(direccion);
                    }
                    else
                        pacman.mover(direccion);
                }
                break;
            case PacMan.DERECHA:
                if(pacman.x >= rejilla[0].length()-1)
                    pacman.x = 0;
                else{
                    if(!isBlockAt(pacman.x+1,pacman.y)){
                        if(pacman.getFase() == 0)
                            direccion = PacMan.DERECHA;

                        pacman.mover(direccion);
                    }
                    else
                        pacman.mover(direccion);
                }
                break;
            case PacMan.ARRIBA:
                if(pacman.x <= 0 && direccion == PacMan.IZQUIERDA)
                    pacman.x = rejilla[0].length()-1;
                else if(pacman.x >= rejilla[0].length()-1 && direccion == PacMan.DERECHA)
                    pacman.x = 0;
                else{
                    if(!isBlockAt(pacman.x,pacman.y-1)){
                        if(pacman.getFase() == 0)
                            direccion = PacMan.ARRIBA;

                        pacman.mover(direccion);
                    }
                    else
                        pacman.mover(direccion);
                }
                break;
            case PacMan.ABAJO:
                if(pacman.x <= 0 && direccion == PacMan.IZQUIERDA)
                    pacman.x = rejilla[0].length()-1;
                else if(pacman.x >= rejilla[0].length()-1 && direccion == PacMan.DERECHA)
                    pacman.x = 0;
                else{
                    if(!isBlockAt(pacman.x,pacman.y+1)){
                        if(pacman.getFase() == 0)
                            direccion = PacMan.ABAJO;

                        pacman.mover(direccion);
                    }
                    else
                        pacman.mover(direccion);
                }
                break;
            default:
                break;
        }

        if(pacman.getFase() == 0){
            x = pacman.x;
            y = pacman.y;
            if(isLittlePointAt(x,y)){
                this.laberinto[y][x] = ' ';
                return LITTLEPOINT;
            }
            else if(isBigPointAt(x,y)){
                this.laberinto[y][x] = ' ';
                fantasmasMuertos=0;
                nuevoFantasmaComido=false;
                return BIGPOINT;
            }
            else if(isGhostAt(x,y))
                {
                return GHOST;
                }
            else
                return VACIO;
        }
        else
            return VACIO;
    }

    public void drawMessage(String mensaje){
        this.mensaje = mensaje;
        textColor = blanco;
        if(!mensaje.isEmpty() && g2d!=null){
            g2d.setColor(textColor);
            g2d.drawString(mensaje,(getWidth()/2)-(mifuente.getSize2D()*mensaje.length())/4,(alto_casilla*18));
            repaint();
        }
    }

    public void drawMessage(String mensaje, Color color){
        this.mensaje = mensaje;
        textColor = color;
        if(!mensaje.isEmpty() && g2d!=null){
            g2d.setColor(textColor);
            g2d.drawString(mensaje,(getWidth()/2)-(mifuente.getSize2D()*mensaje.length())/4,(alto_casilla*18));
            repaint();
        }
    }

    
    
    @Override
    synchronized public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        g2d = (Graphics2D) g;
        g2d.setRenderingHints(rh);
        g2d.setStroke(stroke);
        g2d.setColor(azul);
        g2d.setFont(mifuente);
        ancho_casilla = (int) (getWidth() / 28 + 0.5);
        alto_casilla = (int) (getHeight() / 31 + 0.5);
        for(int i=0; i<rejilla.length; i++){
            y = alto_casilla*i;
            for(int j=0; j< rejilla[0].length(); j++){
                x = ancho_casilla*j;
                if(laberinto[i][j] == '1'){
                    int [] xPoints = {x,x+ancho_casilla,x+ancho_casilla,x+(ancho_casilla/2),x,x};
                    int [] yPoints = {y,y,y+(alto_casilla)/2,y+alto_casilla,y+alto_casilla};
                    Polygon p = new Polygon(xPoints,yPoints,5);
                    g2d.fillPolygon(p);
                }
                if (laberinto[i][j] == '2'){
                    int [] xPoints = {x,x+ancho_casilla,x+ancho_casilla,x+(ancho_casilla/2),x};
                    int [] yPoints = {y,y,y+alto_casilla,y+alto_casilla,y+(alto_casilla/2)};
                    Polygon p = new Polygon(xPoints,yPoints,5);
                    g2d.fillPolygon(p);
                }
                if(laberinto[i][j] == '3'){
                    int [] xPoints = {x,x+(ancho_casilla/2),x+ancho_casilla,x+ancho_casilla,x};
                    int [] yPoints = {y,y,y+(alto_casilla/2),y+alto_casilla,y+alto_casilla};
                    Polygon p = new Polygon(xPoints,yPoints,5);
                    g2d.fillPolygon(p);
                }
                if(laberinto[i][j] == '4'){
                    int [] xPoints = {x+(ancho_casilla/2),x+ancho_casilla,x+ancho_casilla,x,x};
                    int [] yPoints = {y,y,y+alto_casilla,y+alto_casilla,y+(alto_casilla)/2};
                    Polygon p = new Polygon(xPoints,yPoints,5);
                    g2d.fillPolygon(p);
                }
                if(laberinto[i][j] == '5'){
                    int [] xPoints = {x+ancho_casilla,x+ancho_casilla,x+(ancho_casilla/2)};
                    int [] yPoints = {y+(alto_casilla/2),y+alto_casilla,y+alto_casilla};
                    Polygon p = new Polygon(xPoints,yPoints,3);
                    g2d.fillPolygon(p);
                }
                if(laberinto[i][j] == '6'){
                    int [] xPoints = {x,x+(ancho_casilla/2),x};
                    int [] yPoints = {y+(alto_casilla/2),y+alto_casilla,y+alto_casilla};
                    Polygon p = new Polygon(xPoints,yPoints,3);
                    g2d.fillPolygon(p);
                }
                if(laberinto[i][j] == '7'){
                    int [] xPoints = {x+(ancho_casilla/2),x+ancho_casilla,x+ancho_casilla};
                    int [] yPoints = {y,y,y+(alto_casilla/2)};
                    Polygon p = new Polygon(xPoints,yPoints,3);
                    g2d.fillPolygon(p);
                }
                if(laberinto[i][j] == '8'){
                    int [] xPoints = {x,x+(ancho_casilla/2),x};
                    int [] yPoints = {y,y,(y+alto_casilla/2)};
                    Polygon p = new Polygon(xPoints,yPoints,3);
                    g2d.fillPolygon(p);
                }
                if(laberinto[i][j] == 'e'){
                    int [] xPoints = {x,x+ancho_casilla,x+ancho_casilla,x+(ancho_casilla/2),x,x};
                    int [] yPoints = {y,y,y+(alto_casilla/2),y+alto_casilla,y+alto_casilla};
                    Polygon p = new Polygon(xPoints,yPoints,5);
                    g2d.fillPolygon(p);
                }
                if(laberinto[i][j] == 'f'){
                    int [] xPoints = {x,x+ancho_casilla,x+ancho_casilla,x+(ancho_casilla/2),x};
                    int [] yPoints = {y,y,y+alto_casilla,y+alto_casilla,y+(alto_casilla/2)};
                    Polygon p = new Polygon(xPoints,yPoints,5);
                    g2d.fillPolygon(p);
                }
                if(laberinto[i][j] == 'g'){
                    int [] xPoints = {x,x+(ancho_casilla/2),x+ancho_casilla,x+ancho_casilla,x};
                    int [] yPoints = {y,y,y+(alto_casilla/2),y+alto_casilla,y+alto_casilla};
                    Polygon p = new Polygon(xPoints,yPoints,5);
                    g2d.fillPolygon(p);
                }
                if(laberinto[i][j] == 'h'){
                    int [] xPoints = {x,x+ancho_casilla,x+ancho_casilla,x+(ancho_casilla/2),x,x};
                    int [] yPoints = {y,y,y+(alto_casilla/2),y+alto_casilla,y+alto_casilla};
                    Polygon p = new Polygon(xPoints,yPoints,5);
                    g2d.fillPolygon(p);
                }
                if(laberinto[i][j] == 'n'){
                    int [] xPoints = {x,x+ancho_casilla,x+ancho_casilla,x+(ancho_casilla/2),x};
                    int [] yPoints = {y,y,y+alto_casilla,y+alto_casilla,y+(alto_casilla/2)};
                    Polygon p = new Polygon(xPoints,yPoints,5);
                    g2d.fillPolygon(p);
                }
                if(laberinto[i][j] == 'm'){
                    int [] xPoints = {x+(ancho_casilla/2),x+ancho_casilla,x+ancho_casilla,x,x};
                    int [] yPoints = {y,y,y+alto_casilla,y+alto_casilla,y+(alto_casilla/2)};
                    Polygon p = new Polygon(xPoints,yPoints,5);
                    g2d.fillPolygon(p);
                }
                if(laberinto[i][j] == 'P'){
                    g2d.setColor(blanco);
                    int yAux = (int) (y + (alto_casilla /((4 / 3.0))));
                    g2d.fill(new Rectangle2D.Float(x,yAux,ancho_casilla,(alto_casilla/5)));
                    g2d.setColor(azul);
                }
                if(laberinto[i][j] == 'A'){
                    g2d.fill(new Rectangle2D.Float(x,y,ancho_casilla,(alto_casilla/2)));
                }
                if(laberinto[i][j] == 'a'){
                    g2d.fill(new Rectangle2D.Float(x,y,ancho_casilla,(alto_casilla/2)));
                }
                if(laberinto[i][j] == 'B'){
                    g2d.fill(new Rectangle2D.Float(x,y+(alto_casilla/2),ancho_casilla,(alto_casilla/2)));
                }
                if(laberinto[i][j] == 'b'){
                    g2d.fill(new Rectangle2D.Float(x,y+(alto_casilla/2),ancho_casilla,(alto_casilla/2)));
                }
                if(laberinto[i][j] == 'D'){
                    g2d.fill(new Rectangle2D.Float(x+(ancho_casilla/2),y,(ancho_casilla/2),alto_casilla));
                }
                if(laberinto[i][j] == 'd'){
                    g2d.fill(new Rectangle2D.Float(x+(ancho_casilla/2),y,(ancho_casilla/2),alto_casilla));
                }
                if(laberinto[i][j] == 'I'){
                    g2d.fill(new Rectangle2D.Float(x,y,(ancho_casilla/2),alto_casilla));
                }
                if(laberinto[i][j] == 'i'){
                    g2d.fill(new Rectangle2D.Float(x,y,(ancho_casilla/2),alto_casilla));
                }
                if(laberinto[i][j] == '.'){
                    g2d.setColor(blanco);
                    g2d.fill(new Ellipse2D.Float(x+(ancho_casilla/2)-((ancho_casilla/5)/2),y+(alto_casilla/2)-((alto_casilla/5)/2),(ancho_casilla/5),(alto_casilla/5)));
                    g2d.setColor(azul);
                }
                if(laberinto[i][j] == 'o'){
                    g2d.setColor(blanco);
                    g2d.fill(new Ellipse2D.Float(x+(ancho_casilla/2)-(ancho_casilla/4),y+(alto_casilla/2)-(alto_casilla/4),(ancho_casilla/2),(alto_casilla/2)));
                    g2d.setColor(azul);
                }
                if(i == pacman.getY() && j == pacman.getX())
                    if(!pacman_muerto)
                        pacman.drawPacMan(g);
                    
            }
        }
        drawMessage(mensaje,textColor);
        

        //LOS COCOS:
        clyde.DrawCoco(g);
        blinky.DrawCoco(g);
        pinky.DrawCoco(g);
        inky.DrawCoco(g);
        if(pacman_muerto)
            {
            pacman.pacmanMUERE(g);
            }
    }

    class PacMan extends Point{
        ImageIcon pacmanLab;
        ImageIcon pacmanLce;
        ImageIcon pacmanRab;
        ImageIcon pacmanRce;
        ImageIcon pacmanUab;
        ImageIcon pacmanUce;
        ImageIcon pacmanDab;
        ImageIcon pacmanDce;
        ImageIcon pacmanDL;
        ImageIcon pacmanDR;
        ImageIcon pacmanDU;
        ImageIcon pacmanDD;
        ImageIcon pacmanDEAD;

        public static final int IZQUIERDA = 0;
        public static final int DERECHA = 1;
        public static final int ARRIBA = 2;
        public static final int ABAJO = 3;

        private int direccion = IZQUIERDA;
        private int faseMovimiento = 0;

        PacMan(){
            super();
            pacmanLab = new ImageIcon(getClass().getResource("resources/pacmanLab.gif"));
            pacmanLce = new ImageIcon(getClass().getResource("resources/pacmanLce.gif"));
            pacmanRab = new ImageIcon(getClass().getResource("resources/pacmanRab.gif"));
            pacmanRce = new ImageIcon(getClass().getResource("resources/pacmanRce.gif"));
            pacmanUab = new ImageIcon(getClass().getResource("resources/pacmanUab.gif"));
            pacmanUce = new ImageIcon(getClass().getResource("resources/pacmanLce.gif"));
            pacmanDab = new ImageIcon(getClass().getResource("resources/pacmanDab.gif"));
            pacmanDce = new ImageIcon(getClass().getResource("resources/pacmanDce.gif"));
            pacmanDL = new ImageIcon(getClass().getResource("resources/pacmanDL.gif"));
            pacmanDR = new ImageIcon(getClass().getResource("resources/pacmanDR.gif"));
            pacmanDU = new ImageIcon(getClass().getResource("resources/pacmanDU.gif"));
            pacmanDD = new ImageIcon(getClass().getResource("resources/pacmanDD.gif"));
            pacmanDEAD = new ImageIcon(getClass().getResource("resources/pacmanDEAD.gif"));
        }

        PacMan(int x, int y){
            super(x,y);
            pacmanLab = new ImageIcon(getClass().getResource("resources/pacmanLab.gif"));
            pacmanLce = new ImageIcon(getClass().getResource("resources/pacmanLce.gif"));
            pacmanRab = new ImageIcon(getClass().getResource("resources/pacmanRab.gif"));
            pacmanRce = new ImageIcon(getClass().getResource("resources/pacmanRce.gif"));
            pacmanUab = new ImageIcon(getClass().getResource("resources/pacmanUab.gif"));
            pacmanUce = new ImageIcon(getClass().getResource("resources/pacmanLce.gif"));
            pacmanDab = new ImageIcon(getClass().getResource("resources/pacmanDab.gif"));
            pacmanDce = new ImageIcon(getClass().getResource("resources/pacmanDce.gif"));
            pacmanDL = new ImageIcon(getClass().getResource("resources/pacmanDL.gif"));
            pacmanDR = new ImageIcon(getClass().getResource("resources/pacmanDR.gif"));
            pacmanDU = new ImageIcon(getClass().getResource("resources/pacmanDU.gif"));
            pacmanDD = new ImageIcon(getClass().getResource("resources/pacmanDD.gif"));
            pacmanDEAD = new ImageIcon(getClass().getResource("resources/pacmanDEAD.gif"));
        }

        public void setDireccion(int direccion){
            this.direccion = direccion;
        }

        public void setFase(int fase){
            faseMovimiento = fase;
        }

        public int getDireccion(){
            return direccion;
        }

        public int getFase(){
            return faseMovimiento;
        }

        public boolean mover(){
            return mover(direccion);
        }

        public boolean mover(int sentido){
            boolean mover = false;

            switch(sentido){
            case IZQUIERDA:
                if(!isBlockAt(x-1,y)){
                    if(faseMovimiento == 0)
                        faseMovimiento = 1;
                    else if(faseMovimiento == 1)
                        faseMovimiento = 2;
                    else if(faseMovimiento == 2)
                        faseMovimiento = 3;
                    else if(faseMovimiento == 3)
                        faseMovimiento = 4;
                    else if(faseMovimiento == 4)
                        faseMovimiento = 5;
                    else if(faseMovimiento == 5)
                        faseMovimiento = 6;
                    else if(faseMovimiento == 6)
                        faseMovimiento = 7;
                    else if(faseMovimiento == 7)
                        faseMovimiento = 8;
                    else if(faseMovimiento == 8)
                        faseMovimiento = 9;
                    else if(faseMovimiento == 9){
                        faseMovimiento = 0;
                        x--;
                    }
                }
                break;
            case DERECHA:
                if(!isBlockAt(x+1,y)){
                    if(faseMovimiento == 0)
                        faseMovimiento = 1;
                    else if(faseMovimiento == 1)
                        faseMovimiento = 2;
                    else if(faseMovimiento == 2)
                        faseMovimiento = 3;
                    else if(faseMovimiento == 3)
                        faseMovimiento = 4;
                    else if(faseMovimiento == 4)
                        faseMovimiento = 5;
                    else if(faseMovimiento == 5)
                        faseMovimiento = 6;
                    else if(faseMovimiento == 6)
                        faseMovimiento = 7;
                    else if(faseMovimiento == 7)
                        faseMovimiento = 8;
                    else if(faseMovimiento == 8)
                        faseMovimiento = 9;
                    else if(faseMovimiento == 9){
                        faseMovimiento=0;
                        x++;
                    }
                }
                break;
            case ARRIBA:
                if(!isBlockAt(x,y-1)){
                    if(faseMovimiento == 0)
                        faseMovimiento = 1;
                    else if(faseMovimiento == 1)
                        faseMovimiento = 2;
                    else if(faseMovimiento == 2)
                        faseMovimiento = 3;
                    else if(faseMovimiento == 3)
                        faseMovimiento = 4;
                    else if(faseMovimiento == 4)
                        faseMovimiento = 5;
                    else if(faseMovimiento == 5)
                        faseMovimiento = 6;
                    else if(faseMovimiento == 6)
                        faseMovimiento = 7;
                    else if(faseMovimiento == 7)
                        faseMovimiento = 8;
                    else if(faseMovimiento == 8)
                        faseMovimiento = 9;
                    else if(faseMovimiento == 9){
                        faseMovimiento = 0;
                        y--;
                    }
                }
                break;
            case ABAJO:
                if(!isBlockAt(x,y+1)){
                    if(faseMovimiento == 0)
                        faseMovimiento = 1;
                    else if(faseMovimiento == 1)
                        faseMovimiento = 2;
                    else if(faseMovimiento == 2)
                        faseMovimiento = 3;
                    else if(faseMovimiento == 3)
                        faseMovimiento = 4;
                    else if(faseMovimiento == 4)
                        faseMovimiento = 5;
                    else if(faseMovimiento == 5)
                        faseMovimiento = 6;
                    else if(faseMovimiento == 6)
                        faseMovimiento = 7;
                    else if(faseMovimiento == 7)
                        faseMovimiento = 8;
                    else if(faseMovimiento == 8)
                        faseMovimiento = 9;
                    else if(faseMovimiento == 9){
                        faseMovimiento = 0;
                        y++;
                    }
                }
                break;
            default:
                mover = false;
            }
            return mover;
        }



        public void drawPacMan(Graphics g){
        int inicio_x = x*ancho_casilla;
        int inicio_y = y*alto_casilla;
        if(Laberinto.this.direccion == PacMan.IZQUIERDA){
            if(faseMovimiento == 0){
                g.drawImage(pacmanLab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 1){
                inicio_x -= ancho_casilla/10;
                g.drawImage(pacmanLab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 2){
                inicio_x -= ancho_casilla*2/10;
                g.drawImage(pacmanLab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 3){
                inicio_x -= ancho_casilla*3/10;
                g.drawImage(pacmanLab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 4){
                inicio_x -= ancho_casilla*4/10;
                g.drawImage(pacmanLab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 5){
                inicio_x -= ancho_casilla*5/10;
                g.drawImage(pacmanLce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 6){
                inicio_x -= ancho_casilla*6/10;
                g.drawImage(pacmanLce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 7){
                inicio_x -= ancho_casilla*7/10;
                g.drawImage(pacmanLce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 8){
                inicio_x -= ancho_casilla*8/10;
                g.drawImage(pacmanLce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 9){
                inicio_x -= ancho_casilla*9/10;
                g.drawImage(pacmanLab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
        }
        else if(Laberinto.this.direccion == PacMan.DERECHA){
            if(faseMovimiento == 0){
                g.drawImage(pacmanRab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 1){
                inicio_x += ancho_casilla/10;
                g.drawImage(pacmanRab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 2){
                inicio_x += ancho_casilla*2/10;
                g.drawImage(pacmanRab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 3){
                inicio_x += ancho_casilla*3/10;
                g.drawImage(pacmanRab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 4){
                inicio_x += ancho_casilla*4/10;
                g.drawImage(pacmanRab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 5){
                inicio_x += ancho_casilla*5/10;
                g.drawImage(pacmanRce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 6){
                inicio_x += ancho_casilla*6/10;
                g.drawImage(pacmanRce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 7){
                inicio_x += ancho_casilla*7/10;
                g.drawImage(pacmanRce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 8){
                inicio_x += ancho_casilla*8/10;
                g.drawImage(pacmanRce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 9){
                inicio_x += ancho_casilla*9/10;
                g.drawImage(pacmanRab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
        }
        else if(Laberinto.this.direccion == PacMan.ARRIBA){
            if(faseMovimiento == 0){
                g.drawImage(pacmanUab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 1){
                inicio_y -= ancho_casilla/10;
                g.drawImage(pacmanUab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 2){
                inicio_y -= ancho_casilla*2/10;
                g.drawImage(pacmanUab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 3){
                inicio_y -= ancho_casilla*3/10;
                g.drawImage(pacmanUab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 4){
                inicio_y -= ancho_casilla*4/10;
                g.drawImage(pacmanUab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 5){
                inicio_y -= ancho_casilla*5/10;
                g.drawImage(pacmanUce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 6){
                inicio_y -= ancho_casilla*6/10;
                g.drawImage(pacmanUce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 7){
                inicio_y -= ancho_casilla*7/10;
                g.drawImage(pacmanUce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 8){
                inicio_y -= ancho_casilla*8/10;
                g.drawImage(pacmanUce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 9){
                inicio_y -= ancho_casilla*9/10;
                g.drawImage(pacmanUab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
        }
        else if(Laberinto.this.direccion == PacMan.ABAJO){
            if(faseMovimiento == 0){
                g.drawImage(pacmanDab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 1){
                inicio_y += ancho_casilla/10;
                g.drawImage(pacmanDab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 2){
                inicio_y += ancho_casilla*2/10;
                g.drawImage(pacmanDab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 3){
                inicio_y += ancho_casilla*3/10;
                g.drawImage(pacmanDab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 4){
                inicio_y += ancho_casilla*4/10;
                g.drawImage(pacmanDab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 5){
                inicio_y += ancho_casilla*5/10;
                g.drawImage(pacmanDce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 6){
                inicio_y += ancho_casilla*6/10;
                g.drawImage(pacmanDce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 7){
                inicio_y += ancho_casilla*7/10;
                g.drawImage(pacmanDce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 8){
                inicio_y += ancho_casilla*8/10;
                g.drawImage(pacmanDce.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
            else if(faseMovimiento == 9){
                inicio_y += ancho_casilla*9/10;
                g.drawImage(pacmanDab.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            }
        }
    }

        public void pacmanMUERE(Graphics g){
        int inicio_x = x*ancho_casilla;
        int inicio_y = y*alto_casilla;
            pacman_muerto=true;
        if(Laberinto.this.pacman.getDireccion()==Laberinto.PacMan.IZQUIERDA)
            g.drawImage(pacmanDL.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
        else
            if(Laberinto.this.pacman.getDireccion()==Laberinto.PacMan.DERECHA)
                g.drawImage(pacmanDR.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
            else
                if(Laberinto.this.pacman.getDireccion()==Laberinto.PacMan.ARRIBA)
                    g.drawImage(pacmanDU.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);
                else
                    if(Laberinto.this.pacman.getDireccion()==Laberinto.PacMan.ABAJO)
                        g.drawImage(pacmanDD.getImage(),inicio_x-ancho_casilla/4,inicio_y-alto_casilla/4,(ancho_casilla*3)/2,(alto_casilla*3)/2,null);

            
    }
        
    }




    public void stopCocos(){
        inky.stopCoco();
        blinky.stopCoco();
        pinky.stopCoco();
        clyde.stopCoco();
    }


    public void reanudarCocos(){
        inky.reanudarCoco();
        blinky.reanudarCoco();
        pinky.reanudarCoco();
        clyde.reanudarCoco();
    }

    class ManejadorRaton extends MouseAdapter{
        @Override
        public void mouseEntered(MouseEvent e){
            requestFocus();
        }
    }

    class ManejadorTeclado extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent event){
            if(event.getKeyCode() == KeyEvent.VK_RIGHT){
                pacman.setDireccion(PacMan.DERECHA);
            }
            if(event.getKeyCode() == KeyEvent.VK_LEFT){
                pacman.setDireccion(PacMan.IZQUIERDA);
            }
            if(event.getKeyCode() == KeyEvent.VK_UP){
                pacman.setDireccion(PacMan.ARRIBA);
            }
            if(event.getKeyCode() == KeyEvent.VK_DOWN){
               pacman.setDireccion(PacMan.ABAJO);
            }
        }
    }
    
    class ManejadorComponente extends ComponentAdapter{
        @Override
        public void componentResized(ComponentEvent e) {
            float ancho = (e.getComponent().getWidth()/28);
            float alto = (e.getComponent().getHeight()/31);
            int tamano = (int) (alto + 0.5);

            if(ancho < alto)
                tamano = (int) (ancho + 0.5);

            tamFuente = (int) (((getWidth() / getColumnas()) * 3) / 2 + 0.5);
            mifuente = new Font("Comic Sans MS", Font.BOLD+Font.ITALIC, tamFuente);
            e.getComponent().setSize((tamano*28),(tamano*31));
        }
    }





    



    class Fantasma implements Runnable {
        int x, y;
        boolean coco_quiere_salir=false;
        boolean fuera;
        boolean comestible, comido;
        int faseMovimiento;
        int direccion;
        int delay;
        Thread t;
        ImageIcon ghostU;
        ImageIcon ghostD;
        ImageIcon ghostL;
        ImageIcon ghostR;
        ImageIcon weakCoco;
        ImageIcon ojosU;
        ImageIcon ojosD;
        ImageIcon ojosL;
        ImageIcon ojosR;

        public static final int IZQUIERDA = 0;
        public static final int DERECHA = 1;
        public static final int ARRIBA = 2;
        public static final int ABAJO = 3;
        

        public Fantasma(int x, int y, String name){
            this.x=x;
            this.y=y;
            coco_quiere_salir=false;
            fuera=false;
            comestible=false;
            comido=false;
            faseMovimiento=0;
            direccion=0;
            delay=12;
           

            if(name.equals("clyde")){
                ghostU = new ImageIcon(getClass().getResource("resources/clydeU.gif"));
                ghostD = new ImageIcon(getClass().getResource("resources/clydeD.gif"));
                ghostL = new ImageIcon(getClass().getResource("resources/clydeL.gif"));
                ghostR = new ImageIcon(getClass().getResource("resources/clydeR.gif"));
                }
            else
                if(name.equals("blinky")){
                    ghostU = new ImageIcon(getClass().getResource("resources/blinkyU.gif"));
                    ghostD = new ImageIcon(getClass().getResource("resources/blinkyD.gif"));
                    ghostL = new ImageIcon(getClass().getResource("resources/blinkyL.gif"));
                    ghostR = new ImageIcon(getClass().getResource("resources/blinkyR.gif"));
                    fuera=true;
                    }
                else
                    if(name.equals("pinky")){
                        ghostU = new ImageIcon(getClass().getResource("resources/pinkyU.gif"));
                        ghostD = new ImageIcon(getClass().getResource("resources/pinkyD.gif"));
                        ghostL = new ImageIcon(getClass().getResource("resources/pinkyL.gif"));
                        ghostR = new ImageIcon(getClass().getResource("resources/pinkyR.gif"));
                        }

                    else
                        if(name.equals("inky")){
                            ghostU = new ImageIcon(getClass().getResource("resources/inkyU.gif"));
                            ghostD = new ImageIcon(getClass().getResource("resources/inkyD.gif"));
                            ghostL = new ImageIcon(getClass().getResource("resources/inkyL.gif"));
                            ghostR = new ImageIcon(getClass().getResource("resources/inkyR.gif"));
                            }

            weakCoco = new ImageIcon(getClass().getResource("resources/weak.gif"));

            ojosU = new ImageIcon(getClass().getResource("resources/ojosU.gif"));
            ojosD = new ImageIcon(getClass().getResource("resources/ojosD.gif"));
            ojosL = new ImageIcon(getClass().getResource("resources/ojosL.gif"));
            ojosR = new ImageIcon(getClass().getResource("resources/ojosR.gif"));



            t=new Thread(this);
            t.setName(name);
            t.start();
        }

        private void nextDir(){
            if(comido)
                {
                if(x==14  &&  y==11)
                    {
                direccion=ABAJO;
                y+=2;
                    try {
                        while(y<12){
                            Thread.sleep(100);
                        }
                        delay=30;
                        comido=false;
                        cocos_fuera--;
                        if(t.getName().equals("blinky"))
                            blinky.fuera=false;
                        else
                            if(t.getName().equals("inky"))
                                inky.fuera=false;
                            else
                                if(t.getName().equals("clyde"))
                                    clyde.fuera=false;
                                else
                                    if(t.getName().equals("pinky"))
                                        pinky.fuera=false;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Laberinto.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }


            boolean end=false;
            int j;
            double arriba, abajo, izquierda, derecha, aux;
            arriba=Math.sqrt(Math.pow(   (double)(14) - (double)(x)    , 2.0)+ Math.pow(   (double)(11) - (double)(y-1)  , 2.0));
            abajo=Math.sqrt(Math.pow(   (double)(14) - (double)(x)    , 2.0)+ Math.pow(   (double)(11) - (double)(y+1)  , 2.0));
            izquierda=Math.sqrt(Math.pow(   (double)(14) - (double)(x-1)    , 2.0)+ Math.pow(   (double)(11) - (double)(y)  , 2.0));
            derecha=Math.sqrt(Math.pow(   (double)(14) - (double)(x+1)    , 2.0)+ Math.pow(   (double)(11) - (double)(y)  , 2.0));


            pair up = new pair(ARRIBA,arriba);
            pair down = new pair(ABAJO,abajo);
            pair left = new pair(IZQUIERDA,izquierda);
            pair right = new pair(DERECHA,derecha);

            Vector<pair> v = new Vector<pair>();
            v.add(up);
            v.add(down);
            v.add(left);
            v.add(right);

            //ordenacion por burbuja
        pair temp;
        int t = v.size();
        for (int i = 1; i < t; i++) {
            for (int k = t- 1; k >= i; k--) {
                if(v.get(k).dist < v.get(k-1).dist){
                    temp = v.get(k);
                    v.set(k, v.get(k-1));
                    v.set(k-1, temp);
                }//fin if
            }// fin 2 for
        }


        for(int i=0; i<v.size()  &&  !end; i++){
            switch(v.get(i).dir){
                case ARRIBA:
                    if(!Laberinto.this.isBlockAt(x, y-1))
                        if(direccion!=ABAJO)
                            {
                            direccion=ARRIBA;
                            end=true;
                            }
                    break;
                case ABAJO:
                    if(!Laberinto.this.isBlockAt(x, y+1))
                        if(direccion!=ARRIBA)
                            {
                            direccion=ABAJO;
                            end=true;
                            }
                    break;
                case IZQUIERDA:
                    if(!Laberinto.this.isBlockAt(x-1, y)){
                        if(direccion!=DERECHA)
                            {
                            direccion=IZQUIERDA;
                            end=true;
                            }
                    }
                    break;
                case DERECHA:
                    if(!Laberinto.this.isBlockAt(x+1, y))
                        if(direccion!=IZQUIERDA)
                            {
                            direccion=DERECHA;
                            end=true;
                            }
                    break;
                    }
            }



            }
            else
                {


            boolean end=false;
            int j;
            double arriba, abajo, izquierda, derecha, aux;
            arriba=Math.sqrt(Math.pow(   (double)(Laberinto.this.pacman.getX()) - (double)(x)    , 2.0)+ Math.pow(   (double)(Laberinto.this.pacman.getY()) - (double)(y-1)  , 2.0));
            abajo=Math.sqrt(Math.pow(   (double)(Laberinto.this.pacman.getX()) - (double)(x)    , 2.0)+ Math.pow(   (double)(Laberinto.this.pacman.getY()) - (double)(y+1)  , 2.0));
            izquierda=Math.sqrt(Math.pow(   (double)(Laberinto.this.pacman.getX()) - (double)(x-1)    , 2.0)+ Math.pow(   (double)(Laberinto.this.pacman.getY()) - (double)(y)  , 2.0));
            derecha=Math.sqrt(Math.pow(   (double)(Laberinto.this.pacman.getX()) - (double)(x+1)    , 2.0)+ Math.pow(   (double)(Laberinto.this.pacman.getY()) - (double)(y)  , 2.0));
            

            pair up = new pair(ARRIBA,arriba);
            pair down = new pair(ABAJO,abajo);
            pair left = new pair(IZQUIERDA,izquierda);
            pair right = new pair(DERECHA,derecha);

            Vector<pair> v = new Vector<pair>();
            v.add(up);
            v.add(down);
            v.add(left);
            v.add(right);

            //ordenacion por burbuja
        pair temp;
        int t = v.size();
        for (int i = 1; i < t; i++) {
            for (int k = t- 1; k >= i; k--) {
                if(v.get(k).dist < v.get(k-1).dist){
                    temp = v.get(k);
                    v.set(k, v.get(k-1));
                    v.set(k-1, temp);
                }//fin if
            }// fin 2 for
        }
        for(int i=0; i<v.size()  &&  !end; i++){
            if(  (((int)(Math.random()))%2 == 0)  ||  i==v.size()-1)
            {


            if(comestible)
                j=v.size()-1-i;
            else
                j=i;
            switch(v.get(j).dir){
                case ARRIBA:
                    if(!Laberinto.this.isBlockAt(x, y-1))
                        if(direccion!=ABAJO)
                            {
                            direccion=ARRIBA;
                            end=true;
                            }
                    break;
                case ABAJO:
                    if(!Laberinto.this.isBlockAt(x, y+1))
                        if(direccion!=ARRIBA)
                            {
                            direccion=ABAJO;
                            end=true;
                            }
                    break;
                case IZQUIERDA:
                    if(!Laberinto.this.isBlockAt(x-1, y)){
                        if(direccion!=DERECHA)
                            {
                            direccion=IZQUIERDA;
                            end=true;
                            }
                    }
                    break;
                case DERECHA:
                    if(!Laberinto.this.isBlockAt(x+1, y))
                        if(direccion!=IZQUIERDA)
                            {
                            direccion=DERECHA;
                            end=true;
                            }
                    break;
                    }






            }



            }
        }
        }

        public void setComestible(boolean comer){
                comestible=comer;
                if(comestible)
                    delay=30;
                else
                    delay=15;
        }



        public void mover(){
            if(faseMovimiento==0)
                if(coco_quiere_salir){
                    if(!Laberinto.this.isBlockAt(x, y-1))
                        direccion=ARRIBA;
                    }
                else
                    nextDir();
            switch(direccion){

            case IZQUIERDA:
                if(x <= 0  &&  y == 14)
                    x = rejilla[0].length()-1;
                if(!isBlockAt(x-1,y)){
                    if(faseMovimiento == 0)
                        faseMovimiento = 1;
                    else if(faseMovimiento == 1)
                        faseMovimiento = 2;
                    else if(faseMovimiento == 2)
                        faseMovimiento = 3;
                    else if(faseMovimiento == 3)
                            faseMovimiento = 4;
                    else
                         if(faseMovimiento==4)
                                faseMovimiento=5;
                    else
                         if(faseMovimiento==5)
                                faseMovimiento=6;
                    else
                         if(faseMovimiento==6)
                                faseMovimiento=7;
                    else
                         if(faseMovimiento==7)
                                faseMovimiento=8;
                    else
                         if(faseMovimiento==8)
                                faseMovimiento=9;
                         else
                         if(faseMovimiento == 9){
                                faseMovimiento = 0;
                                x--;
                                }
                }
                else
                    direccion=DERECHA;
                break;
            case DERECHA:
                if(x >= rejilla[0].length()-1)
                    x = 0;
                if(!isBlockAt(x+1,y)){
                    if(faseMovimiento == 0)
                        faseMovimiento = 1;
                    else if(faseMovimiento == 1)
                        faseMovimiento = 2;
                    else if(faseMovimiento == 2)
                        faseMovimiento = 3;
                    else if(faseMovimiento == 3)
                            faseMovimiento = 4;
                    else
                         if(faseMovimiento==4)
                                faseMovimiento=5;
                    else
                         if(faseMovimiento==5)
                                faseMovimiento=6;
                    else
                         if(faseMovimiento==6)
                                faseMovimiento=7;
                    else
                         if(faseMovimiento==7)
                                faseMovimiento=8;
                    else
                         if(faseMovimiento==8)
                                faseMovimiento=9;
                         else
                         if(faseMovimiento == 9){
                                faseMovimiento = 0;
                                x++;
                                }
                }
                else
                    direccion=IZQUIERDA;
                break;
            case ARRIBA:
                if(!isBlockAt(x,y-1)){
                    if(faseMovimiento == 0)
                        faseMovimiento = 1;
                    else if(faseMovimiento == 1)
                        faseMovimiento = 2;
                    else if(faseMovimiento == 2)
                        faseMovimiento = 3;
                    else if(faseMovimiento == 3)
                            faseMovimiento = 4;
                    else
                         if(faseMovimiento==4)
                                faseMovimiento=5;
                    else
                         if(faseMovimiento==5)
                                faseMovimiento=6;
                    else
                         if(faseMovimiento==6)
                                faseMovimiento=7;
                    else
                         if(faseMovimiento==7)
                                faseMovimiento=8;
                    else
                         if(faseMovimiento==8)
                                faseMovimiento=9;
                         else
                         if(faseMovimiento == 9){
                                faseMovimiento = 0;
                                y--;
                                }
                }
                else
                    direccion=IZQUIERDA;
                break;
            case ABAJO:
                
                if(!isBlockAt(x,y+1)){
                    if(faseMovimiento == 0)
                        faseMovimiento = 1;
                    else if(faseMovimiento == 1)
                        faseMovimiento = 2;
                    else if(faseMovimiento == 2)
                        faseMovimiento = 3;
                    else if(faseMovimiento == 3)
                            faseMovimiento = 4;
                    else
                         if(faseMovimiento==4)
                                faseMovimiento=5;
                    else
                         if(faseMovimiento==5)
                                faseMovimiento=6;
                    else
                         if(faseMovimiento==6)
                                faseMovimiento=7;
                    else
                         if(faseMovimiento==7)
                                faseMovimiento=8;
                    else
                         if(faseMovimiento==8)
                                faseMovimiento=9;
                         else
                         if(faseMovimiento == 9){
                                faseMovimiento = 0;
                                y++;
                                }
                }
                else
                    {
                    direccion=DERECHA;}
                break;
            }
        }



        public void DrawCoco(Graphics g)
                {
            Image temp;
            if(direccion == IZQUIERDA){
                if(comido)
                        temp=ojosL.getImage();
                else
                    if(comestible)
                        temp=weakCoco.getImage();
                    else
                        temp=ghostL.getImage();
            if(faseMovimiento == 0){
                g.drawImage(temp, x*ancho_casilla-ancho_casilla/4, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 1){
                g.drawImage(temp, x*ancho_casilla-ancho_casilla/4-(ancho_casilla)/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 2){
                g.drawImage(temp, x*ancho_casilla-ancho_casilla/4-(ancho_casilla)*2/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 3){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4-(ancho_casilla*3)/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
                }
            else
                 if(faseMovimiento == 4){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4-(ancho_casilla)*4/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
                }
            else
                 if(faseMovimiento == 5){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4-(ancho_casilla)*5/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
                }
            else
                 if(faseMovimiento == 6){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4-(ancho_casilla)*6/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
                }
            else
                 if(faseMovimiento == 7){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4-(ancho_casilla)*7/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
                }
            else
                 if(faseMovimiento == 8){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4-(ancho_casilla)*8/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
                }
            else
                 if(faseMovimiento == 9){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4-(ancho_casilla)*9/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
                }
            }
        else if(direccion == DERECHA){
            if(comido)
                    temp=ojosR.getImage();
            else
                if(comestible)
                    temp=weakCoco.getImage();
                else
                    temp=ghostR.getImage();
            if(faseMovimiento == 0){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 1){
                g.drawImage(temp, x*ancho_casilla-ancho_casilla/4+ancho_casilla/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 2){
                g.drawImage(temp, x*ancho_casilla-ancho_casilla/4+(ancho_casilla)*2/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 3){
                g.drawImage(temp, x*ancho_casilla-ancho_casilla/4+ancho_casilla*3/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else
                 if(faseMovimiento == 4){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4+(ancho_casilla)*4/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
                }
            else
                 if(faseMovimiento == 5){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4+(ancho_casilla)*5/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
                }
            else
                 if(faseMovimiento == 6){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4+(ancho_casilla)*6/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
                }
            else
                 if(faseMovimiento == 7){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4+(ancho_casilla)*7/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
                }
            else
                 if(faseMovimiento == 8){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4+(ancho_casilla)*8/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
                }
            else
                 if(faseMovimiento == 9){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4+(ancho_casilla)*9/10, y*alto_casilla+alto_casilla/2-(alto_casilla*3)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
                }
            }
        else if(direccion == ARRIBA){
            if(comido)
                temp=ojosU.getImage();
            else
                if(comestible)
                    temp=weakCoco.getImage();
                else
                        temp=ghostU.getImage();
            if(faseMovimiento == 0){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-(alto_casilla)/4, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 1){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4-alto_casilla/10, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 2){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4-alto_casilla*2/10, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 3){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4-alto_casilla*3/10, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 4){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4-alto_casilla*4/10, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 5){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4-alto_casilla*5/10, ancho_casilla*3/2,alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 6){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4-alto_casilla*6/10, ancho_casilla*3/2,alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 7){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4-alto_casilla*7/10, ancho_casilla*3/2,alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 8){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4-alto_casilla*8/10, ancho_casilla*3/2,alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 9){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4-alto_casilla*9/10, ancho_casilla*3/2,alto_casilla*3/2, null);
            }
            }
        else if(direccion == ABAJO){
            if(comido)
                temp=ojosD.getImage();
            else
                if(comestible)
                    temp=weakCoco.getImage();
                else
                    temp=ghostD.getImage();
            if(faseMovimiento == 0){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 1){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4+alto_casilla/10, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 2){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4+alto_casilla*2/10, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 3){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4+alto_casilla*3/10, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 4){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4+alto_casilla*4/10, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 5){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4+alto_casilla*5/10, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 6){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4+alto_casilla*6/10, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 7){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4+alto_casilla*7/10, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 8){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4+alto_casilla*8/10, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
            else if(faseMovimiento == 9){
                g.drawImage(temp, x*ancho_casilla-(ancho_casilla)/4, y*alto_casilla-alto_casilla/4+alto_casilla*9/10, ancho_casilla*3/2, alto_casilla*3/2, null);
            }
        }
       }


        public void stopCoco(){
            t.suspend();
        }


        public void reanudarCoco(){
            t.resume();
        }


        public void coco_va_a_salir(){
        coco_quiere_salir=true;
    }


        public void comido(){
            delay=10;
            comido=true;
                    comestible=false;
        }

        public void run(){
            
            while(true)
                {
                    try {
                        Thread.sleep(delay);
                        } catch (InterruptedException ex) {
                Logger.getLogger(Laberinto.class.getName()).log(Level.SEVERE, null, ex);
            }
            mover();
            if(x==pacman.getX()  &&  y==pacman.getY())
                if(comestible)
                    {
                    
                    delay=9;
                    if(!comido)
                        {
                        comido=true;
                        fantasmasMuertos++;
                        nuevoFantasmaComido=true;
                    }
                    
                    }
                else{
                    if(!comido)
                        {
                    pacman_muerto=true;
                    Laberinto.this.repaint();
                    }
                    
                    }

   }

}


}

}
