import java.io.*;
import java.net.*;

public class Client implements MyDebug {
    public MemoryTable mt;//TODO: remove this field and use network call instead

    private String host ="localhost";
    private int port = 9999;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public Client(){
        this("localhost",9999);
    }
    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void connect(){
        try {
            if(socket != null ) {
                System.out.println(socket.toString());

                if (DEBUG > 0) System.out.println("Client: closing socket");
                socket.close();
                socket = null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (DEBUG > 0) System.out.println("Client: connecting to server "+host+":"+port);
            socket = new Socket(host, port);
            socket.setTcpNoDelay(true);

            objectOutputStream = new  ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

        }catch(Exception ee){
            ee.printStackTrace();
        }
    }

    public Response call(Request req){
        try {
            if(socket == null || !socket.isConnected() || socket.isClosed()) connect();
            return internalCall(req);
        } catch (Exception e) {
            e.printStackTrace();
            connect();
        }
        EmptyResponse ret = new EmptyResponse();
        ret.errStatus = "Server call error";
        return ret;
    }

    private Response internalCall(Request req) throws IOException, ClassNotFoundException {
        Response ret = new EmptyResponse();

        if (DEBUG > 0) System.out.println("Client: sending request "+req.requestName());

        if(socket == null || !socket.isConnected() || socket.isClosed()){
            connect();
        }

/*     //// через файл для прокерки
        //Сериализация в файл с помощью класса ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream("req.out"));
        objectOutputStream.writeObject(req);
        objectOutputStream.close();

        // Востановление из файла с помощью класса ObjectInputStream
        ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream("req.out"));
        req = (Request) objectInputStream.readObject();
        objectInputStream.close();
*/

        if (DEBUG > 0) System.out.println("Client: sending to server request "+req.requestName());
        objectOutputStream.writeObject(req);
        if (DEBUG > 0) System.out.println("Client: request sent to server, flushing stream");
        objectOutputStream.flush();
        if (DEBUG > 0) System.out.println("Client: waiting response");
        Response rsp = (Response)objectInputStream.readObject();
        ret = rsp;
        if (DEBUG > 0) System.out.println("Client: received response from sever "+rsp.getClass().getName());

        return ret;
    }
}
