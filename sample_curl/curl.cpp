#include <iostream>
#include <string>
#include <curl/curl.h>

using namespace std;

static size_t WriteCallback(void *contents, size_t size, size_t nmemb, void *userp)
{
  ((string*)userp)->append((char*)contents, size * nmemb);
  return size * nmemb;
}
 
int main()
{
  CURL *curl;
  CURLcode res;
  string readBuffer;

  curl_global_init(CURL_GLOBAL_ALL);
  curl = curl_easy_init();
  if(curl) {
    curl_easy_setopt(curl, CURLOPT_URL, "http://118.189.187.18/login.php");

    string email = "test@gmail.com";
    string email_encode = curl_easy_escape(curl,email.c_str(),strlen(email.c_str()));
    string password = "P@ssw0rd";
    string password_encode = curl_easy_escape(curl,password.c_str(),strlen(password.c_str()));
    string output = "email=" + email_encode + "&password=" + password_encode;

    curl_easy_setopt(curl, CURLOPT_POSTFIELDS, output.c_str());
    curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);
    curl_easy_setopt(curl, CURLOPT_WRITEDATA, &readBuffer);

    res = curl_easy_perform(curl);
    if(res != CURLE_OK) 
      fprintf(stderr, "curl_easy_perform() failed: %s\n",
              curl_easy_strerror(res));
 
    curl_easy_cleanup(curl);
    cout << readBuffer << endl;
    string temp;
    temp = readBuffer[10];
    int status = stoi(temp);
    cout << status << endl;
    if (status == 0){
      size_t pos = readBuffer.find("id");
      temp = readBuffer.substr(pos+4);
      temp.pop_back();
      int user_id = stoi(temp); 
      cout << user_id << endl;
    }
  }
  curl_global_cleanup();
  return 0;
}