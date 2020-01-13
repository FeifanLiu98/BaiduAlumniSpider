package whu.alumnispider.gov;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import whu.alumnispider.DAO.AlumniDAO;
import whu.alumnispider.parser.BaiduParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GovBasedOnBaiduUrlPageProcessor implements PageProcessor {
    private Site site = Site.me().setSleepTime(6000).setRetryTimes(2).setTimeOut(20000)
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36")
            .setCharset("UTF-8")
            .addHeader("Accept", "*/*")
            .addHeader("Accept-Encoding", "gzip, deflate, br")
            .addHeader("Connection", "keep-alive")
            //.addHeader("Cookie", "PSTM=" + System.currentTimeMillis() + ";" + "BAIDUID=128B42DC68B8EDF5CA648FC4AC8105CB:FG=1; BIDUPSID=128B42DC68B8EDF5CA648FC4AC8105CB; PSTM=1572510007; BD_UPN=12314753; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; H_PS_PSSID=1439_21095_30210_18559_26350; H_WISE_SIDS=130610_136722_100806_138809_139324_128063_138946_120131_138490_133995_138878_137978_132910_137690_131247_132551_137743_131517_118888_118873_118849_118834_118797_136687_107315_138882_136430_138845_139163_136863_138148_138114_139177_136196_124629_131861_137105_139400_133847_138476_137735_138343_137468_134256_139319_137817_139254_139246_136537_110085_138235_127969_138619_139161_137829_139286_138805_128195_138313_137187_136635_138426_138562_138943_138249_138579_139221_138754; delPer=0; BD_CK_SAM=1; BDRCVFR[dG2JNJb_ajR]=mk3SLVN4HKm; COOKIE_SESSION=320663_0_6_5_0_6_0_1_3_4_2_4_0_0_0_0_0_0_1576029897%7C7%230_0_1576029897%7C1; PSINO=7; H_PS_645EC=bee0jtKG1Md1URGUA%2FfibROzdegDa%2FBfn%2B0qd%2BOyXgkuQOIMIxi%2F1Zy6B9A; WWW_ST=1576032352402");
            .addHeader("Cookie", "BAIDUID=128B42DC68B8EDF5CA648FC4AC8105CB:FG=1; BIDUPSID=128B42DC68B8EDF5CA648FC4AC8105CB; PSTM=1572510007; pgv_pvi=6157703168; pgv_si=s517137408; delPer=0; BD_HOME=0; BD_UPN=12314753; BD_CK_SAM=1; PSINO=6; H_PS_PSSID=1439_21095_30210_18559_30466_30284_26350_30481; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; H_PS_645EC=ec87nqfPMMWwXnUFYUIlgCMuEBrWVIXyluHSeORs9VAXKJBJjQxZvZDgplE");
    private static int index = 0;
    private String subPageXpath = "//div[@class='f13']/a[@class='c-showurl']/@href";
    private String nameXpath = "//h3/a/text()";
    private String nextPageXpath = "//div[@id='page']/a[@class='n']/@href";
    private String baiduKeyword = "百度搜索";
    private Pattern baiduKeywordPattern = Pattern.compile(baiduKeyword);

    @Override
    public void process(Page page) {
        BaiduParser parser = new BaiduParser();
        String processingUrl = page.getRequest().getUrl();
        Selectable subPage;
        Selectable name;
        Selectable nextPage;
        List<String> subPages = new ArrayList<String>();
        List<String> names = new ArrayList<String>();
        String nextPageUrl;
        Matcher baiduKeywordMatcher = baiduKeywordPattern.matcher(page.getHtml().toString());
        AlumniDAO alumniDAO = new AlumniDAO();

        if (baiduKeywordMatcher.find()) {
            subPage = page.getHtml().xpath(subPageXpath);
            name = page.getHtml().xpath(nameXpath);
            subPages = subPage.all();
            names = name.all();

            //nextPage = page.getHtml().xpath(nextPageXpath);
            //nextPageUrl = "https://www.baidu.com/" + nextPage.toString();
            subPages = parser.getTrueUrlFromBaidu(subPages);
            for(String str:subPages) {
                //alumniDAO.add(str, "BaiduPages");
            }

            //page.addTargetRequests(subPages);
            //addTargetRequest(nextPageUrl);
        }
        else {
            //getInfo(page);
        }
    }

    // If one page have more than 1 person's info, it won't be recognized.
    /*private void getInfo(Page page) {
        GovLeaderPerson person = new GovLeaderPerson();
        String cv = page.getHtml().toString();
        KeywordParser parser = new KeywordParser();
        AlumniDAO alumniDAO = new AlumniDAO();

        parser.jovPositionExtractor(cv, person);
        parser.nameExtractor(cv, person);
        person.setUrl(page.getUrl().toString());

        alumniDAO.add(person, "baiduSitePerson");
        System.out.println(person);
    }*/

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        long sysTime = System.currentTimeMillis();
        /*HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy("120.79.212.174", 8000),
                new Proxy("120.77.253.219", 80)
                ));*/

        while (index<570) {
            //String whuBaiduString = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%E7%AE%80%E5%8E%86%20site%3Agov.cn&oq=%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%E7%AE%80%E5%8E%86%20site%3Agov.cn&rsv_pq=a498703800021b99&rsv_t=6304psKffA8j%2FUoBM0Z7iSGeock33BK9sDlFMh0v8Z8Bs4nECeTxtvjj6nI&rqlang=cn&rsv_enter=1&rsv_dl=tb&gpc=stf%3D1449417600%2C1575734398%7Cstftype%3D2&tfflag=1&si=gov.cn&ct=2097152" + "&pn=" + index;
            String whuBaiduString = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E5%B1%80%E9%95%BF%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E5%25B1%2580%25E9%2595%25BF&rsv_pq=e22b0ef50044c33a&rsv_t=d919DTxHEI5UOkDfiwe0gzA57cODLAQfC7Vxn7SFHu%2BEGaV8ABNlhHk8w2M&rqlang=cn&rsv_enter=1&rsv_dl=tb&rsv_sug3=90&rsv_sug2=0&inputT=32149&rsv_sug4=32149";
            Request request = new Request(whuBaiduString);
            Spider.create(new GovBasedOnBaiduUrlPageProcessor())
                    //.setDownloader(httpClientDownloader)
                    .addRequest(request)
                    .thread(1)
                    .run();
            long costTime = System.currentTimeMillis() - sysTime;
            System.out.println("The program costs " + costTime / 1000000);
            index = index + 10;
        }
        System.out.println("The number of pages is " + index);
    }
}
