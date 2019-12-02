/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallogic;

import java.util.*;

/**
 *
 * @author max
 */
public class RegularExpression {

    private boolean notComplete;
    private ArrayList<RegularExpression> parts;
    private int operation;
    private String symbol;
    private String[] alphabet;
    private int ID;

    /**
     * Only ID, doesn't parse
     * @param ID ID
     */
    public RegularExpression(int ID) {
        this.ID = ID;
        notComplete = false;
        parts = new ArrayList();
        symbol = null;
    }
    
    /**
     * For symbols
     * @param ID ID
     * @param symbol Symbol rep
     */
    protected RegularExpression(int ID, String symbol) {
        this.ID = ID;
        this.symbol = symbol;
        operation = NONE;
    }

    /**
     * Upper constructor, parses, final RE
     */
    public RegularExpression(int ID, String parse, String[] alphabet) {
        this.ID = ID;
        notComplete = false;
        parts = new ArrayList();
        this.alphabet = alphabet;
        parse(parse);
    }

    /**
     * Medium constructor
     */
    public RegularExpression(boolean complete, int ID, String parse, String[] alphabet) {
        this.notComplete = complete;
        this.ID = ID;
        parts = new ArrayList();
        this.alphabet = alphabet;
        parse(parse);
    }

    /**
     * For star expressions
     */
    public RegularExpression(int ID, RegularExpression intern, boolean complete, String[] alphabet) {
        this.ID = ID;
        this.parts = new ArrayList<>();
        parts.add(intern);
        operation = STAR;
        notComplete = complete;
        this.alphabet = alphabet;
    }

    private void parse(String regex) {
        String res = regex.replaceAll(" ", "");
        int stackDepth = 0;
        ArrayList<String> regexes = new ArrayList<>();
        int startIndex = 0; //Start index for the substring that will generate a regex string
        for (int i = 0; i < res.length(); i++) {
            char c = res.charAt(i);
            if (c == '(') {
                stackDepth++;
            } else if (c == ')') {
                stackDepth--;
            } else if (c == '+' && stackDepth == 0) {
                regexes.add(res.substring(startIndex, i));
                startIndex = i + 1;
            }
        }
        regexes.add(res.substring(startIndex, res.length())); //Last sequence
        //Now we got the unions, if there are more than one, construct them
        if (regexes.size() > 1) {
            this.operation = SUM;
            for (String s : regexes) {
                parts.add(new RegularExpression(true, 0, s, alphabet));
            }
            if (!notComplete) {
                System.out.println(parts.toString());
            }
            return;
        }

        //Now, the concats
        //This is the worst part I think
        //The only time this is not a concat is: there is only one symbol and 0-1 stars
        regexes.clear();
        stackDepth = 0;
        startIndex = 0; //Start index for the substring that will generate a regex string
        int indexParenthesis, indexStar, lastIndex = -1;
        while (!res.equals("")) {
            indexParenthesis = res.indexOf('(');
            indexStar = res.indexOf('*');
            if (indexParenthesis == -1 && indexStar == -1) {
                lastIndex = res.length();
            } else if (indexParenthesis == -1) {
                lastIndex = indexStar;
            } else if (indexStar == -1) {
                lastIndex = indexParenthesis;
            } else {
                lastIndex = Math.min(indexParenthesis, indexStar);
            }
            String sub = res.substring(startIndex, lastIndex);
            if (!sub.equals("")) {  //There is a regex to parse
                if (lastIndex < res.length() && lastIndex == indexStar) {   //If that regex has a star
                    lastIndex++;    //Count that star in the regex, advance lastIndex so that it counts the whole regex
                    sub = res.substring(startIndex, lastIndex);
                }
                regexes.add(sub);
            }
            if (lastIndex < res.length() && lastIndex == indexParenthesis) { //If the next token is a parenthesis
                //Parse the parenthesis right away
                //Put the strings in regexes and advance lastIndex
                lastIndex = lastIndex + parseParenthesis(regexes, res.substring(lastIndex));
            }
            startIndex = lastIndex;
            res = res.substring(startIndex);
        }
        //We got the tokens
        if (regexes.size() == 1) {
            String only = regexes.get(0);
            if (only.charAt(only.length() - 1) == '*') {
                operation = STAR;
                //Remove parenthesis and star
                parts.add(new RegularExpression(true, 0, only.substring(1, only.length() - 2), alphabet));
                //End
            } else {
                if (only.charAt(0) == '(' && only.charAt(only.length() - 1) == ')') {
                    //We can ignore the parentheses and start again without them
                    parse(only.substring(1, only.length() - 1));
                } else {
                    //We'll see if this is only a symbol
                    ArrayList<String> symb = parseSymbols(only);
                    if (symb.size() == 1) {
                        operation = NONE;
                        symbol = symb.get(0);
                    } else {
                        operation = PRODUCT;
                        addAll(symb);
                        //End
                    }
                }
            }
        } else {
            operation = PRODUCT;
            for (String s : regexes) {
                parts.add(new RegularExpression(true, 0, s, alphabet));
            }
        }
        
    }

