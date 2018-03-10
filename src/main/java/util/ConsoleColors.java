package util;

/**
 * Created by Skillkiller on 10.03.2018.
 */
public enum ConsoleColors {

    BLACK("0"),
    RED("1"),
    GREEN("2"),
    YELLOW("3"),
    BLUE("4"),
    PURPLE("5"),
    CYAN("6"),
    WHITE("7");


    private String code;
    private static String resetCode = "\033[0m";

    ConsoleColors(String code) {
        this.code = code;
    }

    //Normale Farbe
    public String print(String message) {
        return String.format("\033[0;3%sm%s%s", code, message, resetCode);
    }

    //Bold Farbe
    public String bold(String message) {
        return String.format("\033[1;3%sm%s%s", code, message, resetCode);
    }

    //Underline Farbe
    public String underline(String message) {
        return String.format("\033[4;3%sm%s%s", code, message, resetCode);
    }

    //Background Farbe
    public String background(String message) {
        return String.format("\033[4%sm%s%s", code, message, resetCode);
    }

    //High Intensity Farbe
    public String highIntensity(String message) {
        return String.format("\033[0;9%sm%s%s", code, message, resetCode);
    }

    //Bold High Intensity
    public String boldHighIntensity(String message) {
        return String.format("\033[1;9%sm%s%s", code, message, resetCode);
    }

    //High Intensity backgrounds
    public String highIntensityBackground(String message) {
        return String.format("\033[0;10%sm%s%s", code, message, resetCode);
    }
}
