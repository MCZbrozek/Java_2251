public class Main {
    public static void main(String[] args) {

        int[][] array = {
                { 5, 6, 2 },
                { 0, 8, 3 },
                { 7, 5, 1 } };

        int x = array[2][0];
        System.out.println("x  =" + x);
        // Question 6 ------------------------
        // double[][] array = {
        // { 2, 9, 6, 2 },
        // { 3, 6, 3, 4 },
        // { 1, 0, 5, 5 },
        // { 9, 7, 5, 5 },
        // };

        // int r = array.length / 2;
        // for (int i = r + 1; i < array.length; i++) {
        // int c = array[i].length / 2;
        // for (int j = c + 1; j < array[i].length; j++) {
        // System.out.println(array[i][j]);
        // }
        // }

        System.out.println("Next method - This is the correct answer Below!");

        // int u = array.length / 2;
        // for (int i = r; i < array.length; i++) {
        // int c = array[i].length / 2;
        // for (int j = c; j < array[i].length; j++) {
        // System.out.println(array[i][j]);
        // }
        // }

        // System.out.println("Next method");

        // int t = array.length / 2;
        // for (int i = r - 1; i < array.length; i++) {
        // int c = array[i].length / 2;
        // for (int j = c - 1; j < array[i].length; j++) {
        // System.out.println(array[i][j]);
        // }
        // }

        System.out.println("Next method");
        // int l = array.length / 2;
        // for (int i = l + 1; i <= array.length; i++) {
        // int c = array[i].length / 2;
        // for (int j = c + 1; j <= array[i].length; j++) {

        // System.out.println(array[i][j]);
        // }
        // }
        double answer = angleToInterval(180);
        System.out.println("Line 50 answer = " + answer);

        // Question 7
        // private boolean validMove() {
        // if (this.rowChoice < 0 || this.rowChoice > 2 || this.colChoice < 0 ||
        // this.colChoice > 2) {
        // return false;
        // return board[this.rowChoice][this.colChoice] == CellState.EMPTY;
        // }}
    }

    public static double angleToInterval(double angle) {
        return angle / 90.0 - 1;
    }
}
