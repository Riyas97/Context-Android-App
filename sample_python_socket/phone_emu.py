import socket
import sys

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_address = ('127.0.0.1', 1111)

sock.sendto('121'.encode(), server_address)
data,address = sock.recvfrom(1024)

if data.decode() == "received":
    print("sucess")
    print(data.decode())

data,address = sock.recvfrom(1024)

if data.decode() == "send":
    print(data.decode())
    sock.sendto('221:explorer "http://google.com"'.encode(), server_address)
