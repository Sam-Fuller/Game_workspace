package upgrade.tree.shotgun;

import upgrade.Upgrade;
import upgrade.tree.UpgradeTree;

public class UpgradeAttackSpeed extends Upgrade {
	
	public UpgradeAttackSpeed(UpgradeTree parent, State state) {
		super(parent, state);
	}

	@Override
	protected void rerenderIcon(boolean flipped, float x, float y) {
		return;
	}

}
