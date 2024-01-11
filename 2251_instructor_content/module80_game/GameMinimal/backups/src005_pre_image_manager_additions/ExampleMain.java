/**
 * Created by nealh_000 on 5/22/2017.
 */
public class ExampleMain
{
    public static void main(String[] args)
    {
        ExampleBoard b = new ExampleBoard();
        //https://docs.oracle.com/javase/tutorial/essential/concurrency/runthread.html
        Main m = new Main(b);
        m.SHOW_COLLISION = true;
        m.runGameLoop();
    }
}
