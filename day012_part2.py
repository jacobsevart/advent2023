import sys
from collections import defaultdict

def checkMask(mask, rightPortion):
    revMask = list(reversed(mask))
    revHalf = list(reversed(rightPortion))
    for x in range(0, len(rightPortion)):
        if revMask[x] != revHalf[x] and revMask[x] is not None:
            return False
    
    return True

def runs(xs, totalLength, mask):
    memo = defaultdict(lambda: 0)
    leftSpaceTaken = sum(xs) + len(xs) # reserve space for each x with 1 in between

    for xIndex in range(len(xs) - 1, -1, -1):
        leftSpaceTaken -= xs[xIndex] + 1 # take space for this guy
        minPlausibleLength = sum(xs[xIndex:])

        for length in range(minPlausibleLength, totalLength - leftSpaceTaken + 1):
            for leftpad in range(0, length - xs[xIndex] + 1):
                rowPrefix = [0] * leftpad
                rowPrefix += [1] * xs[xIndex]

                key = (xIndex, length)
                if xIndex == len(xs) - 1: # this is the last one
                    row = rowPrefix + [0] * (length - xs[xIndex] - leftpad)
                    if not checkMask(mask, row):
                        continue

                    memo[key] += 1
                else:
                    retrieveKey = (xIndex + 1, (length - xs[xIndex] - leftpad - 1))
                    if retrieveKey not in memo:
                        continue

                    if not checkMask(mask[:-retrieveKey[1]], rowPrefix + [0]):
                        continue

                    memo[key] += memo[retrieveKey]
    
    return memo[(0, totalLength)]

maskToStr = {1: '#', 0: '.', None: '?'}
maskFromStr = {y: x for x, y in maskToStr.items()}


def multiplySeries(series, n):
    out = []
    for x in range(0, n):
        out += series
        if x < n - 1:
            out += [None]
    return out

def parseMask(maskStr):
    return [maskFromStr[x] for x in maskStr]

def renderMask(mask):
    return ''.join([maskToStr[x] for x in mask])

def checkKnown():
    testCases = [
        ("???.###", [1, 1, 3], 1, 1),
        (".??..??...?##.", [1, 1, 3], 4, 16384),
        ("?#?#?#?#?#?#?#?", [1, 3, 1, 6], 1, 1),
        ("????.#...#...", [4, 1, 1], 1, 16),
        ("????.######..#####.", [1, 6, 5], 4, 2500),
        ("?###????????", [3, 2, 1], 10, 506250)
    ]

    for maskStr, lengths, expectSmall, expectBig in testCases:
        print(maskStr, lengths)

        mask = parseMask(maskStr)
        gotSmall = runs(lengths, len(mask), mask)
        if gotSmall != expectSmall:
            raise Exception("expected small from %s: %d got %d" % (maskStr, expectSmall, gotSmall))
        
        print("good small")
        
        bigMask = multiplySeries(mask, 5)
        gotBig = runs(lengths * 5, len(bigMask), bigMask)
        if gotBig != expectBig:
            raise Exception("expected big: %d got %d" % (expectBig, gotBig))
        
        print("good big")

if __name__ == '__main__':
    checkKnown()

    MULTIPLY = 5
    acc = 0
    for line in sys.stdin:
        fst, snd = line.split(" ")
        mask = multiplySeries(parseMask(fst), MULTIPLY)
        nums = [int(x) for x in snd.split(",")]
        foundRuns = runs(nums * MULTIPLY, len(mask), mask)

        print(foundRuns, renderMask(mask), nums)
        acc += foundRuns
    
    print(acc)