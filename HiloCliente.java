import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class HiloCliente extends Thread {
    DatagramSocket socket_cliente;
    List<Question> questions;
    InetAddress address;
    int port;

    public HiloCliente(DatagramSocket socket_cliente, List<Question> questions, InetAddress address, int port) {
        this.socket_cliente = socket_cliente;
        this.questions = questions;
        this.address = address;
        this.port = port;
    }

    public void run() {
        try {
            for (Question question : questions) {
                sendPacket(socket_cliente, address, port, question.getQuestion());
                DatagramPacket packet = receivePacket(socket_cliente, address, port);
                String respuesta = new String(packet.getData(), 0, packet.getLength());

                if (respuesta.equals(question.getCorrectAnswer())) {
                    sendPacket(socket_cliente, address, port, "Correcto");
                } else {
                    sendPacket(socket_cliente, address, port, "Incorrecto. La respuesta correcta es: " + question.getCorrectAnswer());
                }
            }

            sendPacket(socket_cliente, address, port, "Puntaje final: " + (questions.size() * 4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static DatagramPacket receivePacket(DatagramSocket socket, InetAddress address, int port) throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return packet;
    }

    private static void sendPacket(DatagramSocket socket, InetAddress address, int port, String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
    }
}