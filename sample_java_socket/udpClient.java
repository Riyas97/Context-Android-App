import java.net.*; 
import java.io.*;

public class udpClient {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf = new byte[1024];

    public udpClient() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }

    public void sendMsg(String msg) throws IOException {
        buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 27015);
        socket.send(packet);
    }

    public void close(){
        socket.close();
    }

    public static void main(String[] args) throws SocketException,IOException,UnknownHostException{
        udpClient client = new udpClient();
        client.sendMsg("explorer http://www.programming-techniques.com\0");
        client.close();
    }

}