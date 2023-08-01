import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        System.out.println("Server: starting service");
        Thread thr = new Thread(){
            public void run(){
                System.out.println("Server: connection accept thread run");
                while (true) {
                    System.out.println("Server: waiting fro client connection");
                    try (Socket socket = serverSocket.accept()){
                        System.out.println("Server: accepted connection from client");


                        try {
                            while(socket != null && socket.isConnected() && !socket.isClosed()) {
                                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                                Request req = (Request) objectInputStream.readObject();

                                System.out.println("Server: received request " + req.requestName());

                                try {
                                    System.out.println("Server: processing request");

                                    if (req instanceof DropRecRequest) {
                                        System.out.println("Server: drop");
                                        DropRecRequest dropReq = (DropRecRequest) req;
                                        mt.dropRecord(dropReq.id);
                                    } else if (req instanceof ReloadRecRequest) {
                                        System.out.println("Server: reload");
                                        ReloadRecRequest reloadReq = (ReloadRecRequest) req;
                                        mt.reload();
                                    } else if (req instanceof AddRecRequest) {
                                        System.out.println("Server: add row");
                                        AddRecRequest addReq = (AddRecRequest) req;
                                        mt.addRecord(addReq.row);
                                    }


                                    System.out.println("Server: request processed");
                                } finally {
                                    System.out.println("Server: sending response");
                                    objectOutputStream.writeObject(new EmptyResponse());
                                    System.out.println("Server: flushing socket output");
                                    objectOutputStream.flush();
                                }

                            }
                        }catch(Exception e2){
                            e2.printStackTrace();
                        } finally {
                            System.out.println("Server: client socket communication loop is broken, finilising");
                            if(socket != null) {
                                System.out.println("Server: closing socket");
                                socket.close();
                            }
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
