package MainServer;

import java.net.Socket;

public class Task {
    Socket client;// socket to send back the result to client
    int TaskId;

    public Task(Socket client,int TaskId) {
        this.client = client;
        this.TaskId=TaskId;
    }
}
