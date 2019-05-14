# RayTracer
This project was built as part of "Graphics basics and image processing" course in Tel-Aviv University.

### Surfaces
- **Spheres**- Each sphere is defined by the position of its center and its radius.
- **Infinite planes**- Each plane is defined by its normal and an offset along the
normal. A point on the P plane will satisfy the formula P • N = c .
- **Triangle**- Each triangle is defined by three vectors which represents the
triangle's three vertexes.

### Algorithm overview
A ray tracer shoots rays from the observer's eye (the camera) through a screen and into a scene which
contains one or more surfaces. it calculates the ray's intersection with the surfaces,
finds the nearest intersection and calculates the color of the surface according to its material and lighting conditions.
To compute the color of a surface, you must take into account the object's material. If the material is reflective, you need to shoot reflection rays to find objects which would be reflected. If the material is transparent or semi-transparent, you need to shoot rays through the surface to find objects that are behind it.
Each rendered surface (sphere, plane, triangle) is associated with a material in the scene file. Several surfaces can be associated with the same material.
Each material has the following attributes: Diffuse color, Specular color, Phong specularity coefficient, Reflection color, Transparency.
To light the scene, we will use point lights. Point lights are located at a certain point and shoot light equally in all directions around that point. Each light will have the following attributes:Position, Color, Specular intensity, Shadow intensity, Light Radius.
The camera is defined by the following attributes: Position, Look-at point, Up vector, Screen distance, Screen width. There will be only one camera in each scene file.
There are also some general settings that are used to describe a scene: Background color, Number of shadow rays, Maximum recursion level, 
Super sampling level.

### Special features
- **Soft Shadows:** To generate soft shadows, we will send several shadow rays from the light source to a point on the surface. The light intensity that hits the surface from this light source will be multiplied by the number of rays that hit the surface divided by the total number of rays we sent. The sent rays should simulate a light which has a certain area, Each light is defined with a light radius.
- **Super Sampling:** The super sampling technique will help us reduce the problem of aliasing, in this technique we will shoot NxN rays (samples) through each pixel and the color showed in this pixel will be the average color of the NxN samples.

### Result
<p>
  <img src="https://github.com/NoiCoh/RayTracer/blob/master/Images/Pool.png?raw=true" width="250">
  <img src="https://github.com/NoiCoh/RayTracer/blob/master/Images/Room1.png?raw=true" width="250">
  <img src="https://github.com/NoiCoh/RayTracer/blob/master/Images/Triangle.png?raw=true" width="250">
</p>
