package scheduler;

import java.util.ArrayList;

public class PriorityExchange extends PeriodicServer
{
	public PriorityExchange(int serverTime, int per)
	{
		super(serverTime, per);
	}

	@Override
	public void update(ArrayList<TaskInstance> list) 
	{
		
	}
	
}
