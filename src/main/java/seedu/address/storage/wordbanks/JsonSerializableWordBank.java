package seedu.address.storage.wordbanks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.card.Card;
import seedu.address.model.wordbank.ReadOnlyWordBank;
import seedu.address.model.wordbank.WordBank;

/**
 * An Immutable Word Bank that is serializable to JSON format.
 */
@JsonRootName(value = "wordBank")
class JsonSerializableWordBank {

    public static final String MESSAGE_DUPLICATE_CARDS_IN_WORD_BANK = "Word bank contains duplicate card(s).";

    private final List<JsonAdaptedCard> wordBank = new ArrayList<>();

    private String name;

    /**
     * Constructs a {@code JsonSerializableWordBank} with the given word bank.
     */
    @JsonCreator
    public JsonSerializableWordBank(@JsonProperty("wordBank") List<JsonAdaptedCard> card) {
        this.wordBank.addAll(card);
    }

    /**
     * Converts a given {@code ReadOnlyWordBank} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableWordBank}.
     */
    public JsonSerializableWordBank(ReadOnlyWordBank source) {
        this.name = source.getName();
        wordBank.addAll(source.getCardList().stream().map(JsonAdaptedCard::new).collect(Collectors.toList()));
    }

    /**
     * Converts this word bank into the model's {@code WordBank} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public WordBank toModelType() throws IllegalValueException {
        WordBank wordBank = new WordBank(name);
        for (JsonAdaptedCard jsonAdaptedCard : this.wordBank) {
            Card card = jsonAdaptedCard.toModelType();
            if (wordBank.hasCard(card)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_CARDS_IN_WORD_BANK);
            }
            wordBank.addCard(card);
        }
        return wordBank;
    }

    public WordBank toModelTypeWithName(String name) throws IllegalValueException {
        this.name = name;
        return toModelType();
    }

    public String getName() {
        return name;
    }
}
