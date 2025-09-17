#version 330 core

layout(location = 0) in vec2 Position;
layout(location = 1) in vec4 Color;

out vec4 fragColor;

uniform mat4 ProjViewMatrix;
uniform mat4 ModelMatrix;

void main() {
    gl_Position = ProjViewMatrix * ModelMatrix * vec4(Position, 0.0, 1.0);
    fragColor = Color;
}