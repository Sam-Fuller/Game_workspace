package colour;

public class ColourScheme {
	private static final Colour light = new Colour("F7E9BD");
	private static final Colour cave = new Colour("391E53");
	private static final Colour dark = new Colour("161D23");//161D23
	private static final Colour ally = new Colour("10ADBD");
	private static final Colour enemy = new Colour("ff004d");
	private static final Colour black = new Colour("000000");
	

	protected Colour getLight() {return light;}
	protected Colour getCave() {return cave;}
	protected Colour getDark() {return dark;}
	protected Colour getAlly() {return ally;}
	protected Colour getEnemy() {return enemy;}
	protected Colour getBlack() {return black;}
	

	public Colour menuText() {return getLight();}
	
	
	
	public Colour background() {return getBlack();}
	public Colour cave() {return getCave();}

	
	
	public Colour enemySpikes() {return getEnemy();}

	
	
	public Colour player() {return getAlly();}
	public Colour pistolProjectile() {return getAlly();}
	public Colour shotgunInsideProjectile() {return getAlly().mutAlpha(0);}
	public Colour shotgunOutsideProjectile() {return getAlly();}

	
	
	public Colour HUDAbility1Inside() {return getLight();}
	public Colour HUDAbility1Outside() {return getLight();}
	
	public Colour HUDAbility2Inside() {return getLight();}
	public Colour HUDAbility2Outside() {return getLight();}
	
	public Colour HUDAbility3Inside() {return getLight();}
	public Colour HUDAbility3Outside() {return getLight();}
	
	public Colour HUDAbility4Inside() {return getLight();}
	public Colour HUDAbility4Outside() {return getLight();}
	
	public Colour HUDHealthTop() {return getEnemy();}
	public Colour HUDHealthMid() {return getEnemy();}
	public Colour HUDHealthBottom() {return getEnemy();}
	
	public Colour HUDNoHealthTop() {return getLight();}
	public Colour HUDNoHealthMid() {return getLight();}
	public Colour HUDNoHealthBottom() {return getLight();}
	
	
	
	public Colour UpgradeMenuTreeList() {return getLight();}
	public Colour UpgradeMenuHoverColour() {return getLight();}
	
	public Colour UpgradeTreeExampleIcon() {return getDark();}
	public Colour UpgradeTreeExampleIconColour() {return getDark();}
	public Colour UpgradeTreeExampleNoIconColour() {return getCave();}
	public Colour UpgradeTreeExampleLockedIconColour() {return getEnemy();}
	public Colour UpgradeTreeExampleUnlockedIconColour() {return getAlly();}
	public Colour UpgradeTreeExampleBoughtIconColour() {return getLight();}
	
	public Colour UpgradeTreeShotgunIcon() {return getDark();}
	public Colour UpgradeTreeShotgunIconColour() {return getDark();}
	public Colour UpgradeTreeShotgunNoIconColour() {return getCave();}
	public Colour UpgradeTreeShotgunLockedIconColour() {return getEnemy();}
	public Colour UpgradeTreeShotgunUnlockedIconColour() {return getAlly();}
	public Colour UpgradeTreeShotgunBoughtIconColour() {return getLight();}


}
