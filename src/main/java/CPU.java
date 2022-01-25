/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luketomlinson
 */
public class CPU extends Thread {

    int id; //id of the cpu
    MainJFrame frame; // innstance of frame to edit
    ArrayList<Process> proQueue = new ArrayList<>();
    static ArrayList< Process> proQueue_2 = new ArrayList< Process>();

    DecimalFormat df = new DecimalFormat("###.###");

    int sum_st = 0;

    public CPU(int id, MainJFrame frame, ArrayList<Process> pList) {
        this.id = id;
        this.frame = frame;
        proQueue = pList;
        proQueue_2 = pList;
    }

    /**
     *
     */
    @Override
    public void run() {

        if (id == 0) {
            runHRRN(proQueue);
        }
        if (id == 1) {
            RR();
        }
    }

    void runHRRN(ArrayList<Process> processes) {
        float total_nTAT = 0.0f;
        int processesCompleted = 0;

        ArrayList<Process> pList = processes;
        for (int i = 0; i < pList.size(); i++) {
            // Variable for sum of all Burst Times
            sum_st += pList.get(i).getOriginalServiceTime();
        }
        sortByArrival();
        
        for (int t = pList.get(0).getArrivalTime(); t < sum_st;) {

            // Set lower limit to response ratio
            float hrr = -9999;

            // Response Ratio Variable
            float temp;

            // Variable to store next processs selected
            int loc = 0;
            for (int i = 0; i < pList.size(); i++) {
                Process p = pList.get(i);
                // Checking if process has arrived and is Incomplete
                if (p.getArrivalTime() <= t && p.getCompleted() != 1) {

                    // Calculating Response Ratio
                    temp = (p.getOriginalServiceTime() + (t - p.getArrivalTime())) / p.getOriginalServiceTime();

                    // Checking for Highest Response Ratio
                    if (hrr < temp) {

                        // Storing Response Ratio
                        hrr = temp;

                        // Storing Location
                        loc = i;
                    }
                }
            }

            Process tempP = pList.get(loc);

            int remainingTime = tempP.getOriginalServiceTime();
            for (int i = 0; i < tempP.getOriginalServiceTime(); i++) {
                // if the main class says to pause, tell this individual thread to wait
                if (frame.isPaused() == true) {
                    synchronized (this) {
                        try {
                            this.wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CPU.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                try {
                    // sleep for a time unit
                    Thread.sleep(Integer.parseInt(frame.timeUnitTextField.getText()));

                    // update the remaining time
                    remainingTime--;

                    // set the labels
                    frame.cpu1processExecuting.setText("Exec: " + tempP.getProcessID());
                    frame.cpu1timeRemainingLabel.setText("Rem. Time: " + remainingTime);

                } catch (InterruptedException ex) {
                    System.out.println("Error");
                }
            }

            // Updating time value
            t += tempP.getOriginalServiceTime();

            // Calculate waiting time
            tempP.setWaitingTime(t - tempP.getArrivalTime() - tempP.getOriginalServiceTime());

            // Calculate Turn Around Time
            tempP.setTurnAroundTime(t - tempP.getArrivalTime());

            // Calculate Normalized Turn Around Time
            tempP.setNormalizedTurnAroundTime((float) tempP.getTurnAroundTime() / tempP.getOriginalServiceTime());

            // Update total nTAT
            total_nTAT += tempP.getNormalizedTurnAroundTime();

            // Update Completion Status and update number of processes this CPU has completed
            tempP.setCompleted(1);
            processesCompleted++;

            // remove rows from waiting process queue
            for (int i = 0; i < frame.waitingProcesses_dtm_cpu1.getRowCount(); ++i) {
                // if row id equal processID, remove the row at that index
                if (frame.waitingProcesses_dtm_cpu1.getValueAt(i, 0).equals(tempP.getProcessID())) {
                    frame.waitingProcesses_dtm_cpu1.removeRow(i);
                }
            }

            // add process and its data to the finished table frame
            frame.completedProcesses_dtm_cpu1.addRow(new Object[]{tempP.getProcessID(), tempP.getArrivalTime(),
                tempP.getOriginalServiceTime(), t, tempP.getTurnAroundTime(),
                tempP.getNormalizedTurnAroundTime()});
            frame.currentnTAT_cpu1.setText("Current average nTAT: " + df.format(total_nTAT / (double) processesCompleted));

        }
    }

    void sortByArrival() {
        int i, j;
        Process temp;
        // Selection Sort applied
        for (i = 0; i < proQueue.size() - 1; i++) {
            for (j = i + 1; j < proQueue.size(); j++) {

                // Check for lesser arrival time
                if (proQueue.get(i).getArrivalTime() > proQueue.get(j).getArrivalTime()) {

                    // Swap earlier process to front
                    temp = proQueue.get(i);
                    proQueue.set(i, proQueue.get(j));
                    proQueue.set(j, temp);
//                    System.out.println(proQueue.get(j));
                }
            }
        }
    }

    void RR() {
        float total_nTAT = 0.0f;
        int processesCompleted = 0;
        // set the quantum split to the input from the frame
        int timeQuantum = Integer.parseInt(frame.quantumInput.getText());
        /*Algorithm starts here*/
        Queue< Process> queue = new LinkedList<>();

        int timer = 0;
        int counter = 0;
        while (true) {
            for (Process process : proQueue_2) {
                if (process.getArrivalTime() == timer) {
                    queue.add(process);
                }
            }
            if (queue.peek() == null) {
                timer += 1;
                counter = 0;
            } else {
                // update the service time of the current process
                int temp = queue.peek().getServiceTime() - 1;
                queue.peek().setServiceTime(temp);
                timer += 1;
                counter += 1;

                try {
                    // sleep for a time unit
                    Thread.sleep(Integer.parseInt(frame.timeUnitTextField.getText()));

                    // update the labels with remaining time
                    frame.cpu2processExecuting.setText("Exec: " + queue.peek().getProcessID());
                    frame.cpu2timeRemainingLabel.setText("Rem. Time: " + queue.peek().getServiceTime());
                } catch (InterruptedException ex) {
                    System.out.println("Error");
                }

                if (frame.isPaused() == true) {
                    synchronized (this) {
                        try {
                            this.wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CPU.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                // if service time is == 0
                if (queue.peek().getServiceTime() == 0) {
                    Process tempP = queue.peek();
                    tempP.setCompletionTime(timer);

                    // remove the row from the waiting process table
                    for (int i = 0; i < frame.waitingProcesses_dtm_cpu2.getRowCount(); ++i) {
                        // if row id equal processID, remove the row at that index
                        if (frame.waitingProcesses_dtm_cpu2.getValueAt(i, 0).equals(tempP.getProcessID())) {
                            frame.waitingProcesses_dtm_cpu2.removeRow(i);
                        }
                    }
                    // set TAT and nTAT for the current process
                    tempP.setTurnAroundTime(tempP.getCompletionTime() - tempP.getArrivalTime());
                    tempP.setNormalizedTurnAroundTime((float) tempP.getTurnAroundTime() / tempP.getOriginalServiceTime());

                    // Updating total nTAT
                    total_nTAT += tempP.getNormalizedTurnAroundTime();

                    // increment number of processes completed
                    processesCompleted++;

                    // add row to finished processes table for cpu2
                    frame.completedProcesses_dtm_cpu2.addRow(new Object[]{tempP.getProcessID(), tempP.getArrivalTime(),
                        tempP.getOriginalServiceTime(), timer, tempP.getTurnAroundTime(),
                        tempP.getNormalizedTurnAroundTime()});
                    // update the average nTAT
                    frame.currentnTAT_cpu2.setText("Current average nTAT: " + df.format(total_nTAT / (double) processesCompleted));

                    // remove process from queue
                    queue.remove();
                    counter = 0;

                } else if (counter == Integer.parseInt(frame.quantumInput.getText())) {
                    counter = 0;
                    queue.add(queue.peek());
                    queue.remove();
                }
            }

            if (isAllCompleted()) {
                break;
            }
        }
    }

    public static boolean isAllCompleted() {
        /*Check if all process have been completed */
        for (Process process : proQueue_2) {
            if (process.getServiceTime() != 0) {
                return false;
            }
        }
        return true;
    }
}
