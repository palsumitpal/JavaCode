import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.perf4j.*;
import javolution.util.FastTable;
import javolution.util.FastMap;

import java.util.Arrays;
import java.util.Random;

import java.util.concurrent.*;
import java.io.*;
import java.util.*;


public class MSBRadixSortingWithForkJoin
{
	final Logger logger = LoggerFactory.getLogger(MSBRadixSortingWithForkJoin.class);	
	int numbers;
	FastTable initialList;
	FastMap<Integer,FastTable> map;
	final int MAX_DIGITS_SUPPORTED = 10;
	
	public static void main(String[] args)
	{
		if ( args.length == 1 )
		{
			Integer numbers = Integer.valueOf(args[0]);
			MSBRadixSortingWithForkJoin msb = new MSBRadixSortingWithForkJoin(numbers);
		}
		else
			System.out.println("Specify 1 parameter - the name of the file which has integers to be sorted");
	}
	
	
	public MSBRadixSortingWithForkJoin(Integer num)
	{
		initialList = new FastTable();
		numbers = num;
		map = new FastMap<Integer,FastTable>();
		// Max Digits Supported
		for(int i=1;i<MAX_DIGITS_SUPPORTED+1;i++)
			map.put(i,new FastTable());
		startSorting();
	}
	
	private void startSorting()
	{
//		StopWatch stopWatch = new LoggingStopWatch("File Reading and Putting to List");
//		readDataIntoList();
		generateRandomData();
//		stopWatch.stop();
//		stopWatch = new LoggingStopWatch("Start Sorting");
		FastTable<String> f = sortFast();
//		stopWatch.stop();
	}

