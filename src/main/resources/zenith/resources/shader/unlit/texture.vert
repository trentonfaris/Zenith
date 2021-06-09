#version 430

layout (location = 0) in vec3 vertPositionM;
layout (location = 1) in vec2 vertTexCoord;

out vec2 fragTexCoord;

uniform mat4 modelViewProjection;

void main()
{
	gl_Position = modelViewProjection * vec4(vertPositionM, 1.0);
	
	fragTexCoord = vertTexCoord;
}
