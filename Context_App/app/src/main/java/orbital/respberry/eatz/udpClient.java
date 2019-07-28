package orbital.respberry.eatz;
import java.net.*;
import java.io.*;

public class udpClient {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf_send = new byte[1024];
    private byte[] buf_receive = new byte[1024];

    public udpClient() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("118.189.187.18");
    }

    public void sendMsg(String msg) throws IOException {
        buf_send = msg.getBytes();
        DatagramPacket packet_send = new DatagramPacket(buf_send, buf_send.length, address, 1111);
        socket.send(packet_send);
    }

    public String recvMsg() throws IOException{
        DatagramPacket packet_received = new DatagramPacket(buf_receive, buf_receive.length);
        socket.receive(packet_received);
        String received = new String(packet_received.getData(),0, packet_received.getLength());
        return received;
    }

    public void close(){
        socket.close();
    }
}
