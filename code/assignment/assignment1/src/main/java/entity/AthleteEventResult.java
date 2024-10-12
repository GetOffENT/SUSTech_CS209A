package entity;

/**
 * @author Will
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-09-30 17:50
 */
public class AthleteEventResult {
    //edition,edition_id,country_noc,sport,event,result_id,athlete,athlete_id,pos,medal,isTeamSport
    private String edition;
    private Integer edition_id;
    private String country_noc;
    private String sport;
    private String event;
    private Integer result_id;
    private String athlete;
    private Integer athlete_id;
    private String pos;
    private String medal;
    private Boolean isTeamSport;

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

    public String getCountry_noc() {
        return country_noc;
    }

    public void setCountry_noc(String country_noc) {
        this.country_noc = country_noc;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Integer getResult_id() {
        return result_id;
    }

    public void setResult_id(Integer result_id) {
        this.result_id = result_id;
    }

    public String getAthlete() {
        return athlete;
    }

    public void setAthlete(String athlete) {
        this.athlete = athlete;
    }

    public Integer getAthlete_id() {
        return athlete_id;
    }

    public void setAthlete_id(Integer athlete_id) {
        this.athlete_id = athlete_id;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getMedal() {
        return medal;
    }

    public void setMedal(String medal) {
        this.medal = medal;
    }

    public Boolean getTeamSport() {
        return isTeamSport;
    }

    public void setTeamSport(Boolean teamSport) {
        isTeamSport = teamSport;
    }

    @Override
    public String toString() {
        return "AthleteEventResult{" +
                "edition='" + edition + '\'' +
                ", edition_id=" + edition_id +
                ", country_noc='" + country_noc + '\'' +
                ", sport='" + sport + '\'' +
                ", event='" + event + '\'' +
                ", result_id=" + result_id +
                ", athlete='" + athlete + '\'' +
                ", athlete_id=" + athlete_id +
                ", pos='" + pos + '\'' +
                ", medal='" + medal + '\'' +
                ", isTeamSport=" + isTeamSport +
                '}';
    }
}
