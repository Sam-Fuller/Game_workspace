#version 330

layout (location=0) in vec2 position;
layout (location=1) in vec2 texCoord;

uniform vec2 cameraPosition;
uniform float cameraZoom;

out vec4 gl_Position;
out vec2 outTexCoord;

void main()
{
    gl_Position = vec4(position*cameraZoom - cameraPosition, 0.0, 1.0);
    outTexCoord = texCoord;
}