package whu.alumnispider.utilities;

import java.util.Objects;

public class College {
    private String name;
    private String website;

    public College(String name, String website) {
        this.name = name;
        this.website = website;
    }

    public College() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        College college = (College) o;
        return Objects.equals(website, college.website);
    }

    @Override
    public int hashCode() {
        return Objects.hash(website);
    }
}
