package scheduler;

import java.util.ArrayList;

public class Deferrable extends PeriodicServer
{
	
	TaskInstance deferredInstance;
	
	public Deferrable(int serverTime, int per)
	{
		super(serverTime, per);	
		deferredInstance = null;
	}

	//Should only be called when there is server time left
	@Override
	public void update(ArrayList<TaskInstance> activePList, ArrayList<TaskInstance> activeAPList, int instanceNum, int compTimeleft) 
	{
		//Only care if we have AP task waiting
		if(activeAPList.size() > 0 && !activePList.contains(deferredInstance))
		{
			//need arrival and task instance
			deferredInstance = new TaskInstance(period, compTimeleft, 0, true, instanceNum);
			activePList.add(deferredInstance);
		}		
	}
}
