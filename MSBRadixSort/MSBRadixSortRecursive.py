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

def sortListBasedOnNthDigit(listToBucketize, bucketizeOnDigit):
	numberOfDigitsInS = len(str(s[0]))
	tempSortedDict = {0:[],1:[],2:[],3:[],4:[],5:[],6:[],7:[],8:[],9:[]}
	if ( len(listToBucketize) == 1 ):
		return listToBucketize
	if ( bucketizeOnDigit == 0 ):
		return listToBucketize

	for num in listToBucketize:
		msb = getNthDigitFromMSB(num, bucketizeOnDigit)
		for key in tempSortedDict.keys():
			if key == msb:
				tempSortedDict[key].append(num)
				break
 	listToReturn = []

	if ( bucketizeOnDigit != numberOfDigitsInS ):
		for key, val in tempSortedDict.iteritems():
			newList = tempSortedDict.get(key)
			if ( len(newList) != 0 ):
				listToReturn.extend(sortListBasedOnNthDigit(newList, (bucketizeOnDigit+1)))
	else:
		for key, val in tempSortedDict.iteritems():
			listToReturn.extend(val)

	return listToReturn

f = open(sys.argv[1])
s = []
for line in f:
	num = int(line)
	s.append(num)
start_time = time.time()


inputDict = {}
for n in s:
	num = len(str(n))
	if num in inputDict:
		newList = [n]
		inputDict[num].extend(newList)
	else:
		inputDict[num] = [n]


sorted = []
start_time = time.time()
for key in inputDict:
	bucket_time = time.time()
	tempList = sortListBasedOnNthDigit(inputDict[key], 1)
	sorted.extend(tempList)
	print "Time to sort all the " + str(key) + " Digits Number : " + str(len(tempList)) + " -- " + str(time.time() - bucket_time) + " seconds"

print "\n\nSorted : " + str(sorted)
