package de.frosner.dds.core

import java.io.{ByteArrayOutputStream, PrintStream}

import org.scalatest.{FlatSpec, Matchers}

class HelperTest extends FlatSpec with Matchers {
  
  class TestClass {
    def noHelp = ???

    @Help(
      category = "a",
      shortDescription = "short help",
      longDescription = "long help"
    )
    def help = ???

    @Help(
      category = "a",
      shortDescription = "short help",
      longDescription = "long help"
    )
    def xhelp = ???

    @Help(
      category = "bbb",
      shortDescription = "sph",
      longDescription = "long parameter help",
      parameters = "i: Int",
      parameters2 = "s: String"
    )
    def helpWithParameters(i: Int) = ???
  }

  "A helper" should "offer only help for methods with the correct annotation" in {
    val testClass = new TestClass()
    val helper = Helper(testClass.getClass)
    helper.methods.keySet shouldBe Set("a", "bbb")

    val (aMethodName, aMethodHelp) = helper.methods("a")(0)
    aMethodName shouldBe "help"
    aMethodHelp.category shouldBe "a"
    aMethodHelp.shortDescription shouldBe "short help"
    aMethodHelp.longDescription shouldBe "long help"

    val (bMethodName, bMethodHelp) = helper.methods("bbb")(0)
    bMethodName shouldBe "helpWithParameters"
    bMethodHelp.category shouldBe "bbb"
    bMethodHelp.shortDescription shouldBe "sph"
    bMethodHelp.longDescription shouldBe "long parameter help"
    bMethodHelp.parameters shouldBe "i: Int"
    bMethodHelp.parameters2 shouldBe "s: String"
  }

  it should "print only the short description in the method listing" in {
    val result = new ByteArrayOutputStream()
    val out = new PrintStream(result)
    val helper = Helper(new TestClass().getClass)
    helper.printMethods(out)
    result.toString.split("\n") shouldBe Array(
        s"\033[1ma\033[0m",
        "- help(): short help",
        "- xhelp(): short help",
        "",
        s"\033[1mbbb\033[0m",
        "- helpWithParameters(i: Int)(s: String): sph"
      )
  }

}
