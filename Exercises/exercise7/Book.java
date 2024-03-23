
public class Book extends Media {
    private String publisher;
    private String coverArtist;

    public Book(String publisher, String coverArtist) {
        super(releaseYear, writer);
    }

    public String getPublisher() {
        return publisher;
    }

    public String getCoverArtist() {
        return coverArtist;
    }

    @Override
    public String toString() {
        return "The publisher is  " + publisher + " and the cover artist is " + coverArtist;
    }

}
