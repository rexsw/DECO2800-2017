package com.deco2800.marswars.entities.items.ml_ver;
import com.deco2800.marswars.entities.items.ml_ver.ItemMetaData;
import com.deco2800.marswars.entities.items.effects.EffectStats;
import com.deco2800.marswars.entities.items.ml_ver.Item;

public class testItemMetaData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ItemMetaData a = ItemMetaData.WEAPON1;
		System.out.println(a.getName());
		System.out.println(a.getStats());
		Item b = new Item(ItemMetaData.WEAPON1);
		System.out.println(b.getName());
		System.out.println(b.getStats());
	}
}
