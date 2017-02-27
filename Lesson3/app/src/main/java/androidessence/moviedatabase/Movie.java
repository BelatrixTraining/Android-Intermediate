package androidessence.moviedatabase;

/**
 * Created by pjohnson on 27/02/17.
 */

public class Movie {

    private String name;
    private Long genre;
    private String releaseDate;

    public Movie(String name, Long genre, String date) {
        this.name = name;
        this.genre = genre;
        this.releaseDate = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGenre() {
        return genre;
    }

    public void setGenre(Long genre) {
        this.genre = genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
