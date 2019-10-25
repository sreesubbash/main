package seedu.address.storage.wordbanks;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.model.wordbank.ReadOnlyWordBank;
import seedu.address.model.wordbank.WordBank;
import seedu.address.model.wordbanklist.ReadOnlyWordBankList;
import seedu.address.model.wordbanklist.WordBankList;

/**
 * A class to access Dukemon word bank data stored as a json file on the hard disk.
 */
public class JsonWordBankListStorage implements WordBankListStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonWordBankListStorage.class);
    private ReadOnlyWordBankList readOnlyWordBankList;
    private Path wordBanksFilePath; // default : data/wordBanks

    /**
     * Storage that contains all information related to word banks.
     * It also initialises the folder and sample data if necessary.
     *
     * @param filePath of storage. By default, it is at data folder.
     */
    public JsonWordBankListStorage(Path filePath) {
        initData(filePath);
        initWordBankList();
    }

    /**
     * Creates the wordBanks folder if it has not been initialised.
     * By default, it is located at data/wordBanks
     * Also creates a sample.json file if there are no word banks when initialising.
     *
     * @param filePath of storage. By default, it is data.
     */
    private void initData(Path filePath) {
        wordBanksFilePath = Paths.get(filePath.toString(), "wordBanks");
        try {
            if (!filePath.toFile().exists()) {
                Files.createDirectory(filePath);
            }
            if (!wordBanksFilePath.toFile().exists()) {
                Files.createDirectory(wordBanksFilePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File wordBanksDirectory = wordBanksFilePath.toFile();
        String[] wordBanks = wordBanksDirectory.list();
        boolean haveSampleWordBank = false;
        for (int i = 0; i < wordBanks.length; i++) {
            if (wordBanks[i].equals(SampleDataUtil.getName())) {
                haveSampleWordBank = true;
                break;
            }
        }

        if (!haveSampleWordBank) {
            WordBank sampleWb = SampleDataUtil.getSampleWordBank();
            saveWordBank(sampleWb);
        }
    }

    /**
     * Initialise word bank list from the word banks directory on creation
     * All json files will initialise a word bank.
     */
    private void initWordBankList() {
        List<WordBank> wordBankList = new ArrayList<>();
        File wordBanksDirectory = wordBanksFilePath.toFile();
        String[] pathArray = wordBanksDirectory.list();

        for (int i = 0; i < pathArray.length; i++) {
            if (pathArray[i].endsWith(".json")) {
                Path wordBankPath = Paths.get(wordBanksFilePath.toString(), pathArray[i]);
                ReadOnlyWordBank readOnlyWordBank = jsonToWordBank(wordBankPath).get();
                WordBank wbToAdd = (WordBank) readOnlyWordBank;
                wordBankList.add(wbToAdd);
            }
        }
        this.readOnlyWordBankList = new WordBankList(wordBankList);
    }

    /**
     * Creates a word bank object from the specified .json file given as filePath.
     *
     * @param filePath location of the .json word bank file. Cannot be null.
     */
    private Optional<ReadOnlyWordBank> jsonToWordBank(Path filePath) {
        try {
            requireNonNull(filePath);
            Optional<JsonSerializableWordBank> jsonWordBank = JsonUtil.readJsonFile(
                    filePath, JsonSerializableWordBank.class);
            if (jsonWordBank.isEmpty()) {
                return Optional.empty();
            }
            String pathName = filePath.toString();
            int len = filePath.getParent().toString().length();
            String wordBankName = pathName.substring(len + 1, pathName.length() - ".json".length());
            return Optional.of(jsonWordBank.get().toModelType(wordBankName));

        } catch (IllegalValueException | DataConversionException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Save a word bank into the default file location.
     */
    private void saveWordBank(ReadOnlyWordBank wordBank) {
        saveWordBank(wordBank, wordBanksFilePath);
    }

    /**
     * Save a word bank into the specified file location.
     * Typically used by Export command, where user writes to their system.
     *
     * @param filePath location of the data. Cannot be null.
     */
    private void saveWordBank(ReadOnlyWordBank wordBank, Path filePath) {
        try {
            requireNonNull(wordBank);
            requireNonNull(filePath);
            Path wordBankFilePath = Paths.get(filePath.toString(), wordBank.getName() + ".json");
            FileUtil.createIfMissing(wordBankFilePath);
            JsonUtil.saveJsonFile(new JsonSerializableWordBank(wordBank), wordBankFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a word bank into the internal list.
     *
     * @param wordBank data. Cannot be null.
     */
    private void addWordBank(ReadOnlyWordBank wordBank) {
        WordBankList wbl = (WordBankList) readOnlyWordBankList;
        wbl.addBank(wordBank);
    }

    private void deleteWordBank(WordBank wordBank) {
        Path filePath = Paths.get(wordBanksFilePath.toString(), wordBank.getName() + ".json");
        File toDelete = filePath.toFile();
        if (toDelete.exists()) {
            toDelete.delete();
        }
        WordBankList wbl = ((WordBankList) readOnlyWordBankList);
        wbl.removeWordBank(wordBank);
    }

    /**
     * Retrieves the WordBankList that WordBankListStorage holds.
     */
    public Optional<ReadOnlyWordBankList> getWordBankList() {
        return Optional.of(readOnlyWordBankList);
    }

    /**
     * Creates a word bank, add into the internal list, and save it into storage.
     *
     * @param wordBankName cannot be null.
     */
    @Override
    public void createWordBank(String wordBankName) {
        WordBank wb = new WordBank(wordBankName);
        addWordBank(wb);
        saveWordBank(wb);
    }

    /**
     * Retrieves the specified word bank, delete from storage, and then remove from internal list.
     *
     * @param wordBankName cannot be null.
     */
    @Override
    public void removeWordBank(String wordBankName) {
        WordBank wb = readOnlyWordBankList.getWordBankFromName(wordBankName);
        deleteWordBank(wb);
    }

    /**
     * Creates the word bank specified by the file path, add to internal list, and then add to storage.
     *
     * @param filePath cannot be null.
     */
    @Override
    public void importWordBank(Path filePath) {
        WordBank wb = (WordBank) jsonToWordBank(filePath).get();
        addWordBank(wb);
        saveWordBank(wb);
    }

    /**
     * Retrieves the word bank, add to internal list, then add to storage.
     *
     * @param wordBankName cannot be null.
     * @param filePath cannot be null.
     */
    @Override
    public void exportWordBank(String wordBankName, Path filePath) {
        WordBank wb = readOnlyWordBankList.getWordBankFromName(wordBankName);
        saveWordBank(wb, filePath);
    }

    /**
     * Updates any changes to word banks that were manipulated in ModelManager.
     *
     * @param wordBank cannot be null.
     */
    @Override
    public void updateWordBank(WordBank wordBank) {
        saveWordBank(wordBank);
    }
}
