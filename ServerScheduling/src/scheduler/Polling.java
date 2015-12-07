package scheduler;

import java.util.ArrayList;

public class Polling extends PeriodicServer
{
	public Polling(int serverTime, int per)
	{
		super(serverTime, per);
	}

	@Override
	public void update(ArrayList<TaskInstance> list) 
	{
		//Polling doesn't do anything here
	}
	
	
}
