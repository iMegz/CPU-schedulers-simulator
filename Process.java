package cpuscheduler;

public class Process implements Comparable<Process>{
    private float arrivalTime, burst, remainingTime, waitingTime, turnAroundTime;
    private int id, priority;
    
    public static int idCounter = 0;
    
    //Constuctors
    public Process(float arrivalTime, float burst, int priority) {
        this.arrivalTime = arrivalTime;
        this.burst = burst;
        this.priority = priority;
        this.remainingTime = burst;
    }
    
    public Process(float arrivalTime, float burst) {this(arrivalTime, burst, 0);}
   
    //Sorting
    @Override
    public int compareTo(Process p) {return (int)(arrivalTime - p.getArrivalTime());}
    
    //Getters
    public static int getIdCounter() {return idCounter;}
    
    public int getId() {return id;}
    public float getArrivalTime() {return arrivalTime;}
    public float getBurst() {return burst;}
    public int getPriority() {return priority;}
    public float getRemainingTime() {return remainingTime;}
    public float getWaitingTime() {return waitingTime;}
    public float getTurnAroundTime() {return turnAroundTime;}
    
    //Setters
    public void editProcess(float arrivalTime, float burst, int priority){
        this.arrivalTime = arrivalTime;
        this.burst = burst;
        this.priority = priority;
    }
    public void setRemainingTime(float remainingTime) {this.remainingTime = remainingTime;}
    public void setWaitingAndTurnaroundTime(float endTime){
        this.waitingTime = endTime - arrivalTime - burst;
        this.turnAroundTime = endTime - arrivalTime;
    }   
}