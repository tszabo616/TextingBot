from App import TextBot

#############################
# TextBot
textFilePath = "text.txt"
tb = TextBot.TextBot(textFilePath)
tb.encode()
tb.create_training_examples()
tb.build_model()
tb.model_summary()
tb.compile_model()
tb.create_checkpoints()
tb.train_model()


# # from keras.preprocessing import sequence
# # import keras
# import tensorflow as tf
# import numpy as np
# import os
# 
# def split_input_target(chunk):  # for the example: hello
#     input_text = chunk[:-1]  # hell
#     target_text = chunk[1:]  # ello
#     return input_text, target_text  # hell, ello
# 
# # Read, then decode for py2 compatibility.
# text = open("text.txt", 'rb').read().decode(encoding='utf-8')
# 
# ####################################################################################
# # Encoding
# vocab = sorted(set(text))
# # Creating a mapping from unique characters to indices
# char2idx = {u:i for i, u in enumerate(vocab)}
# idx2char = np.array(vocab)
# 
# def text_to_int(text):
#     return np.array([char2idx[c] for c in text])
# 
# text_as_int = text_to_int(text)
# 
# def int_to_text(ints):
#     # convert numeric values to text.
#     try:
#         ints = ints.numpy()
#     except:
#         pass
#     return ''.join(idx2char[ints])
# 
# ####################################################################################
# # Creating Training Examples
# seq_length = 100  # length of sequence for a training example
# examples_per_epoch = len(text)//(seq_length+1)
# 
# # Create training examples / targets
# char_dataset = tf.data.Dataset.from_tensor_slices(text_as_int)
# 
# sequences = char_dataset.batch(seq_length+1, drop_remainder=True)
# 
# dataset = sequences.map(split_input_target)  # we use map to apply the above function to every entry
# 
# # BATCH_SIZE = 64
# BATCH_SIZE = 32
# VOCAB_SIZE = len(vocab)  # vocab is number of unique characters
# EMBEDDING_DIM = 256
# RNN_UNITS = 1024
# 
# # Buffer size to shuffle the dataset
# # (TF data is designed to work with possibly infinite sequences,
# # so it doesn't attempt to shuffle the entire sequence in memory. Instead,
# # it maintains a buffer in which it shuffles elements).
# BUFFER_SIZE = 10000
# 
# data = dataset.shuffle(BUFFER_SIZE).batch(BATCH_SIZE, drop_remainder=True)
# 
# ####################################################################################
# # Building the Model
# # def build_model(vocab_size, embedding_dim, rnn_units, batch_size):
# #     model = tf.keras.Sequential([
# #         tf.keras.layers.Embedding(vocab_size, embedding_dim,
# #             batch_input_shape=[batch_size, None]),
# #         tf.keras.layers.LSTM(rnn_units,
# #             return_sequences=True,
# #             stateful=True,
# #             recurrent_initializer='glorot_uniform'),
# #         tf.keras.layers.Dense(vocab_size)
# #     ])
# #     return model
# 
# def build_model(vocab_size, embedding_dim, rnn_units, batch_size):
#     model = tf.keras.Sequential([
#         tf.keras.layers.Embedding(vocab_size, embedding_dim,
#             batch_input_shape=[batch_size, None]),
#         tf.keras.layers.LSTM(rnn_units,
#             return_sequences=True,
#             stateful=True,
#             recurrent_initializer='glorot_uniform'),
#         tf.keras.layers.LSTM(rnn_units,
#             return_sequences=True,
#             stateful=True,
#             recurrent_initializer='glorot_uniform'),
#         tf.keras.layers.Dense(vocab_size)
#     ])
#     return model
# 
# model = build_model(VOCAB_SIZE,EMBEDDING_DIM, RNN_UNITS, BATCH_SIZE)
# model.summary()
# 
# ####################################################################################
# # Creating a Loss Function
# # for input_example_batch, target_example_batch in data.take(1):
# #     example_batch_predictions = model(input_example_batch)  # ask our model for a prediction on our first batch of training data (64 entries)
# #     print(example_batch_predictions.shape, "# (batch_size, sequence_length, vocab_size)")  # print out the output shape
# # 
# # # The prediction is an array of 64 arrays, one for each entry in the batch
# # print(len(example_batch_predictions))
# # print(example_batch_predictions)
# # 
# # # lets examine one prediction
# # pred = example_batch_predictions[0]
# # print(len(pred))
# # print(pred)
# # # notice this is a 2d array of length 100, where each interior array is the prediction for the next character at each time step
# # 
# # # and finally well look at a prediction at the first time step
# # time_pred = pred[0]
# # print(len(time_pred))
# # print(time_pred)
# # # and of course its 65 values representing the probability of each character occurring next
# # 
# # # If we want to determine the predicted character we need to sample the output distribution (pick a value based on probabillity)
# # sampled_indices = tf.random.categorical(pred, num_samples=1)
# # 
# # # now we can reshape that array and convert all the integers to numbers to see the actual characters
# # sampled_indices = np.reshape(sampled_indices, (1, -1))[0]
# # predicted_chars = int_to_text(sampled_indices)
# #  
# # predicted_chars  # and this is what the model predicted for training sequence 1
# 
# # loss function that can compare that output to the expected output and give us some numeric value representing how close the two were.
# def loss(labels, logits):
#     return tf.keras.losses.sparse_categorical_crossentropy(labels, logits, from_logits=True)
# 
# ####################################################################################
# # Compiling the Model
# model.compile(optimizer='adam', loss=loss)
# 
# ####################################################################################
# # Creating Checkpoints
# # Directory where the checkpoints will be saved
# checkpoint_dir = './training_checkpoints'
# # Name of the checkpoint files
# checkpoint_prefix = os.path.join(checkpoint_dir, "ckpt_{epoch}")
# 
# checkpoint_callback = tf.keras.callbacks.ModelCheckpoint(
#     filepath=checkpoint_prefix,
#     save_weights_only=True)
# 
# ####################################################################################
# # Training
# history = model.fit(data, epochs=50, callbacks=[checkpoint_callback])
# 
# ####################################################################################
# # Loading the Model
# # model = build_model(VOCAB_SIZE, EMBEDDING_DIM, RNN_UNITS, batch_size=1)
# # 
# # model.load_weights(tf.train.latest_checkpoint(checkpoint_dir))
# # model.build(tf.TensorShape([1, None]))












