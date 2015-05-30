#version 150 core

in vec3 position;
in vec3 color;
in vec2 vertexUV;
in vec3 normal;

out vec3 vertexColor;
out vec2 UV;
out vec3 LightDirection_cameraspace;
out vec3 Normal_cameraspace;
out vec3 EyeDirection_cameraspace;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    vertexColor = color;
    UV = vertexUV;
    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(position, 1.0);
 
	// Position of the vertex, in worldspace : M * position
	vec3 Position_worldspace = (model * vec4(position, 1)).xyz;
 
	// Vector that goes from the vertex to the camera, in camera space.
	// In camera space, the camera is at the origin (0,0,0).
	vec3 vertexPosition_cameraspace = ( view * model * vec4(position, 1)).xyz;
	EyeDirection_cameraspace = vec3(0,0,0) - vertexPosition_cameraspace;
 
	// Vector that goes from the vertex to the light, in camera space. M is ommited because it's identity.
	vec3 LightPosition_worldspace = vec3(-5, 10, -8);
	vec3 LightPosition_cameraspace = ( view * vec4(LightPosition_worldspace,1)).xyz;
	LightDirection_cameraspace = LightPosition_cameraspace + EyeDirection_cameraspace;
 
	// Normal of the the vertex, in camera space
	Normal_cameraspace = ( view * model * vec4(normal,0)).xyz; // Only correct if ModelMatrix does not scale the model ! Use its inverse transpose if not.
}