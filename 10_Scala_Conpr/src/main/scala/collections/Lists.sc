// head, tail, indexed, ::, map, filter, reduce, zip, groupBy, find
  println("Welcome to the Scala worksheet")

  val l = List("Hello", "World", "!")

  l.head
	l.tail.head
	val ll = "bla" :: l
  l
  
  val sizes = l.map(w => w.size)
  sizes.filter(i => i > 2)
  
  sizes.reduce((l, r) => l+r)
  
  l.zip(List(1, 2, 3))
  
  val days = List("Mo", "Mi", "Do")
  days.groupBy(d => d.charAt(0))
 
  days.find(d => d.charAt(1) == 'x')
