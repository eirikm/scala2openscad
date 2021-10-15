import dsl.*
import bosl.*
import javax.swing.plaf.synth.Region

case class Switch(
    keyholeLength: Double
) {
  val plate_thickness = 2
  val plate_vertical_padding = 1.7
  val plate_horizontal_padding = 1.7

  def plate_width = keyholeLength + 2 * plate_horizontal_padding
  def plate_height = keyholeLength + 2 * plate_vertical_padding

  val column_support_height = 6
  val column_support_thickness = plate_thickness
  val column_support_spacing = keyholeLength + column_support_thickness * 2
  val column_support_center_offset =
    (column_support_spacing - column_support_thickness) / 2

  val slot_height = 2
  val slot_width = plate_thickness
  val slot_padding = 1

}

val mxSwitch = Switch(keyholeLength = 14)
val chocSwitch = Switch(keyholeLength = 13.8)

object Main extends App {

  def plate(switch: Switch, w: Int = 1, h: Int = 1, align: Int = 0): List[List[Point]] = {
    val width = switch.plate_width * w
    val height = switch.plate_height * h
    val center_offset = switch.column_support_center_offset

    val plate_top = height / 2
    val plate_right = width / 2

    val slot_right = center_offset * w + switch.slot_width / 2
    val slot_left = center_offset * w - switch.slot_width / 2

    val slot_bottom = plate_top - switch.column_support_thickness / 2
    val slot_distance_from_edge =
      width / 2 - (center_offset * w + switch.column_support_thickness / 2)

    val corner_profile = List(
      Point(
        plate_right,
        if (slot_distance_from_edge > 0.2) plate_top
        else slot_bottom
      ),
      Point(
        slot_right,
        if (slot_distance_from_edge > 0.2) plate_top
        else slot_bottom
      ),
      Point(slot_right, slot_bottom),
      Point(slot_left, slot_bottom),
      Point(slot_left, plate_top)
    )

    val outer_points =
      List(
        corner_profile,
        corner_profile.map(v => Point(-v.x, v.y)).reverse,
        corner_profile.map(v => Point(-v.x, -v.y)),
        corner_profile.map(v => Point(v.x, -v.y)).reverse
      ).flatten

    val alignment = align * (w - 1) * switch.plate_width / 2

    val inner_points = List(
      Point(switch.keyholeLength / 2 + alignment, switch.keyholeLength / 2),
      Point(-switch.keyholeLength / 2 + alignment, switch.keyholeLength / 2),
      Point(-switch.keyholeLength / 2 + alignment, -switch.keyholeLength / 2),
      Point(switch.keyholeLength / 2 + alignment, -switch.keyholeLength / 2)
    )

    List(outer_points, inner_points)
  }

  val paths : List[List[Point]] = plate(mxSwitch)

  val obj = 
    Translate(0,0, -mxSwitch.plate_thickness)(
      LinearExtrude(mxSwitch.plate_thickness)(
        region(paths)
      )
    )

  println(obj.toOpenScad(0))
}
