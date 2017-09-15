package com.deco2800.marswars;

import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.worlds.map.tools.MapContainer;
import org.junit.Assert;
import org.junit.Test;

public class CustomizedWorldTest extends BaseTest{

	@Test
	public void testCustomizedWorldConstructor(){
		MapContainer mapContainer = new MapContainer("resources/mapAssets/tinyMars.tmx");
		CustomizedWorld world = new CustomizedWorld(mapContainer);
		Assert.assertTrue("World map should not be null at this point",
				world.getMap() != null);
	}

	@Test
	public void testLoadMapContainer() {
		MapContainer mapContainer = new MapContainer("resources/mapAssets/tinyMars.tmx");
		CustomizedWorld world = new CustomizedWorld(mapContainer);
		Assert.assertTrue("Map shouldn't contain entities at this point",
				world.getEntities().size() == 0);
		mapContainer.setStructure(new BuildingEntity(0f,0f,0f,BuildingType.BASE, null));
		Assert.assertTrue("Map shouldn't contain entities at this point",
				world.getEntities().size() == 0);
	}

}
