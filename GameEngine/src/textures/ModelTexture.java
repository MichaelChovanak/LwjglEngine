package textures;

public class ModelTexture {
	private int textureID;
	private int specularID;
	
	private float shineDamper = 1.0f;
	private float reflectivity = 0;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	private boolean hasSpecularMap = false;
	
	private int numberOfRows = 1;
	
	public ModelTexture(int textureID)
	{
		this.textureID = textureID;
	}
	
	public void setSpecularMap(int specularID) {
		this.specularID = specularID;
		this.hasSpecularMap = true;
	}
	
	public boolean hasSpecularMap() {
		return hasSpecularMap;
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}
	
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	public int getID()
	{
		return this.textureID;
	}
}
