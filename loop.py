#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import os
import subprocess
import sys
import time


#g_command = "git pull".split(' ')
b_start = f"{sys.executable} bot.py".split(' ')
stopfile = "shutdown"

if os.path.isfile(stopfile):
    os.remove(stopfile)

print("\n\nStarting bot...\n\n")
while 1:
    #print("getting latest version")

    #callback = subprocess.call(g_command)
    #if callback != 0:
    #    break
    #print("\nstarting bot...\n\n")
    try:
        callback = subprocess.call(b_start)
    except Exception as e:
        print("ERROR: ", e)
        
    
    #check if there is a stopfile
    if os.path.isfile(stopfile):
        break
    time.sleep(2)
    print("\n\nRestarting bot...\n\n")