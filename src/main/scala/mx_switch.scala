trait Obj:
  def toOpenScad(indentationLevel: Int) : String

trait ObjWithChildren extends Obj:
  def children: Seq[Obj]
  def renderChildren(indentationLevel: Int): String =
    children.map(_.toOpenScad(indentationLevel+1) + "\n" ).mkString("")


def calcIndent(indentLevel: Int): String = 
  val indentationSize = 2
  " " * (indentLevel * indentationSize)

case class Color(name: String)(val children: Obj*) extends ObjWithChildren:
  def toOpenScad(indentationLevel: Int): String = 
    val indent = calcIndent(indentationLevel)
    indent + s"color(\"$name\") { \n" 
      + renderChildren(indentationLevel)
      + indent + "}"

case class Translate(x: Double, y: Double, z: Double)(val children: Obj*) extends ObjWithChildren:
  def toOpenScad(indentationLevel: Int = 0): String = 
    val indent = calcIndent(indentationLevel)
    indent + s"translate([$x, $y, $z]) { \n" 
      + renderChildren(indentationLevel)
      + indent + "}"

case class Cube(x: Double, y: Double, z: Double, center: Boolean) extends Obj:
  def toOpenScad(indentationLevel: Int = 0): String = 
    val indent = calcIndent(indentationLevel)
    indent + s"cube([$x, $y, $z], center=$center);"

case class Difference()(val children : Obj*) extends ObjWithChildren:
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

case class LinearExtrude(height: Double)(val children: Obj*) extends ObjWithChildren:
  def toOpenScad(indentationLevel: Int = 0): String = 
    val indent = calcIndent(indentationLevel)
    indent + s"linear_extrude($height) { \n" 
      + renderChildren(indentationLevel)
      + indent + "}"

val stem = color("saddlebrown")(
  translate(0,0,16)(cube(7, 5.3, 2, center=true)),
  translate(0,0,16)(cube(4.1, 1.17, 7.2, center=true)),
  translate(0,0,16)(cube(1.17, 4.1, 7.2, center=true))
)

val switchTop = color("gray")(
  difference()(
    Hull()(
      translate(0, .75, 6) (LinearExtrude(height=0.01) (Square(10.25, 9, center=true))),
      translate(0, 0, 1) (LinearExtrude(height=.01) (Square(13.97, 13.97, center=true)))
    )
  )
)
object Main extends App {
  println(switchTop.toOpenScad(0))
}