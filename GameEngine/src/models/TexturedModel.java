package models;

import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.Loader;
import textures.ModelTexture;

public class TexturedModel {
	private RawModel rawModel;
	private ModelTexture texture;
	
	public TexturedModel(RawModel rawModel, ModelTexture texture)
	{
		this.rawModel = rawModel;
		this.texture = texture;
		
	}
	
	public TexturedModel(String name, Loader loader)
	{
		try {
			ModelData data = OBJFileLoader.loadOBJ(name + "Model");
			this.rawModel = loader.loadToVAO(data.getVertices(), data.getIndices(), data.getNormals(), data.getTextureCoords());
			this.texture = new ModelTexture(loader.loadTexture(name + "Texture"));
		} catch (Exception e) {
			System.err.println("Could not create TexturedModel: " + name);
		}
		
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
	
	
}
