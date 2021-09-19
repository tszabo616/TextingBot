import re

pattern = "^\[[0-9]+/[0-9]+/[0-9]+.*?\]\s(.*)"
fileName = "Apu___908__655-8916_.txt"
newFileName = "text.txt"
with open(fileName, "r", encoding="utf8") as textFile:
    with open(newFileName, "w", encoding="utf8") as newTextFile:
        i = 0
        for line in textFile:
            if i > 0:
                m = re.search(pattern, line)
                
                if m != None:
                    line = m.group(1)
                newTextFile.write(line)
            i += 1

