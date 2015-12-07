package scheduler;

import java.util.ArrayList;

public class PriorityExchange extends PeriodicServer
{
	TaskInstance switchedTask;
	TaskInstance deferredInstance;
	int switchTimeLeft;
	int tempPeriod;
	
	public PriorityExchange(int serverTime, int per)
	{
		super(serverTime, per);
		switchedTask = null;
		tempPeriod = per;
		deferredInstance = null;
		switchTimeLeft = 0;
	}

	public ArrayList<TaskInstance> doServer(ArrayList<TaskInstance> activePList, ArrayList<TaskInstance> activeAPList)
	{
		//Want to exchange priorities here
		if(activeAPList.size() == 0)
		{
			for(int i=0;i<activePList.size();i++)
			{
				//Switch priorities
				if(activePList.get(i).period > period)
				{
					switchedTask = activePList.get(i);
					tempPeriod = activePList.get(i).period;
					activePList.get(i).period = period;
					switchTimeLeft = totalCompTime;
				}
			}
			return null;
		}
		
		//Add some tasks and reset variables
		deferredInstance = null;
		switchedTask = null;
		tempPeriod = period;
		switchTimeLeft = 0;	
		
		int sum=0;
		ArrayList<TaskInstance> toRun = new ArrayList<TaskInstance>();
		
		for(int i=0;i<activeAPList.size();i++)
		{
			toRun.add(activeAPList.get(i));
			sum+=activeAPList.get(i).getCompTimeRemaining();
			//Task time less than allocatable server time
			if(sum >= totalCompTime)
			{
				i=activeAPList.size();
			}
		}
		return toRun;
	}
	
	@Override
	public void update(ArrayList<TaskInstance> activePList, ArrayList<TaskInstance> activeAPList, int instanceNum, int compTimeleft) 
	{
		
		//Check if we need to reset period of switched task
		if(switchedTask != null)
		{
			//Still an active task
			if(activePList.contains(switchedTask))
			{
				switchTimeLeft--;
				if(switchTimeLeft <= 0)
				{
					switchedTask.period = tempPeriod;
					switchedTask = null;
					switchTimeLeft = 0;
					tempPeriod = period;
				}
			}
			else
			{
				switchedTask = null;
				switchTimeLeft = 0;
				tempPeriod = period;
			}			
		}
		
		
		//If there are periodic tasks waiting
		if(activeAPList.size() > 0 && !activePList.contains(deferredInstance))
		{
			deferredInstance = new TaskInstance(tempPeriod, compTimeleft, 0, true, instanceNum);
			activePList.add(deferredInstance);
		}	
		
	}
	
}
