package colors;

import java.util.*;

public class ColorList {
    private HashMap<String, String> colorList = new HashMap<>();
    public ColorList() {
        // Reset
        colorList.put("RESET", "\033[0m"); // Text Reset

        // Regular Colors
        colorList.put("BLACK", "\033[0;30m");  // BLACK
        colorList.put("RED", "\033[0;31m");    // RED
        colorList.put("GREEN", "\033[0;32m");  // GREEN
        colorList.put("YELLOW", "\033[0;33m"); // YELLOW
        colorList.put("BLUE", "\033[0;34m");   // BLUE
        colorList.put("PURPLE", "\033[0;35m"); // PURPLE
        colorList.put("CYAN", "\033[0;36m");   // CYAN
        colorList.put("WHITE", "\033[0;37m");  // WHITE

        // Bold
        colorList.put("BLACK:B", "\033[1;30m"); // BLACK
        colorList.put("RED:B", "\033[1;31m");   // RED
        colorList.put("GREEN:B", "\033[1;32m"); // GREEN
        colorList.put("YELLOW:B", "\033[1;33m");// YELLOW
        colorList.put("BLUE:B", "\033[1;34m");  // BLUE
        colorList.put("PURPLE:B", "\033[1;35m");// PURPLE
        colorList.put("CYAN:B", "\033[1;36m");  // CYAN
        colorList.put("WHITE:B", "\033[1;37m"); // WHITE

        // Underline
        colorList.put("BLACK:U", "\033[4;30m"); // BLACK
        colorList.put("RED:U", "\033[4;31m");   // RED
        colorList.put("GREEN:U", "\033[4;32m"); // GREEN
        colorList.put("YELLOW:U", "\033[4;33m");// YELLOW
        colorList.put("BLUE:U", "\033[4;34m");  // BLUE
        colorList.put("PURPLE:U", "\033[4;35m");// PURPLE
        colorList.put("CYAN:U", "\033[4;36m");  // CYAN
        colorList.put("WHITE:U", "\033[4;37m"); // WHITE

        // Background
        colorList.put("BLACK:BG", "\033[40m"); // BLACK
        colorList.put("RED:BG", "\033[41m");   // RED
        colorList.put("GREEN:BG", "\033[42m"); // GREEN
        colorList.put("YELLOW:BG", "\033[43m");// YELLOW
        colorList.put("BLUE:BG", "\033[44m");  // BLUE
        colorList.put("PURPLE:BG", "\033[45m");// PURPLE
        colorList.put("CYAN:BG", "\033[46m");  // CYAN
        colorList.put("WHITE:BG", "\033[47m"); // WHITE

        // High Intensity
        colorList.put("BLACK:BR", "\033[0;90m"); // BLACK
        colorList.put("RED:BR", "\033[0;91m");   // RED
        colorList.put("GREEN:BR", "\033[0;92m"); // GREEN
        colorList.put("YELLOW:BR", "\033[0;93m");// YELLOW
        colorList.put("BLUE:BR", "\033[0;94m");  // BLUE
        colorList.put("PURPLE:BR", "\033[0;95m");// PURPLE
        colorList.put("CYAN:BR", "\033[0;96m");  // CYAN
        colorList.put("WHITE:BR", "\033[0;97m"); // WHITE

        // Bold High Intensity
        colorList.put("BLACK:B:BR", "\033[1;90m");// BLACK
        colorList.put("RED:B:BR", "\033[1;91m");  // RED
        colorList.put("GREEN:B:BR", "\033[1;92m"); // GREEN
        colorList.put("YELLOW:B:BR", "\033[1;93m");// YELLOW
        colorList.put("BLUE:B:BR", "\033[1;94m"); // BLUE
        colorList.put("PURPLE:B:BR", "\033[1;95m");// PURPLE
        colorList.put("CYAN:B:BR", "\033[1;96m"); // CYAN
        colorList.put("WHITE:B:BR", "\033[1;97m");// WHITE

        // High Intensity backgrounds
        colorList.put("BLACK:BG:BR", "\033[0;100m");// BLACK
        colorList.put("RED:BG:BR", "\033[0;101m");// RED
        colorList.put("GREEN:BG:BR", "\033[0;102m");// GREEN
        colorList.put("YELLOW:BG:BR", "\033[0;103m");// YELLOW
        colorList.put("BLUE:BG:BR", "\033[0;104m");// BLUE
        colorList.put("PURPLE:BG:BR", "\033[0;105m");// PURPLE
        colorList.put("CYAN:BG:BR", "\033[0;106m"); // CYAN
        colorList.put("WHITE:BG:BR", "\033[0;107m");  // WHITE
    }

    public HashMap<String, String> getList() { return colorList; }
}
