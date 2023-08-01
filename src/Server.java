import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;

public class Server {

    private int port = 9999;
    private ServerSocket serverSocket;

    MemoryTable mt;



    public Server(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    public int getPort(){
        return port;
    }

    public void startService(){
        System.out.println("Server: startin service");
        Thread thr = new Thread(){
            public void run(){
                System.out.println("Server: connection accept thread run");
                while (true) {
                    try (Socket socket = serverSocket.accept()){
                        System.out.println("Server: accepted connection from client");
                        try {
                            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                            Request req = (Request) objectInputStream.readObject();

                            System.out.println("Server: received request"+req.requestName());
                            if(req instanceof DropRecRequest){
                                DropRecRequest dropReq=  (DropRecRequest)req;
                                mt.dropRecord(dropReq.id);
                            }

                        }catch(Exception e2){
                            e2.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // read data from the client
                    // send data to the client
                }
            }
        };
        thr.start();
    }




}
