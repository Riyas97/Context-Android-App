#include <winsock2.h>
#include <Ws2tcpip.h>
#include <stdio.h>
#include <windows.h>
#include <string>
#include <cstring>
#include <curl/curl.h>

#include "resource.h"

static size_t WriteCallback(void *contents, size_t size, size_t nmemb, void *userp)
{
  ((std::string*)userp)->append((char*)contents, size * nmemb);
  return size * nmemb;
}

LPCSTR g_szClassName = "myWindowClass";
int iResult;
WSADATA wsaData;

SOCKET SendSocket = INVALID_SOCKET;
sockaddr_in RecvAddr;

unsigned short Port = 1111;

char RecvBuf[1024];
char SendBuf[1024] = "11";
int BufLen = 1024;
int RecvAddr_size = sizeof(RecvAddr);

DWORD WINAPI receving_thread(LPVOID lpParameter){
    HWND hwnd = (HWND) lpParameter;
    while(true){
        iResult = recvfrom(SendSocket,
        RecvBuf, BufLen, 0, (SOCKADDR *) & RecvAddr, &RecvAddr_size);
        if (iResult == SOCKET_ERROR) {
            wprintf(L"sendto failed with error: %d\n", WSAGetLastError());
            closesocket(SendSocket);
            WSACleanup();
        }
        printf("%s\n", RecvBuf);
        system(RecvBuf);
        memset(&RecvBuf[0], 0, sizeof(RecvBuf));
        SetDlgItemText(hwnd, 800 , "Received. Opening...\nSend another website from Phone");
    }
    return 0;
}

