import os
os.environ["CUDA_VISIBLE_DEVICES"] = "-1"
from App import TextBot
from App import Conversation

# try:
#     # Disable all GPUS
#     tf.config.set_visible_devices([], 'GPU')
#     visible_devices = tf.config.get_visible_devices()
#     for device in visible_devices:
#         assert device.device_type != 'GPU'
# except:
#     # Invalid device or cannot modify virtual devices once initialized.
#     pass

#############################
# TextBot
textFilePath = "text.txt"
tb = TextBot.TextBot(textFilePath)
tb.encode()
tb.load_model()

#############################
# Conversation
convo = Conversation.Conversation()

#############################
# Generate Text

speaker = 'Trevor'
talkingTo = 'Apu'

inp = input("Type a starting string: ")

convo.addText(speaker, inp)

response = tb.generate_text(speaker, talkingTo, convo)

# convo.addText(talkingTo, response)
convo.addText("", response)

outputFileName = "generatedText.txt"
with open(outputFileName, "w", encoding="utf8") as outputTextFile:
    outputTextFile.write(convo.toString())
#     for letter in generatedText:
#         outputTextFile.write(letter)

os.startfile(outputFileName)



# # from keras.preprocessing import sequence
# # import keras
# import tensorflow as tf
# import numpy as np
# import os
# 
# # Read, then decode for py2 compatibility.
# text = open("text.txt", 'rb').read().decode(encoding="utf-8")
# 
# ####################################################################################
# # Encoding
# vocab = sorted(set(text))
# 
# # Creating a mapping from unique characters to indices
# char2idx = {u:i for i, u in enumerate(vocab)}
# idx2char = np.array(vocab)
# 
# BATCH_SIZE = 32
# VOCAB_SIZE = len(vocab)  # vocab is number of unique characters
# EMBEDDING_DIM = 256
# RNN_UNITS = 1024
# 
# ####################################################################################
# # Building the Model
# def build_model(vocab_size, embedding_dim, rnn_units, batch_size):
#     model = tf.keras.Sequential([
#         tf.keras.layers.Embedding(vocab_size, embedding_dim,
#             batch_input_shape=[batch_size, None]),
#         tf.keras.layers.LSTM(rnn_units,
#             return_sequences=True,
#             stateful=True,
#             recurrent_initializer='glorot_uniform'),
#         tf.keras.layers.Dense(vocab_size)
#     ])
#     return model
# 
# ####################################################################################
# # Loading the Model
# # Directory where the checkpoints will be saved
# checkpoint_dir = './training_checkpoints'
# 
# model = build_model(VOCAB_SIZE, EMBEDDING_DIM, RNN_UNITS, batch_size=1)
# 
# model.load_weights(tf.train.latest_checkpoint(checkpoint_dir))
# model.build(tf.TensorShape([1, None]))
# 
# ####################################################################################
# # Generating Text
# def generate_text(model, start_string):
#     # Evaluation step (generating text using the learned model)
#     start_string = "Me: " + start_string + "\n"
#     # Number of characters to generate
#     num_generate = 800
#     
#     # Converting our start string to numbers (vectorizing)
#     input_eval = [char2idx[s] for s in start_string]
#     input_eval = tf.expand_dims(input_eval, 0)
#     
#     # Empty string to store our results
#     text_generated = []
#     
#     # Low temperatures results in more predictable text.
#     # Higher temperatures results in more surprising text.
#     # Experiment to find the best setting.
#     temperature = 0.5
#     
#     # Here batch size == 1
#     model.reset_states()
#     for i in range(num_generate):
#         predictions = model(input_eval)
#         # remove the batch dimension
#       
#         predictions = tf.squeeze(predictions, 0)
#     
#         # using a categorical distribution to predict the character returned by the model
#         predictions = predictions / temperature
#         predicted_id = tf.random.categorical(predictions, num_samples=1)[-1,0].numpy()
#     
#         # We pass the predicted character as the next input to the model
#         # along with the previous hidden state
#         input_eval = tf.expand_dims([predicted_id], 0)
#     
#         text_generated.append(idx2char[predicted_id])
#     
# #     return (start_string + ''.join(text_generated))
#     return ''.join(text_generated)
# 
# inp = input("Type a starting string: ")
# 
# generatedText = generate_text(model, inp)
# 
# 
# # for letter in generatedText:
# #     print(letter, end ="")
# 
# # print(generatedText)
# 
# outputFileName = "generatedText.txt"
# with open(outputFileName, "w", encoding="utf8") as outputTextFile:
#     outputTextFile.write(generatedText)
# #     for letter in generatedText:
# #         outputTextFile.write(letter)
# 
# os.startfile("generatedText.txt")
