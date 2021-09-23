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

val stem = Color("saddlebrown")(
  Translate(0, 0, 16)(Cube(7, 5.3, 2, center = true)),
  Translate(0, 0, 16)(Cube(4.1, 1.17, 7.2, center = true)),
  Translate(0, 0, 16)(Cube(1.17, 4.1, 7.2, center = true))
)

def switchTop(detailed: Boolean = false) = Color("gray")(
  Difference()(
    Hull()(
      Translate(0, .75, 6)(
        LinearExtrude(height = 0.01)(Square(10.25, 9, center = true))
      ),
      Translate(0, 0, 1)(
        LinearExtrude(height = .01)(Square(13.97, 13.97, center = true))
      )
    ),
    OnCondition(detailed)(
      Translate(0, -7, 5)(Cube(7.8, 6, 7.5, center = true))
    )
  )
)

def switchBottom(detailed: Boolean = false) = Color("dimgray")(
  Translate((-13.97 / 2 - .5 + 2.5), (-13.97 / 2 - .5 + 1), 0.5)(
    Cube(5, 2, 1, center = true)
  ),
  Translate((-13.97 / 2 - .5 + 2.5), -(-13.97 / 2 - .5 + 1), 0.5)(
    Cube(5, 2, 1, center = true)
  ),
  Translate(-(-13.97 / 2 - .5 + 2.5), -(-13.97 / 2 - .5 + 1), 0.5)(
    Cube(5, 2, 1, center = true)
  ),
  Translate(-(-13.97 / 2 - .5 + 2.5), (-13.97 / 2 - .5 + 1), 0.5)(
    Cube(5, 2, 1, center = true)
  ),
  Difference()(
    Hull()(
      Translate(0, 0, 1)(
        LinearExtrude(height = 0.01)(Square(13.97, 13.97, center = true))
      ),
      Translate(0, 0, 0)(
        LinearExtrude(height = 0.01)(Square(13.97, 13.97, center = true))
      ),
      Translate(0, 0, -5)(
        LinearExtrude(height = 0.01)(Square(12.72, 13.97, center = true))
      )
    ),
    OnCondition(detailed)(
      Translate((14 - 2.11) / 2, 0, -4.55 / 2)(
        Cube(2.11, 3.81, 4.55, center = true)
      ),
      Translate(-(14 - 2.11) / 2, 0, -4.55 / 2)(
        Cube(2.11, 3.81, 4.55, center = true)
      )
    )
  ),
  Translate(0, 0, -5 - 3)(Cylinder(d = 4, h = 3))
)

object Main extends App {
  println(switchBottom(detailed = true).toOpenScad(0))
}
