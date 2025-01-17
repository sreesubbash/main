package seedu.address.logic.commands.cardcommands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;

/**
 * Empties the word bank.
 */
public class ClearCommand extends CardCommand {

    public static final String COMMAND_WORD = "clear";
    private static final String MESSAGE_SUCCESS = "Word bank has been cleared!";
    public static final String MESSAGE_USAGE = COMMAND_WORD + " \n";

    /**
     * Creates a ClearCommand.
     */
    public ClearCommand() {
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.clearWordBank();
        model.clearWordBankStatistics();
        return new CardCommandResult(MESSAGE_SUCCESS);
    }
}
