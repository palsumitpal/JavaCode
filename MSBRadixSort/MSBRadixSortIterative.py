import time
import sys

# get the Digit in a given place
def getNthDigitFromMSB(s, num):
	digits = len(str(s))
	modNumber = 10**(digits-num+1)
	modNumber1 = 10**(digits-num)
	remainder = s/modNumber
	digit = (s-remainder*modNumber)/modNumber1
	return digit


def bucketize(listToBucketize, bucketizeOnDigit):
	bucket = {0:[],1:[],2:[],3:[],4:[],5:[],6:[],7:[],8:[],9:[]}
	for num in listToBucketize:
		msb = getNthDigitFromMSB(num, bucketizeOnDigit)
		for key in bucket.keys():
			if key == msb:
				bucket[key].append(num)
				break
	return bucket

def sortListBasedOnNthDigit(listToBucketize, bucketizeOnDigit):
	numberOfDigitsInOriginalList = len(str(s[0]))
	tempSortedDict = {0:[],1:[],2:[],3:[],4:[],5:[],6:[],7:[],8:[],9:[]}
	if ( len(listToBucketize) == 1 ):
		return listToBucketize
	if ( bucketizeOnDigit == 0 ):
		return listToBucketize

	tempSortedDict = bucketize(listToBucketize, bucketizeOnDigit)
	listToReturn = []
	listOfLists = []
	# now we have bucketized on the 1st MSB Already
	# Take each Bucket's list starting from 0th Bucket and further bucketize each list using the next MSB till u reach MSB = numberOfDigits
	for key, val in tempSortedDict.iteritems():
		if ( len(val) != 0 ):
			listOfLists.append(val)

	if ( numberOfDigitsInOriginalList == 1 ):
		for l in listOfLists:
			listToReturn.extend(l)
		return listToReturn

	while (len(listOfLists) != 0):
		for i in range(2,numberOfDigitsInOriginalList+1):
			subList = listOfLists.pop(0)
			tempBucket = bucketize(subList, i);
			loc = 0
			for key, val in tempBucket.iteritems():
				if ( (i == numberOfDigitsInOriginalList) ):
					listToReturn.extend(val)
				elif ( len(val) > 0):
					listOfLists.insert(loc,val)
					loc+=1

	return listToReturn

f = open(sys.argv[1])
s = []
for line in f:
	num = int(line)
	s.append(num)

start_time = time.time()
#break up the incoming list into a dict of List - where each list has numbers with the same number of digits
inputDict = {}
for n in s:
	num = len(str(n))
	if num in inputDict:
		newList = [n]
		inputDict[num].extend(newList)
	else:
		inputDict[num] = [n]
#print "Time to get the Initial Bucket : " + str(time.time() - start_time), " seconds"

sorted = []
start_time = time.time()
for key in inputDict:
	bucket_time = time.time()
	tempList = sortListBasedOnNthDigit(inputDict[key], 1)
	sorted.extend(tempList)
	print "Time to sort all the " + str(key) + " Digit Numbers : " + str(len(tempList)) + " -- "  + str(time.time() - bucket_time), " seconds"
#print time.time() - start_time, " seconds"
print sorted
