# Mobil-Virtual-Assistant

## What is this?
It is a vitual asssitant application that work on android device which can help you do some simple tasks such as below:

1.打開/關掉wifi (turn on/off wifi)

2.打開/關掉藍芽 (turn on/off the bletooth)

3.調亮/調暗銀幕亮度 (brighten/darken the screen)

4.調大/調小音量 (volume up/down)

5.打開相機 (open the camera)

6.關機 (turn off the device)

7.簡單自我介紹 (introduction)

## Language
This virtual assistant can only understand chinese, but can be transformed into English or other languages if there are training data, our model is fixable for different languages.

## How is works?
The basic idea of this app is shown as below:
1. Speech to text (transform users command of voice into texts)
2. Send the text back to the server by internet.
3. Server will geenrate the correspoding answer about what shoud robot do and send back to android device by internet.
4. Android device get the answer and finish the task and use text to speech tool to reponse users' command.

## What happen in the server?
Given a command, robot need to know what it means and what to do next, so the sever is responsible for this part of task.
There is a deep learning model in the server which called seq2seq model, so given a command ssntence as input, this model will 
output the corresponding todo sequence and to-answer sequence.

Todo sequence is the action that robot have a complete, for example:
1. 1 5 (1 means turn on; 5 means wifi)
2. 2 5 (2 means turn off; 5 means wifi)
so when robot get a todo sequence of 1 5, it will turn on the wifi and vice versa.

What about to-answer sequence? to-answer sequence is the sentence that robot will response to users such as
"No problem, wifi is turning on." when user commadn to turn on the wifi.

For more details about the model, you can chech my github project about virtual assistant.
https://github.com/r06922085/Virtual-Assistant

## Author
https://github.com/r06922085
