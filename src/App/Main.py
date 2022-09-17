import os
os.environ["CUDA_VISIBLE_DEVICES"] = "-1"
import sys
from App import TextBot
from App import Conversation
import tkinter as tk
# import tensorflow as tf

# try:
#     # Disable all GPUS
#     tf.config.set_visible_devices([], 'GPU')
#     visible_devices = tf.config.get_visible_devices()
#     for device in visible_devices:
#         assert device.device_type != 'GPU'
# except:
#     # Invalid device or cannot modify virtual devices once initialized.
#     pass

def resource_path(relative_path):
    """ Get absolute path to resource, works for dev and for PyInstaller """
    try:
        # PyInstaller creates a temp folder and stores path in _MEIPASS
        base_path = getattr(sys, '_MEIPASS', '.') + '/'
    except Exception:
        base_path = os.path.abspath(".")

    return os.path.join(base_path, relative_path)

#############################
# TextBot
textFilePath = resource_path("text.txt")
tb = TextBot.TextBot(textFilePath)
tb.encode()
# tb.load_model_from_checkpoint()
tb.load_model()

#############################
# Conversation
convo = Conversation.Conversation()

#############################
# GUI
def getSpeaker():
    if talkingTo.get() == 'Apu':
        return 'Trevor'
    else:
        return 'Apu'

def send():
    convo.addText(getSpeaker(), textField.get('1.0', 'end'))
    response = tb.generate_text(getSpeaker(), talkingTo.get(), convo)
    convo.addText(talkingTo.get(), response)
    sentLabel = tk.Label(messagesFrame, text=textField.get('1.0', 'end'), wraplength=225, justify="left", anchor="w")
    sentLabel.grid(row=convo.getNumTexts()-1, column=1, sticky='ne', padx=5, pady=5)
    responseLabel = tk.Label(messagesFrame, text=response, wraplength=225, justify="left", anchor="w")
    responseLabel.grid(row=convo.getNumTexts(), column=0, sticky='nw', padx=5, pady=5)
    textField.delete('1.0', 'end')

def clearFrame():
    # destroy all widgets from frame
    for widget in messagesFrame.winfo_children():
        widget.destroy()
    
    # clear frame and frame will be empty
    messagesFrame.grid_forget()

def resetConversation():
    convo = Conversation.Conversation()
    clearFrame()
    
root = tk.Tk()
root.title('Apu Chat')
root.geometry("510x700")
root.resizable(width=False, height=False)

icon = tk.PhotoImage(file = resource_path("Media/Apu No Background.png"))
root.iconphoto(False, icon)

talkToLabel = tk.Label(root, text="Talk To:")

talkingTo = tk.StringVar()
talkingTo.set('Apu')
talkingTo
ApuButton = tk.Radiobutton(root, text='Apu', variable=talkingTo, value='Apu', command=resetConversation)
TrevorButton = tk.Radiobutton(root, text='Trevor', variable=talkingTo, value='Trevor', command=resetConversation)

textField = tk.Text(root, width=30, height=2)

sendButton = tk.Button(root, text='Send', command=send)

# Configure Scrollable Messages Frame
container = tk.Frame(root, bd=2, relief=tk.GROOVE)
canvas = tk.Canvas(container, width=470, height=525, bg="white")
scrollbar = tk.Scrollbar(container, orient="vertical", command=canvas.yview)
messagesFrame = tk.Frame(canvas, width=470, height=525, bg="white")
messagesFrame.grid_columnconfigure(index=0, minsize=200) #, maxsize=225)
messagesFrame.columnconfigure(index=1, minsize=200) #, maxsize=225)

messagesFrame.bind(
    "<Configure>",
    lambda e: canvas.configure(
        scrollregion=canvas.bbox("all")
    )
)
canvas.create_window((0, 0), window=messagesFrame, anchor="nw")
canvas.configure(yscrollcommand=scrollbar.set)

# Place Widgets
talkToLabel.grid(row=0, column=0, sticky='w')
ApuButton.grid(row=1, column=0, sticky='w')
TrevorButton.grid(row=2, column=0, sticky='w')
textField.grid(row=3, column=0, sticky='we', padx=5, pady=5)
sendButton.grid(row=4, column=0, sticky='e', padx=5, pady=5)
# messagesFrame.grid(row=5, column=0, sticky='we', padx=5, pady=10)
container.grid(row=5, column=0, sticky='we', padx=5, pady=10)
canvas.pack(side="left", fill="both", expand=True)
scrollbar.pack(side="right", fill="y")

root.mainloop()


