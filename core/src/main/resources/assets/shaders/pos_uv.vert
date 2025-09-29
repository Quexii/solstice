#version 330 core

layout (location = 0) in vec2 Position;
layout (location = 1) in vec2 TexCoord;

out vec2 texCoord;

uniform mat4 ProjMatrix;
uniform mat4 ModelMatrix;

void main() {
    gl_Position = ProjMatrix * ModelMatrix * vec4(Position, 0.0, 1.0);
    texCoord = TexCoord;
}
