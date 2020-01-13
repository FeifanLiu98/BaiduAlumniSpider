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

public class GovBasedOnBaiduBaikeNameProcessor implements PageProcessor {
    private Site site = Site.me().setSleepTime(6000).setRetryTimes(2).setTimeOut(20000)
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36")
            .setCharset("UTF-8")
            .addHeader("Accept", "*/*")
            .addHeader("Accept-Encoding", "gzip, deflate, br")
            .addHeader("Connection", "keep-alive")
            //.addHeader("Cookie", "PSTM=" + System.currentTimeMillis() + ";" + "BAIDUID=128B42DC68B8EDF5CA648FC4AC8105CB:FG=1; BIDUPSID=128B42DC68B8EDF5CA648FC4AC8105CB; PSTM=1572510007; BD_UPN=12314753; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; H_PS_PSSID=1439_21095_30210_18559_26350; H_WISE_SIDS=130610_136722_100806_138809_139324_128063_138946_120131_138490_133995_138878_137978_132910_137690_131247_132551_137743_131517_118888_118873_118849_118834_118797_136687_107315_138882_136430_138845_139163_136863_138148_138114_139177_136196_124629_131861_137105_139400_133847_138476_137735_138343_137468_134256_139319_137817_139254_139246_136537_110085_138235_127969_138619_139161_137829_139286_138805_128195_138313_137187_136635_138426_138562_138943_138249_138579_139221_138754; delPer=0; BD_CK_SAM=1; BDRCVFR[dG2JNJb_ajR]=mk3SLVN4HKm; COOKIE_SESSION=320663_0_6_5_0_6_0_1_3_4_2_4_0_0_0_0_0_0_1576029897%7C7%230_0_1576029897%7C1; PSINO=7; H_PS_645EC=bee0jtKG1Md1URGUA%2FfibROzdegDa%2FBfn%2B0qd%2BOyXgkuQOIMIxi%2F1Zy6B9A; WWW_ST=1576032352402");
            .addHeader("Cookie", "BAIDUID=128B42DC68B8EDF5CA648FC4AC8105CB:FG=1; BIDUPSID=128B42DC68B8EDF5CA648FC4AC8105CB; PSTM=1572510007; pgv_pvi=6157703168; pgv_si=s517137408; delPer=0; BD_HOME=0; BD_UPN=12314753; BD_CK_SAM=1; PSINO=6; H_PS_PSSID=1439_21095_30210_18559_30466_30284_26350_30481; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; H_PS_645EC=ec87nqfPMMWwXnUFYUIlgCMuEBrWVIXyluHSeORs9VAXKJBJjQxZvZDgplE");
    private static int index = 0;
    private static int number = 0;
    private String nameXpath = "//h3/a/text()";
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
            name = page.getHtml().xpath(nameXpath);
            names = name.all();
            names = parser.nameFilter(names);
            //nextPage = page.getHtml().xpath(nextPageXpath);
            //nextPageUrl = "https://www.baidu.com/" + nextPage.toString();

            for(String str:names) {
                if(str.length() <= 3) {
                    alumniDAO.add(str, "name", "BaiduBaikeNameSetC");
                }
            }

            //page.addTargetRequests(subPages);
            //addTargetRequest(nextPageUrl);
        }
        else {
            //getInfo(page);
        }
    }

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
        List<String> govBaiduList = new ArrayList<String>();

        // keyword：厅长，局长，市长，县长，区长，书记，党组成员，司长，处长，主席，部长,主任
