# from keras.preprocessing import sequence
# import keras
import tensorflow as tf
import numpy as np
import os
import re
import sys

def resource_path(relative_path):
    """ Get absolute path to resource, works for dev and for PyInstaller """
    try:
        # PyInstaller creates a temp folder and stores path in _MEIPASS
        base_path = getattr(sys, '_MEIPASS', '.') + '/'
    except Exception:
        base_path = os.path.abspath(".")

    return os.path.join(base_path, relative_path)

class TextBot():
    def __init__(self, textFile):
        # Read, then decode for py2 compatibility.
        self.text = open(textFile, 'rb').read().decode(encoding='utf-8')
        self.vocab = sorted(set(self.text))
        
#         self.BATCH_SIZE = 64
        self.BATCH_SIZE = 32  # Model 1, 2
        self.VOCAB_SIZE = len(self.vocab)  # vocab is number of unique characters
        self.EMBEDDING_DIM = 256
        self.RNN_UNITS = 1024  # Model 1, 2
        
        # Buffer size to shuffle the dataset
        # (TF data is designed to work with possibly infinite sequences,
        # so it doesn't attempt to shuffle the entire sequence in memory. Instead,
        # it maintains a buffer in which it shuffles elements).
        self.BUFFER_SIZE = 10000
        
        # Directory where the checkpoints will be saved
        self.checkpoint_dir = resource_path('training_checkpoints')
        
    def split_input_target(self, chunk):  # for the example: hello
        input_text = chunk[:-1]  # hell
        target_text = chunk[1:]  # ello
        return input_text, target_text  # hell, ello
    
    ####################################################################################
    # Encoding
    def text_to_int(self, text):
        return np.array([self.char2idx[c] for c in text])
    
    def int_to_text(self, ints):
        # convert numeric values to text.
        try:
            ints = ints.numpy()
        except:
            pass
        return ''.join(self.idx2char[ints])
    
    def encode(self):
        # Creating a mapping from unique characters to indices
        self.char2idx = {u:i for i, u in enumerate(self.vocab)}
        self.idx2char = np.array(self.vocab)
        self.text_as_int = self.text_to_int(self.text)
    
    ####################################################################################
    # Creating Training Examples
    def create_training_examples(self):
        self.seq_length = 100  # length of sequence for a training example
        self.examples_per_epoch = len(self.text)//(self.seq_length+1)
        
        # Create training examples / targets
        self.char_dataset = tf.data.Dataset.from_tensor_slices(self.text_as_int)
        self.sequences = self.char_dataset.batch(self.seq_length+1, drop_remainder=True)
        self.dataset = self.sequences.map(self.split_input_target)  # we use map to apply the above function to every entry
        self.data = self.dataset.shuffle(self.BUFFER_SIZE).batch(self.BATCH_SIZE, drop_remainder=True)

    ####################################################################################
    # Building the Model
#     def __build_model(self, vocab_size, embedding_dim, rnn_units, batch_size):
#         model = tf.keras.Sequential([
#             tf.keras.layers.Embedding(vocab_size, embedding_dim,
#                 batch_input_shape=[batch_size, None]),
#             tf.keras.layers.LSTM(rnn_units,
#                 return_sequences=True,
#                 stateful=True,
#                 recurrent_initializer='glorot_uniform'),
#             tf.keras.layers.LSTM(rnn_units,
#                 return_sequences=True,
#                 stateful=True,
#                 recurrent_initializer='glorot_uniform'),
#             tf.keras.layers.Dense(vocab_size)
#         ])
#         return model

