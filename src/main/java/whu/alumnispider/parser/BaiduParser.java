package whu.alumnispider.parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaiduParser {
    public String getTrueUrlFromBaidu(String fakeUrl) {
                Connection.Response response = null;
                int initTimeout = 60000;
                try {
                    response = Jsoup.connect(fakeUrl).timeout(initTimeout).method(Connection.Method.GET).followRedirects(false).execute();
                    return response.header("Location");
                } catch (IOException e) {
                    e.printStackTrace();
                }

        return "";
    }

    public List<String> nameFilter(List<String> names) {
        for(int index = 0;index <names.size();index++) {
            String str = names.get(index);
            int signal = -1;

            for(int i = 0;i<str.length();i++) {
                if(str.charAt(i) <= 0x0391 || str.charAt(i) >= 0xFFE5 || str.charAt(i) == '　') {
                    signal = i;
                    break;
                }
            }

            if (signal != -1) {
                names.set(index, str.substring(0, signal));
            }
        }

        return names;
    }

    public List<String> getTrueUrlFromBaidu(List<String> fakeUrls) {
        Connection.Response response = null;
        int initTimeout = 60000;
        List<String> rets = new ArrayList<String>();

        for (String fakeUrl:fakeUrls) {
            try {
                response = Jsoup.connect(fakeUrl).timeout(initTimeout).method(Connection.Method.GET).followRedirects(false).execute();
                String ret = response.header("Location");
                rets.add(ret);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return rets;
    }
}