	private FastTable<String> sortFast()
	{
		try
		{
			FastTable<String> finalSortedList = new FastTable<String>();
			// get the List - Fast Table under each Key -- this list will have all numbers with same length
	//		logger.info("Before Sorting Whole List : " + map.toString());

			ForkJoinPool forkJoinPool = new ForkJoinPool(MAX_DIGITS_SUPPORTED);
			List<FastSort> listOfJobs = new ArrayList();
			
			for(int i=1;i<MAX_DIGITS_SUPPORTED+1;i++)
			{
				FastTable<String> sameLengthList = map.get(i);
	//			logger.info("Before Sorting : " + sameLengthList);
				FastSort sortItem = new FastSort(sameLengthList, 0, i);
				listOfJobs.add(sortItem);
			}

			List <Future<Entry>> results = forkJoinPool.invokeAll(listOfJobs);
			forkJoinPool.shutdown();

			// Print Output of Data
			for (Future<Entry> result : results) 
			{
				Entry e = result.get();
	            System.out.println(e.getDigit() + " -- " + e.getList());	// Print the sorted output
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return null;
	}
	
	private void printMap()
	{
		for(int i=1;i<MAX_DIGITS_SUPPORTED+1;i++)
			logger.info(map.get(i).toString());
	}
	
	
	private void generateRandomData()
	{
		int[] arr = makeRandomArray(numbers);
		for(int x : arr )
		{
			String s = Integer.toString(x);
			int length = s.length();
			FastTable t = map.get(length);
			if ( t != null )
				t.add(s);
		}

	}
	
	
	public int[] makeRandomArray(int arrSize) 
	{
		Random rand = new Random(System.currentTimeMillis());
		int[] data = new int[arrSize];
		for (int i = 0; i < data.length; i++) 
			data[i] = rand.nextInt();
		return data;
	}
	
/*	
	private void readDataIntoList()
	{
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader(fileName), 8192);
			String line;
			while ((line = in.readLine()) != null) 
			{
				int length = line.length();
				FastTable t = map.get(length);
				if ( t != null )
					t.add(line);
			}
			in.close();
		} 
		catch (Exception e) 
		{
			logger.error(" File Read Exception:" + e );
		}
	}
*/	


    class FastSort implements Callable<Entry> 
	{
		FastTable<String> listToSort;
		int digitToStartFrom;
		int maxDigitsToSortOn;
        FastSort(FastTable<String> list, int startDigit, int max) 
		{
			listToSort = list;
			digitToStartFrom = startDigit;
			maxDigitsToSortOn = max;
		}
        
        @Override
        public Entry call() 
		{
//			System.out.println("Starting to Sort - List with Digits = " + maxDigitsToSortOn);
			StopWatch stopWatch = new LoggingStopWatch("SortStartDigit" + maxDigitsToSortOn);
			FastTable<String> sorted = sortListRecursivelyUsingDigitsFromMSB(listToSort, digitToStartFrom, maxDigitsToSortOn);
			stopWatch.stop();
//			System.out.println("Completed Sorting on Digits = " + maxDigitsToSortOn);
			return new Entry(maxDigitsToSortOn, sorted);
        }   

		// sortOnDigit - is from the LHS - starts with 0 and gets incremented till it reaches maxDigitsInList in each recursive
		// call
		private FastTable<String> sortListRecursivelyUsingDigitsFromMSB(FastTable<String> inList, int sortOnDigit, int maxDigitsInList)
		{
	//		logger.info("Calling sortListRecursivelyUsingDigitsFromMSB : " + inList + ", sortOnDigit : " + sortOnDigit + ", maxDigitsInList : " + maxDigitsInList);
			if ( inList == null || inList.size() == 0 || inList.size() == 1)
				return inList;

			if ( sortOnDigit >= maxDigitsInList )
				return inList;
				
			FastTable<String> outList = new FastTable<String>();
				
			// Section 1 -- take each number from the list and look at the digit in the sortOnDigits place from LHS
			// then put each one into the BucketdMap - which is a map with list of numbers
			// the key for the map is the digit and the value is the list of numbers in inList - which gets added to it
			FastMap<String, FastTable<String>> bucketedMap = new FastMap<String, FastTable<String>>();
			for(String number: inList)
			{
	//			logger.info("In Step1 Number : " + number);
				char keyInMap = number.charAt(sortOnDigit);
	//			logger.info("In Step1 Key : " + keyInMap);
				FastTable<String> listForKey = bucketedMap.get(String.valueOf(keyInMap));
				if (listForKey != null )
					listForKey.add(number);
				else
				{
					FastTable<String> newListForKey = new FastTable<String>();
					newListForKey.add(number);
					bucketedMap.put(String.valueOf(keyInMap), newListForKey);
				}
			}

	//		logger.info("BucketMap after Step1 : " + bucketedMap.toString());
			
			// Now we do the recursive part of this Algorithm
			// we take each Value from the bucketedMap - which is a list of numbers - which all have the same
			// sortOnDigit - and then call the same method again - but this time sorting on the
			// sortOnDigit++ th digit
			if ( bucketedMap != null )
			{
				for(int i=0;i<=9;i++)
				{
					int currentDigit = sortOnDigit;
					Character c = Character.forDigit(i,10);
					String keyInMap = c.toString();
	//				logger.info("In Step2 Key : " + keyInMap);
					FastTable<String> sortedListForThisKey = bucketedMap.get(keyInMap);
	//				logger.info("In Step2 List : " + sortedListForThisKey);
					
					if ( sortedListForThisKey != null && sortedListForThisKey.size() != 0 && sortedListForThisKey.size() != 1)
						bucketedMap.put(keyInMap,sortListRecursivelyUsingDigitsFromMSB(sortedListForThisKey, ++currentDigit, maxDigitsInList));
				}
			}
		
			// if we have reached here - we have completed the recursive call for this and now we concatenate the list
			if ( bucketedMap != null )
			{
				for(int i=0;i<=9;i++)
				{
					Character c = Character.forDigit(i,10);
					String keyInMap = c.toString();
					FastTable<String> sortedListForThisKey = bucketedMap.get(keyInMap);
						
					if ( sortedListForThisKey != null )
						outList.addAll(outList.size(), sortedListForThisKey);
				}
			}
	//		logger.info("Step 3 Final Result : " + outList);
			return outList;
		}
		
		
    }
	
	class Entry
	{
		int digit;
		FastTable<String> numberList;
		
		public Entry(int d, FastTable<String> l)
		{
			digit = d;
			numberList = l;
		}
		
		public int getDigit() 
		{
			return digit;
		}
		
		public String getDigitToString()
		{
			return new Integer(digit).toString();
		}
		
		public FastTable<String> getList()
		{
			return numberList;
		}
	}
	
	
	
}




	