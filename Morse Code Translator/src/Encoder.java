public class Encoder {

    //array that contains the needed letters and special characters
    private static final char[] romanAlphabet = {'a', 'b', 'c', 'd', 'e',
                                    'f', 'g', 'h', 'i', 'j',
                                    'k', 'l', 'm', 'n', 'o',
                                    'p', 'q', 'r', 's', 't',
                                    'u', 'v', 'w', 'x', 'y',
                                    'z', '1', '2', '3', '4',
                                    '5', '6', '7', '8', '9',
                                    '0', '.', ',', ';', ':'};

    //array that contains the needed morse code equivalents
    private static final String[] morseCode = {".-", "-...", "-.-.", "-..", ".",
                                "..-.", "--.", "....", "..", ".---",
                                "-.-", ".-..", "--", "-.", "---",
                                ".--.", "--.-", ".-.", "...", "-",
                                "..-", "...-", ".--", "-..-", "-.--",
                                "--..", ".----", "..---", "...--", "....-",
                                ".....", "-....", "--...", "---..", "----.",
                                "-----", ".-.-.-", "--..--", "-.-.-.", "---..."};

    /**
     * this function takes in a word and returns the morse code equivalent
     * uses a string builder to assemble the needed string
     */
    public static StringBuilder translateToMorse(String inputStr){
        inputStr = inputStr.toLowerCase();

        StringBuilder outputStr = new StringBuilder(); // string builder to make the word

        for(int x = 0; x < inputStr.length(); x++){
            for(int y = 0; y < romanAlphabet.length; y++){
                if(inputStr.charAt(x) == romanAlphabet[y]){
                    outputStr.append(morseCode[y]).append(" "); // if a match is found the morse is added with a space
                    break;
                }
            }
        }

        return outputStr; // returns string builder
    }

    /**
     *  this function takes in a morse code snippet and returns what word it is
     *  uses a string builder to assemble the needed string
     */
    public static StringBuilder translateToLetters(String inputCode){
        StringBuilder outputStr = new StringBuilder();

        String[] splitMorseCode = inputCode.split(" "); // split word up into letters

        for (int x = 0; x < splitMorseCode.length; x++) {
            for (int y = 0; y < morseCode.length; y++) {
                if(splitMorseCode[x].compareTo(morseCode[y]) == 0){
                    outputStr.append(romanAlphabet[y]); // if a match is found the letter is added with a space
                    break;
                }
            }
        }

        return outputStr;
    }

}
