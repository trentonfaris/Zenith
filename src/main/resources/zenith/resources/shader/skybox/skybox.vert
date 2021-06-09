#version 430

layout (location = 0) in vec3 vertPositionM;

out vec3 fragPositionM;

uniform mat4 viewProjection;

void main() {
	gl_Position = (viewProjection * vec4(vertPositionM, 1.0)).xyww;
	
	fragPositionM = vertPositionM;
}