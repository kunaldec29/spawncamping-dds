package de.frosner.dds.core

import de.frosner.dds.chart.{Series, SeriesData, Chart}

object Launch extends App {

  DDS.start()
  DDS.linePlot(List(1,2,3))

}