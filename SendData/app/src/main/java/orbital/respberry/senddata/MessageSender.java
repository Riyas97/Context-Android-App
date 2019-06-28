package orbital.respberry.senddata;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.*;


public class MessageSender extends AsyncTask<String, Void, Void> {
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buf = new byte[1024];

    public MessageSender() throws SocketException, UnknownHostException{
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }

    @Override
    protected Void doInBackground(String... voids) {
        String message = voids[0];
        try {
            buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 27015);
            socket.send(packet);
        }catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
