package Controller;

import Model.Game;
import Model.Pacman;
//
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//
//public class TAdapter extends KeyAdapter{
//
//    @Override
//    public void keyPressed(KeyEvent e){
//        int key = e.getKeyCode();
//        if(key == KeyEvent.VK_LEFT){
//            game.setReq_dx(-1);
//            game.setReq_dy(0);
//        } else if (key == KeyEvent.VK_RIGHT) {
//            game.setReq_dx(1);
//            game.setReq_dy(0);
//        } else if (key == KeyEvent.VK_UP) {
//            game.setReq_dx(0);
//            game.setReq_dy(-1);
//        } else if (key == KeyEvent.VK_DOWN) {
//            game.setReq_dx(0);
//            game.setReq_dy(1);
//        } else if (key == KeyEvent.VK_ESCAPE && game.getTimer().isRunning()) {
//            game.setInGame(false);
//        } else if (key == KeyEvent.VK_SPACE) {
//            game.setInGame(true);
//            game.initGame();
//        }
//    }
//}
