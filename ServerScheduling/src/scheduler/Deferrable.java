package scheduler;

import java.util.ArrayList;

public class Deferrable extends PeriodicServer
{
	public Deferrable(int serverTime, int per)
	{
		super(serverTime, per);	
	}

	//Should only be called when there is server time left
	@Override
	public void update(ArrayList<TaskInstance> list) {
		//need arrival and task instance
		list.add(new TaskInstance(period, totalCompTime, 0, true, 0));
		
	}
}
