package whu.alumnispider.gov;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import whu.alumnispider.DAO.AlumniDAO;
import whu.alumnispider.parser.KeywordParser;
import whu.alumnispider.utilities.GovLeaderPerson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GovBasedOnBaiduAlumniInfoPageProcessor implements PageProcessor {
    private Site site = Site.me().setSleepTime(3000).setRetryTimes(2).setTimeOut(20000)
            .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
    private static AlumniDAO alumniDAO = new AlumniDAO();

    @Override
    public void process(Page page) {
        KeywordParser parser = new KeywordParser();
        GovLeaderPerson person = new GovLeaderPerson();
        String html = page.getHtml().toString();

        parser.nameExtractor(html, person);
        parser.jovPositionExtractor(html, person);
        person.setUrl(page.getUrl().toString());

        //alumniDAO.add(person, "baiduSitePerson");
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        List<String> targetUrls = new ArrayList<String>();
        List<String> possibleUrls = new ArrayList<String>();
        possibleUrls = alumniDAO.readFromTable("baiduPages", "URL");

        Spider.create(new GovBasedOnBaiduAlumniInfoPageProcessor())
                .addUrl("http://www.gxnandan.jcy.gov.cn/jcyw1/201803/t20180323_2167495.shtml")
                .thread(1)
                .run();
        /*for(String possibleUrl:possibleUrls) {
            if (!targetUrls.contains(possibleUrl)) {
                targetUrls.add(possibleUrl);
                Request request = new Request(possibleUrl);

                Spider.create(new GovBasedOnBaiduAlumniInfoPageProcessor())
                        .addRequest(request)
                        .thread(1)
                        .run();
            }
        }*/
    }
}
