/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comecocos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Santi
 */
public class PanelPuntuaciones extends JPanel{
    private int ancho = 0;
    private int alto = 0;
    private int tamFuente = 36;
    private Font mifuente = new Font("Comic Sans MS", Font.BOLD+Font.ITALIC, tamFuente);
    private int puntuacion = 0;
    private int vidas = 3;
    private Graphics2D g2d = null;
    private ImageIcon pacmanLab = new ImageIcon(getClass().getResource("resources/pacmanLab.gif"));;

    public PanelPuntuaciones(){
        ancho = getWidth();
        alto = getHeight();
        addComponentListener(new ManejadorComponente());
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setFont(mifuente);
        g2d.drawString("Score ",10,getHeight()/2);
        g2d.setColor(Color.YELLOW);
        g2d.drawString(""+puntuacion+"",10+(3*mifuente.getSize2D()),getHeight()/2);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Lives ",getWidth()/2-20,getHeight()/2);
        for(int i=0; i<vidas; i++)
            g2d.drawImage(pacmanLab.getImage(), (int) (getWidth()/2-20 + (3 * mifuente.getSize2D())) + i*(getHeight()/2)+ i*10,getHeight()/13,getHeight()/2,getHeight()/2,null);
    }

    public void setPuntuacion(int puntuacion){
        this.puntuacion = puntuacion;
        repaint();
    }

    public void setVidas(int vidas){
        this.vidas = vidas%10;  //el maximo de vidas permitido es 10
        repaint();
    }

    class ManejadorComponente extends ComponentAdapter{
        @Override
        public void componentResized(ComponentEvent e) {
            float ancho = (e.getComponent().getWidth()/28);
            float alto = (e.getComponent().getHeight()/31);
            int tamano = (int) (alto + 0.5);

            if(ancho < alto)
                tamano = (int) (ancho + 0.5);

            tamFuente = (int) (e.getComponent().getHeight() / 2.0);
            mifuente = new Font("Comic Sans MS", Font.BOLD+Font.ITALIC, tamFuente);
        }
    }
}
