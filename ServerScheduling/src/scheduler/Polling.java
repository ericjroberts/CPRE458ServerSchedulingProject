package scheduler;

import java.util.ArrayList;

public class Polling extends PeriodicServer
{
	public Polling(int serverTime, int per)
	{
		super(serverTime, per);
	}

	@Override
	public void update(ArrayList<TaskInstance> activePList, ArrayList<TaskInstance> activeAPList, int instanceNum, int compTimeleft) 
	{
		//Polling doesn't do anything here
	}
	
	
}
