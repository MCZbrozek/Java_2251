import java.util.Arrays;

public class StringSplit
{
	public static void main(String[] args)
	{
		String CSV = "Google,Apple,Microsoft";
		String[] values = CSV.split(",");
		System.out.println(Arrays.toString(values));
	}
}