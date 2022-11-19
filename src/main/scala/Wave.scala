import scalafx.scene.shape.Circle

object Wave {

  private val maximumSamplesCount: Int = 1200
  private var positions: List[Circle] = List[Circle]()

  def addPosition(dot: Circle): Unit = {
    positions.foreach(c => c.centerX = c.centerX.value + 0.5D)
    positions = (dot :: positions).take(maximumSamplesCount)
  }


}
