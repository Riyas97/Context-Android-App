import socket
import sys


def process_data(data,address):
    msg = data.decode();
    # 1 - connection, 2 - relay
    if msg[0] == '1':
    # 1 - PC, 2 - phone
        if msg[1] == '1':
            sock.sendto("received".encode(), address)
            connection_array[0] = address
            if connection_array[1] != 0:
                sock.sendto("send".encode(), connection_array[1])
                
        elif msg[1] == '2':
            sock.sendto("received".encode(), address)
            connection_array[1] = address
            if connection_array[0] != 0:
                sock.sendto("send".encode(), address)
                
        else:
            print("invalid message\n")
            sock.sendto("invalid".encode(), address)
            
    elif msg[0] == '2':
        
        if msg[1] == '1':
            sock.sendto("received".encode(), address)
            sock.sendto(msg[2:].encode(),connection_array[1])
                
        elif msg[1] == '2':
            sock.sendto("received".encode(), address)
            sock.sendto(msg[2:].encode(),connection_array[0])
            
        else:
            print("invalid message\n")
            sock.sendto("invalid".encode(), address)

    else:
        print("invalid message\n")
        sock.sendto("invalid".encode(), address)



sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

server_address = ('', 1111)
sock.bind(server_address)
print('Starting up on port %d\n' % server_address[1])

connection_array = [0,0]
local_msg = ["empty"]

while True:
    print("waiting for incoming...\n")
    data, address = sock.recvfrom(1024)
    process_data(data,address)
    print(connection_array)
    print(local_msg)