import java.util.*;

public class LevenshteinDistancePlain
{
    private final String str1;
    private final String str2;
    private final int str1Len;
    private final int str2Len;
    
	int[][] matrix;
    LevenshteinDistancePlain(String str1, String str2)
    {
        this.str1 = str1;
        this.str2 = str2;
        this.str1Len = str1.length();
        this.str2Len = str2.length();
		
		matrix = new int[str1Len][];
		for(int i=0;i<str1Len;i++)
			matrix[i] = new int[str2Len];
    }
    
	protected Integer compute()
    {
		for(int i=0;i<str1Len;i++)
			matrix[i][0] = i;
			
		for(int i=0;i<str2Len;i++)
			matrix[0][i] = i;
			
		for(int i=1;i<str1Len;i++)
		{
			for(int j=1;j<str2Len;j++)
			{
				if ( str1.charAt(i) == str2.charAt(j) )
					matrix[i][j] = matrix[i-1][j-1];
				else
				{
					int one = matrix[i-1][j] + 1;
					int two = matrix[i][j-1] + 1;	
					int three = matrix[i-1][j-1] + 1;
					
					matrix[i][j] = Math.min(one, Math.min(two, three));
				}
			}
		}
		return matrix[str1Len-1][str2Len-1];
    }
	
	public void printMatrix()
	{
		System.out.println("Distance Matrix");
	
		for(int i=0;i<str1Len;i++)
		{
			for(int j=0;j<str2Len;j++)
			{
				System.out.print(matrix[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
    
    public static void main(String[] args)
    {
        String str1 = "BostonMassachusett";
        String str2 = "BostonMasachuset";
		if ( args.length >= 2 )
		{
			LevenshteinDistancePlain l = new LevenshteinDistancePlain(args[0], args[1]);
			int result = l.compute();
			System.out.println("Lev Distance Between : " + args[0] + ", " + args[1] + "= " + result);
			l.printMatrix();
		}
		else
		{
			LevenshteinDistancePlain l = new LevenshteinDistancePlain(str1, str2);
			int result = l.compute();
			System.out.println("Lev Distance Between : " + str1 + ", " + str2 + "= " + result);
			l.printMatrix();
		}
    }
}