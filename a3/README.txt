Brandon Nguyen
20733209 bknguyen
openjdk version "11.0.8" 2020-11-13
macOS 10.15.6 (MacBook Pro 2017)

This is my version of SketchIt!

All the the required features are implemented.

Extra features implemented: Cut-Copy-Paste

Some extra features I decided to implement:
- You can use the fill tool to set the background colour of the canvas

- By default when you draw a shape, it is drawn with a transparent fill and a black stroke. I added the
  options to fill the shape with the selected fill colour as it's being drawn. This is done by checking
  the checkbox that says "Fill Shape".

- Additionally, if a shape is selected and you click the "Fill Shape" button, it will remove/add the fill
  colour to the selected shape. This button will get disabled if the selected shape doesn't have a border
  since removing the fill colour of a shape with no border will make the shape disappear. This was added to
  simplify the process of removing the fill colour of the selected shape (instead of having to go into the
  colour picker and choosing the Transparent colour).

- There is an extra button in the "Line Thickness" section called "No Border" which allows you to draw
  a Rectangle or a Circle without the black border IF the fill colour is not transparent (it wouldn't
  make sense to draw a transparent shape with no border).
  This button also allows you to remove the border on the selected shape.
  This button is disabled when the selected tool is a Line and when the selected shape has a transparent
  fill colour.
  When "No Border" is selected, all of the line styles are disabled as well since there is no border to style.


Sources for the images used:

- Select tool: https://iconarchive.com/show/flatastic-6-icons-by-custom-icon-design/Path-selection-tool-icon.html

- Eraser tool: https://www.iconfinder.com/icons/2431840/adobe_tool_eraser_eraser_tool_photoshop_icon

- Fill tool: https://www.pinclipart.com/maxpin/TiRooT/
