package bosl

import dsl.*

def sum(vs: List[Point]) = {
  vs.foldLeft(Point(0, 0))((a, b) => Point(a.x + b.x, a.y + b.y))
}

def region(r: List[List[Point]]) = {
    val points = r.flatten

    val paths = 
        0.to(r.length-1).toList.map{
          i =>
            val start = 0.to(i-1).map {
              j => r(j).length
            }.sum

            0.to(r(i).length-1).toList.map {
              k => start + k
            }
        }

    Polygon(points, paths)
}