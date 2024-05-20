import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

public class servidor {
    public static void main(String[] args) {
        int puerto = 5000;
        try {
            DatagramSocket socket = new DatagramSocket(puerto);
            System.out.println("Servidor esperando conexiones......");

            List<Question> questions = Arrays.asList(
                    new Question("¿Cual es la raiz cuadrada de 4?, responda en numeros", "2"),
                    new Question("¿Cuales son las siglas de la Escuela Politecnica Nacional? Responda con numeros", "epn"),
                    new Question("¿Organo encargado de la respiracion?", "pulmon"),
                    new Question("¿Cual es el tercer planeta del sistema solar?", "tierra"),
                    new Question("Amigo de nobita", "doraemon")
            );

            while (true) {
                byte[] bufferEntrada = new byte[1024];
                DatagramPacket paqueteEntrada = new DatagramPacket(bufferEntrada, bufferEntrada.length);
                socket.receive(paqueteEntrada);

                InetAddress direccionIpCliente = paqueteEntrada.getAddress();
                int puertoCliente = paqueteEntrada.getPort();

                int score = 0;
                for (Question question : questions) {
                    String questionText = question.getQuestion();
                    byte[] bufferSalida = questionText.getBytes();
                    DatagramPacket paqueteSalida = new DatagramPacket(bufferSalida, bufferSalida.length,
                            direccionIpCliente, puertoCliente);
                    socket.send(paqueteSalida);

                    bufferEntrada = new byte[1024];
                    paqueteEntrada = new DatagramPacket(bufferEntrada, bufferEntrada.length);
                    socket.receive(paqueteEntrada);
                    String respuesta = new String(paqueteEntrada.getData(), 0, paqueteEntrada.getLength()).trim();

                    String resultado;
                    if (respuesta.equalsIgnoreCase(question.getCorrectAnswer())) {
                        resultado = "Correcto";
                        score += 4;
                    } else {
                        resultado = "Incorrecto. La respuesta correcta es: " + question.getCorrectAnswer();
                    }

                    bufferSalida = resultado.getBytes();
                    paqueteSalida = new DatagramPacket(bufferSalida, bufferSalida.length,
                            direccionIpCliente, puertoCliente);
                    socket.send(paqueteSalida);
                }

                String puntaje = "Puntaje final: " + score;
                byte[] bufferSalida = puntaje.getBytes();
                DatagramPacket paqueteSalida = new DatagramPacket(bufferSalida, bufferSalida.length,
                        direccionIpCliente, puertoCliente);
                socket.send(paqueteSalida);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}