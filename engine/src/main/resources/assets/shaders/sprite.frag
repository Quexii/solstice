#version 330 core

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D Texture;
uniform vec4 Color;

void main() {
    vec4 texColor = texture(Texture, texCoord);
    if (texColor.a == 0) discard;
    fragColor = texColor * Color;
}
