package scheduler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class ServerScheduler 
{
	static int compTime = 2;
	static int period = 5;
	static int totalTime = 120;
	static String fileName = "taskSet1.txt";
	
	public static void main(String[] args) 
	{
		//Read in task set. Make a list of tasks
		Queue<TaskInstance> pollingInstanceList = getTaskSetFromFile(fileName);
		Queue<TaskInstance> deferrableInstanceList = getTaskSetFromFile(fileName);
		Queue<TaskInstance> priorityInstanceList = getTaskSetFromFile(fileName);
		Polling pollingServer = new Polling(compTime, period);
		Deferrable deferrableServer = new Deferrable(compTime, period);
		PriorityExchange priorityExchangeServer = new PriorityExchange(compTime, period);
		
		RMSScheduler pollingServerScheduling = new RMSScheduler(pollingServer, pollingInstanceList, totalTime);
		RMSScheduler deferrebleServerScheduling = new RMSScheduler(deferrableServer, deferrableInstanceList, totalTime);
		RMSScheduler priorityExchangeServerScheduling = new RMSScheduler(priorityExchangeServer, priorityInstanceList, totalTime);
		
		pollingServerScheduling.run();
		deferrebleServerScheduling.run();
		priorityExchangeServerScheduling.run();
		
		pollingServerScheduling.printResults();
		System.out.println(pollingServerScheduling.getBuiltScheduleString());
		//deferrebleServerScheduling.printResults();
		//priorityExchangeServerScheduling.printResults();
	}
	
	private static Queue<TaskInstance> getTaskSetFromFile(String fileName)
	{
		String line, file;
		ArrayList<Task> taskList = new ArrayList<Task>();
		ArrayList<TaskInstance> instances = new ArrayList<TaskInstance>();
		ArrayList<Integer> periods = new ArrayList<Integer>();
		file = fileName;
        try {
            FileReader fReader = new FileReader(file);

            BufferedReader bReader =  new BufferedReader(fReader);

            while((line = bReader.readLine()) != null) 
            {
            	String[] params;
                switch(line)
                {
                	case "P":
            			line = bReader.readLine();
            			params = line.split(" ");
            			taskList.add(new Task(params[0], Integer.parseInt(params[2]), Integer.parseInt(params[1]), false));
                		break;
                	case "A":
            			line = bReader.readLine();
            			params = line.split(" ");
            			instances.add(new TaskInstance(params[0], 0, Integer.parseInt(params[1]), Integer.parseInt(params[2]), false, 0, Integer.parseInt(params[3])));
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
        
        
        //Generating instances to be scheduled
        taskList.add(new Task("Server", period, compTime, true));
        
        for(int i=0;i<taskList.size();i++)
        {
        	if(taskList.get(i).period != 0)
        	{
        		periods.add(taskList.get(i).period);
        	}      	
        }
        
        totalTime = findLcm(periods);
        System.out.println("LCM: " + totalTime);
        
        for(Task T: taskList)
        {
        	int numInstances = (int) Math.ceil(totalTime/T.getPeriod());
        	
        	for(int i = 1; i <= numInstances; i++)
        	{
        		instances.add(T.generateInstance());
        	}
        }
        
        Collections.sort(instances, TaskInstance.getArrivalTimeComparator());
        Queue<TaskInstance> instanceQue = new LinkedList<TaskInstance>();
        for(TaskInstance I: instances)
        {
        	instanceQue.add(I);
        }
        
        return instanceQue;
	}

	private static int findLcm(ArrayList<Integer> periods)
	{
		int temp = periods.get(0).intValue();
	    for(int i=1;i<periods.size();i++)
	    {
	    	temp = (temp * (periods.get(i).intValue() / findGcd(temp, periods.get(i).intValue())));
	    }
	    return temp;
	}
	
	private static int findGcd(int num1, int num2)
	{
		int first = num1, second = num2;
		int tmp;
		
	    while (second > 0)
	    {
	        tmp = second;
	        second = first % second; 
	        first = tmp;
	    }
	    return first;
	}
	
}
