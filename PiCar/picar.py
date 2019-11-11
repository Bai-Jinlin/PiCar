#!/usr/bin/env python

import socket
import time
import Moter

def main():
    host='0.0.0.0'
    port=8008
    tcpSocket=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    tcpSocket.bind((host,port))
    tcpSocket.listen(1)

    (client,_)=tcpSocket.accept()
    client.setblocking(0)
    control=Moter.Moter(17,18,27,22)
    print('car start')
    while True:
        try:
            time.sleep(0.05)
            data=client.recv(512)
            if(len(data)==0):
                break
            data=bytes.decode(data)
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
                continue
        except socket.error:
            continue
        except KeyboardInterrupt:
            client.close()
            tcpSocket.close()
            break
        except Exception as e:
            print(e)
    print('over')
    client.close()
    tcpSocket.close()

main()
