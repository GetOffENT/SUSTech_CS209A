package entity;

/**
 * @author Will
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-09-30 17:50
 */
public class Result {
    //result_id,event_title,edition,edition_id,sport,sport_url,result_date,result_location,result_participants,result_format,result_detail,result_description
    private Integer result_id;
    private String event_title;
    private String edition;
    private Integer edition_id;
    private String sport;
    private String sport_url;
    private String result_date;
    private String result_location;
    private String result_participants;
    private String result_format;
    private String result_detail;
    private String result_description;

    public Integer getResult_id() {
        return result_id;
    }

    public void setResult_id(Integer result_id) {
        this.result_id = result_id;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

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

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getSport_url() {
        return sport_url;
    }

    public void setSport_url(String sport_url) {
        this.sport_url = sport_url;
    }

    public String getResult_date() {
        return result_date;
    }

    public void setResult_date(String result_date) {
        this.result_date = result_date;
    }

    public String getResult_location() {
        return result_location;
    }

    public void setResult_location(String result_location) {
        this.result_location = result_location;
    }

    public String getResult_participants() {
        return result_participants;
    }

    public void setResult_participants(String result_participants) {
        this.result_participants = result_participants;
    }

    public String getResult_format() {
        return result_format;
    }

    public void setResult_format(String result_format) {
        this.result_format = result_format;
    }

    public String getResult_detail() {
        return result_detail;
    }

    public void setResult_detail(String result_detail) {
        this.result_detail = result_detail;
    }

    public String getResult_description() {
        return result_description;
    }

    public void setResult_description(String result_description) {
        this.result_description = result_description;
    }

    @Override
    public String toString() {
        return "Result{" +
                "result_id=" + result_id +
                ", event_title='" + event_title + '\'' +
                ", edition='" + edition + '\'' +
                ", edition_id=" + edition_id +
                ", sport='" + sport + '\'' +
                ", sport_url='" + sport_url + '\'' +
                ", result_date='" + result_date + '\'' +
                ", result_location='" + result_location + '\'' +
                ", result_participants='" + result_participants + '\'' +
                ", result_format='" + result_format + '\'' +
                ", result_detail='" + result_detail + '\'' +
                ", result_description='" + result_description + '\'' +
                '}';
    }
}
