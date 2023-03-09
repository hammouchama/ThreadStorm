package RessourcesForRMI;

import java.rmi.Naming;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WorkerDataList {
    public static List<WorkerData> ListWorkers = new CopyOnWriteArrayList<WorkerData>();
    public static WorkerData ArduinoWorker;


    public static void addWorker(String linkRMI){
        ListWorkers.add(new WorkerData(linkRMI));
    }


    public static List<WorkerData> DispoWorkers(int inNeed){
        List<WorkerData> tmpWorkers=new CopyOnWriteArrayList<WorkerData>();
        for (WorkerData tmpWorker : ListWorkers) {
            if(tmpWorker.dispo==1){
                Filters tmpStub = connectToWorker(tmpWorker);
                if(tmpStub !=null){
                    tmpWorker.dispo=0;
                    tmpWorker.stub=tmpStub;
                    tmpWorkers.add(tmpWorker);
                }
            }

            if(tmpWorkers.size()>=inNeed)
                break;
        }
        return tmpWorkers;
    }

    public static Filters connectToWorker(WorkerData wd){
        try{
            Filters stub = (Filters) Naming.lookup(wd.linkRMI);
            return stub;
        }catch(Exception e){
            return null;
        }
    }

}

// Hello stub = (Hello) Naming.lookup("rmi://localhost:1099/BK");



