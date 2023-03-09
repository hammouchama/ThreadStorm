package MainServer;


import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import RessourcesForRMI.WorkerData;
import RessourcesForRMI.WorkerDataList;

public class MainServer extends Thread {
    // TODO
    /*
     * Listen to clients
     * push to TaskQueue
     */
    // ! FOR TaskSchedule
    // ? Loop in TaskQueue
    // ? Define difficulty
    // ? Check disponibily of Worker(i)
    // ? Send PartTask To Workers
    // ? Recieve from Worker
    /*
     * Build Data
     * Send to client
     */

    private static Executor executor;
    private static TaskQueue taskQueue;
    private static int TaskID=100;

    static Integer MainServer_port=null;

    static String ArduinoSen_host=null;
    static Integer ArduinoSen_port=null;

    static String FileConfiguration=null;
    
    //Initialize colors
    static String RESET = "\u001B[0m", RED = "\u001B[31m", GREEN = "\u001B[32m", YELLOW = "\u001B[33m", BLUE = "\u001B[34m", PURPLE = "\u001B[35m", CYAN = "\u001B[36m", WHITE = "\u001B[37m";
    
    public static void main(String[] args) {
        readFlags(args);

        /* 
        * Read Proprieties from file
        */
        Properties prop=new Properties();
        FileInputStream ip;

        if(FileConfiguration==null)
            //Par defaut
            FileConfiguration= "cfgMainServer.properties";
        
        try {
            ip = new FileInputStream(FileConfiguration);
            prop.load(ip);
        } catch (Exception e2) {
            System.out.println(RED+"Waring : Invalid config file !"+RESET);
        }
        

        if(ArduinoSen_host==null){
            try {
                ArduinoSen_host = prop.getProperty("ArduinoSen.host");
            } catch (Exception e) {
                ArduinoSen_host = "localhost";
            }
        }
        if(ArduinoSen_port==null){
            try {
                ArduinoSen_port = Integer.parseInt(prop.getProperty("ArduinoSen.port"));
            } catch (Exception e) {
                ArduinoSen_port = 6666;
            }
        }

        //Save Arduino worker
        WorkerDataList.ArduinoWorker=new WorkerData("rmi://"+ArduinoSen_host+":"+ArduinoSen_port+"/Sensor");
        

        /* 
        * Read the MainServer port
        */

        if(MainServer_port==null){
            try {
                MainServer_port = Integer.parseInt(prop.getProperty("MainServer.port"));
            } catch (Exception e) {
                MainServer_port = 5200;
            }
        }
            
        // Creating an object of ServerSocket class
        // in the main() method for socket connection

        /*
        * *
        * GET SELVERS
        */
        getSelvers(prop);
        System.out.println("Number of Workers :"+WorkerDataList.ListWorkers.size());


        ServerSocket ss;
        try {
            ss = new ServerSocket(MainServer_port);

            executor = Executors.newFixedThreadPool(8);
            taskQueue = new TaskQueue();

            //* Let the socket be nonblocking
            ss.setReuseAddress(true);
            ss.setSoTimeout(0);

            // Get the address of the local host
            InetAddress localHost = InetAddress.getLocalHost();
            // Get the IP address of the local host
            String ipAddress = localHost.getHostAddress();
            // Print the IP address of the local host
            System.out.println("MainServer is running at  "+PURPLE + ipAddress +":"+ MainServer_port +RESET);
            
            //! Lunch Mini Workers Threads executing the Queue
            for (int i = 0; i < 7; i++) {
                executor.execute(new ServerThread(taskQueue));
            }
            
            // !Listen to client
            while (true) {
                // Accept the socket from client
                Socket soc = ss.accept();
                System.out.println(CYAN+"+("+TaskID+") : New Task in queue"+RESET);
                Task newTask = new Task(soc,TaskID++);
                taskQueue.add(newTask);
            }

        } catch (Exception e1) {
        }
    }



    public static int getSelvers(Properties prop) {

        String hostSlv = prop.getProperty("Worker1.host");
        String portSlv = prop.getProperty("Worker1.port");
        int i=1;
        while(hostSlv != null){
            WorkerDataList.addWorker("rmi://"+hostSlv+":"+portSlv+"/Worker");
            i++;
            
            hostSlv = prop.getProperty("Worker"+i+".host");
            portSlv = prop.getProperty("Worker"+i+".port");
        }
        // listSlevers.add(new ListSlevers("100.100.33.150", 1111));
        return i-1;
    }




    /* Flags messages */
    public static void readFlags(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h") || args[i].equals("--help")) {
                System.out.println(
                "---------------------\n"+
                "Usage: "+GREEN+"java MainServer "+YELLOW+"[-h] [-p <port>] [-o <host>] [-c <config-file>]"+RESET+"\n"+"\n"+

                "Options:"+"\n"+
                ""+YELLOW+"  -h, --help                 "+CYAN+" Print this help message."+"\n"+
                ""+YELLOW+"  -p, --port <port>          "+CYAN+" Specify the port to listen on. Default: 5200."+"\n"+
                ""+YELLOW+"  -n, --host <host>          "+CYAN+" Specify the host name or IP address to bind to. Default: localhost."+"\n"+
                ""+YELLOW+"  -c, --config <config-file> "+CYAN+" Load the specified configuration file. Default: cfgMainServer.properties."+"\n"+RESET+
                "---------------------\n"
                );
                
                System.exit(0);
            }
        
            else if (args[i].equals("-p") || args[i].equals("--port")) {
                try {
                    MainServer_port = Integer.parseInt(args[i+1]);
                } catch (Exception e) {
                    System.out.println(RED+"Invalid syntax !"+RESET);System.exit(-1);
                }
            }
            
            else if (args[i].equals("-c") || args[i].equals("--config")) {
                try {
                    FileConfiguration=args[i+1];
                } catch (Exception e) {
                    System.out.println(RED+"Invalid syntax !"+RESET);System.exit(-1);
                }
            }
        
        }
    }
}
