
public class Process{

//start and end times will be accessed by thread with specific integers parsed in
    private int timeUnit;
    private int completed;
    private int waitingTime;
    private int turnAroundTime;
    private float normalizedTurnAroundTime;
    private int completionTime;
    private int originalServiceTime;
//elements to be read in from .csv file
    private int priority;
    private int arrivalTime;
    private int serviceTime;
    private String processID;
//constructors

    /**
     *
     * Default(NULL) Process Constructor
     */
    public Process() {
        this.arrivalTime = 0;
        this.processID = "";
        this.serviceTime = 0;
        this.priority = 0;
        this.timeUnit = 0;
        this.completed = 0;
        this.waitingTime = 0;
        this.turnAroundTime = 0;
        this.normalizedTurnAroundTime = 0.0f;
        this.completionTime = 0;
        this.originalServiceTime = 0;
    }

    public Process(int arrival, String id, int service, int priority, int originalTime) {
        super();
        this.arrivalTime = arrival;
        this.processID = id;
        this.serviceTime = service;
        this.priority = priority;
        this.timeUnit = 0;
        this.completed = 0;
        this.waitingTime = 0;
        this.turnAroundTime = 0;
        this.normalizedTurnAroundTime = 0.0f;
        this.completionTime = 0;
        this.originalServiceTime = originalTime;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setArrivalTime(int arrival) {
        this.arrivalTime = arrival;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setServiceTime(int service) {
        this.serviceTime = service;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setProcessID(String id) {
        this.processID = id;
    }

    public String getProcessID() {
        return processID;
    }

    public void setTimeUnit(int time) {
        this.timeUnit = time;
    }

    public int getTimeUnit() {
        return timeUnit;
    }
    
    public void setCompleted(int c) {
        this.completed = c;
    }

    public int getCompleted() {
        return completed;
    }
    
    public void setWaitingTime(int time) {
        this.waitingTime = time;
    }

    public int getWaitingTime() {
        return waitingTime;
    }
    
    public void setTurnAroundTime(int time) {
        this.turnAroundTime = time;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }
    
    public void setNormalizedTurnAroundTime(float time) {
        this.normalizedTurnAroundTime = time;
    }

    public float getNormalizedTurnAroundTime() {
        return normalizedTurnAroundTime;
    }
    
    public void setCompletionTime(int time) {
        this.completionTime = time;
    }

    public int getCompletionTime() {
        return completionTime;
    }
    
    public int getOriginalServiceTime() {
        return originalServiceTime;
    }
    
    public void setOriginalServiceTime(int time) {
        this.originalServiceTime = time;
    }
}
