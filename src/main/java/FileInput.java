import java.io.*;
import java.io.FileReader;
import java.util.ArrayList;

public class FileInput{
    private String fileSource;  // file to be parsed
    private ArrayList<Process> processList= new ArrayList<>(); // array of the processes


    public void setFileSource(String source){this.fileSource = source; }
    public String getFileSource(){return fileSource;}

    public void readFile(String file) throws Exception {
        String line = "";
        String splitBy = ",";

        BufferedReader br = new BufferedReader(new FileReader(file));
        while((line = br.readLine()) != null){
            String[] proList = line.split(splitBy);
            Process process = new Process(); // create new process
            process.setArrivalTime(Integer.parseInt(proList[0])); // parsing, set arrival time
            process.setProcessID(proList[1]); // parsing, set process ID
            process.setServiceTime(Integer.parseInt(proList[2])); // parsing, set service time
            process.setOriginalServiceTime(Integer.parseInt(proList[2])); // parsing, set orig service time
            process.setPriority(Integer.parseInt(proList[3])); // parsing, set priority
            processList.add(process);
        }
    }
    public ArrayList<Process> getProcessList(){return processList;};
}
