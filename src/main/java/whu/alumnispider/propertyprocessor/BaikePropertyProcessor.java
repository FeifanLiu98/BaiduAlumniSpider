package whu.alumnispider.propertyprocessor;

import whu.alumnispider.DAO.AlumniDAO;
import whu.alumnispider.parser.PropertyParser;
import whu.alumnispider.utilities.Province;
import whu.alumnispider.utilities.ReExpUtility;
import whu.alumnispider.utilities.University;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaikePropertyProcessor {
    private static AlumniDAO alumniDAO = new AlumniDAO();
    private static PropertyParser parser = new PropertyParser();
    private static Pattern birthYearPattern = Pattern.compile(ReExpUtility.reYear);
    private static List<String> mainContents = alumniDAO.readFromTable("alumnus_v2","main_content");
    private static List<String> briefIntros = alumniDAO.readFromTable("alumnus_v2", "brief_intro");
    private static List<String> websites = alumniDAO.readFromTable("alumnus_v2", "website");
    private static List<String> ids = alumniDAO.readFromTable("alumnus_v2", "id");
    private static List<String> jobs = alumniDAO.readFromTable("alumnus_v2", "job");
    private static List<String> universitiesAreas = alumniDAO.readFromTable("tb_school", "area_name");
    private static List<String> universitiesNames = alumniDAO.readFromTable("tb_school", "school_name");
    private static List<String> provinceIndexes = alumniDAO.readFromTable("provinces", "number");
    private static List<String> provinceNames = alumniDAO.readFromTable("provinces", "name");
    private static int listSize = mainContents.size();

    private static void levelProcessor() {
        List<String> levels = new ArrayList<String>();
        String level = null;

    }

    private static void locationProcessor() {
        List<String> locations = new ArrayList<String>();
        List<University> universities = new ArrayList<University>();
        List<Province> provinces = new ArrayList<Province>();
        String location = null;

        for (int i = 0;i < universitiesNames.size();i++) {
            University university = new University(universitiesAreas.get(i), universitiesNames.get(i));
            universities.add(university);
        }

        for (int i = 0;i < provinceNames.size();i++) {
            String tempName = provinceNames.get(i);
            tempName = tempName.trim();
            provinceNames.set(i, tempName);

            Province province = new Province(provinceIndexes.get(i), provinceNames.get(i));
            provinces.add(province);
        }

        for(int i = 0;i < listSize;i++) {
            location = locationPredictor(universities, provinces, jobs.get(i));

            if (location != null) {
               alumniDAO.update(location, "location", ids.get(i), "alumnus_v2");
            }
        }
    }

    private static String locationPredictor(List<University> universities, List<Province> provinces, String str) {
        String ret = null;
        if (str != null) {
            for (University university : universities) {
                Pattern pattern = Pattern.compile(university.name);
                Matcher matcher = pattern.matcher(str);

                if (matcher.find()) {
                    ret = university.province;
                    break;
                }
            }

            if (ret == null) {
                for (Province province : provinces) {
                    if (province.name != null) {
                        Pattern patternA = Pattern.compile(province.name);
                        //Pattern patternB = Pattern.compile(province.name.substring(0,2));
                        Matcher matcherA = patternA.matcher(str);
                        //Matcher matcherB = patternB.matcher(str);
                        String index = null;

                        if (matcherA.find()) {
                            index = province.index;
                            index = index.substring(0, 2);
                            ret = parser.locationParser(index);
                            break;
                        }
                    }
                }
            }

            return ret;
        }
        else {
            return null;
        }
    }
/*
    private static String locationFilter(String source) {
        int negativeIndex = -1;

        for (int i=0;i<source.length();i++) {
            if (source.charAt(i) <= 0x0391 || source.charAt(i) >=0xFFE5 || source.charAt(i) == ''|| source.charAt(i) == ' ') {
                negativeIndex = i;
            }
        }

        if (negativeIndex != -1) {
            source = source.substring(negativeIndex + 1);
        }

        return source;
    }*/

    // 0 represents alive, while 1 represents dead.
    private static void aliveProcessor() {
        int[] alives = new int[listSize];
        String[] passAwayKeywordCHN = {"逝世日期", "过世日期"};

        for (int i = 0;i<listSize;i++) {
            String ret = getTableContentFromDatabase(websites.get(i), passAwayKeywordCHN);

            if (ret == null) {
                alives[i] = 0;
            } else {
                alives[i] = 1;
            }
        }

        for(int i = 0;i < listSize;i++) {
            alumniDAO.update(alives[i], "alive", ids.get(i), "alumnus_v2");
        }
    }

    // add "date_birth" property in database.
    private static void dateBirthProcessor() {
        List<String> dateBirths = new ArrayList<String>();
        String[] dateBirthKeywordCHN = {"出生日期"};

        // use table_content first to match the dateBirth first.
        for (int i = 0;i < listSize;i++) {
            String dateBirthStr = getTableContentFromDatabase(websites.get(i), dateBirthKeywordCHN);
            Matcher birthYearMatcher = null;

            if(dateBirthStr != null) {
                birthYearMatcher = birthYearPattern.matcher(dateBirthStr);
            }

            if (dateBirthStr == null || !birthYearMatcher.find()) {
                dateBirthStr = parser.birthDateParser(briefIntros.get(i));

                if (dateBirthStr == null) {
                    dateBirthStr = parser.birthDateParser(mainContents.get(i));
                }
            }

            if (dateBirthStr != null) {
                birthYearMatcher = birthYearPattern.matcher(dateBirthStr);

                if(birthYearMatcher.find()) {
                    dateBirthStr = birthYearMatcher.group(0);
                }
                else {
                    dateBirthStr = null;
                }
            }

            dateBirths.add(dateBirthStr);
        }

        for(int i = 0;i < listSize;i++) {
            if (dateBirths.get(i) != null) {
                alumniDAO.update(dateBirths.get(i), "birthday", ids.get(i), "alumnus_v2");
            }
        }
    }

    private static String getTableContentFromDatabase(String website, String[] attrNames){
        String content;
        String[] rgexArray;
        String blank160Rgex = "\\u00A0*";
        String indexPath = "\\[\\d*?-?\\d*?]";
        // 将传入的属性名转化为正则表达式
        List<String> rgexList = new ArrayList<>();
        for (String attrName : attrNames) {
            rgexList.add(attrName + " (.*?) ");
        }
        rgexArray = new String[rgexList.size()];
        rgexList.toArray(rgexArray);
        content = alumniDAO.getTableContent(website);
        //结尾加空格，使得dd的内容始终被空格包裹
        content = content + " ";
        content = content.replaceAll(blank160Rgex, "");
        content = content.replaceAll(indexPath, "");
        content = getMatching(content, rgexArray);

        return content;
    }

    private static String getMatching(String soap, String[] rgexs) {
        for (String rgex : rgexs) {
            Pattern pattern = Pattern.compile(rgex);
            Matcher m = pattern.matcher(soap);
            if (m.find()) {
                return m.group(1);
            }
        }

        return null;
    }

    public static void main(String[] args) {
        //dateBirthProcessor();
        //aliveProcessor();
        locationProcessor();
    }

}
