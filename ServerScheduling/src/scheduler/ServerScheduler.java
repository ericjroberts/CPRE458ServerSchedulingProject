package scheduler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ServerScheduler 
{
	
	
	public static void main(String[] args) 
	{
		//Read in task set. Make a list of tasks
		ArrayList<Task> taskList = getTaskSetFromFile("taskSet1.txt");
        
        //Now do something with the task list
        for(int i=0;i<taskList.size();i++)
        {
        	System.out.println(taskList.get(i).toString());
        }

	}
	
	private static ArrayList<Task> getTaskSetFromFile(String fileName)
	{
		String line, file;
		ArrayList<Task> taskList = new ArrayList<Task>();
		file = fileName;
        try {
            FileReader fReader = new FileReader(file);

            BufferedReader bReader =  new BufferedReader(fReader);

            while((line = bReader.readLine()) != null) 
            {
            	System.out.println(line);
            	String[] params;
                switch(line)
                {
            		case "S":
            			line = bReader.readLine();
            			params = line.split(" ");
            			taskList.add(new Task(Integer.parseInt(params[1]), Integer.parseInt(params[0]), 0, true));
            			break;	
                	case "P":
            			line = bReader.readLine();
            			params = line.split(" ");
            			taskList.add(new Task(Integer.parseInt(params[1]), Integer.parseInt(params[0]), 0, false));
                		break;
                	case "A":
            			line = bReader.readLine();
            			params = line.split(" ");
            			taskList.add(new Task(0, Integer.parseInt(params[0]), Integer.parseInt(params[1]), false));
                		break;
                	default:
                		break;
                }
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
        
        return taskList;
	}

}
