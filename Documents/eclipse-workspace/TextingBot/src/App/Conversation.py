
class Conversation():
    def __init__(self):
        self.__texts = []
        self._title = ""
    
    def setTitle(self, title):
        self._title = title
        
    def getTitle(self):
        return self.__title
    
    def addText(self, speaker, text):
        self.__texts.append((speaker, text))
    
    def getTexts(self):
        return self.__texts
    
    def getNumTexts(self):
        return len(self.__texts)
    
    def getLastText(self):
        return self.__texts(len(self.__texts))
    
    def toString(self):
        result = ""
        for line in self.__texts:
            result += line[0] + ": " + line[1] + "\n"
        return result


