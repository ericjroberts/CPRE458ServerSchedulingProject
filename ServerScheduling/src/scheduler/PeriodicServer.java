package scheduler;

import java.util.ArrayList;

public abstract class PeriodicServer 
{
	
	int totalCompTime;
	int period;
	
	protected PeriodicServer(int serverTime, int per)
	{
		totalCompTime = serverTime;
		period = per;
	}
	
	//Given the list of arrived aperiodic tasks
	//Should return the list of tasks to be run for the server time
	public ArrayList<TaskInstance> doServer(ArrayList<TaskInstance> list)
	{
		if(list.size() == 0)
		{
			return null;
		}
		
		int sum=0;
		ArrayList<TaskInstance> toRun = new ArrayList<TaskInstance>();
		
		for(int i=0;i<list.size();i++)
		{
			sum+=list.get(i).getCompTimeRemaining();
			//Task time less than allocatable server time
			if(sum <= totalCompTime)
			{
				toRun.add(list.get(i));
			}
			else
			{
				i=list.size();
			}
		}
		return toRun;
	}
	
	public abstract void update(ArrayList<TaskInstance> list);
}
