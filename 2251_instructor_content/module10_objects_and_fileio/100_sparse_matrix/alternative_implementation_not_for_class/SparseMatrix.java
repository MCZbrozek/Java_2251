import java.util.ArrayList;

public class SparseMatrix
{
	private ArrayList<SparseElement> elements = new ArrayList<SparseElement>();
	private int default_value = 0;
	
	public SparseMatrix(int[][] to_convert, int default_value)
	{
		this.default_value = default_value;
		for(int r = 0; r<to_convert.length; r++)
		{
			for(int c = 0; c<to_convert[r].length; c++)
			{
				if(to_convert[r][c] != default_value)
				{
					SparseElement se = new SparseElement(r, c, to_convert[r][c]);
					elements.add(se);
				}
			}
		}
	}
}