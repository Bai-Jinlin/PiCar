#!/bin/python3
import socket
def main():
    host='0.0.0.0'
    port=8008
    tcpSocket=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    tcpSocket.bind((host,port))
    tcpSocket.listen(1)

    client,_=tcpSocket.accept()
    data=client.recv(512)
    print(data)
    data=client.recv(512)
    print('over')
    client.close()
    tcpSocket.close()

main()
