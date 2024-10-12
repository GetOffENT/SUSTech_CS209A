package entity;

/**
 * @author Will
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-09-30 17:50
 */
public class GamesMedalTally {
    //edition,edition_id,year,country,country_noc,gold,silver,bronze,total
    private String edition;
    private Integer edition_id;
    private String year;
    private String country;
    private String country_noc;
    private Integer gold;
    private Integer silver;
    private Integer bronze;
    private Integer total;

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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_noc() {
        return country_noc;
    }

    public void setCountry_noc(String country_noc) {
        this.country_noc = country_noc;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public Integer getSilver() {
        return silver;
    }

    public void setSilver(Integer silver) {
        this.silver = silver;
    }

    public Integer getBronze() {
        return bronze;
    }

    public void setBronze(Integer bronze) {
        this.bronze = bronze;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "GamesMedalTally{" +
                "edition='" + edition + '\'' +
                ", edition_id=" + edition_id +
                ", year='" + year + '\'' +
                ", country='" + country + '\'' +
                ", country_noc='" + country_noc + '\'' +
                ", gold=" + gold +
                ", silver=" + silver +
                ", bronze=" + bronze +
                ", total=" + total +
                '}';
    }
}
