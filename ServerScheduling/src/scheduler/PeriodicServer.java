package scheduler;

import java.util.ArrayList;

public abstract class PeriodicServer 
{
	
	int totalCompTime;
	
	protected PeriodicServer(int serverTime)
	{
		totalCompTime = serverTime;
	}
	
	//Given the list of arrived aperiodic tasks
	//Should return the list of tasks to be run for the server time
	public abstract ArrayList<TaskInstance> doServer(ArrayList<TaskInstance> list);
	
	//Returns a task list for tasks to run in cases like deferred server
	public abstract ArrayList<TaskInstance> update(ArrayList<TaskInstance> list);
}
