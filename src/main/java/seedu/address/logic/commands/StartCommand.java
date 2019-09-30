package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class StartCommand extends Command {

    public static final String COMMAND_WORD = "start";
    public static final String MESSAGE_SUCCESS = "Game has started!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        // TODO stuff
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
