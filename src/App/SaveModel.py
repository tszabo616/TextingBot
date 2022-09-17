from App import TextBot

#############################
# TextBot
textFilePath = "text.txt"
tb = TextBot.TextBot(textFilePath)
tb.encode()
tb.load_model_from_checkpoint()
tb.save_model()