    public static int parseParenthesis(Collection<String> container, String regex) {
        int stackDepth = 0;
        int startIndex = 0;
        int i = 0;
        boolean star = false;
        while (i < regex.length() && regex.charAt(startIndex) == '(') {
            i = startIndex + 1;
            stackDepth++;
            while (stackDepth > 0) {
                if (regex.charAt(i) == '(') {
                    stackDepth++;
                } else if (regex.charAt(i) == ')') {
                    stackDepth--;
                }
                i++;
            }
            if (i < regex.length() && regex.charAt(i) == '*') {
                i++;
                star = true;
            } else {
                star = false;
            }
            container.add(regex.substring(star ? startIndex : startIndex + 1, star ? i : i - 1));
            startIndex = i;
        }
        return i;
    }

    /**
     * Precondition: no strange characters, only symbols
     *
     * @param regex string to parse
     * @return
     */
    private ArrayList<String> parseSymbols(String regex) {
        ArrayList<String> res = new ArrayList<>(regex.length()); //Good estimation of the number of symbols
        String temp = regex;
        while (!temp.equals("")) {
            int i = 0;
            while (i < alphabet.length && !temp.startsWith(alphabet[i])) {
                i++;
            }
            if (i == alphabet.length) {
                throw new InputMismatchException();
            } else {
                res.add(alphabet[i]);
                temp = temp.substring(alphabet[i].length());
            }
        }
        return res;
    }

    private void addAll(ArrayList<String> symbs) {
        for (String s : symbs) {
            parts.add(new RegularExpression(0, s));
        }
    }
    
    
    public int getID() {
        return ID;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RegularExpression && ((RegularExpression) o).ID == ID;
    }

    @Override
    public String toString() {
        String res = "";
        switch (operation) {
            case NONE:
                return symbol;
            case SUM:
                for (int i = 0; i < parts.size() - 1; i++) {
                    res += parts.get(i).toString() + " + ";
                }
                if (!parts.isEmpty()) {
                    res += parts.get(parts.size() - 1);
                }
                return res;
            case PRODUCT:
                RegularExpression er;
                for (int i = 0; i < parts.size() - 1; i++) {
                    er = parts.get(i);
                    if (er.operation != NONE) {
                        res += String.format("(%s).", er.toString());
                    } else {
                        res += String.format("%s.", er.toString());
                    }
                }
                if (!parts.isEmpty()) {
                    er = parts.get(parts.size() - 1);
                    if (er.operation != NONE) {
                        res += String.format("(%s)", er.toString());
                    } else {
                        res += er.toString();
                    }
                }
                return res;
            case STAR:
                res += "(" + parts.get(0) + ")*";
                return res;
        }
        return res;
    }

    //Constants for operations
    public static final int NONE = 0;
    public static final int SUM = 1;
    public static final int PRODUCT = 2;
    public static final int STAR = 3;
    
    public boolean isSymbol() {
        return this.operation == NONE;
    }
    
    public String getRep() {
        return symbol;
    }
}

/*
First approach to regex parser
String res = regex.trim();
        while (!(res.equals(""))) {
            if (res.startsWith("(")) {
                if (!parts.isEmpty() && (operation == NONE) ){
                    operation = PRODUCT;
                }
                int lastIndex = res.lastIndexOf(")");
                if (lastIndex == -1) {
                    System.err.println("Bad Regex syntax: No closing parenthesis");
                    throw new InputMismatchException();
                }
                String parenthesis = res.substring(1, lastIndex);   //Get contents within the parenthesis
                res = res.substring(lastIndex + 1).trim();                 //res now is only the remaining part
                if (res.equals("")) {
                    parse(parenthesis);
                } else if (res.startsWith("*")) {
                    RegularExpression op = new RegularExpression(null, new RegularExpression(true, null, parenthesis, alphabet), true, alphabet);
                    parts.add(op);
                    res = res.substring(1).trim();
                } else {
                    RegularExpression op = new RegularExpression(false, null, parenthesis, alphabet);
                    parts.add(op);
                }
            } else if (res.startsWith("+")) {
                res = res.substring(1).trim();
                operation = SUM;
            } else {
                int i = 0;
                while (i < alphabet.length && !res.startsWith(alphabet[i].getRep())) {
                    i++;
                }
                if (i == alphabet.length) {
                    System.err.println("Cannot identify symbol");
                    throw new InputMismatchException();
                }
                res = res.substring(alphabet[i].getRep().length()).trim();
                if (res.startsWith("*")) {
                    RegularExpression op = new RegularExpression(null, alphabet[i], true, alphabet);
                    parts.add(op);
                    res = res.substring(1).trim();
                }
                parts.add(alphabet[i]);
            }
        }

 */
 /*

 */
