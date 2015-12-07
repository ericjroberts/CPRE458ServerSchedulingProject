package scheduler;

import java.util.ArrayList;

public class Polling extends PeriodicServer
{
	public Polling(int serverTime)
	{
		super(serverTime);
	}

	@Override
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

	@Override
	public ArrayList<TaskInstance> update(ArrayList<TaskInstance> list) 
	{
		//Polling doesn't do anything here
		return null;
	}
	
	
}
