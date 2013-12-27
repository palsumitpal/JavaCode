import java.util.concurrent.*;
import java.util.*;

public class LevenshteinDistanceUsingDynamicProgrammingForkAndJoin
{
    
	static class LevenshteinDistanceUsingDynamicProgrammingForkAndJoinTask extends RecursiveTask<Integer> 
	{
	
		String str1, str2;
		
		LevenshteinDistanceUsingDynamicProgrammingForkAndJoinTask(String s1, String s2)
		{
			str1 = s1;
			str2 = s2;
		}

		@Override
		protected Integer compute()
		{
			if ( str1.length() == 0 )
				return str2.length();
			if ( str2.length() == 0 )
				return str1.length();
				
			String newStr1 = str1.substring(1);
			String newStr2 = str2.substring(1);
			if ( str1.charAt(0) == str2.charAt(0) )
			{
				LevenshteinDistanceUsingDynamicProgrammingForkAndJoinTask l = new LevenshteinDistanceUsingDynamicProgrammingForkAndJoinTask(newStr1, newStr2);
				return l.compute();
			}
			else
			{
				// get the String after the 1st Char of str1 and str2
				LevenshteinDistanceUsingDynamicProgrammingForkAndJoinTask t1 = new LevenshteinDistanceUsingDynamicProgrammingForkAndJoinTask(newStr1, newStr2);
				LevenshteinDistanceUsingDynamicProgrammingForkAndJoinTask t2 = new LevenshteinDistanceUsingDynamicProgrammingForkAndJoinTask(newStr1, str2);
				LevenshteinDistanceUsingDynamicProgrammingForkAndJoinTask t3 = new LevenshteinDistanceUsingDynamicProgrammingForkAndJoinTask(str1, newStr2);
				invokeAll(t1, t2, t3);
				return Math.min(1+t1.join(), Math.min(1+t2.join(), 1+t3.join()));
			}
		}
    }


    public static void main(String[] args)
    {
        String str1 = "BostonMassachusett";
        String str2 = "BostonMasachusets";
        str1 = "SumitPal";
        str2 = "SummitPaul";
		ForkJoinPool pool = new ForkJoinPool(20);
		if ( args.length >= 2 )
		{
			LevenshteinDistanceUsingDynamicProgrammingForkAndJoinTask l = new LevenshteinDistanceUsingDynamicProgrammingForkAndJoinTask(args[0], args[1]);
			Integer result = pool.invoke(l);
			System.out.println("Lev Distance between : " + args[0] + ", " + args[1] + " = " + result);
		}
		else
		{
			LevenshteinDistanceUsingDynamicProgrammingForkAndJoinTask l = new LevenshteinDistanceUsingDynamicProgrammingForkAndJoinTask(str1, str2);
			Integer result = pool.invoke(l);
			System.out.println("Lev Distance between : " + str1 + ", " + str2 + " = " + result);
		}
    }
}