package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {
    public static  Pattern pattern = Pattern.compile("^(\\{{2})([a-zA-Z]+)(\\}{2})$");

    public static boolean checkIsPattern(String testDataField) {
        System.out.println("TestDataField:" + testDataField);
        System.out.println(pattern.matcher(testDataField).matches());
        return pattern.matcher(testDataField).matches();
    }

    public static String findTestDataField(String testDataField) {
        Matcher matcher = pattern.matcher(testDataField);
        if (matcher.matches()) {
            return matcher.group(2);
        }
        return testDataField;
    }

}
