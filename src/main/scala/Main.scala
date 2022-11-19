import javafx.{animation => jfxAnimation}
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import scalafx.scene.shape._
import scalafx.Includes._
import scalafx.animation.AnimationTimer
import scalafx.geometry._
import scalafx.scene.control._
import scalafx.scene.layout.GridPane
import scalafx.scene.text.Text

import scala.annotation.tailrec
import scala.util.Try


object Main extends JFXApp3 {

  @tailrec
  def fourier(series: Int, angle: Double, acc: Double = 0): Double = {
    if (series < 0) acc else {
      val n = 2 * series + 1
      val currentSeriesAndAngleFourier = (4 * Math.sin(n * angle)) / (n * Math.PI)
      fourier(series - 1, angle, acc + currentSeriesAndAngleFourier)
    }
  }

  val sinusScale = 100
  val waveTopMargin = 220

  val stageWidth = 600
  val stageHeight = 450

  var currentFourierSeries = 0

  def newDot(x: Double, y: Double): Circle = {
    new Circle() {
      centerX = x
      centerY = y
      radius = 1.5D
      fill = Green
    }
  }

  val currentSeriesValuePosition: Circle = new Circle {
    radius = 5D
    fill = White

    centerX = stageWidth / 2
    centerY = stageHeight / 2
  }

  def onEachFrame(handleFrame: () => Unit): AnimationTimer = new AnimationTimer(new jfxAnimation.AnimationTimer() {
    override def handle(l: Long): Unit = handleFrame()
  }) {}

  lazy val fourierSeriesInput: TextField = new TextField()
  lazy val submitFourierSeriesButton: Button = new Button() {
    onAction = () => {
      val input = fourierSeriesInput.text.value
      currentFourierSeries = Try(input.toInt).getOrElse(0)
    }
    text = "submit"
  }
  lazy val fourierSeriesInputText: Text = new Text("Enter fourier series:") {
    fill = White
    alignmentInParent = Pos.TopCenter
  }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Fourier series"
      width = stageWidth
      height = stageHeight
      scene = new Scene {
        fill = Black

        onEachFrame { () =>
          val angle = Angle.next
          currentSeriesValuePosition.centerY = (fourier(currentFourierSeries, angle) * sinusScale) + waveTopMargin
          val currentSeriesValuePositionSnapshot = newDot(
            currentSeriesValuePosition.centerX.value,
            currentSeriesValuePosition.centerY.value
          )
          Wave.addPosition(currentSeriesValuePositionSnapshot)
          content.add(currentSeriesValuePositionSnapshot)
        }.start()

        val pane: GridPane = new GridPane() {
          val (topPadding, rightPadding, bottomPadding, leftPadding) = (10, 100, 0, 200)
          padding = Insets.apply(topPadding, rightPadding, bottomPadding, leftPadding)
        }
        GridPane.setHalignment(fourierSeriesInputText, HPos.Center)
        GridPane.setHalignment(submitFourierSeriesButton, HPos.Center)
        pane.addRow(0, fourierSeriesInputText)
        pane.addRow(1, fourierSeriesInput)
        pane.addRow(2, submitFourierSeriesButton)
        content.add(pane)
        content.add(currentSeriesValuePosition)
      }
    }
  }
}