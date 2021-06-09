#version 430

layout (location = 0) in vec3 vertPositionM;
layout (location = 1) in vec3 vertNormalM;
layout (location = 2) in vec2 vertTexCoord;
layout (location = 3) in vec3 vertTangentM;
layout (location = 4) in vec3 vertBitangentM;

out vec2 fragTexCoord;

void main()
{
	gl_Position = vec4(vertPositionM.x, vertPositionM.y, 0.0, 1.0);

	fragTexCoord = vertTexCoord;
}