LRESULT CALLBACK WndProc(HWND hwnd, UINT Message, WPARAM wParam, LPARAM lParam)
{
    static HWND hwndEmail, hwndPass, EmailText, PassText, LoginButton, ErrorMsg;
	switch(Message)
	{
        case WM_CREATE:
        {
            hwndEmail = CreateWindowEx(
                WS_EX_LEFT, "EDIT",   // predefined class 
                NULL,         // no window title 
                WS_CHILD | WS_VISIBLE | ES_LEFT | ES_AUTOHSCROLL | WS_TABSTOP, 
                110, 30, 240, 20,   // set size in WM_SIZE message 
                hwnd,         // parent window 
                (HMENU) 300,   // edit control ID 
                (HINSTANCE) GetWindowLong(hwnd, GWL_HINSTANCE), 
                NULL);        // pointer not needed

            EmailText = CreateWindow("STATIC", TEXT("Username:"), 
                WS_VISIBLE | WS_CHILD | SS_LEFT, 
                20, 30, 80, 20, hwnd, 
                (HMENU) 400, 
                (HINSTANCE)GetWindowLong(hwnd, GWL_HINSTANCE), 
                NULL);
            
            hwndPass = CreateWindowEx(
                WS_EX_LEFT, "EDIT",   // predefined class 
                NULL,         // no window title 
                WS_CHILD | WS_VISIBLE | ES_LEFT | ES_AUTOHSCROLL | ES_PASSWORD | WS_TABSTOP, 
                110, 55, 240, 20,   // set size in WM_SIZE message 
                hwnd,         // parent window 
                (HMENU) 500,   // edit control ID 
                (HINSTANCE) GetWindowLong(hwnd, GWL_HINSTANCE), 
                NULL);        // pointer not needed

            PassText = CreateWindow("STATIC", TEXT("Password:"), 
                WS_VISIBLE | WS_CHILD | SS_LEFT, 
                20, 55, 80, 20, hwnd, 
                (HMENU) 600, 
                (HINSTANCE)GetWindowLong(hwnd, GWL_HINSTANCE), 
                NULL);

            LoginButton = CreateWindow("button", "Login",
                WS_VISIBLE | WS_CHILD, 50, 100, 80, 25,
                hwnd, (HMENU) 700, NULL, NULL);
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
				case 100:
                    //START PROCESS FOR RECEIVING DATA
                    iResult = sendto(SendSocket,
                     SendBuf, BufLen, 0, (SOCKADDR *) & RecvAddr, sizeof (RecvAddr));
                    SetDlgItemText(hwnd, 800 , "Making connection with server...");
                    if (iResult == SOCKET_ERROR) {
                        wprintf(L"sendto failed with error: %d\n", WSAGetLastError());
                        closesocket(SendSocket);
                        WSACleanup();
                    }
                    memset(&RecvBuf[0], 0, sizeof(RecvBuf));
                    iResult = recvfrom(SendSocket,
                                    RecvBuf, BufLen, 0, (SOCKADDR *) & RecvAddr, &RecvAddr_size);
                    SetDlgItemText(hwnd, 800 , "Connected to server!");
                    if (iResult == SOCKET_ERROR) {
                        wprintf(L"sendto failed with error: %d\n", WSAGetLastError());
                        closesocket(SendSocket);
                        WSACleanup();
                    }
                    printf("%s\n", RecvBuf);
                    memset(&RecvBuf[0], 0, sizeof(RecvBuf));
                    SetDlgItemText(hwnd, 800 , "Waiting to Receive, Please use app to send...");
                    ShowWindow(GetDlgItem(hwnd, 100),SW_HIDE);
                    CreateThread(NULL, 0, receving_thread, hwnd, 0, 0);
				break;
				case 200:
					PostMessage(hwnd, WM_CLOSE, 0, 0);
				break;
                case IDOK:
                case 700:
                    TCHAR email_buf[256], password_buf[256];
                    HWND email_edit = GetDlgItem(hwnd, 300);
                    HWND pass_edit = GetDlgItem(hwnd, 500);
                    GetWindowText(email_edit, email_buf, 256);
                    GetWindowText(pass_edit, password_buf, 256);
                    //START OF CURL CODE

                    CURL *curl;
                    CURLcode res;
                    std::string readBuffer;
                    curl_global_init(CURL_GLOBAL_ALL);
                    curl = curl_easy_init();
                    if(curl) {
                        curl_easy_setopt(curl, CURLOPT_URL, "http://118.189.187.18/login.php");

                        std::string email = email_buf;
                        std::string email_encode = curl_easy_escape(curl,email.c_str(),strlen(email.c_str()));
                        std::string password = password_buf;
                        std::string password_encode = curl_easy_escape(curl,password.c_str(),strlen(password.c_str()));
                        std::string output = "email=" + email_encode + "&password=" + password_encode;
                        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, output.c_str());
                        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);
                        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &readBuffer);
                        curl_easy_setopt(curl, CURLOPT_TIMEOUT, 5L);
                        res = curl_easy_perform(curl);
                        if(res != CURLE_OK){
                            MessageBox(hwnd, "Are you connected to the internet?", "Connection Failed", MB_OK);
                            break;
                        }
                        curl_easy_cleanup(curl);
                        std::string temp;
                        temp = readBuffer[10];
                        int status = stoi(temp);
                        if (status == 0){
                            size_t pos = readBuffer.find("id");
                            temp = readBuffer.substr(pos+4);
                            temp.pop_back();
                            strcat(SendBuf, temp.c_str());
                        } else {
                            MessageBox(hwnd, "Invalid username and password combination", "Error", MB_OK);
                            break;
                        }
                    }
                    curl_global_cleanup();

                    DestroyWindow(hwndPass);
                    DestroyWindow(hwndEmail);
                    DestroyWindow(EmailText);
                    DestroyWindow(PassText);
                    DestroyWindow(LoginButton);

                    CreateWindow(TEXT("button"), TEXT("Receive"),    
                        WS_VISIBLE | WS_CHILD ,
                        100, 80, 80, 25,        
                        hwnd, (HMENU) 100, NULL, NULL);    

                    CreateWindow(TEXT("button"), TEXT("Quit"),    
                        WS_VISIBLE | WS_CHILD ,
                        200, 80, 80, 25,        
                        hwnd, (HMENU) 200, NULL, NULL);

                    CreateWindow("STATIC", "Press receive to get website from app", 
                        WS_VISIBLE | WS_CHILD | SS_CENTER, 
                        40, 10, 300, 50, 
                        hwnd, (HMENU) 800, NULL, NULL);
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
		MessageBox(NULL, "Window Registration Failed!", "Error!",
			MB_ICONEXCLAMATION | MB_OK);
		return 0;
	}

	hwnd = CreateWindowEx(
		WS_EX_CLIENTEDGE,
		g_szClassName,
		"Context",
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
		MessageBox(NULL, "Window Creation Failed!", "Error!",
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

    while (GetMessage (&Msg, NULL, 0, 0) > 0) {
        if (IsDialogMessage(hwnd, &Msg) == 0){
            TranslateMessage(&Msg);
            DispatchMessage(&Msg);
        }
    }
	return Msg.wParam;

}