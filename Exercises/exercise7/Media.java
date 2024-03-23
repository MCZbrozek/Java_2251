
public class Media {
    private int releaseYear;

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    private String writer;

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public Media(int releaseYear, String writer) {
        this.releaseYear = releaseYear;
        this.writer = writer;
    }

    public int getReleaseYear() {
        return this.releaseYear;
    }

    public String getWriter() {
        return this.writer;
    }

    @Override
    public String toString() {
        return "The writer is " + writer + " and the release year is " + releaseYear;
    }
}
