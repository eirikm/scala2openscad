// module sa_keycap(steps=4) {
//   u = is_undef($u) ? 1 : $u;
//   h = is_undef($h) ? 1 : $h;
//   rot = !is_undef($rot) ? $rot : 0;
//   x = rot == 90 ? h : u;
//   y = rot == 90 ? u : h;
//   detail = !is_undef($detail) && $detail;
//   pressed = !is_undef($key_pressed) && $key_pressed;

//   bottom_radius = sa_bottom_corner_radius;
//   top_radius = sa_top_corner_radius;
//   dish_radius = 90;

//   width = sa_keycap_width * x;
//   depth = sa_keycap_depth * y;
//   height = sa_keycap_height;
//   mount_height = sa_keycap_top_height
//     - (pressed ? mx_switch_travel : 0)
//     - height;

//   inset = [
//     (sa_keycap_width - sa_top_width) / 2,
//     (sa_keycap_depth - sa_top_depth) / 2,
//     0
//   ];

//   function l (t, s) = (
//     let(cos4t=pow(cos(t), s))
//     let(sin4t=pow(sin(t), s))
//     let(rho=1/pow(cos4t + sin4t, 1/s))
//     [
//       rho * cos(t),
//       rho * sin(t)
//     ]
//   );

//   function make_squircle(r=1, s=5, steps=40) = (
//     let(quadrant = concat([
//       for (t=[0:steps/4])
//       l(t/(steps/4)*90, s) * r
//     ], [[0, r]]))

//     concat(
//       quadrant,
//       [ for (p=quadrant) [-p.x,  p.y] ],
//       [ for (p=quadrant) [-p.x, -p.y] ],
//       [ for (p=quadrant) [ p.x, -p.y] ]
//     )
//   );

//   module squircle(r=1, s=5, steps=40) {
//     polygon(make_squircle(r, s, steps));
//   }

//   module draw_layer_profile(r, s, steps) {
//     points = [for (p=make_squircle(r, s, steps)) [
//       (p.x != 0 && x > 1) ? sign(p.x) * (width/2 - sa_keycap_width/2) + p.x: p.x,
//       (p.y != 0 && y > 1) ? sign(p.y) * (depth/2 - sa_keycap_depth/2) + p.y: p.y
//     ]];

//     polygon(points);
//   }

//   module cap() {
//     s = [3.75, 10];
//     r = [sa_top_width/2, sa_keycap_width/2];

//     hull()
//     {
//       for (i=[0:steps-1]) {
//         t = i / steps;

//         translate([0, 0, sa_keycap_height] * t)
//         linear_extrude(height=0.1)
//         draw_layer_profile(
//           r=r[1] + (r[0] - r[1]) * t*t,
//           s=s[1] + (s[0] - s[1]) * t,
//           steps=40
//         );
//       }

//       translate([0, 0, sa_keycap_height] * 0.99)
//         linear_extrude(height=0.1)
//         draw_layer_profile(
//           r=sa_top_width/2 * 0.99,
//           s=s[1] + (s[0] - s[1]) * 0.99,
//           steps=40
//         );


//       translate([0, 0, sa_keycap_height] * 1.025)
//         linear_extrude(height=0.1)
//         draw_layer_profile(
//           r=sa_top_width/2 * .95,
//           s=s[1] + (s[0] - s[1]) * 1.025,
//           steps=40
//         );

//     }
//   }

//   translate([0, 0, mount_height])
//   color("whitesmoke")
//   cap();
// }

val sa_keycap_width : Double = 18.42;
val sa_keycap_depth : Double = 18.42;
val sa_top_width : Double = 12.7;
val sa_top_depth : Double = 12.7;
val sa_keycap_height : Double = 12.42;
val sa_keycap_top_height : Double = sa_keycap_height + 6;

val sa_top_corner_radius : Double = 1.16;
val sa_bottom_corner_radius : Double = 0.57;

case class Point(x : Double, y : Double)
extension (p: Point)
  def *(mult: Double): Point = Point(p.x*mult, p.y*mult)

def l(t: Double ,s: Double) : Point = {
  val cos4t = Math.pow(Math.cos(t), s)
  val sin4t = Math.pow(Math.sin(t), s)
  val rho = 1.0 / Math.pow(cos4t + sin4t, 1/s)
  Point(rho * Math.cos(t), rho * Math.sin(t) )
}

def make_squircle(r: Int =1, s: Int=5, steps : Int=40): List[Point] = {
  val quadrant : List[Point]= 0.to(steps/4).toList.map {
    t => 
      l(t/(steps/4).toDouble*90, s.toDouble) * r
  } ++ List(Point(r.toDouble, 0.0))

  quadrant 
    ++ quadrant.map(p => Point(-p.x, p.y))
    ++ quadrant.map(p => Point(-p.x, -p.y))
    ++ quadrant.map(p => Point(p.x, -p.y))
}

def drawLayerProfile(r : , s, steps) = {

}

def cap(steps: Int = 4) = {
    val s= (3.75, 10)
    val r = (sa_top_width/2, sa_keycap_width/2)

    Hull() {
      0.to(steps).map{i => 
        val t = i / steps

        Translate(0,0, sa_keycap_height * t)
          LinearExtrude(height = 0.1) {
            drawLayerProfile
          }
      }
    }
    ???
}

object Main extends App {
  Translate(0,0, mount_height) {
    Color("whitesmoke") {
      cap()
    }
  }
  List(stem, switchTop(detailed = true), switchBottom(detailed = true), pins).foreach {
    o => println(o.toOpenScad(0))
  }
}
