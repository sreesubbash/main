package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Clears the address book.
 */
public class StartCommand extends Command {

    public static final String COMMAND_WORD = "start";
    public static final String MESSAGE_SUCCESS = "Game has started!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        // Assign game timer to model
        Timer timer = new java.util.Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.err.println("#!!#!#!: timer prints");
                // Call game model to end
            }
        };

        timer.schedule(task,5000);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
