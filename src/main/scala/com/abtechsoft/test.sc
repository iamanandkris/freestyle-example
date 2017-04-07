val list=Set("a","b","c")

def m(inp:String*): Unit ={
  println("before")
  inp.foreach(x => println(x))
  println("after")
}

m(list.toSeq:_*)