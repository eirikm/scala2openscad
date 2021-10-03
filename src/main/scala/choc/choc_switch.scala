package choc

import dsl.*

val switchTop =
  Color("lightgray")(
    Translate(0, 0, 1.4)(Cube(13.8, 13.8, 2.8, center = true)),
    Translate(0, 6.9, 0.4)(Cube(15.0, 1.2, 0.8, center = true)),
    Translate(0, -6.9, 0.4)(Cube(15.0, 1.2, 0.8, center = true))
  )

val switchBottom =
  Color("dimgray")(
    Translate(0, 0, -1.1)(Cube(13.8, 13.8, 2.2, center = true)),
    Translate(0, 0, -2.2)(Rotate(180, 0, 0)(Cylinder(d = 3.4, h = 2.65)))
  )

val pins =
  Color("yellow")(
    Translate(0, -5.9, -2.2)(Rotate(180, 0, 0)(Cylinder(d = 1.2, h = 2.65))),
    Translate(5, -3.8, -2.2)(Rotate(180, 0, 0)(Cylinder(d = 1.2, h = 2.65)))
  )

def stem(detailed: Boolean = false) =
  Color("brown")(
    Translate(0, 0, 2.5 + 1.5 + .3)(
      Difference()(
        Cube(12, 5.5, 3, center = true),
        OnCondition(detailed)(
          Translate(-5.7 / 2, 0, 0)(Cube(1.2, 3.0, 5, center = true)),
          Translate(5.7 / 2, 0, 0)(Cube(1.2, 3.0, 5, center = true))
        )
      )
    )
  )

def chocSwitch(detailed: Boolean = false) =
  List(
    stem(detailed),
    switchTop,
    switchBottom,
    pins
  ).map { o =>
    o.toOpenScad(0)
  }.mkString("")

object Main extends App {
  println(chocSwitch(true))
}
