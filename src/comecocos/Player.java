/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comecocos;

import java.io.File;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

/**
 *
 *
 *@author Javi
 */public class Player{


    private BasicPlayer player;

    Player(){
        player = new BasicPlayer();
    }


    Player(String file) throws BasicPlayerException{
        player = new BasicPlayer();
        player.open(new File(file));
    }


    public void Play() throws Exception {
  player.play();
}

    public void AbrirFichero(String ruta) throws Exception {
  player.open(new File(ruta));
}

    public void Pausa() throws Exception {
  player.pause();
}

    public void Continuar() throws Exception {
  player.resume();
}

    public void Stop() throws Exception {
  player.stop();
}



}

