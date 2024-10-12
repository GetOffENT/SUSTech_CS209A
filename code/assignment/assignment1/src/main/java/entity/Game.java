package entity;

/**
 * @author Will
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-09-30 17:51
 */
public class Game {
    //edition,edition_id,edition_url,year,city,country_flag_url,country_noc,start_date,end_date,competition_date,isHeld
    private String edition;
    private Integer edition_id;
    private String edition_url;
    private String year;
    private String city;
    private String country_flag_url;
    private String country_noc;
    private String start_date;
    private String end_date;
    private String competition_date;
    private String isHeld;

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public Integer getEdition_id() {
        return edition_id;
    }

    public void setEdition_id(Integer edition_id) {
        this.edition_id = edition_id;
    }

    public String getEdition_url() {
        return edition_url;
    }

    public void setEdition_url(String edition_url) {
        this.edition_url = edition_url;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry_flag_url() {
        return country_flag_url;
    }

    public void setCountry_flag_url(String country_flag_url) {
        this.country_flag_url = country_flag_url;
    }

    public String getCountry_noc() {
        return country_noc;
    }

    public void setCountry_noc(String country_noc) {
        this.country_noc = country_noc;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getCompetition_date() {
        return competition_date;
    }

    public void setCompetition_date(String competition_date) {
        this.competition_date = competition_date;
    }

    public String getIsHeld() {
        return isHeld;
    }

    public void setIsHeld(String isHeld) {
        this.isHeld = isHeld;
    }

    @Override
    public String toString() {
        return "Game{" +
                "edition='" + edition + '\'' +
                ", edition_id=" + edition_id +
                ", edition_url='" + edition_url + '\'' +
                ", year='" + year + '\'' +
                ", city='" + city + '\'' +
                ", country_flag_url='" + country_flag_url + '\'' +
                ", country_noc='" + country_noc + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", competition_date='" + competition_date + '\'' +
                ", isHeld='" + isHeld + '\'' +
                '}';
    }
}
