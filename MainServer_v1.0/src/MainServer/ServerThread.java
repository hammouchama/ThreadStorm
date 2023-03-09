package MainServer;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import Ressources.DataConvolution;
import Ressources.DataResult;
import Ressources.SensorData;
import RessourcesForRMI.WorkerDataList;
import RessourcesForRMI.ImageDivider.SubMatrix;
import RessourcesForRMI.Filters;
import RessourcesForRMI.ImageDivider;
import RessourcesForRMI.SaveDataFromSencer;
import RessourcesForRMI.WorkerData;

/* import java.awt.Graphics2D; */

public class ServerThread implements Runnable {
    private TaskQueue taskQueue;

    /* 
     * Devide image to multiple parts, depends to number of workers
     * Avaible filters :
     * "COLOR HALFTONE FILTER"
     * "CONVOLUTION FILTER"
     */

    public ServerThread(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                
                // Take task from the queue
                Task newTask = taskQueue.take();
                Socket soc = newTask.client;
                System.out.println(MainServer.GREEN+"+(" + (newTask.TaskId) + ") : Entred"+MainServer.RESET);

                // Send to client "active" to demand the rest of data
                ObjectOutputStream d = new ObjectOutputStream(soc.getOutputStream());
                d.writeObject("active");

                // Wait for name task
                ObjectInputStream dis = new ObjectInputStream(soc.getInputStream());
                String taskName = (String) dis.readObject();

                System.out.println(" (" + (newTask.TaskId) + ") : Task is " + taskName);

                String[] availableTasks = {"COLOR HALFTONE FILTER","SMART BLUR FILTER","CONVOLUTION FILTER"};
                String[] availableSensorTasks = {"T_","H_"};


                if(Arrays.asList(availableTasks).contains(taskName.toUpperCase())){
                    applyFilter(taskName, dis, soc ,newTask);
                }
                /* //!!!!!!!! COLOR HALFTONE FILTER !!!!!!!!!!
                if (taskName.compareToIgnoreCase("COLOR HALFTONE FILTER") == 0) {
                    applyFilter(taskName, dis, soc ,newTask);
                }     
                //!!!!!!!! SMART BLUR FILTER !!!!!!!!!!
                else if (taskName.compareToIgnoreCase("SMART BLUR FILTER") == 0) {
                    applyFilter(taskName, dis, soc ,newTask);
                }     
                //!!!!!!!! CONVOLUTION FILTER !!!!!!!!!!
                else if (taskName.compareToIgnoreCase("CONVOLUTION FILTER") == 0) {
                    applyFilter(taskName, dis, soc ,newTask);
                } */
                //Sensor
                
                else if(Arrays.asList(availableSensorTasks).contains(taskName.toUpperCase())){
                    SaveDataFromSencer stub = null;
                    try {
                        
                        int attempts=1;
                        do {
                            if (attempts != 1)
                                Thread.sleep(2000);
                            System.out.println(" (" + (newTask.TaskId) + ") : Connecting to Arduino worker at "+MainServer.BLUE+WorkerDataList.ArduinoWorker.linkRMI+MainServer.RESET+" ...(attempt n° "+(attempts)+"/5)");
                            
                        } while (WorkerDataList.ArduinoWorker.dispo==0 && attempts++ < 5);
                        
                        
                        if(WorkerDataList.ArduinoWorker.dispo==0){
                            //Abdended
                            System.out.println("-(" + (newTask.TaskId) + ") :"+MainServer.RED+"WorkerError : The Arduino worker not found, task is abended"+MainServer.RESET);
                            sendError("Error: Server at full capacity",soc);
                            continue;

                        }


                        stub = (SaveDataFromSencer) Naming.lookup(WorkerDataList.ArduinoWorker.linkRMI);
                        WorkerDataList.ArduinoWorker.dispo=1;
                        System.out.println(" (" + (newTask.TaskId) + ") : Connected with the Arduino worker");
                        SensorData data;


                        if(taskName.compareToIgnoreCase("T_") == 0){
                            data =new SensorData(stub.temperature());
                        }else{
                            data=new SensorData(stub.humidity());
                        }

                        d.writeObject(data);
                        System.out.println("+(" + (newTask.TaskId) + ") : "+MainServer.YELLOW+"Result sent");

                    } catch (Exception ex) {
                        System.out.println("-(" + (newTask.TaskId) + ") :"+MainServer.RED+"Error : Invalide/Unavailable worker - (Sensor) !"+MainServer.RESET);
                    }
                }else{
                    System.out.println("-(" + (newTask.TaskId) + ") :"+MainServer.RED+"Error : Invalide task name !"+MainServer.RESET);
                }


            } catch (IOException e) {
                System.out.println("-(XXX) :"+MainServer.RED+"Error : Something wrong occured while reading/sending object !"+MainServer.RESET);
            } catch (ClassNotFoundException e) {
                System.out.println("-(XXX) :"+MainServer.RED+"Error : Something wrong occured while reading the object !"+MainServer.RESET);
            } catch (InterruptedException e) {
                System.out.println("-(XXX) :"+MainServer.RED+"Error : Something wrong occured while taking task from taskQueue !"+MainServer.RESET);
            }
        }
    }











    private void applyFilter(String taskName, ObjectInputStream dis, Socket soc ,Task newTask) {
        try{
            long startTime = System.currentTimeMillis();
        
            DataResult dataResult = null;
            DataConvolution dataConvolution = null;
            int[][] myImage = null;
            int[][] result = null;

            Object dataRecieved = dis.readObject();

            //* Recieving Data
            if (taskName.compareToIgnoreCase("COLOR HALFTONE FILTER") == 0) {
                //Read image
                dataResult = (DataResult) dataRecieved;
                myImage= dataResult.image;
            } else if (taskName.compareToIgnoreCase("SMART BLUR FILTER") == 0) {
                //Read image
                dataResult = (DataResult) dataRecieved;
                myImage= dataResult.image;
            } else {
                //Read image + kernel
                dataConvolution = (DataConvolution) dataRecieved;
                myImage= dataConvolution.image;
            }
        
            System.out.println("+(" + (newTask.TaskId) + ") : All data received");

            int w = myImage[0].length;
            int h = myImage.length;
            int resDif = isDifficult(taskName, w, h);
        
            System.out.println(" (" + (newTask.TaskId) + ") : dim of image is ["+w+"x"+h+"]");
            System.out.println("         need "+resDif+" workers");

            if (resDif <= 1) {
                if (taskName.compareToIgnoreCase("COLOR HALFTONE FILTER") == 0) {
                    result = ImageFilters.applyFilterColorHalftone(dataResult.image);
                } else if (taskName.compareToIgnoreCase("SMART BLUR FILTER") == 0) {
                    result = ImageFilters.applyFilterSmartBlur(dataResult.image);
                } else if (taskName.compareToIgnoreCase("CONVOLUTION FILTER") == 0) {
                    result = ImageFilters.applyFilterConvolution(dataConvolution.image, dataConvolution.kernel);
                }
            } else {
                //! Call RMI
                List<WorkerData> dispoWorkers=new CopyOnWriteArrayList<WorkerData>();
                // Try to connect to workers, if there is no worker, do sleep and try again for 5 times
                int attempts = 1;
                do {
                    if (attempts != 1)
                        Thread.sleep(2000);
                    System.out.println(" (" + (newTask.TaskId) + ") : Connecting to a worker...(attempt n° "+(attempts)+"/5)");
                    dispoWorkers.addAll(WorkerDataList.DispoWorkers(resDif));

                    System.out.println(" (" + (newTask.TaskId) + ") : Found "+dispoWorkers.size()+" dispo worker(s)");
        
                } while (dispoWorkers.size() < resDif && attempts++ < 5);
                if (dispoWorkers.size() == 0) {
                    System.out.println("-(" + (newTask.TaskId) + ") :"+MainServer.RED+"WorkerError : No worker found, task is abended"+MainServer.RESET);
                    sendError("Error: Server at full capacity",soc);
                    return;
                }

                System.out.println(" (" + (newTask.TaskId) + ") : Found "+dispoWorkers.size()+" free workers");
        
                int numberOfWorkers = Math.min(resDif, dispoWorkers.size());
        
                ArrayList<SubMatrix> AllSubMatrix = new ArrayList<SubMatrix>();
                // Devide image
                ArrayList<SubMatrix> sub = ImageDivider.divide(myImage, numberOfWorkers);
        
                int countWorkers=0;
                for (int i = 0; i < numberOfWorkers ; i++) {
                    WorkerData tmpWorker = dispoWorkers.get(i);
                    try {
                        Filters stub = tmpWorker.stub;
                        SubMatrix subMatrix = null;
                        if (taskName.compareToIgnoreCase("COLOR HALFTONE FILTER") == 0) {
                            subMatrix = stub.applyFilterColorHalftone(sub.get(i));
                        } else if (taskName.compareToIgnoreCase("SMART BLUR FILTER") == 0) {
                            subMatrix = stub.applyFilterSmartBlur(sub.get(i));
                        } else if (taskName.compareToIgnoreCase("CONVOLUTION FILTER") == 0) {
                            subMatrix = stub.applyFilterConvolution(sub.get(i), dataConvolution.kernel);
                        }
                        AllSubMatrix.add(subMatrix);        
                    } catch (RemoteException e) {
                        tmpWorker.dispo=1;
                        System.out.println("-(" + (newTask.TaskId) + ") :"+MainServer.RED+"WorkerError : Something wrong occured, please try later"+MainServer.RESET);
                        continue;
                    }finally{
                        //The worker task has ended , and now the worker is free for other tasks
                        if(tmpWorker.dispo==0){
                            tmpWorker.dispo=1;
                            countWorkers++;
                            System.out.println(" (" + (newTask.TaskId) + ") : "+countWorkers+"/"+dispoWorkers.size()+" finished thier task");
                        }
                    }
                }

                System.out.println(" (" + (newTask.TaskId) + ") : Recieved "+countWorkers+"/"+dispoWorkers.size()+" part(s) in total");
                System.out.println("         with "+(dispoWorkers.size()-countWorkers)+" part(s) lost");
                System.out.println("         Merging parts");
        
                result = ImageDivider.merge(AllSubMatrix, h, w);
            }
        
            DataResult objectToSend = new DataResult(result);
        
            ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
            oos.writeObject(objectToSend);
        
            System.out.println("+(" + (newTask.TaskId) + ") : "+MainServer.YELLOW+"Result sent");
        
            long endTime = System.currentTimeMillis();
            System.out.println("         The time took is : " + (endTime - startTime) +" ms"+MainServer.RESET);
        }catch(Exception e){
            System.out.println("-(" + (newTask.TaskId) + ") :"+MainServer.RED+"Error : Something wrong occured, please try later"+MainServer.RESET);
        }
    }
    








    public int isDifficult(String taskName,int width, int height) {
        int nbPixels=width*height;
        int nbWorkers;
        /* nbWorkers=20; */
        //for SMART BLUR FILTER no need for workers
        if (taskName.compareToIgnoreCase("SMART BLUR FILTER") == 0){
            nbWorkers=(int) Math.round((nbPixels/1e6)*0.6);
            return nbWorkers;
        }
        else if (taskName.compareToIgnoreCase("COLOR HALFTONE FILTER") == 0){
            nbWorkers=(int) Math.round((nbPixels/1e6)*2.5);
            return nbWorkers;
        }
        else if (taskName.compareToIgnoreCase("CONVOLUTION FILTER") == 0){
            nbWorkers=(int) Math.round((nbPixels/1e6)*1.2);
            return nbWorkers;
        }
        else
            return 0;
    }

    public void sendError(String message,Socket soc) throws IOException{
        DataResult data = new DataResult(message);
        ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
        oos.writeObject(data);
    }
}
