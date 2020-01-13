package whu.alumnispider.utilities;

public class GovSubpage {
    public String url;
    public String organizer;

    public GovSubpage(String url, String organizer) {
        this.url = url;
        this.organizer = organizer;
    }

    public GovSubpage() {

    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOrganizer() {
        return this.organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }
}
