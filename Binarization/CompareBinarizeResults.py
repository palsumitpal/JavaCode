#-------------------------------------------------------------------------------
# Name:        module1
# Purpose:
#
# Author:      SumitPal
#
# Created:     21/11/2013
# Copyright:   (c) SumitPal 2013
# Licence:     <your licence>
#-------------------------------------------------------------------------------
import sys

def compare(file01, file02, mapDict):
    for keys in mapDict.iterkeys:
        value = mapDict[keys]
        if(file01[keys] != file02[value]):
            return false, keys
    return true

def main():
    pass
if __name__ == '__main__':
    main()
inFileName01 = sys.argv[1]
inFileName02 = sys.argv[2]

#Map file name format - 1st file fieldNumber TAB    2nd file FielaNumber
mapFileName = sys.argv[3]


mapfileDict = {}
mapFile = open(mapFileName)
for line in mapFile.readlines():
    line = line.strip()
    listFromLine = line.split('\t')
    mapfileDict[listFromLine[0]] = listFromLine[1]

#print mapfileDict

inFile01 = open(inFileName01)
inFile02 = open(inFileName02)
numberOfLinesFile01 = len(inFile01.readlines())         #get the number of lines in the file1
numberOfLinesFile02 = len(inFile02.readlines())         #get the number of lines in the file2

# assume both the input files have 1st line as header
inFile01Line = ''
inFile02Line = ''

inFile01 = open(inFileName01)
inFile02 = open(inFileName02)
i = 0

# consume the header lines
inFile01Line = inFile01.readline()
inFile02Line = inFile02.readline()

match = True
while i < min(numberOfLinesFile01-1, numberOfLinesFile02-1):
    inFile01Line = inFile01.readline()
    inFile01Line = inFile01Line.strip()
    inFile01LineList = inFile01Line.split(',')

    inFile02Line = inFile02.readline()
    inFile02Line = inFile02Line.strip()
    inFile02LineList = inFile02Line.split(',')
    for keys in mapfileDict:
        value = mapfileDict[keys]
        if(inFile01LineList[int(keys)-1] != inFile02LineList[int(value)-1]):
            print "Line " + `i+1` + " does not match at File1 field " + `int(keys)` + " value - " + inFile01LineList[int(keys)-1] + " and File2 field " + `int(value)` + " value " + inFile02LineList[int(value)-1]
            match = False
            break
    if match == False:
        break
    i += 1

if match == True:
    print "All data matches "

