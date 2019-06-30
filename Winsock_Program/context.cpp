#ifndef UNICODE
#define UNICODE
#endif

#include <winsock2.h>
#include <Ws2tcpip.h>
#include <stdio.h>
#include <windows.h>

#include "resource.h"

const wchar_t g_szClassName[] = L"myWindowClass";
int iResult;
WSADATA wsaData;

SOCKET SendSocket = INVALID_SOCKET;
sockaddr_in RecvAddr;

unsigned short Port = 1111;

char RecvBuf[1024];
char SendBuf[1024] = "11";
int BufLen = 1024;
int RecvAddr_size = sizeof(RecvAddr);

LRESULT CALLBACK WndProc(HWND hwnd, UINT Message, WPARAM wParam, LPARAM lParam)
{
	switch(Message)
	{
        case WM_CREATE:
        {
            CreateWindow(TEXT("button"), TEXT("Receive"),    
                        WS_VISIBLE | WS_CHILD ,
                        100, 80, 80, 25,        
                        hwnd, (HMENU) 1, NULL, NULL);    

            CreateWindow(TEXT("button"), TEXT("Quit"),    
                        WS_VISIBLE | WS_CHILD ,
                        200, 80, 80, 25,        
                        hwnd, (HMENU) 2, NULL, NULL);
            CreateWindow(L"STATIC", L"Press receive to get website from app", 
            WS_VISIBLE | WS_CHILD | SS_CENTER, 
            40, 10, 300, 50, 
            hwnd, (HMENU) 3, NULL, NULL);
        }
        break;
        case WM_PAINT:
        {
            PAINTSTRUCT ps;
            HDC hdc = BeginPaint(hwnd, &ps);
			
            FillRect(hdc, &ps.rcPaint, CreateSolidBrush(RGB(66, 138, 245)) );

            EndPaint(hwnd, &ps);
        }
        break;
		case WM_COMMAND:
			switch(LOWORD(wParam))
			{
				case 1:                    
                    //START PROCESS FOR RECEIVING DATA
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
                    SetDlgItemText(hwnd, 3 , L"Waiting to Receive, Please use app to send...");
                    
                    iResult = recvfrom(SendSocket,
                                    RecvBuf, BufLen, 0, (SOCKADDR *) & RecvAddr, &RecvAddr_size);
                    if (iResult == SOCKET_ERROR) {
                        wprintf(L"sendto failed with error: %d\n", WSAGetLastError());
                        closesocket(SendSocket);
                        WSACleanup();
                        return 1;
                    }
                    printf("%s\n", RecvBuf);
                    SetDlgItemText(hwnd, 3 , L"Received. Opening...\nPress Receive again to open another website");
                    system(RecvBuf);
				break;
				case 2:
					PostMessage(hwnd, WM_CLOSE, 0, 0);
				break;
			}
		break;
		case WM_CLOSE:
			DestroyWindow(hwnd);
		break;
		case WM_DESTROY:

            //close socket and clean up
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

			PostQuitMessage(0);
		break;
		default:
			return DefWindowProc(hwnd, Message, wParam, lParam);
	}
	return 0;
}

int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance,
	LPSTR lpCmdLine, int nCmdShow)
{
	WNDCLASSEX wc;
	HWND hwnd;
	MSG Msg;

	wc.cbSize		 = sizeof(WNDCLASSEX);
	wc.style		 = 0;
	wc.lpfnWndProc	 = WndProc;
	wc.cbClsExtra	 = 0;
	wc.cbWndExtra	 = 0;
	wc.hInstance	 = hInstance;
	wc.hIcon		 = LoadIcon(GetModuleHandle(NULL), MAKEINTRESOURCE(IDI_MYICON));
	wc.hCursor		 = LoadCursor(NULL, IDC_ARROW);
	wc.hbrBackground = (HBRUSH)(COLOR_WINDOW+1);
	wc.lpszMenuName  = NULL;
	wc.lpszClassName = g_szClassName;
	wc.hIconSm		 = (HICON)LoadImage(GetModuleHandle(NULL), MAKEINTRESOURCE(IDI_MYICON), IMAGE_ICON, 16, 16, 0);

	if(!RegisterClassEx(&wc))
	{
		MessageBox(NULL, L"Window Registration Failed!", L"Error!",
			MB_ICONEXCLAMATION | MB_OK);
		return 0;
	}

	hwnd = CreateWindowEx(
		WS_EX_CLIENTEDGE,
		g_szClassName,
		L"Context",
		WS_OVERLAPPEDWINDOW,
		CW_USEDEFAULT, CW_USEDEFAULT, 400, 200,
		NULL, NULL, hInstance, NULL);
    
    RECT rc;

    GetWindowRect ( hwnd, &rc ) ;

    int xPos = (GetSystemMetrics(SM_CXSCREEN) - rc.right)/2;
    int yPos = (GetSystemMetrics(SM_CYSCREEN) - rc.bottom)/2;

    SetWindowPos( hwnd, 0, xPos, yPos, 0, 0, SWP_NOZORDER | SWP_NOSIZE );


	if(hwnd == NULL)
	{
		MessageBox(NULL, L"Window Creation Failed!", L"Error!",
			MB_ICONEXCLAMATION | MB_OK);
		return 0;
	}

	ShowWindow(hwnd, nCmdShow);
	UpdateWindow(hwnd);

    //////////////////////////////
    //WINSOCK CODE START HERE////
    ////////////////////////////
    memset(&RecvBuf[0], 0, sizeof(RecvBuf));
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
    /////////////////////////////
    //WINSOCK CODE ENDS HERE////
    ///////////////////////////


	while(GetMessage(&Msg, NULL, 0, 0) > 0)
	{
		TranslateMessage(&Msg);
		DispatchMessage(&Msg);
	}
	return Msg.wParam;
}



//OLD SOCKET CODE
/* 
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
*/