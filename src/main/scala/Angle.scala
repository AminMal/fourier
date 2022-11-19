object Angle {

  private final val step: Double = 0.02D
  private var currentAngle: Double = 0

  def next: Double = {
    val angle = currentAngle
    currentAngle += step
    angle
  }

}
