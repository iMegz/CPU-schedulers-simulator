package cpuscheduler;

import java.util.ArrayList;
import java.util.List;

public class FCFS extends Scheduler{

    public FCFS(Process[] processes) {super(processes);}

    @Override
    public List<float[]> schedule() {
        List<float[]> timeline = new ArrayList<>();
        float time = 0;
        for(Process p : processes){
            if(p.getArrivalTime() > time) { //IDLE case
                timeline.add(new float[]{-1, p.getArrivalTime() - time}); //Add idle slot to timeline
                time = p.getArrivalTime(); //Update time
            }
            timeline.add(new float[]{p.getId(), p.getBurst()}); //Add process to timeline
            time += p.getBurst(); //Update time
            p.setWaitingAndTurnaroundTime(time); //Set waiting and turnaround time
        }
        return timeline;
    } 
}