#     def __build_model(self, vocab_size, embedding_dim, rnn_units, batch_size):
#         forward_layer = tf.keras.layers.LSTM(rnn_units,
#                                              return_sequences=True,
#                                              stateful=True,
#                                              recurrent_initializer='glorot_uniform')
# #         backward_layer = tf.keras.layers.LSTM(rnn_units,
# #                                              return_sequences=True,
# #                                              stateful=True,
# #                                              recurrent_initializer='glorot_uniform',
# #                                              go_backwards=True)
#         model = tf.keras.Sequential([
#             tf.keras.layers.Embedding(vocab_size, embedding_dim,
#                                       batch_input_shape=[batch_size, None]),
# #             tf.keras.layers.Bidirectional(forward_layer, 
# #                                           backward_layer=backward_layer),
#             tf.keras.layers.Bidirectional(forward_layer),
# #             tf.keras.layers.LSTM(rnn_units,
# #                                 return_sequences=True,
# #                                 stateful=True,
# #                                 recurrent_initializer='glorot_uniform'),
# #             tf.keras.layers.LSTM(rnn_units,
# #                                 return_sequences=True,
# #                                 stateful=True,
# #                                 recurrent_initializer='glorot_uniform',
# #                                 go_backwards=True),
#             tf.keras.layers.Dense(vocab_size)
#         ])
#         return model
    
    def __build_model(self, vocab_size, embedding_dim, rnn_units, batch_size):
        model = tf.keras.Sequential([
            tf.keras.layers.Embedding(vocab_size, embedding_dim,
                batch_input_shape=[batch_size, None]),
            tf.keras.layers.LSTM(2*rnn_units,
                return_sequences=True,
                stateful=True,
                recurrent_initializer='glorot_uniform'),
            tf.keras.layers.Dense(vocab_size)
        ])
        return model

    def build_model(self):
        self.model = self.__build_model(self.VOCAB_SIZE,self.EMBEDDING_DIM, self.RNN_UNITS, self.BATCH_SIZE)
    
    def model_summary(self):
        return self.model.summary()

    ####################################################################################
    # Creating a Loss Function
    # loss function that can compare that output to the expected output and give us some numeric value representing how close the two were.
    def loss(self, labels, logits):
        return tf.keras.losses.sparse_categorical_crossentropy(labels, logits, from_logits=True)
    
    ####################################################################################
    # Compiling the Model
    def compile_model(self):
        self.model.compile(optimizer='adam', loss=self.loss)
    
    ####################################################################################
    # Creating Checkpoints
    def create_checkpoints(self):
        # Name of the checkpoint files
        self.checkpoint_prefix = os.path.join(self.checkpoint_dir, "ckpt_{epoch}")
        
        self.checkpoint_callback = tf.keras.callbacks.ModelCheckpoint(
#                 monitor='val_loss',
                filepath=self.checkpoint_prefix,
                save_weights_only=True)
    
    ####################################################################################
    # Training
    def train_model(self):
        self.history = self.model.fit(self.data, epochs=50, verbose=2, callbacks=[self.checkpoint_callback])

    ####################################################################################
    # Loading the Model from Checkpoint
    def load_model_from_checkpoint(self):
        self.model = self.__build_model(self.VOCAB_SIZE, self.EMBEDDING_DIM, self.RNN_UNITS, batch_size=1)
        self.model.load_weights(tf.train.latest_checkpoint(self.checkpoint_dir))
        self.model.build(tf.TensorShape([1, None]))
    
    ####################################################################################
    # Loading the Model
    def load_model(self):
        self.model = tf.keras.models.load_model(resource_path("model.h5"))
#         self.model.load("model.h5")
     
    ####################################################################################
    # Save the Model
    def save_model(self):
        self.model.save("model.h5")
    
    ####################################################################################
    # Generating Text
    def __generate_text(self, model, start_string):
        # Evaluation step (generating text using the learned model)
        # Number of characters to generate
        self.num_generate = 500
        
        # Converting our start string to numbers (vectorizing)
        input_eval = [self.char2idx[s] for s in start_string]
        input_eval = tf.expand_dims(input_eval, 0)
        
        # Empty string to store our results
        text_generated = []
        
        # Low temperatures results in more predictable text.
        # Higher temperatures results in more surprising text.
        # Experiment to find the best setting.
        temperature = 0.8
        
        # Here batch size == 1
        self.model.reset_states()
        for i in range(self.num_generate):
            predictions = model(input_eval)
            
            # remove the batch dimension
            predictions = tf.squeeze(predictions, 0)
        
            # using a categorical distribution to predict the character returned by the model
            predictions = predictions / temperature
            predicted_id = tf.random.categorical(predictions, num_samples=1)[-1,0].numpy()
        
            # Pass the predicted character as the next input to the model
            # along with the previous hidden state
            input_eval = tf.expand_dims([predicted_id], 0)
        
            text_generated.append(self.idx2char[predicted_id])
        
#         return (start_string + ''.join(text_generated))
        return ''.join(text_generated)
    
    def startOfLine(self, talkingTo):
        if talkingTo == 'Apu':
            return 'Me: '
        elif talkingTo == 'Trevor':
            return 'Apu: '
        else:
            return ""
        
    def speakerStartOfLine(self, speaker):
        if speaker == 'Trevor':
            return 'Me: '
        elif speaker == 'Apu':
            return 'Apu: '
        else:
            return ''

    def generate_text(self, speaker, talkingTo, conversation):
        # Modify start_string
        start_string = self.__conversationToTextString(conversation)
        
        # Generate text
        gen_text = self.__generate_text(self.model, start_string)
          
        # Parse generated text
        return self.__parseGeneratedText(speaker, talkingTo, gen_text)
    
    def __conversationToTextString(self, conversation):
        result = ""
        for line in conversation.getTexts():
            result += self.speakerStartOfLine(line[0]) + line[1] + "\n"
        return result
    
    def __parseGeneratedText(self, speaker, talkingTo, genText):
        result = ""
        speakerStart = self.speakerStartOfLine(speaker)
        talkingToStart = self.speakerStartOfLine(talkingTo)
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
