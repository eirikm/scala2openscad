package mx

import dsl.*

val stem = Color("saddlebrown")(
  Translate(0, 0, 6)(Cube(7, 5.3, 2, center = true)),
  Translate(0, 0, 6)(Cube(4.1, 1.17, 7.2, center = true)),
  Translate(0, 0, 6)(Cube(1.17, 4.1, 7.2, center = true))
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

val pins =
  Color("gold")(
    Translate(2 * 1.27, 4 * 1.27, 0)(
      Translate(0, 0, -5 - 3.3)(Cylinder(d = 1.5, h = 3.3))
    ),
    Translate(-3 * 1.27, 2 * 1.27, 0)(
      Translate(0, 0, -5 - 3.3)(Cylinder(d = 1.5, h = 3.3))
    )
  )

def mxSwitch = {
  List(stem, switchTop(detailed = true), switchBottom(detailed = true), pins).foreach {
    o => println(o.toOpenScad(0))
  }
}
