package cpuscheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PrioritySJF extends Scheduler{
    public static final int SJF = 0, PRIORITY = 1;
    
    private final int type;
    private final boolean preemptive;
    
    //Constructors
    public PrioritySJF(Process[] processes, int type, boolean preemptive) {
        super(processes);
        this.type = type;
        this.preemptive = preemptive;
    }
    public PrioritySJF(Process[] processes, int type){this(processes, type, true);}
    
    public PrioritySJF(Process[] processes){this(processes, SJF, true);}
    
    //Sorting
    static class sortByBurst implements Comparator<Process>{
        @Override
        public int compare(Process p1, Process p2) {
            if(p1.getBurst() == p2.getBurst()) return (int)(p1.getArrivalTime() - p2.getArrivalTime());
            else return (int)(p1.getBurst() - p2.getBurst());
        }
    }
    
    static class sortByPriority implements Comparator<Process>{
        @Override
        public int compare(Process p1, Process p2) {
            if(p1.getPriority()== p2.getPriority()) 
                if(p1.getArrivalTime() == p2.getArrivalTime()) return (int)(p1.getBurst() - p2.getBurst());
                else return (int)(p1.getArrivalTime() - p2.getArrivalTime());
            else return (int)(p1.getPriority() - p2.getPriority());
        }
    }
    
    @Override
    public List<float[]> schedule() {
        List<float[]> timeline = new ArrayList<>();
        List<Process> readyQ = new ArrayList<>();
        Process current;
        float time = 0;
        int next = 0;
        while(next < processes.size() || !readyQ.isEmpty()){
            
            //Add all processes that arrived to ready queue
            for(int i = next; i < processes.size(); i++){
                if(processes.get(i).getArrivalTime() <= time){
                    readyQ.add(processes.get(i));
                    next++;
                }else break;
            }
            
            //Sort ready queue based on priority or burst time
            if(type == SJF) Collections.sort(readyQ, new sortByBurst());
            else Collections.sort(readyQ, new sortByPriority());
            
            if(readyQ.isEmpty()){ //IDLE case
                timeline.add(new float[]{-1, processes.get(next).getArrivalTime() - time}); // Add idle slot to timeline
                time = processes.get(next).getArrivalTime(); //Update time
            }else{
                current = readyQ.get(0);
                if(preemptive){
                    //Condition 1 : a process will come will current process is running
                    boolean condtion = processes.get(next).getArrivalTime() < time + current.getRemainingTime(); 
                    
                    //Remainging time for current process when it gets interrupted
                    float timeLeftAtInterrupt = time + current.getRemainingTime() - processes.get(next).getArrivalTime(); 
                    
                    //Condition 2 : the new process has higher priority for excution
                    if(type == SJF) condtion = condtion && processes.get(next).getBurst() < timeLeftAtInterrupt;
                    else condtion = condtion && processes.get(next).getPriority() < current.getPriority();
                    
                    if(next < processes.size() && condtion){//A process wil interrupt
                        timeline.add(new float[]{current.getId(), current.getRemainingTime() - timeLeftAtInterrupt}); //Add process to timeline
                        time = processes.get(next).getArrivalTime();
                        current.setRemainingTime(timeLeftAtInterrupt);
                    }else{
                        timeline.add(new float[]{current.getId(), current.getRemainingTime()});
                        time += current.getRemainingTime();
                        current.setWaitingAndTurnaroundTime(time);
                        readyQ.remove(0);
                    }
                }else{
                    timeline.add(new float[]{current.getId(), current.getBurst()}); //Add process to timeline
                    time += current.getBurst(); //Update time
                    current.setWaitingAndTurnaroundTime(time); //Set waiting and turnaround time
                    readyQ.remove(0); //Remove process from ready queue
                }
            }
        }
        return timeline;
    }
}