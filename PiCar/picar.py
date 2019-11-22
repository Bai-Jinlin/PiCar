#!/usr/bin/env python

from TestMoter import Moter
import socket
import time
import sys

def main():
    host='0.0.0.0'
    port=8008
    tcpSocket=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    tcpSocket.bind((host,port))
    tcpSocket.listen(1)
    client,_=tcpSocket.accept()
    # client.setblocking(0)
    control=Moter(17,18,27,22)
    print('car start')
    while True:
        try:
            time.sleep(0.05)
            data=client.recv(512)
            if(len(data)==0):
                break
            data=bytes.decode(data).strip(b'\x00'.decode())
            if data=='forword':
                control.forword()
            elif data=='back':
                control.back()
            elif data=='left':
                control.left()
            elif data=='right':
                control.right()
            elif data=='stop':
                control.stop()
            elif data=='shutdown':
                print('shutdown connect')
                break
            else:
                raise Exception("unknown data format",data.encode())
        except KeyboardInterrupt:
            break
        except Exception as e:
            print(e)
            sys.exit(1)
    print('over')
    client.close()
    tcpSocket.close()

main()
