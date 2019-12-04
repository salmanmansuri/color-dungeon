/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        
        Room yellow, red, orange, yellow2, blue, green, red2, blue2, violet, red3, GreenSmall,
             purple, white, black, grey, spawn, dungeon;
      
        // create the rooms
        yellow = new Room("in the Yellow Room");
        red = new Room("in the Red Room");
        orange = new Room("in the Orange Room");
        yellow2 = new Room("in Another Yellow Room");
        blue = new Room("in the Blue Room");
        green = new Room("in the Green Room");
        red2 = new Room("in Another Red Room");
        blue2 = new Room("in Another Blue Room");
        violet = new Room("in the Violet Room");
        red3 = new Room("in Another Red Room");
        GreenSmall = new Room("in the Small Green Room");
        purple = new Room("in the Purple Room");
        white = new Room("in the White Room");
        black = new Room("in the Black Room"); 
        grey = new Room("in the Grey Room");
        spawn = new Room("in the Spawn Room");
        dungeon = new Room("in the Dungeon");
        
        // initialise room exits
        yellow.setExit("South-East", orange);
        
        red.setExit("South-West", orange);
        
        orange.setExit("East", green);
        orange.setExit("South", grey);
        orange.setExit("South-East", spawn);
        orange.setExit("North-East", red);
        orange.setExit("North-West", yellow);
        
        yellow2.setExit("South-East", green);
        
        blue.setExit("South-West", green);
        
        green.setExit("South", violet);
        green.setExit("West", orange);
        green.setExit("South-West", spawn);
        green.setExit("North-West", yellow2);
        
        red2.setExit("South-West", violet);
        
        blue2.setExit("North-West", violet);
        
        violet.setExit("North",green);
        violet.setExit("North-West",spawn);
        violet.setExit("South-West",purple);
        violet.setExit("North-East", red2);
        violet.setExit("South-East",blue2);
     
        red3.setExit("North-East", purple);
        GreenSmall.setExit("North-West", purple);
        
        purple.setExit("North-East",violet);
        purple.setExit("North",spawn);
        purple.setExit("North West",grey);
        purple.setExit("South-East", GreenSmall);
        purple.setExit("South-West", red3); 
        
        white.setExit("South-East", grey);
        black.setExit("North-East", grey);
        
        grey.setExit("North",orange);
        grey.setExit("North-East",spawn);
        grey.setExit("South-East",purple);
        grey.setExit("South-West", black);
        grey.setExit("North-West", white);
        
        spawn.setExit("North-East", green);
        spawn.setExit("South-East", violet);
        spawn.setExit("South", purple);
        spawn.setExit("South-West", grey);
        spawn.setExit("North-West", orange);
        spawn.setExit("North", dungeon);
        
        dungeon.setExit("South", spawn);

        currentRoom = spawn;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