/*        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E5%8E%85%E9%95%BF%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E5%25B1%2580%25E9%2595%25BF%2520site%253Abaike.baidu.com&rsv_pq=f5caf2e20000b277&rsv_t=ec87nqfPMMWwXnUFYUIlgCMuEBrWVIXyluHSeORs9VAXKJBJjQxZvZDgplE&rqlang=cn&rsv_enter=0&rsv_dl=tb&rsv_sug3=106&rsv_sug2=0&inputT=1745&rsv_sug4=2338");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E5%B1%80%E9%95%BF%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E5%25B1%2580%25E9%2595%25BF%2520site%253Abaike.baidu.com&rsv_pq=ff35f3f100761062&rsv_t=a8fewF9e5walTvbwef4aAPuEulos6tGMLTx4DWSgpJQK%2FqpBQuiAYpSGkM4&rqlang=cn&rsv_enter=0&rsv_dl=tb&rsv_sug3=93&rsv_sug4=636856");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E5%B8%82%E9%95%BF%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E5%25B1%2580%25E9%2595%25BF%2520site%253Abaike.baidu.com&rsv_pq=935fc58c0012a9e3&rsv_t=7f33ZVwRYvD1uOcyc9%2F5%2BGh6FglCYRkitOw7MPb6uEKQ8dhYygppoE1EHfY&rqlang=cn&rsv_enter=0&rsv_dl=tb&inputT=87063&rsv_sug3=122&rsv_sug4=89521");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E5%8E%BF%E9%95%BF%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E5%25B1%2580%25E9%2595%25BF%2520site%253Abaike.baidu.com&rsv_pq=e0e7165f000cb32e&rsv_t=46113DBoWEieWAzGBUiWlGFnIf%2BJFSZSsGHlEP%2FYoSkWZLvPLJipgJcM54w&rqlang=cn&rsv_enter=1&rsv_dl=tb&rsv_sug3=13&rsv_sug2=0&inputT=2454&rsv_sug4=3929");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E4%B9%A6%E8%AE%B0%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E5%258E%25BF%25E9%2595%25BF%2520site%253Abaike.baidu.com&rsv_pq=e84a14b30001a999&rsv_t=dd3cmhmupbqh%2FunSsxeBrPoM8tx48RWE%2F0OVZe72huilQDd8KmP1PF6xyTA&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=1539&rsv_sug3=22&rsv_sug2=0&rsv_sug4=2993");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E5%8C%BA%E9%95%BF%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E4%25B9%25A6%25E8%25AE%25B0%2520site%253Abaike.baidu.com&rsv_pq=ba4f0d24000134dc&rsv_t=7886R4%2BPQUj%2Fw%2Bk6Iz9Dua5rh8Pg0fcSS7zLLV3dAJqaOKhmuQ63E6z%2FMh4&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=1263&rsv_sug3=36&rsv_sug2=0&rsv_sug4=28376");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E5%85%9A%E7%BB%84%E6%88%90%E5%91%98%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E5%258C%25BA%25E9%2595%25BF%2520site%253Abaike.baidu.com&rsv_pq=a35005da0002f005&rsv_t=b6d4UZ24aZJzU0wKdP8AdGq44Y8aB%2FmLvYt4ZHsdcJ5oFXtJ50C50L3iFEU&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=1954&rsv_sug3=51&rsv_sug2=0&rsv_sug4=17031");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E5%8F%B8%E9%95%BF%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E5%2585%259A%25E7%25BB%2584%25E6%2588%2590%25E5%2591%2598%2520site%253Abaike.baidu.com&rsv_pq=9da441e90002f7d6&rsv_t=ed06ftrPM%2Bto%2FmBenkIu1kQLwZTXE%2FNC61tXiPk0eJyCOlKalaqU%2F05CJJA&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=4565&rsv_sug3=71&rsv_sug2=0&rsv_sug4=6261");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E5%A4%84%E9%95%BF%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E5%258F%25B8%25E9%2595%25BF%2520site%253Abaike.baidu.com&rsv_pq=e5abbb440002e484&rsv_t=69f0bGErJp%2FRbFy9RKoqkSTNFRjyLtPyiPp49q5eaiYosvgplomsP4swDok&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=958&rsv_sug3=77&rsv_sug2=0&rsv_sug4=1499");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E4%B8%BB%E5%B8%AD%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E5%25A4%2584%25E9%2595%25BF%2520site%253Abaike.baidu.com&rsv_pq=9658236200030de6&rsv_t=88aewkWlkBhK5%2B%2Fgr2eFYfVhIdCFEkz7dSGWSxjPbzo3MCAmeAzmqqTq80I&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=867&rsv_sug3=84&rsv_sug2=0&rsv_sug4=1144");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E9%83%A8%E9%95%BF%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E4%25B8%25BB%25E5%25B8%25AD%2520site%253Abaike.baidu.com&rsv_pq=c64de9fe0000580d&rsv_t=da24Ufw31PSB84uxB527DoYe1Xqxv4Neer9KaZMPp61YWGURqPFD4pwHJpQ&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=1221&rsv_sug3=96&rsv_sug2=0&rsv_sug4=9367");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E4%B8%BB%E4%BB%BB%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E9%2583%25A8%25E9%2595%25BF%2520site%253Abaike.baidu.com&rsv_pq=ae3c60ad0001187e&rsv_t=9f0cE%2BhK0vBvHybMiG2R9FhDXg35egKg%2B1FWzNTpca3s8KwOwgnW0totQbo&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=1443&rsv_sug3=104&rsv_sug2=0&rsv_sug4=1662");
  */
        // keyword: 区长， 秘书长, 委员，台长, 副部级，组长
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E5%8C%BA%E9%95%BF%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E4%25B8%25BB%25E4%25BB%25BB%2520site%253Abaike.baidu.com&rsv_pq=8025068400055d00&rsv_t=399aEMyFg5i5eRSh7VL5FMi2UA7FHPaU1Ezali0UDMi2g2i2cEgD0Hk37qc&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=1104&rsv_sug3=127&rsv_sug2=0&rsv_sug4=6271");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E7%A7%98%E4%B9%A6%E9%95%BF%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E5%258C%25BA%25E9%2595%25BF%2520site%253Abaike.baidu.com&rsv_pq=a4cacca100080d1e&rsv_t=32fcXt0qUZxo7sQlxAEE9W1%2F52gXx%2FKs9zLvp1RcYJ9F%2B7OtWdTF5AQ46iw&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=1736&rsv_sug3=142&rsv_sug2=0&rsv_sug4=633792");
        govBaiduList.add("https://www.baidu.com/s?wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E5%A7%94%E5%91%98%20site%3Abaike.baidu.com&pn=180&oq=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E5%A7%94%E5%91%98%20site%3Abaike.baidu.com&ie=utf-8&rsv_idx=1&rsv_pq=dba7bb4a0005b2a4&rsv_t=fa97sL9V48E%2FCU8aq%2BRoGvjMY25OXNG7Ow9VoMUVKNh5msR%2BoF5WVw4bWto");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AF%95%E4%B8%9A%20%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E5%8F%B0%E9%95%BF%20site%3Abaike.baidu.com&oq=%25E6%25AF%2595%25E4%25B8%259A%2520%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E5%25A7%2594%25E5%2591%2598%2520site%253Abaike.baidu.com&rsv_pq=e3e85b2b0005f837&rsv_t=c2ee0KSZlgIELL%2F9b7jEy5ksW57T%2FoVV9LFy5TiWcsWuwpD1fi8bCcv%2BGCs&rqlang=cn&rsv_enter=1&rsv_dl=tb&rsv_sug3=20&rsv_sug2=0&inputT=2609&rsv_sug4=4316");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E5%89%AF%E9%83%A8%E7%BA%A7%20%E6%AF%95%E4%B8%9A%20site%3Abaike.baidu.com&oq=%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E5%2589%25AF%25E9%2583%25A8%25E7%25BA%25A7%2520site%253Abaike.baidu.com&rsv_pq=efb79b2d0003fd22&rsv_t=3029fCUsQaIEOnKAylFXGsMBkV5OKte02sMG4pjQB9YK7vQcibRJP4jwy0s&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=5125&rsv_sug3=56&rsv_sug2=0&rsv_sug4=5908");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E6%AD%A3%E5%8E%85%E7%BA%A7%20%E6%AF%95%E4%B8%9A%20site%3Abaike.baidu.com&oq=%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E5%2589%25AF%25E9%2583%25A8%25E7%25BA%25A7%2520%25E6%25AF%2595%25E4%25B8%259A%2520site%253Abaike.baidu.com&rsv_pq=fc418f2c00042363&rsv_t=da12ql2OLAByCTixAyE3UxorWarKw8bPBnFiwb%2F%2BP6kRjOAnDiuZ5pU05UU&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=1827&rsv_sug3=69&rsv_sug2=0&rsv_sug4=2151");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E5%89%AF%E5%8E%85%E7%BA%A7%20%E6%AF%95%E4%B8%9A%20site%3Abaike.baidu.com&oq=%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E6%25AD%25A3%25E5%258E%2585%25E7%25BA%25A7%2520%25E6%25AF%2595%25E4%25B8%259A%2520site%253Abaike.baidu.com&rsv_pq=eec502890003a95f&rsv_t=179d52tMdf9kv90WFGDA9UIoPu2rj9onRZKyLzDsVtc6M7qNyhGsOlynFq0&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=867&rsv_sug3=74&rsv_sug2=0&rsv_sug4=1513");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E6%AD%A3%E5%A4%84%E7%BA%A7%20%E6%AF%95%E4%B8%9A%20site%3Abaike.baidu.com&oq=%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E5%2589%25AF%25E5%258E%2585%25E7%25BA%25A7%2520%25E6%25AF%2595%25E4%25B8%259A%2520site%253Abaike.baidu.com&rsv_pq=aa8cf8ae00077032&rsv_t=381a1iPoaHvDJs4d0J8FlnBTRCBDBHTsqryRYt3%2FNb0lTDLyNKqiTBY92WU&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=23299&rsv_sug3=88&rsv_sug2=0&rsv_sug4=24305");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E7%BB%84%E9%95%BF%20%E6%AF%95%E4%B8%9A%20site%3Abaike.baidu.com&oq=%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E5%25B8%25B8%25E5%25A7%2594%2520%25E6%25AF%2595%25E4%25B8%259A%2520site%253Abaike.baidu.com&rsv_pq=cd9cd53f0007241b&rsv_t=4c82xUFhZCyoJE%2B4tz5LcfIqKeXerm4wH0xgqYF26Wm2C%2BHFbEWkUdg%2BPdo&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=1208&rsv_sug3=117&rsv_sug2=0&rsv_sug4=2571");
        govBaiduList.add("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%20%E8%A1%8C%E9%95%BF%20%E6%AF%95%E4%B8%9A%20site%3Abaike.baidu.com&oq=%25E6%25AD%25A6%25E6%25B1%2589%25E5%25A4%25A7%25E5%25AD%25A6%2520%25E7%25BB%2584%25E9%2595%25BF%2520%25E6%25AF%2595%25E4%25B8%259A%2520site%253Abaike.baidu.com&rsv_pq=d947b19c0002154a&rsv_t=ada8UIB04Bud3csy5R%2BKie482godHBUKw96%2BvOIcEo9g3sfBkBgXAjEA8fo&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=1171&rsv_sug3=128&rsv_sug2=0&rsv_sug4=1658");

        while (number < govBaiduList.size()) {
            //String whuBaiduString = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%E7%AE%80%E5%8E%86%20site%3Agov.cn&oq=%E6%AD%A6%E6%B1%89%E5%A4%A7%E5%AD%A6%E7%AE%80%E5%8E%86%20site%3Agov.cn&rsv_pq=a498703800021b99&rsv_t=6304psKffA8j%2FUoBM0Z7iSGeock33BK9sDlFMh0v8Z8Bs4nECeTxtvjj6nI&rqlang=cn&rsv_enter=1&rsv_dl=tb&gpc=stf%3D1449417600%2C1575734398%7Cstftype%3D2&tfflag=1&si=gov.cn&ct=2097152" + "&pn=" + index;
            String whuBaiduString = govBaiduList.get(number);
            whuBaiduString = whuBaiduString + "&pn=" + index;

            Request request = new Request(whuBaiduString);
            Spider.create(new GovBasedOnBaiduBaikeNameProcessor())
                    //.setDownloader(httpClientDownloader)
                    .addRequest(request)
                    .thread(1)
                    .run();
            long costTime = System.currentTimeMillis() - sysTime;
            System.out.println("The program costs " + costTime / 1000000);
            index = index + 10;

            if (index == 570) {
               index = 0;
               number++;
            }
        }
    }
}
