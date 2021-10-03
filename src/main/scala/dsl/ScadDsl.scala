package dsl

trait Obj:
  def toOpenScad(indentationLevel: Int): String

trait ObjWithChildren extends Obj:
  def children: Seq[Obj]
  def renderChildren(indentationLevel: Int): String =
    children.map(_.toOpenScad(indentationLevel + 1) + "\n").mkString("")

def calcIndent(indentLevel: Int): String =
  val indentationSize = 2
  " " * (indentLevel * indentationSize)

case class Color(name: String)(val children: Obj*) extends ObjWithChildren:
  def toOpenScad(indentationLevel: Int): String =
    val indent = calcIndent(indentationLevel)
    indent + s"color(\"$name\") { \n"
      + renderChildren(indentationLevel)
      + indent + "}"

case class Translate(x: Double, y: Double, z: Double)(val children: Obj*)
    extends ObjWithChildren:
  def toOpenScad(indentationLevel: Int = 0): String =
    val indent = calcIndent(indentationLevel)
    indent + s"translate([$x, $y, $z]) { \n"
      + renderChildren(indentationLevel)
      + indent + "}"

case class Cube(x: Double, y: Double, z: Double, center: Boolean) extends Obj:
  def toOpenScad(indentationLevel: Int = 0): String =
    val indent = calcIndent(indentationLevel)
    indent + s"cube([$x, $y, $z], center=$center);"

case class Difference()(val children: Obj*) extends ObjWithChildren:
  def toOpenScad(indentationLevel: Int = 0): String =
    val indent = calcIndent(indentationLevel)
    indent + s"difference() { \n"
      + renderChildren(indentationLevel)
      + indent + "}"

case class Hull()(val children: Obj*) extends ObjWithChildren:
  def toOpenScad(indentationLevel: Int = 0): String =
    val indent = calcIndent(indentationLevel)
    indent + s"hull() { \n"
      + renderChildren(indentationLevel)
      + indent + "}"

case class Square(x: Double, y: Double, center: Boolean) extends Obj:
  def toOpenScad(indentationLevel: Int = 0): String =
    val indent = calcIndent(indentationLevel)
    indent + s"square($x, $y, center=$center);"

case class LinearExtrude(height: Double)(val children: Obj*)
    extends ObjWithChildren:
  def toOpenScad(indentationLevel: Int = 0): String =
    val indent = calcIndent(indentationLevel)
    indent + s"linear_extrude($height) { \n"
      + renderChildren(indentationLevel)
      + indent + "}"

case object Noop extends Obj {
  def toOpenScad(indentationLevel: Int = 0): String = ""
}

case class AsOne(children: Obj*) extends ObjWithChildren:
  def toOpenScad(indentationLevel: Int = 0): String =
    renderChildren(indentationLevel)

case class OnCondition(condition: Boolean)(val children: Obj*)
    extends ObjWithChildren:
  def toOpenScad(indentationLevel: Int = 0): String =
    if (condition) renderChildren(indentationLevel)
    else ""

case class Cylinder(h: Double, d: Double, center: Boolean = false) extends Obj:
  def toOpenScad(indentationLevel: Int = 0): String =
    val indent = calcIndent(indentationLevel)
    indent + s"cylinder(h = $h, d = $d, center=$center);"

case class Rotate(deg_x: Int, deg_y: Int, deg_z: Int)(val children: Obj*) extends ObjWithChildren:
  def toOpenScad(indentationLevel: Int = 0): String =
    val indent = calcIndent(indentationLevel)
    indent + s"rotate([${deg_x}, ${deg_y}, ${deg_z}]) { \n"
      + renderChildren(indentationLevel)
      + indent + "}"
