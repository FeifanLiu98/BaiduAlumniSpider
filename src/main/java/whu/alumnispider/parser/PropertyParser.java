package whu.alumnispider.parser;

import whu.alumnispider.utilities.ReExpUtility;
import whu.alumnispider.utilities.Person;
import whu.alumnispider.DAO.AlumniDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyParser {
    public static AlumniDAO alumniDAO = new AlumniDAO();

    public String birthDateParser(String source) {
        Pattern birthDatePattern = Pattern.compile(ReExpUtility.reDateBirth);
        Matcher birthDateMatcher = birthDatePattern.matcher(source);
        int index = -1;
        String retStr = "blank";

        while(birthDateMatcher.find()) {
            index++;

            if (index == 0) {
                retStr = birthDateMatcher.group();

                return retStr;
                }
            }

        return null;
    }

    public String locationParser(String index) {
        if (index == "11") return "北京市";
        if (index == "12") return "天津市";
        if (index == "13") return "河北省";
        if (index == "14") return "山西省";
        if (index == "15") return "内蒙古";
        if (index == "21") return "辽宁省";
        if (index == "22") return "吉林省";
        if (index == "23") return "黑龙江省";
        if (index == "31") return "上海市";
        if (index == "32") return "江苏省";
        if (index == "33") return "浙江省";
        if (index == "34") return "安徽省";
        if (index == "35") return "福建省";
        if (index == "36") return "江西省";
        if (index == "37") return "山东省";
        if (index == "41") return "河南省";
        if (index == "42") return "湖北省";
        if (index == "43") return "湖南省";
        if (index == "44") return "广东省";
        if (index == "45") return "广西省";
        if (index == "46") return "海南省";
        if (index == "50") return "重庆市";
        if (index == "51") return "四川省";
        if (index == "52") return "贵州省";
        if (index == "53") return "云南省";
        if (index == "54") return "西藏省";
        if (index == "61") return "陕西省";
        if (index == "62") return "甘肃省";
        if (index == "63") return "青海省";
        if (index == "64") return "宁夏省";
        if (index == "65") return "新疆省";

        return null;
    }
}
