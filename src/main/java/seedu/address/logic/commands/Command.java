package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    /*
    Step 13.
    Extends to Step 14 in StartCommand.java

    Modify commands to take in a game object.
     */

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException;

    public abstract boolean check(Model model) throws CommandException;

    /**
     * Checks if preconditions satisfy for excecuting command
     * @param model
     * @return boolean true if satisfied
     */
    public boolean precondition(Model model) {
        return true;
    }

    public boolean postcondition() {
        return true;
    }


}
