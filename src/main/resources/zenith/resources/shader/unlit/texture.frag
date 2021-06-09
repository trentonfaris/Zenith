#version 430

in vec2 fragTexCoord;

out vec4 outColor;

uniform sampler2D texture2D;

void main()
{
	outColor = vec4(texture(texture2D, fragTexCoord).rgb, 1.0);
}
