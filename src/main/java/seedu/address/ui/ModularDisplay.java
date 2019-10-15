package seedu.address.ui;

import javafx.scene.layout.StackPane;
import seedu.address.gamemanager.GameManager;
import seedu.address.statistics.GameStatistics;
import seedu.address.ui.modules.GameResultPanel;
import seedu.address.ui.modules.CardListPanel;
import seedu.address.ui.modules.LoadBankPanel;
import seedu.address.ui.modules.TitleScreenPanel;

/**
 * Displays the screen for Dukemon.
 */
public class ModularDisplay {

    //private final LoadBankPanel loadBankPanel;
    private CardListPanel cardListPanel;
    private final LoadBankPanel loadBankPanel;
    private final TitleScreenPanel titleScreenPanel;
    private final GameManager gameManager;

    /**
     * Changes the screen.
     *
     * @param gameManager GameManager who will render lists.
     */
    public ModularDisplay(GameManager gameManager) {
        loadBankPanel = new LoadBankPanel(gameManager.getFilteredWordBankList());
        cardListPanel = new CardListPanel(gameManager.getFilteredPersonList());
        titleScreenPanel = new TitleScreenPanel();
        this.gameManager = gameManager;
    }

    /**
     * Initially displays title.
     *
     * @param paneToDisplay The view to change.
     */
    public void displayTitle(StackPane paneToDisplay) {
        paneToDisplay.getChildren().add(titleScreenPanel.getRoot());
    }

    /**
     * Changes back to home display.
     *
     * @param paneToDisplay The view to change.
     */
    public void swapToHome(StackPane paneToDisplay) {
        paneToDisplay.getChildren().clear();
        paneToDisplay.getChildren().add(titleScreenPanel.getRoot());
    }

    /**
     * Changes to the word list.
     *
     * @param paneToDisplay The view to change.
     */
    public void swapToList(StackPane paneToDisplay) {
        paneToDisplay.getChildren().clear();
        cardListPanel = new CardListPanel(gameManager.getFilteredPersonList());
        paneToDisplay.getChildren().add(cardListPanel.getRoot());
    }

    /**
     * Changes to the game result.
     *
     * @param paneToDisplay The view to change.
     * @param gameStatistics The statistics to be shown in the game result panel.
     */
    public void swapToGameResult(StackPane paneToDisplay, GameStatistics gameStatistics) {
        paneToDisplay.getChildren().clear();
        paneToDisplay.getChildren().add(new GameResultPanel(gameStatistics).getRoot());
    }

    /**
     * Changes to list the word banks.
     *
     * @param paneToDisplay The view to change.
     */
    public void swapToBanks(StackPane paneToDisplay) {
        paneToDisplay.getChildren().clear();
        paneToDisplay.getChildren().add(loadBankPanel.getRoot());
    }

}
