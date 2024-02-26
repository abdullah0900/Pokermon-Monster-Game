import java.util.*;
import java.awt.Font;
import java.io.File;
import java.io.IOException;


public class aaaMain {
    
    public static void main(String avg[]) 
        throws IOException
    { 
        //loading game
        
        DataClass data = new DataClass();
        data.initBasePokemon();
        data.initBaseAbility();
        data.initBaseMove();
        data.initNonVolatileStatus();
        data.merge();
        ArrayList<ActivePokemon> team1List = data.teambuilder(1);
        ArrayList<ActivePokemon> team2List = data.teambuilder(2);
        
        Game game = new Game(team1List,team2List,data);
        int winner = game.runGame();
        
        System.out.println("Team " + winner + " wins!");
    }
}