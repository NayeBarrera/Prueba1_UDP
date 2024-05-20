import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class cliente {
    public static void main(String[] args) {
        int puerto = 5000;

        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress direccionIP_servidor = InetAddress.getByName("172.31.115.150");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                byte[] bufferEntrada = new byte[1024];
                DatagramPacket paqueteEntrada = new DatagramPacket(bufferEntrada, bufferEntrada.length);

 
                String mensajeInicio = "Inicio";
                byte[] bufferSalida = mensajeInicio.getBytes();
                DatagramPacket paqueteSalida = new DatagramPacket(bufferSalida, bufferSalida.length, direccionIP_servidor, puerto);
                socket.send(paqueteSalida);

                for (int i = 0; i < 5; i++) {
                    // Recibir la pregunta del servidor
                    bufferEntrada = new byte[1024];
                    paqueteEntrada = new DatagramPacket(bufferEntrada, bufferEntrada.length);
                    socket.receive(paqueteEntrada);
                    String pregunta = new String(paqueteEntrada.getData(), 0, paqueteEntrada.getLength()).trim();
                    System.out.println("Pregunta: " + pregunta);

 
                    String respuesta = scanner.nextLine();

                    // Enviar la respuesta al servidor
                    bufferSalida = respuesta.getBytes();
                    paqueteSalida = new DatagramPacket(bufferSalida, bufferSalida.length, direccionIP_servidor, puerto);
                    socket.send(paqueteSalida);

                    // Recibir el resultado de la respuesta
                    bufferEntrada = new byte[1024];
                    paqueteEntrada = new DatagramPacket(bufferEntrada, bufferEntrada.length);
                    socket.receive(paqueteEntrada);
                    String resultado = new String(paqueteEntrada.getData(), 0, paqueteEntrada.getLength()).trim();
                    System.out.println(resultado);
                }

                
                bufferEntrada = new byte[1024];
                paqueteEntrada = new DatagramPacket(bufferEntrada, bufferEntrada.length);
                socket.receive(paqueteEntrada);
                String puntajeFinal = new String(paqueteEntrada.getData(), 0, paqueteEntrada.getLength()).trim();
                System.out.println("Puntaje final: " + puntajeFinal);

                break; 
            }

            
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}