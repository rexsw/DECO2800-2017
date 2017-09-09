package com.deco2800.marswars.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameBlackBoard extends Manager implements TickableManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(AiManager.class);
	private List<Manager> teams;
	private Map<Manager,List<Integer>> values;
	
	/**
	 * testing use please keep for now 
	 */
	@Override
	public void onTick(long i) {
//		List<Manager> deepcopy = new ArrayList<Manager>((List<Manager>) GameManager.get().getManagerList());
//		Iterator<Manager> managersIter =  deepcopy.iterator();
//		while(managersIter.hasNext()) {
//			Manager m = managersIter.next();
//			if (m instanceof AbstractPlayerManager) {
//				if (m instanceof PlayerManager) {
//					ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
//					LOGGER.info("" + m.toString()+" has " + rm.getRocks() + " rocks");
//				} else if (m instanceof AiManagerTest) {
//					LOGGER.info("" + m.toString() +" has "+ ((AiManagerTest) m).getResources().getRocks() + " rocks");
//				}
//			}
//		}	
	}
	
	public void set() {
		teams = new ArrayList<Manager>(GameManager.get().getManagerList());
		values = new HashMap<Manager,List<Integer>>();
	}

}