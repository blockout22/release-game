#version 400

in vec2 position;
in vec2 texCoords;

out vec2 textureCoords;

uniform mat4 modelMatrix;

void main(){
	vec4 worldPos = modelMatrix * vec4(position, 1.0, 1.0);
	gl_Position = worldPos;
	textureCoords = texCoords;
}