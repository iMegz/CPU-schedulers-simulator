package cpuscheduler;

import java.util.Arrays;
import java.util.List;

public abstract class Scheduler {
    protected List<Process> processes;
    
    public static final int WAITING_TIME = 0;
    public static final int TURNAROUND_TIME = 1;
    
    //Constructor
    public Scheduler(Process[] processes) {
        Arrays.sort(processes);
        this.processes = Arrays.asList(processes);
    }
    
    //Scheduling function
    public abstract List<float[]> schedule();
    
    public void print(){
        List<float[]> timeline = schedule();
        timeline.forEach(slot ->System.out.println(slot[0] + " for " + slot[1]));
        System.out.println("waiting time = " + getAverageTime(WAITING_TIME) + "\nTurnaround time = " + getAverageTime(TURNAROUND_TIME));
    }
    
    //Average waiting and turnaround time calculator
    public float getAverageTime(int time, int round){
        float numberOfProcesses = (float) this.processes.size();
        float totalTime = 0.0f;
        for(Process p : processes) totalTime += time == WAITING_TIME ? p.getWaitingTime() : p.getTurnAroundTime();
        return (Math.round(Math.pow(10, round) * (totalTime / numberOfProcesses)) / (float)Math.pow(10, round));
    }
    
    public float getAverageTime(int time){return getAverageTime(time, 2);}
    
    //Getters
    public List<Process> getProcesses(){return this.processes;}
}