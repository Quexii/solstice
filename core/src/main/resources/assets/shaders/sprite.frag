#version 330 core

in vec2 fragTexCoord;
in vec4 fragColor;

out vec4 color;

uniform sampler2D texture0;

void main() {
    color = texture(texture0, fragTexCoord) * fragColor;
}
