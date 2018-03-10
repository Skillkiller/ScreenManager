package util;

/**
 * Created by Skillkiller on 10.03.2018.
 */
public enum ConsoleColors {

    //TODO Weiter Farben hinzufügen - https://stackoverflow.com/a/45444716

    RESET("\033[0m"),

    BLACK("\033[0;30m"),
    RED("\033[0;31m"),
    GREEN("\033[0;32m"),
    YELLOW("\033[0;33m"),
    BLUE("\033[0;34m"),
    PURPLE("\033[0;35m"),
    CYAN("\033[0;36m"),
    WHITE("\033[0;37m");


    private String code;

    ConsoleColors(String code) {
        this.code = code;
    }

    public String print(String message) {
        return code + message + "\033[0m";
    }
}
