import numpy as np
import codecs
import sys
import urllib.parse

#file name
file_name_ans = 'ans.txt'
file_name_ask = 'ask.txt'

#record ans's len number
lines = len(open(file_name_ans, 'r').readlines())

#write question
w = open(file_name_ask, 'a')
w.write(sys.argv[1])
w.write('\n')
w.close()

while(True):
    lines_update = len(open(file_name_ans, 'r').readlines())

    if(lines_update != lines):
        
        f = open(file_name_ans, 'r').read().split('\n')
        f = np.asarray(f)

        print(f[-2])
        break;
