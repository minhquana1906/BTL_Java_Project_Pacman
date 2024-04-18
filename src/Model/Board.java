package Model;

import java.awt.*;
import java.util.Timer;

public class Board {
    //Define board's stats

    private Dimension dimension;
    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;

    public Dimension getDimension() {
        return dimension;
    }

    public int getBLOCK_SIZE() {
        return BLOCK_SIZE;
    }

    public int getN_BLOCKS() {
        return N_BLOCKS;
    }

    public int getSCREEN_SIZE() {
        return SCREEN_SIZE;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }
}
