import socket
import sys

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_address = ('118.189.187.18', 1111)

sock.sendto('11'.encode(), server_address)
data,address = sock.recvfrom(1024)

if data.decode() == "received":
    print("sucess")
