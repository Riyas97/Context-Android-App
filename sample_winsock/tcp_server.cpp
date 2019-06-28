#define _WIN32_WINNT _WIN32_WINNT_WINXP
#include <ws2tcpip.h>
#include <windows.h>
#include <winsock2.h>
#include <stdio.h>

#define DEFAULT_PORT "27111"
#define DEFAULT_BUFLEN 512


int main(){
    char recvbuf[DEFAULT_BUFLEN];
    WSADATA wsaData;
    int iResult, iSendResult;
    int recvbuflen = DEFAULT_BUFLEN;

    iResult = WSAStartup(MAKEWORD(2,2), &wsaData);
    if (iResult != 0){
        printf("failed\n");
        return 1;
    }
    struct addrinfo *result = NULL, *ptr = NULL, hints;

    ZeroMemory(&hints, sizeof (hints));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_DGRAM;
    hints.ai_protocol = IPPROTO_UDP;
    hints.ai_flags = AI_PASSIVE;

    // Resolve the local address and port to be used by the server
    iResult = getaddrinfo(NULL, DEFAULT_PORT, &hints, &result);
    if (iResult != 0) {
        printf("getaddrinfo failed: %d\n", iResult);
        WSACleanup();
        return 1;
    }

    SOCKET ListenSocket = INVALID_SOCKET;
    ListenSocket = socket(result->ai_family, result->ai_socktype, result->ai_protocol);
    if (ListenSocket == INVALID_SOCKET) {
        printf("Error at socket(): %d\n", WSAGetLastError());
        freeaddrinfo(result);
        WSACleanup();
        return 1;
    }

    iResult = bind( ListenSocket, result->ai_addr, (int)result->ai_addrlen);
    if (iResult == SOCKET_ERROR) {
        printf("bind failed with error: %d\n", WSAGetLastError());
        freeaddrinfo(result);
        closesocket(ListenSocket);
        WSACleanup();
        return 1;
    }

    freeaddrinfo(result);
    //FOR TCP
    /*
    if ( listen( ListenSocket, SOMAXCONN ) == SOCKET_ERROR ) {
    printf( "Listen failed with error: %d\n", WSAGetLastError() );
    closesocket(ListenSocket);
    WSACleanup();
    return 1;
    }
    */

    SOCKET ClientSocket;
    ClientSocket = INVALID_SOCKET;

    // FOR TCP
    // Accept a client socket
    /*
    ClientSocket = accept(ListenSocket, NULL, NULL);
    if (ClientSocket == INVALID_SOCKET) {
        printf("accept failed: %d\n", WSAGetLastError());
        closesocket(ListenSocket);
        WSACleanup();
        return 1;
    }
    */
`
   getsockname(ListenSocket, result->ai_addr, (int*)result->ai_addrlen);
   printf("")
   
   do {
       iResult = recvfrom(ListenSocket, recvbuf,)
       iResult = recvfrom(ClientSocket, recvbuf, recvbuflen, 0);
       if (iResult > 0) {
           printf("received: %s\n", recvbuf);
           printf("Bytes received: %d\n", iResult);
           // Echo the buffer back to the sender
           printf("sending: %s\n", recvbuf);
           iSendResult = send(ClientSocket, recvbuf, iResult, 0);
           if (iSendResult == SOCKET_ERROR) {
               printf("send failed: %d\n", WSAGetLastError());
               closesocket(ClientSocket);
               WSACleanup();
               return 1;
            }
            printf("Bytes sent: %d\n", iSendResult);
        } else if (iResult == 0) {
            printf("Connection closing...\n");
        } else {
            printf("recv failed: %d\n", WSAGetLastError());
            closesocket(ClientSocket);
            WSACleanup();
            return 1;
        }
    } while (iResult > 0);

    iResult = shutdown(ClientSocket, SD_SEND);
    if (iResult == SOCKET_ERROR) {
        printf("shutdown failed: %d\n", WSAGetLastError());
        closesocket(ClientSocket);
        WSACleanup();
        return 1;
    }

    closesocket(ClientSocket);
    WSACleanup();

    return 0;
    /*
    cout << "starting" << endl;
    system("explorer http://www.programming-techniques.com");
    cout << "done" << endl;
    return 0;
    */
}