package whu.alumnispider.parser;


import whu.alumnispider.utilities.Person;
import whu.alumnispider.utilities.ReExpUtility;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordParser {
    private static Pattern WhuPattern = Pattern.compile(ReExpUtility.reWuhanUniversity);
    private static Pattern cvKeywordPattern = Pattern.compile(ReExpUtility.reKeywordinCV);
    private static Pattern cvPlaceBirthPattern = Pattern.compile(ReExpUtility.rePlaceBirth);
    private static Pattern cvDateBirthPattern = Pattern.compile((ReExpUtility.reDateBirth));
    private static Pattern cvNamePattern = Pattern.compile(ReExpUtility.reNameGenderSuffix);
    private static Pattern cvJobPositionPattern = Pattern.compile(ReExpUtility.reJobPosition);
    private static Pattern cvMultiJobPositionPattern = Pattern.compile(ReExpUtility.reUnionYear);

    public void extractor(String CV, Person person) {
        Matcher whuMatcher = WhuPattern.matcher(CV);

        if (whuMatcher.find()) {
            person.setGraduatedSchool("武汉大学");
        }
    }

    public void jovPositionExtractor(String CV, Person person) {
        Matcher jobPositionMatcher = cvJobPositionPattern.matcher(CV);
        int index = 0;
        String ret = "";

        while (jobPositionMatcher.find()) {
            index++;
            ret = jobPositionMatcher.group();
        }

        //noneChineseFilter(ret);
        if (index == 1) {
            person.setJobPosition(ret);
        } else {
            multiJobPositionExtractor(CV, person);
        }
    }

    // deal with occasion like "2017.02-2018.3  司法部部长、党组书记
    //                          2018.03-  最高人民检察院检察长"
    public void multiJobPositionExtractor(String CV, Person person) {
        String uselessTextA = "<span>";
        String uselessTextB = "</span>";
        CV = CV.replace(uselessTextA, "");
        CV = CV.replace(uselessTextB, "");

        String ret = "";
        Matcher multiJobPositionMatcher = cvMultiJobPositionPattern.matcher(CV);
        while (multiJobPositionMatcher.find()) {
            if (ret == "") {
                ret = multiJobPositionMatcher.group();
                //ret = noneChineseFilter(ret);
            } else {
                ret = ret + "," + multiJobPositionMatcher.group();
                //ret = noneChineseFilter(ret);
            }
        }

        person.setJobPosition(ret);
    }

    public void nameExtractor(String CV, Person person) {
        Matcher nameMatcher = cvNamePattern.matcher(CV);
        int index = 0;
        String ret = "";
        while (nameMatcher.find()) {
            index++;
            ret = nameMatcher.group();
        }

        //ret = noneChineseFilter(ret);
        if (index == 1) {
            person.setName(ret);
        }
    }

    public boolean isKeyWordExtracted(String htmlString) {
        Matcher placeBirthMatcher = cvPlaceBirthPattern.matcher(htmlString);
        Matcher whuMatcher = WhuPattern.matcher(htmlString);
        Matcher cvKeywordMatcher = cvKeywordPattern.matcher(htmlString);
        Matcher cvDateBirthMatcher = cvDateBirthPattern.matcher(htmlString);

        if (placeBirthMatcher.find() && whuMatcher.find() && cvKeywordMatcher.find() && cvDateBirthMatcher.find()) {
            return true;
        }

        return false;
    }

  /*  // delete the char in string which is not Chinese character.
    private String noneChineseFilter(String str) {
        List<int> negativeIndex = new ArrayList<int>();

        for (int i=0;i<str.length();i++) {
            if (str.charAt(i) <= 0x0391 || str.charAt(i) >=0xFFE5 || str.charAt(i) == '　') {
                negativeIndex = i;
            }
        }

        if (negativeIndex != -1) {
            str = str.substring(negativeIndex + 1);
        }

        return str;
    }*/
}
