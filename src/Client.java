import java.io.*;
import java.net.*;

public class Client {
    public MemoryTable mt;//TODO: remove this field and use network callinstead

    private String host ="localhost";
    private int port = 9999;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;

    public Client(){
        this("localhost",9999);
    }
    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void connect(){
        try {
            if(socket != null) socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Client: connecting to server "+host+":"+port);
            socket = new Socket(host, port);
            objectOutputStream = new  ObjectOutputStream(socket.getOutputStream());
        }catch(Exception ee){
            ee.printStackTrace();
        }
    }

    public Response call(Request req) throws IOException, ClassNotFoundException {
        Response ret = new EmptyResponse();

        System.out.println("Client: sending request"+req.requestName());

        if(socket == null || !socket.isConnected() || socket.isClosed()){
            socket = null;
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




        if(req instanceof DropRecRequest){
           DropRecRequest dropReq=  (DropRecRequest)req;
           mt.dropRecord(dropReq.id);
        }

        System.out.println("Client:  sending to server request"+req.requestName());
        objectOutputStream.writeObject(req);
        objectOutputStream.flush();

        return ret;
    }


}