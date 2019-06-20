package my.com.tutionathome.calvinlau.tuitionathome;

/**
 * Created by calvinlau on 20/08/2016.
 */import java.util.ArrayList;

public class tutorMData {
    private String title, thumbnailUrl, subject;
    private int year;
    private String distance;
    private ArrayList<String> genre;

    public tutorMData() {
    }

    public tutorMData(String name,String subject, String thumbnailUrl, int year, String distance,
                 ArrayList<String> genre) {
        this.title = name;
        this.subject = subject;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.distance = distance;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }

}
