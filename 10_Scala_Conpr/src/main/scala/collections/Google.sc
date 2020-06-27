
  println("Hey Google")

  import scala.io._
  
  val htmlWord = Set(
     "class", "div", "http", "span", "href", "www", "html", "img",
     "title", "src", "alt", "width", "height", "strong", "clearfix",
     "target", "style", "script", "type", "_blank", "item", "last",
     "text", "javascript", "middot", "point", "display", "none", "data",
     "share", "path", "_self", "DDTHH", "YYYY")
  
  val content = Source.fromURL("https://www.blick.ch")(Codec.UTF8).getLines.mkString

  val words = content.split("\\W").toList

  val important = words
    .filter(w => w.size > 3)
    .filter(w => !htmlWord(w))
    .filter(w => w.charAt(0).isUpper)

  val grouped = important.groupBy(w => w)
  val list = grouped.view.mapValues(l => l.size).toList

  val top10 = list.sortBy(p => p._2).reverse.take(10)





   

