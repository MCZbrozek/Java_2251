import java.util.ArrayList;

public class Movie extends Media {

    private String director;
    private ArrayList<String> actors;

    public Movie(int releaseYear, String writer, String director, ArrayList<String> actors) {

    }

    public String getDirector() {
        return director;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    @Override
    public String toString() {

        return "Movie includes" + director + " and the following actors " + actors;
    }

}
