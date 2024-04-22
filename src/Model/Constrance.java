package Model;

public interface Constrance {
    //Define the size of the board
    int BLOCK_SIZE = 24;
    int N_BLOCKS = 15;
    int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    int SLEEP_TIME = 40;
    int MAX_GHOSTS = 12;
    int VALID_SPEEDS[] = {1, 2, 3, 4, 6, 8};
    int MAX_SPEED = 6;
}
