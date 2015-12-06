package scheduler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ServerScheduler {
	
	ArrayList<Task> taskList;
	ArrayList<TaskInstance> activePTasks;
	//Send to server when it's time
	ArrayList<TaskInstance> activeAPTasks;
	
	
	
	public static void main(String[] args) {		
		//Read in task set. Make a list of tasks
		String line, file;
		file = "taskSet1.txt";
        try {
            FileReader fReader = new FileReader(file);

            BufferedReader bReader =  new BufferedReader(fReader);

            while((line = bReader.readLine()) != null) 
            {
                System.out.println(line);
            }   

            bReader.close();         
        }
        catch(FileNotFoundException ex) 
        {
            System.out.println("File not found: " + file);   
            ex.printStackTrace();
        }
        catch(IOException ex)
        {
            System.out.println("Error reading file: " + file);       
             ex.printStackTrace();
        }

	}

}
