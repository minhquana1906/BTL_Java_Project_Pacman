package View;

import Model.Board;
import Model.Game;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public Main() {
//        initUI();
        add(new Game());
    }

    private void initUI(){
        Game game = new Game();
        add(game);
        setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(380, 420);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.setVisible(true);
        main.setTitle("Pacman");
        main.setSize(380,420);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setLocationRelativeTo(null);
    }
}
