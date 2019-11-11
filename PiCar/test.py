import sys
import atexit
import time
import select
import tty
import termios
import Moter

old_settings=None

def init_anykey():
   global old_settings
   old_settings = termios.tcgetattr(sys.stdin)
   new_settings = termios.tcgetattr(sys.stdin)
   new_settings[3] = new_settings[3] & ~(termios.ECHO | termios.ICANON) # lflags
   new_settings[6][termios.VMIN] = 0  # cc
   new_settings[6][termios.VTIME] = 0 # cc
   termios.tcsetattr(sys.stdin, termios.TCSADRAIN, new_settings)

@atexit.register
def term_anykey():
   global old_settings
   if old_settings:
      termios.tcsetattr(sys.stdin, termios.TCSADRAIN, old_settings)

init_anykey()
control=Moter.Moter(17,18,27,22)
p='w'
while True:
    c=sys.stdin.read(1)
    if c=='w':
        control.forword()
    elif c=='s':
        control.back()
    elif c=='a':
        control.left()
    elif c=='d':
        control.right()
    elif c=='':
        if p=='':
            continue
        control.stop()
    elif c=='q':
        break
    else:
        continue
    p=c
    time.sleep(0.1)
