/**
 * @author Michael Chovanak
 * LWJGL 2 Game Engine using OpenGL.
 * Made by following ThinMatrix's Youtube tutorials found here: https://www.youtube.com/playlist?list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP
 */

package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GUIRenderer;
import guis.GUITexture;
import models.TexturedModel;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import postProcessing.Fbo;
import postProcessing.PostProcessing;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import shadows.ShadowMapMasterRenderer;
import terrain.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;

public class MainGameLoop {

	public static List<Entity> entitiesList;
	
	public static void main(String[] args) 
	{

		
		//***************************Management*******************************
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		GUIRenderer guiRenderer = new GUIRenderer(loader);
		TexturedModel person = new TexturedModel("person", loader);
		Player player = new Player(person, new Vector3f(200,10,-200), 0, 180, 0, 0.4f);
		Camera camera = new Camera(player);
		MasterRenderer renderer = new MasterRenderer(loader, camera);
		TextMaster.init(loader);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		
		//***************************Fonts/Text*******************************
		FontType font = new FontType(loader.loadFontTexture("segoe"), new File("res/segoe.fnt"));
		GUIText text = new GUIText("Test Text!!!", 1, font, new Vector2f(0,0), 1f, true);
		//text.setColor(0, 0, 0);
		
		
		//***************************Terrain Textures*******************************
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		Terrain terrain0 = new Terrain(0, -1, loader, texturePack, blendMap, "heightMap");
		//Terrain terrain1 = new Terrain(-1, -1, loader, texturePack, blendMap, "heightMap");
		
		
		//***************************TexturedModels*********************************
		TexturedModel tree = new TexturedModel("tree", loader);
		//tree.getTexture().setHasTransparency(true);
		TexturedModel grass = new TexturedModel("grass", loader);
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		TexturedModel fern = new TexturedModel("fern", loader);
		fern.getTexture().setHasTransparency(true);
		fern.getTexture().setNumberOfRows(2);
		TexturedModel test = new TexturedModel("random", loader);

		//***************************Entities*********************************
		entitiesList = new ArrayList<Entity>();
		entitiesList.add(new Entity(test, new Vector3f(200,10,-200), 0, 0, 0, 1));
		Random random = new Random();
		float x, y, z;
		for(int i = 0; i < 500; i++)
		{
			x = random.nextFloat()*1800 - 400;
			z =  random.nextFloat()*-1600;
			y = terrain0.getTerrainHeight(x, z);
			entitiesList.add(new Entity(tree, new Vector3f(x, y, z), 0, 0, 0, 1));
			/*
			x = random.nextFloat()*800 - 400;
			z =  random.nextFloat()*-600;
			y = terrain0.getTerrainHeight(x, z);
			entities.add(new Entity(grass, new Vector3f(x, y, z), 0, 0, 0, 1));*/
			x = random.nextFloat()*800 - 400;
			z =  random.nextFloat()*-600;
			y = terrain0.getTerrainHeight(x, z);
			entitiesList.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 0.6f));
		}
		entitiesList.add(player);
		

		//***************************Lights*********************************
		Light sun = new Light(new Vector3f(100000,10000000,-70000000), new Vector3f(0.4f,0.4f,0.4f));
		List<Light> lights = new ArrayList<>();
		lights.add(sun);
		lights.add(new Light(new Vector3f(-200,10,-200), new Vector3f(10,0,0), new Vector3f(1,0.01f,0.002f)));
		lights.add(new Light(new Vector3f(200,10,200), new Vector3f(0,10,0), new Vector3f(1,0.01f,0.002f)));
		lights.add(new Light(new Vector3f(200,10,-200), new Vector3f(0,0,10), new Vector3f(1,0.01f,0.002f)));
		

		//***************************MousePicker*********************************
		
		MousePicker mousePicker = new MousePicker(camera, renderer.getProjectionMatrix());


		
		//***************************GUI/Text*********************************
		List<GUITexture> guis = new ArrayList<GUITexture>();
		GUITexture gui = new GUITexture(loader.loadTexture("socuwan"), new Vector2f(0.5f,0.5f), new Vector2f(0.25f, 0.25f));
		//guis.add(gui);
		//GUITexture shadowMap = new GUITexture(renderer.getShadowMapTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f));
		//guis.add(shadowMap);
		
		
		//***************************Particle Systems*******************************
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particleAtlas"), 4);
		ParticleSystem particleSystem = new ParticleSystem(50, 30, 1, 5, particleTexture);
		ParticleTexture particleTexture2 = new ParticleTexture(loader.loadTexture("particleStar"), 1);
		ParticleSystem particleSystem2 = new ParticleSystem(50, 10, 2, 2, particleTexture2);
		
		
		//Fbo multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight());
		Fbo outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		PostProcessing.init(loader);
		
		

		//***************************Game Loop*******************************
		while(!Display.isCloseRequested())
		{
			player.move(terrain0);
			camera.move();
			mousePicker.update();
			renderer.renderShadowMap(entitiesList, sun);
			ParticleMaster.update();
			

			particleSystem.generateParticles(new Vector3f(200,30,-200));
			particleSystem2.generateParticles(new Vector3f(200,30,-200));
			renderer.processEntity(player);
			renderer.processTerrain(terrain0);
			//renderer.processTerrain(terrain1);
			for(Entity entity: entitiesList)
			{
				renderer.processEntity(entity);
			}
			outputFbo.bindFrameBuffer();
			renderer.render(lights, camera);
			ParticleMaster.render(camera);
			outputFbo.unbindFrameBuffer();
			PostProcessing.doPostProcessing(outputFbo.getColorTexture());
			
			guiRenderer.render(guis);
			TextMaster.render();
			DisplayManager.updateDisplay();
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				break;
			}
		}
		
		PostProcessing.cleanUp();
		//multisampleFbo.cleanUp();
		outputFbo.cleanUp();
		TextMaster.cleanUp();
		ParticleMaster.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
