import java.util.ArrayList;

public class SparseMatrix
{
	//Attributes (variables belonging to a SparseMatrix)
	private int rows;
	private int columns;
	private int default_value;
	
	//A way to store three ints
	private ArrayList<Triplet> values = new ArrayList<Triplet>();
	

	/* This constructor creates a SparseMatrix
	using an int[][] as input. */
	public SparseMatrix(int[][] m)
	{
		this.rows = m.length;
		this.columns = m[0].length;
		
		ArrayList<NumberCounts> values = new ArrayList<NumberCounts>();
		for(int row=0; row<m.length; row++)
		{
			for(int column=0; column<m[row].length; column++)
			{
				countNumber(values, m[row][column]);
			}
		}
		
		//Find the default value
		NumberCounts nc = values.get(0);
		this.default_value = nc.number;
		int count = nc.count;
		for(int i=1; i<values.size(); i++)
		{
			nc = values.get(i);
			if(nc.count > count)
			{
				this.default_value = nc.number;
				count = nc.count;
			}
		}
		
		//Add in non-default values
		for(int row=0; row<m.length; row++)
		{
			for(int column=0; column<m[row].length; column++)
			{
				if(m[row][column] != this.default_value)
				{
					addValue(m[row][column], row, column);
				}
			}
		}
	}

	
	//Constructor
	public SparseMatrix(int rows, int columns, int default_value)
	{
		this.rows = rows;
		this.columns = columns;
		this.default_value = default_value;
	}
	
	
	@Override
	public String toString()
	{
		/* TODO: Follow along as we replace this
		code with a version that displays the
		entire matrix. */
		return "Rows: "+rows+". Columns: "+columns;
	}
	
	
	public int[][] getMatrix()
	{
		int[][] matrix = new int[rows][columns];
		for(int row=0; row<rows; row++)
		{
			for(int column=0; column<columns; column++)
			{
				matrix[row][column] = default_value;
			}
		}
		
		//for each loop ("enhanced for loop" according to book)
		for(Triplet t : values)
		{
			matrix[t.row][t.col] = t.value;
		}
		
		return matrix;
	}


	public void addValue(int value, int row, int col)
	{
		Triplet t = new Triplet(value, row, col);
		values.add(t);
	}
	
	//getters
	public int getRows(){ return rows; }
	public int getColumns(){ return columns; }
	
	//Increase the count of toAdd in the ArrayList
	private void countNumber(ArrayList<NumberCounts> values, int toAdd)
	{
		for(NumberCounts nc : values) //for each
		{
			if(nc.number == toAdd)
			{
				nc.count++;
				return; //end the method
			}
		}
		//This is the case in which it's not found
		NumberCounts nc = new NumberCounts();
		nc.number = toAdd;
		values.add(nc);
	}
	
}