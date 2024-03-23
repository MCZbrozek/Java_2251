import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Book myBook = new Book(1977, "Douglas Darnell", "Castle Face Publishing", "Sabrina Cho");

        ArrayList<String> actorsList = new ArrayList<String>();
        actorsList.add("Mike Zbrozek");

        Movie myMovie = new Movie(1968, "Mike Zbrozek", "Martin Scorsese", actorsList);

        System.out.println(myBook.toString());
        System.out.println(myMovie.toString());
    }

}
