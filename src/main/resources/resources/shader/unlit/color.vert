#version 430

layout (location = 0) in vec3 vertPositionM;

uniform mat4 modelViewProjection;

void main()
{
	gl_Position = modelViewProjection * vec4(vertPositionM, 1.0);
}
