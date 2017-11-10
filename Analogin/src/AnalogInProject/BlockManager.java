package AnalogInProject;

public class BlockManager {
	static void checkedBlockUpdate(Block checkedBlock)
	{
		for(Block b : GIM.blockObject)
		{
			if(b!=checkedBlock)
				b.setBorder(null);
		}
	}
}
