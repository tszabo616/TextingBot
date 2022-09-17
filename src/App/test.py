# from keras.preprocessing import sequence
# import keras
# import tensorflow as tf
# import os
# import numpy as np
import re

# print(chr(100))
# 
# print(ord("#"))

# with open("generatedText.txt", "r", encoding="utf8") as textFile:
#     
#     for line in textFile:
#         for letter in line:
#             print(ord(letter))

# print("\U0001F600")

# for i in range(1,1033):
#     try:
#         print(i, ": ", chr(i))
#     except:
#         continue

def speakerStartOfLine(speaker):
        if speaker == 'Trevor':
            return 'Me: '
        elif speaker == 'Apu':
            return 'Apu: '
        else:
            return ''

def parseGeneratedText(speaker, talkingTo, genText):
    result = ""
    speakerStart = speakerStartOfLine(speaker)
    talkingToStart = speakerStartOfLine(talkingTo)
    found = 0
    
    speakerPattern = "^" + speakerStart + ".*"
    talkingToPattern = "^" + talkingToStart + "(.*)"
    genText = genText.split("\n")
    
    for line in genText:
        if (line == '') or (line == ' '): continue
        
        speakerMatch = re.search(speakerPattern, line)
        talkingToMatch = re.search(talkingToPattern, line)

        if (found >= 1) and (speakerMatch != None): break
        
        if talkingToMatch != None:
            result += talkingToMatch.group(1) + "\n"
            found += 1
        
        if (found >= 1) and (talkingToMatch == None) and (speakerMatch == None):
            result += line + "\n"
    return result
        

genText = """
Me: The house.

Apu: Nick's birthday,he saw your family? Michael and Lauren still in NYC?? 

Apu: P.s. I'm just going to start calling you a disticion seems right now

Apu: WE HAVE A TUX

Me: For Lauren's wedding

Me: Not sure when that'll be like a drop in the bucket. 

How does it point to there isn't one? I like your thinking cause it's basically me questioning why even have the word speculative means

I agree that there has to be done for a project. None of it sound like conjecture only has a negative
"""

# print(genText)

speaker = 'Trevor'
talkingTo = 'Apu'

# speaker = 'Apu'
# talkingTo = 'Trevor'

result = parseGeneratedText(speaker, talkingTo, genText)

print(result)
