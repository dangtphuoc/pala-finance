package pala.gui;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MainClass extends Thread {
  private ServerSocket serverSocket;
  private MainApp mainApp;

  public MainClass(int port, MainApp mainApp) throws IOException {
    serverSocket = new ServerSocket(port);
    this.mainApp = mainApp;
  }

  public void run() {
    while (true) {
      try {
        System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
        Socket server = serverSocket.accept();

        /*System.out.println("Just connected to " + server.getRemoteSocketAddress());
        DataInputStream in = new DataInputStream(server.getInputStream());
        System.out.println(in.readUTF());

        DataOutputStream out = new DataOutputStream(server.getOutputStream());
        out
            .writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
                + "\nGoodbye!");*/
        mainApp.frame.setVisible(true);

        server.close();
      } catch (SocketTimeoutException s) {
        System.out.println("Socket timed out!");
        break;
      } catch (IOException e) {
        e.printStackTrace();
        break;
      }
    }
  }

  public static void main(String[] args) {
    int port = Integer.parseInt(args[0]);

    try {
      Thread t = new MainClass(port, null);
      t.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}