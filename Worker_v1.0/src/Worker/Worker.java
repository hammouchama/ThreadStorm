package Worker;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

public class Worker {
    // TODO
    /*
     * Listen to MainServer
     * Send OK
     * Recieve Task + Data
     * (The data depends on the type of task)
     * Execute Task
     * Send Result to MainServer
     * 
     * 
     */
    
    //Initialize colors
    static String RESET = "\u001B[0m", RED = "\u001B[31m", GREEN = "\u001B[32m", YELLOW = "\u001B[33m", BLUE = "\u001B[34m", PURPLE = "\u001B[35m", CYAN = "\u001B[36m", WHITE = "\u001B[37m";

    static Integer Worker_port=null;
    static String FileConfiguration=null;
    public static void main(String[] args) {
        readFlags(args);

        
        /* 
        * Read Proprieties from file
        */
        Properties prop=new Properties();
        FileInputStream ip;
        
        
        if(FileConfiguration==null)
            //Par defaut
            FileConfiguration= "cfgWorker.properties";
        
        
        
        if(Worker_port==null){
            try {
                ip = new FileInputStream(FileConfiguration);
                prop.load(ip);
                Worker_port = Integer.parseInt(prop.getProperty("Worker.port"));
            } catch (Exception e) {
                System.out.println(RED+"Waring : Invalid config file !"+RESET);
                Worker_port = 10000;
            }
        }
        
        


        /* 
        * Read the Worker port
        */
        
        
        try {
            LocateRegistry.createRegistry(Worker_port);
            ImplFilters ob = new ImplFilters();
            /*
            * Hello skeleton = (Hello) UnicastRemoteObject.exportObject(ob,1099);
            * Registry registry= LocateRegistry.getRegistry(1099);
            * registry.bind("Hello",skeleton);
            */
            // System.out.println(ob.toString());
            
            // Get the address of the local host
            InetAddress localHost = InetAddress.getLocalHost();
            // Get the IP address of the local host
            String ipAddress = localHost.getHostAddress();
            // Print the IP address of the local host
            System.out.println("Worker is running at  "+PURPLE + ipAddress +":"+ Worker_port+"/Worker" +RESET);
            
            
            Naming.rebind("rmi://"+ipAddress+":"+Worker_port+"/Worker", ob);
            
            System.out.println("Worker is ready.........");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    





    /* Flags messages */
    public static void readFlags(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h") || args[i].equals("--help")) {
                System.out.println(
                "---------------------\n"+
                "Usage: "+GREEN+"java Worker "+YELLOW+"[-h] [-p <port>] [-c <config-file>]"+RESET+"\n"+"\n"+

                "Options:"+"\n"+
                ""+YELLOW+"  -h, --help                 "+CYAN+" Print this help message."+"\n"+
                ""+YELLOW+"  -p, --port <port>          "+CYAN+" Specify the port to listen on. Default: 10000."+"\n"+
                ""+YELLOW+"  -c, --config <config-file> "+CYAN+" Load the specified configuration file. Default: cfgWorker.properties."+"\n"+RESET+
                "---------------------\n"
                );
                
                System.exit(0);
            }
        
            else if (args[i].equals("-p") || args[i].equals("--port")) {
                try {
                    Worker_port = Integer.parseInt(args[i+1]);
                } catch (Exception e) {
                    System.out.println(RED+"Invalid syntax !"+RESET);System.exit(-1);
                }
            }
            
            /* else if (args[i].equals("-n") || args[i].equals("--host")) {
                try {
                    MainServer_host=args[i+1];
                } catch (Exception e) {
                    System.out.println(RED+"Invalid syntax !"+RESET);System.exit(-1);
                }
            } */
            
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
