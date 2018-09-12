trait A{
  def first:String
}

case class ABC() extends A{
  override def first: String ="ABC"
  def second:String = "ABC"
}
case class EFG() extends A{
  override def first: String ="EFB"
  def second:String = "EFG"
}

def function[T <: >: EFG, K <: A](val1:T, val2:K):String={
  val2.first
}