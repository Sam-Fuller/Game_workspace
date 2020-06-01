package graphics;

/**
 * contains all necessary functions for a mesh
 * @author Sam
 *
 */
public interface Mesh {
    public void cleanUp();
    public boolean isTransparent();
    public void render();
    public void render(float x, float y);
    public void render(float x, float y, float zoom);
}
