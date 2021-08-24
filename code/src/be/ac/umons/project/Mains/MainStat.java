package be.ac.umons.project.Mains;

import be.ac.umons.project.Game.Game;
import be.ac.umons.project.Game.GameData;
import be.ac.umons.project.Players.AiPlayer;



public class MainStat {

    public static void main(String[] args) {

        try{
            int n = Integer.parseInt(args[0]);
            if(n<0){
                throw new ArithmeticException();
            }
            int guesses = 0;
            for(int i =0; i<n; i++) {
                //System.out.println("game number " + i);
                Game.getInstance().getInit().defaultGame();
                GameData game = Game.getInstance().getGameData();
                game.setAiPlaying(true);
                game.setAi(new AiPlayer(game));
                while(!Game.getInstance().isEnded()) {
                    int[] guess = game.getAi().makeGuess();
                    Object[] feedback = game.processGuess(guess[0], guess[1]);
                    game.getAi().processFeedback(feedback);
                }
                guesses+=Game.getInstance().getGameData().getGuessArray().size();
            }
            int moy = guesses/n;

            System.out.println("The game was played " + n + " time(s)");
            System.out.println("Total amount of guesses: " + guesses);
            System.out.println("Average amount of guesses per game: " + moy);

        }catch(NumberFormatException e){
            System.out.println("You need to enter a number, for example type :\njava be.ac.umons.project.Mains.MainStat 30");
        }catch (ArithmeticException e){
            System.out.println("This Ai wants to play at least 1 game");
        }
    }


}

