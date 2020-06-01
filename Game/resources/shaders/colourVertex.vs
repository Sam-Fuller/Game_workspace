#version 330

layout (location=0) in vec2 position;
layout (location=1) in vec4 inColour;

uniform vec2 cameraPosition;
uniform float cameraZoom;

uniform bool litUniform;
uniform vec3 lightSource;

uniform float aspectRatio;

out vec4 exColour;

void main()
{
	gl_Position = vec4(position*cameraZoom - cameraPosition, 0.0, 1.0);

	bool lit = litUniform;
	
	float litamount = 0;
	
	float distance = sqrt(pow(lightSource.x-gl_Position.x*aspectRatio, 2) + pow(lightSource.y-gl_Position.y, 2));

	if (litUniform) {
		litamount = 1;
		
	} else if (distance < lightSource.z) {
		litamount = 1;
		
	} else if (distance < 2*lightSource.z) {
		litamount = 1-(distance-lightSource.z)/(lightSource.z);
	}
	
	exColour = vec4(inColour.x*litamount, inColour.y*litamount, inColour.z*litamount, inColour.w);
}